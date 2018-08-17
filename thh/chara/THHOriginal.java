package chara;

import static java.lang.Math.*;

import bullet.Bullet;
import bullet.BulletInfo;
import effect.EffectInfo;
import thh.Chara;
import thh.THH;

public abstract class THHOriginal extends Chara {

	// •–•»•Î•—©`•»ÈvﬂB
	protected int charaID, charaTeam, charaHP, charaME, charaBaseHP, charaBaseME, charaSpellCharge,
			charaSize, charaStatus;
	protected double charaX, charaY, charaXSpeed, charaYSpeed, charaShotAngle;
	protected boolean charaOnLand;

	// Weapon
	protected int slot_spell, slot_weapon;
	protected final int spellSlot_max = 6, weaponSlot_max = 6, weapon_max = 10;
	protected final int[] spellSlot = new int[spellSlot_max], weaponSlot = new int[weaponSlot_max];

	// GUI

	// Resource
	// Images
	protected int charaIID;
	protected final int bulletIID[] = new int[weapon_max], effectIID[] = new int[10];

	@Override
	protected void loadImageData() { // ª≠œÒ’i§ﬂﬁz§ﬂ
	}

	@Override
	protected void spawn(int charaID, int charaTeam, int x, int y) { // ≥ı∆⁄ªØÑI¿Ì
		super.resetOrder();
		this.charaID = charaID;
		this.charaTeam = charaTeam;
		charaX = x;
		charaY = y;
		charaXSpeed = charaYSpeed = 0.0;
		charaStatus = NONE;
		charaOnLand = false;
		slot_spell = 0;
	}

	@Override
	protected void idle(boolean isActive) {
		final int mouseX = thh.getMouseX(), mouseY = thh.getMouseY();
		final double mouseAngle = atan2(mouseY - charaY, mouseX - charaX);
		// dynam
		charaX += charaXSpeed;
		charaY += charaYSpeed;
		if (charaXSpeed < -0.5 || 0.5 < charaXSpeed)
			charaXSpeed *= 0.9;
		else
			charaXSpeed = 0.0;
		if (charaYSpeed < -0.5 || 0.5 < charaYSpeed)
			charaYSpeed *= 0.9;
		else
			charaYSpeed = 0.0;
		// dodge
		if (super.dodgeOrder)
			dodge(mouseX, mouseY);
		// attack
		if (isActive) {
			// death
			if (charaHP <= 0) {
				return;
			}
			// attack
			if (super.attackOrder) {
				charaShotAngle = mouseAngle;
				final int weapon = weaponSlot[slot_weapon];
				if (weapon != NONE)
					bulletSpawn(weapon);
			}
			// spell
			if (super.spellOrder) {
				charaShotAngle = mouseAngle;
				final int spell = spellSlot[slot_spell];
				if (spell != NONE)
					bulletSpawn(spell);
			}
			// move
			if (super.moveOrder) {
				charaX += (mouseX - charaX) / 10;
				charaY += (mouseY - charaY) / 10;
			}
			// weaponChange
			int roll = super.weaponChangeOrder;
			if (roll != 0) {
				int target = slot_spell;
				if (roll > 0) {
					while (target < spellSlot_max - 1) {
						if (spellSlot[++target] != NONE) {
							if (--roll == 0)
								break;
						}
					}
				} else {
					while (target > 0) {
						if (spellSlot[--target] != NONE) {
							if (++roll == 0)
								break;
						}
					}
				}
				slot_spell = target;
			}
		}
		// paintChara
		this.animationPaint();
	}
	@Override
	protected void animationPaint() {
		this.freezePaint();
	}
	@Override
	protected void freezePaint() {
		thh.drawImageTHH(charaIID, (int) charaX, (int) charaY);
		thh.paintHPArc((int) charaX, (int) charaY, charaHP, charaBaseHP);
	}
	
	// control
	// judge
	public final boolean bulletEngage(Bullet bullet) {
		return THH.squreCollision((int) charaX, (int) charaY, charaSize, (int) bullet.x, (int) bullet.y, bullet.SIZE)
				&& (bullet.team == charaTeam ^ bullet.atk > 0);
	}

	// acceleration
	@Override
	public final void addAccel(double xAccel, double yAccel) {
		charaXSpeed += xAccel;
		charaYSpeed += yAccel;
	}

	public final void setAccel(double xAccel, double yAccel) {
		charaXSpeed = xAccel;
		charaYSpeed = yAccel;
	}

	private final void dodge(double targetX, double targetY) {
		final double ANGLE = atan2(targetY - charaY, targetX - charaX);
		charaXSpeed += 40 * cos(ANGLE);
		charaYSpeed += 40 * sin(ANGLE);
		charaOnLand = false;
	}
	@Override
	public final void gravity(double g) {
		if (!charaOnLand) {
			if (THH.stage.hitLandscape((int) charaX - 10, (int) charaY + 40, 20, 20)) {
				charaYSpeed = 0.0;
				do {
					charaY -= 1.0;
				} while (THH.stage.hitLandscape((int) charaX - 10, (int) charaY + 30, 20, 10));
				if (charaXSpeed == 0.0)
					charaOnLand = true;
			}else
				charaYSpeed += g;
		}
	}

	// decrease
	@Override
	public final int decreaseME_amount(int amount) {
		charaME -= amount;
		return amount;
	}

	@Override
	public final int decreaseME_rate(double rate) {
		final int value = (int) (charaME * rate);
		charaME -= value;
		return value;
	}

	@Override
	public final int damage_amount(int amount) {
		charaHP -= amount;
		return amount;
	}

	@Override
	public final int damage_rate(double rate) {
		final int damage = (int) (charaHP * rate);
		charaHP -= damage;
		return damage;
	}

	@Override
	public final boolean kill() {
		charaHP = 0;
		return true;
	}

	// information
	@Override
	public final int getTeam() {
		return charaTeam;
	}
	@Override
	public final int getHP() {
		return charaHP;
	}

	@Override
	public final double getHPRate() {
		return (double) charaHP / (double) charaBaseHP;
	}

	@Override
	public final int getME() {
		return charaME;
	}

	@Override
	public final double getMERate() {
		return (double) charaME / (double) charaBaseME;
	}

	@Override
	public final int getStatus() {
		return charaStatus;
	}
	@Override
	public final double getX() {
		return charaX;
	}
	@Override
	public final double getY() {
		return charaY;
	}

	protected void bulletSpawn(int kind) {
		THH.prepareBulletInfo(charaID);
		BulletInfo.kind = kind;
	}

	protected void effectSpawn(int kind,double x,double y) {
		THH.prepareEffectInfo(charaID);
		EffectInfo.kind = kind;
	}
}