package weapon;

import java.io.Serializable;

import core.GHQ;
import core.Standpoint;
import physicis.HasDynam;
import unit.Unit;

/**
 * A class for performing object's attack delay.
 * @author bluelaserpointer
 * @since alpha1.0
 */
public class Weapon implements Serializable{
	private static final long serialVersionUID = -2165271452612401269L;
	public static final Weapon NULL_WEAPON;
	static {
		WeaponInfo.clear();
		NULL_WEAPON = new Weapon();
	}
	public final String
		NAME;
	protected final int
		COOL_TIME, //
		COOL_SPEED, //
		RELOAD_TIME, //
		RELOAD_SPEED, //
		MAGAZINE_SIZE; //
	protected int
		coolProgress, //
		reloadProgress, //
		magazine, //
		magazineConsumptionSpeed;
	protected boolean
		autoReload;
	public Weapon() {
		NAME = WeaponInfo.name;
		COOL_TIME = WeaponInfo.coolTime;
		COOL_SPEED = WeaponInfo.coolSpeed;
		RELOAD_TIME = WeaponInfo.reloadTime;
		RELOAD_SPEED = WeaponInfo.reloadSpeed;
		MAGAZINE_SIZE = WeaponInfo.magazineSize;
		if(MAGAZINE_SIZE == GHQ.MAX)
			magazine = GHQ.MAX;
		magazineConsumptionSpeed = WeaponInfo.magazineConsumptionSpeed;
		coolProgress = COOL_TIME;
		autoReload = WeaponInfo.autoReload;
	}
	/**
	 * End current cool and reload process and unload the magazine.
	 */
	public void reset() {
		coolProgress = COOL_TIME;
		reloadProgress = RELOAD_TIME;
		if(MAGAZINE_SIZE != GHQ.MAX)
			magazine = 0;
	}
	///////////////////
	//idle
	///////////////////
	/**
	 * Process the cool or reloading process.
	 * It does nothing if both of the cool and reloading process is finished.
	 */
	public void idle() {
		if(isReloadFinished()) {
			if(!isCoolFinished()) { //cooling process is on going
				if((coolProgress += COOL_SPEED) >= COOL_TIME)
					coolEnd();
			}
			//both of the cool and reloading is finished -> check magazine amount and reload.
			if(autoReload && magazine == 0) {
				startReload();
			}
		}else { //reloading process is on going
			if((reloadProgress += RELOAD_SPEED) >= RELOAD_TIME)
				reloadEnd();
		}
	}
	///////////////////
	//fire
	///////////////////
	/**
	 * Judge if the weapon is ready for next fire.
	 * @return true - ready / false - not ready
	 */
	public boolean canFire() {
		return isCoolFinished() && isReloadFinished() && magazine >= magazineConsumptionSpeed;
	}
	/**
	 * Normally trigger this weapon. It will do nothing until the cool down process finished, or the required magazines are not enough.<br>
	 * The bullets' initial Dynam is based on shooter's current dynam.<br>
	 * Turn the dynam if you want the bullets face to another direction.
	 * @param shooter
	 * @param standpoint
	 * @return
	 */
	public boolean trigger(HasDynam shooter, Standpoint standpoint) {
		if(canFire()) {
			startCool();
			if(magazine != GHQ.MAX)
				magazine -= magazineConsumptionSpeed;
			setBullets(shooter, standpoint);
			return true;
		}
		return false;
	}
	public final boolean trigger(Unit shooter) {
		return trigger(shooter, shooter.getStandpoint());
	}
	/**
	 * Set bullets.
	 * @param shooter - the shooter of the bullets
	 */
	public void setBullets(HasDynam shooter, Standpoint standpoint) {
		
	}
	///////////////////
	//cooldown
	///////////////////
	//*CooldownProcess start automatically on every fire.
	/**
	 * Start / Restart cool process.
	 * CooldownProcess start automatically on every fire.
	 */
	public void startCool() {
		coolProgress = 0;
	}
	public void startCoolIfNotDoing() {
		if(isCoolFinished())
			startCool();
	}
	/**
	 * Boost cool down process.
	 * @param value
	 */
	public void coolBoost(int value) {
		if(!isCoolFinished()) {
			coolProgress += value;
			if(isCoolFinished())
				coolEnd();
		}
	}
	/**
	 * Finish current cool process.
	 */
	public void coolEnd() {
		coolProgress = COOL_TIME;
	}
	/**
	 * Judge if the cool process is on going.<br>
	 * Note that it will returns true until the weapon is reloading.
	 * @return true - finished cooling / false - not finished
	 */
	public final boolean isCoolFinished() {
		return coolProgress >= COOL_TIME;
	}
	public final int getCoolProgress() {
		return coolProgress;
	}
	///////////////////
	//reload
	///////////////////
	//*ReloadProcess does not start automatically when run out magazines.
	//Need to call this startReload() method from external class.
	/**
	 * Try to start / restart reload process.Can set a cap for amount of required ammo.<br>
	 * ReloadProcess does not start automatically when run out magazines.<br>
	 * Need to call this startReload() method from external class.<br>
	 * <br>
	 * It will end the cool process at the same time.
	 * @param ammoLeft - max amount of required ammo
	 * @return amount going to feed
	 */
	public int startReload() {
		final int AMMO_LEFT = getLeftAmmo();
		if(AMMO_LEFT <= 0)
			return 0;
		final int REQUIRED_AMMO = MAGAZINE_SIZE - magazine;
		if(REQUIRED_AMMO == 0) //already full
			return 0;
		coolEnd();
		if(RELOAD_TIME == 0) //no reload time
			reloadEnd();
		else
			reloadProgress = 0;
		return REQUIRED_AMMO < AMMO_LEFT ? REQUIRED_AMMO : AMMO_LEFT;
	}
	public int startReloadIfNotDoing() {
		if(!isReloadFinished())
			return 0;
		return startReload();
	}
	/**
	 * Boost reload process.
	 * @param value - boost value
	 */
	public void reloadBoost(int value) {
		if(!isReloadFinished()) {
			reloadProgress += RELOAD_SPEED;
			if(isReloadFinished())
				reloadEnd();
		}
	}
	/**
	 * Finish current reload process and feed the magazine to full.
	 * @return feed amount
	 */
	public int reloadEnd() {
		reloadProgress = RELOAD_TIME;
		final int FEED_AMOUNT = Math.min(getMagazineEmptySpace(), getLeftAmmo());
		magazine += FEED_AMOUNT;
		consumeAmmo(FEED_AMOUNT);
		return FEED_AMOUNT;
	}
	public void consumeAmmo(int value) {
		
	}
	/**
	 * Cancel current reload process and does not feed the magazine.
	 * @return feed amount (still not feed in)
	 */
	public int reloadCancel() {
		reloadProgress = RELOAD_TIME;
		return MAGAZINE_SIZE - magazine;
	}
	/**
	 * Unload and return the amount of unfired bullets in the magazine.
	 * @return amount of unfired bullets in the magazine.
	 */
	public int unload() {
		final int temp = magazine;
		magazine = 0;
		return temp;
	}
	/**
	 * Unload and return the amount of unfired bullets in the magazine.
	 * @param amount - amount of magazine to unload.
	 * @return amount of unfired bullets in the magazine.
	 */
	public int unload(int amount) {
		if(magazine > amount) {
			if(magazine != GHQ.MAX) {
				magazine -= amount;
				return amount;
			}
			return GHQ.MAX;
		}else {
			return unload();
		}
	}
	/**
	 * Judge if the reload process is on going.
	 * @return true - finished reloading / false - not finished
	 */
	public boolean isReloadFinished() {
		return reloadProgress >= RELOAD_TIME;
	}
	public final int getReloadProgress() {
		return reloadProgress;
	}
	public final void setMagazineConsumptionSpeed(int value) {
		magazineConsumptionSpeed = value;
	}
	///////////////////
	//information about magazine
	///////////////////
	/**
	 * Return how many ammo lefts.<br>
	 * Override this method to describe the amount referring the unit's inventory etc.<br>
	 * By default, it always returns {@link GHQ#MAX}<br>
	 * You can set it always returns a constant number(like 1), to perform separative reloads such as pump-action shotgun.
	 * @return amount of left ammo.
	 */
	public int getLeftAmmo() {
		return GHQ.MAX;
	}
	/**
	 * Get the amount of remained magazine in current cartridge.
	 * @return amount of remained magazine in current cartridge
	 */
	public final int getMagazineFilledSpace() {
		return magazine;
	}
	/**
	 * Get the amount of fired magazine in current cartridge.
	 * @return amount of fired magazine in current cartridge
	 */
	public final int getMagazineEmptySpace() {
		return magazine != GHQ.MAX ? MAGAZINE_SIZE - magazine : GHQ.MAX;
	}
	public final int getMagazineComsumptionSpeed() {
		return magazineConsumptionSpeed;
	}
	public final double getMagazineEmptySpacePercentage() {
		return (double)magazine/(double)MAGAZINE_SIZE;
	}
}