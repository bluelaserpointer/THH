package weapon;

import java.io.Serializable;

import core.GHQ;
import physics.HasAnglePoint;
import physics.Standpoint;
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
	public int
		coolTime, //
		coolSpeed, //
		reloadTime, //
		reloadSpeed, //
		magazineSize; //
	protected int
		coolProgress, //
		reloadProgress, //
		magazine, //
		magazineConsumptionSpeed;
	protected boolean
		autoReload;
	public Weapon() {
		NAME = WeaponInfo.name;
		coolTime = WeaponInfo.coolTime;
		coolSpeed = WeaponInfo.coolSpeed;
		reloadTime = WeaponInfo.reloadTime;
		reloadSpeed = WeaponInfo.reloadSpeed;
		magazineSize = WeaponInfo.magazineSize;
		if(magazineSize == GHQ.MAX)
			magazine = GHQ.MAX;
		magazineConsumptionSpeed = WeaponInfo.magazineConsumptionSpeed;
		coolProgress = coolTime;
		autoReload = WeaponInfo.autoReload;
	}
	/**
	 * End current cool and reload process and unload the magazine.
	 */
	public void reset() {
		coolProgress = coolTime;
		reloadProgress = reloadTime;
		if(magazineSize != GHQ.MAX)
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
				if((coolProgress += coolSpeed) >= coolTime)
					coolEnd();
			}
			//both of the cool and reloading is finished -> check magazine amount and reload.
			if(autoReload && magazine == 0) {
				startReload();
			}
		}else { //reloading process is on going
			if((reloadProgress += reloadSpeed) >= reloadTime)
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
	public boolean trigger(HasAnglePoint shooter, Standpoint standpoint) {
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
		return trigger(shooter, shooter.standpoint());
	}
	/**
	 * Set bullets.
	 * @param shooter - the shooter of the bullets
	 */
	public void setBullets(HasAnglePoint shooter, Standpoint standpoint) {
		
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
		coolProgress = coolTime;
	}
	/**
	 * Judge if the cool process is on going.<br>
	 * Note that it will returns true until the weapon is reloading.
	 * @return true - finished cooling / false - not finished
	 */
	public final boolean isCoolFinished() {
		return coolProgress >= coolTime;
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
		final int REQUIRED_AMMO = magazineSize - magazine;
		if(REQUIRED_AMMO == 0) //already full
			return 0;
		coolEnd();
		if(reloadTime == 0) //no reload time
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
			reloadProgress += reloadSpeed;
			if(isReloadFinished())
				reloadEnd();
		}
	}
	/**
	 * Finish current reload process and feed the magazine to full.
	 * @return feed amount
	 */
	public int reloadEnd() {
		reloadProgress = reloadTime;
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
		reloadProgress = reloadTime;
		return magazineSize - magazine;
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
		return reloadProgress >= reloadTime;
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
		return magazine != GHQ.MAX ? magazineSize - magazine : GHQ.MAX;
	}
	public final int getMagazineComsumptionSpeed() {
		return magazineConsumptionSpeed;
	}
	public final double getMagazineEmptySpacePercentage() {
		return (double)magazine/(double)magazineSize;
	}
}