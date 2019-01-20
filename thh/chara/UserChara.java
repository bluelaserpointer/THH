package chara;

import static java.lang.Math.*;

import action.Action;
import action.ActionInfo;
import bullet.Bullet;
import thh.Chara;
import thh.DynamInteractable;
import thh.THH;
import weapon.Weapon;

public abstract class UserChara extends Chara {

	// バトルパート関連
	public int charaID, charaTeam, charaHP, charaME, charaBaseHP, charaBaseME, charaSpellCharge,
			charaSize, charaStatus;
	public double charaX, charaY, charaDstX, charaDstY, charaSpeed = 30, charaXSpeed, charaYSpeed, charaAngle;
	public double charaLastXSpd, charaLastYSpd;
	public boolean charaOnLand;

	// Weapon
	public int slot_spell, slot_weapon;
	public final int spellSlot_max = 6, weaponSlot_max = 6, weapon_max = 10;
	public final int[] spellSlot = new int[spellSlot_max], weaponSlot = new int[weaponSlot_max];
	protected final Weapon weaponController[] = new Weapon[10];

	// GUI

	// Resource
	// Images
	public int charaIID;
	public final int bulletIID[] = new int[weapon_max], effectIID[] = new int[10];

	@Override
	public void loadImageData() { // 画像読み込み
	}

	@Override
	public void respawn(int charaID, int charaTeam, int x, int y) { // 初期化処理
		super.resetOrder();
		this.charaID = charaID;
		this.charaTeam = charaTeam;
		charaDstX = charaX = x;
		charaDstY = charaY = y;
		charaXSpeed = charaYSpeed = 0.0;
		charaLastXSpd = charaLastYSpd = 0.0;
		charaStatus = NONE;
		charaOnLand = false;
		slot_spell = 0;
	}
	@Override
	public void respawn(int charaID, int charaTeam, int x, int y,int hp) { // 初期化処理2
		charaHP = charaBaseHP = hp;
		this.respawn(charaID, charaTeam, x, y);
	}
	@Override
	public void dynam() {
		if(!isMovable())
			return;
		charaX += charaXSpeed;
		charaY += charaYSpeed;
		charaLastXSpd += charaXSpeed;
		charaLastYSpd += charaYSpeed;
		if (charaXSpeed < -0.5 || 0.5 < charaXSpeed)
			charaXSpeed *= 0.9;
		else
			charaXSpeed = 0.0;
		if (charaYSpeed < -0.5 || 0.5 < charaYSpeed)
			charaYSpeed *= 0.9;
		else
			charaYSpeed = 0.0;
	}
	@Override
	public void activeCons() {
		// death
		if (charaHP <= 0) {
			return;
		}
		final int mouseX = THH.getMouseX(), mouseY = THH.getMouseY();
		final double mouseAngle = atan2(mouseY - charaY, mouseX - charaX);
		charaAngle = mouseAngle;
		// dodge
		if (super.dodgeOrder)
			dodge(mouseX, mouseY);
		// attack
		if (super.attackOrder) {
			final int weapon = weaponSlot[slot_weapon];
			if (weapon != NONE && useWeapon(weapon))
				setBullet(weapon,this);
			charaLastXSpd = charaLastYSpd = 0.0;
		}
		// spell
		if (super.spellOrder) {
			final int spell = spellSlot[slot_spell];
			if (spell != NONE && useWeapon(spell))
				setBullet(spell,this);
		}
		// move
		if (super.moveOrder) {
			charaX += (mouseX - charaX) / 10;
			charaY += (mouseY - charaY) / 10;
		}
		final double DX = charaDstX - charaX,DY = charaDstY - charaY;
		final double DISTANCE = sqrt(DX*DX + DY*DY);
		if(DISTANCE <= charaSpeed) {
			charaX = charaDstX;
			charaY = charaDstY;
		}else {
			final double RATE = charaSpeed/DISTANCE;
			charaX += DX*RATE;
			charaY += DY*RATE;
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
	@Override
	public void passiveCons() {
	}
	@Override
	public void paint(boolean doAnimation) {
		if(charaHP <= 0)
			return;
		THH.drawImageTHH_center(charaIID, (int) charaX, (int) charaY);
		thh.paintHPArc((int) charaX, (int) charaY, 20,charaHP, charaBaseHP);
	}
	
	// control
	// move
	@Override
	public void moveRel(int x,int y) {
		charaDstX += x;
		charaDstY += y;
	}
	@Override
	public void moveTo(int x,int y) {
		charaDstX = x;
		charaDstY = y;
	}
	@Override
	public void teleportRel(int x,int y) {
		charaDstX += x;charaX += x;charaLastXSpd += x;
		charaDstY += y;charaY += y;charaLastYSpd += y;
	}
	@Override
	public void teleportTo(int x,int y) {
		charaLastXSpd += x - charaX;
		charaLastYSpd += y - charaY;
		charaDstX = charaX = x;
		charaDstY = charaY = y;
	}
	private Action actionPlan;
	private int initialFrame;
	@Override
	public void loadActionPlan(Action action) {
		actionPlan = action;
	}
	protected void doActionPlan() {
		int countedFrame = 0;
		for(int i = 0;i < actionPlan.frame.length;i++) {
			final int FRAME = actionPlan.frame[i];
			if((countedFrame += FRAME) == initialFrame) { //reach planned timing
				final double X = actionPlan.x[i],
					Y = actionPlan.y[i];
				switch(actionPlan.meaning[i]) {
				case ActionInfo.DST:
					charaDstX = X;
					charaDstY = Y;
					break;
				case ActionInfo.MOVE:
					charaDstX = charaX + X;
					charaDstY = charaY + Y;
					break;
				case ActionInfo.ATTACK:
					setBullet(weaponSlot[slot_weapon],this);
					break;
				case ActionInfo.SPEED:
					charaX += X;charaLastXSpd += X;
					charaY += Y;charaLastYSpd += Y;
					break;
				}
			}
			
		}
	}
	// judge
	@Override
	public final boolean bulletEngage(Bullet bullet) {
		return charaHP > 0 && THH.squreCollision((int) charaX, (int) charaY, charaSize, (int) bullet.getX(), (int) bullet.getY(), bullet.SIZE)
				&& (bullet.team == charaTeam ^ bullet.atk >= 0);
	}
	//XY
	@Override
	public void setX(double x) {
		charaLastXSpd += x - charaX;
		charaX = x;
	}
	@Override
	public void setY(double y) {
		charaLastYSpd += y - charaY;
		charaY = y;
	}
	@Override
	public void setXY(double x,double y) {
		charaLastXSpd += x - charaX;
		charaLastYSpd += y - charaY;
		charaX = x;charaY = y;
	}
	@Override
	public void addXY(double x,double y) {
		charaLastXSpd += x;
		charaLastYSpd += y;
		charaX += x;charaY += y;
	}
	// angle
	@Override
	public final void setAngle(double angle) {
		charaAngle = angle;
	}
	// hp
	@Override
	public final void setHP(int hp) {
		charaHP = hp;
	}
	// acceleration
	@Override
	public final void setXSpeed(double xSpeed) {
		charaXSpeed = xSpeed;
	}
	@Override
	public final void setYSpeed(double ySpeed) {
		charaYSpeed = ySpeed;
	}
	@Override
	public final void setSpeed(double xPower, double yPower) {
		charaXSpeed = xPower;
		charaYSpeed = yPower;
	}
	@Override
	public final void addSpeed(double xPower, double yPower) {
		charaXSpeed += xPower;
		charaYSpeed += yPower;
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
			if (THH.hitLandscape((int) charaX - 10, (int) charaY + 40, 20)) {
				charaYSpeed = 0.0;
				do {
					charaY -= 1.0;
				} while (THH.hitLandscape((int) charaX - 10, (int) charaY + 30, 20));
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
	public final boolean kill(boolean force) {
		charaHP = 0;
		return true;
	}

	// information
	@Override
	public String getName() {
		return THH.NOT_NAMED;
	}
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
	@Override
	public final boolean inStage() {
		return THH.inStage((int)charaX,(int)charaY);
	}
	@Override
	public final boolean inArea(int x,int y,int w,int h) {
		return abs(x - charaX) < w && abs(y - charaY) < h;
	}
	@Override
	public final double getDistance(double x,double y) {
		final double XD = x - charaX,YD = y - charaY;
		return sqrt(XD*XD + YD*YD);
	}
	@Override
	public final double getXSpeed() {
		return charaXSpeed;
	}
	@Override
	public final double getYSpeed() {
		return charaYSpeed;
	}
	@Override
	public final double getSpeed() {
		return sqrt(charaXSpeed*charaXSpeed + charaYSpeed*charaYSpeed);
	}
	@Override
	public final boolean isStop() {
		return charaXSpeed == 0.0 && charaYSpeed == 0.0;
	}
	@Override
	public boolean isMovable() {
		return true;
	}
	@Override
	public final double getAngle() {
		return charaAngle;
	}
	public boolean useWeapon(int kind) {
		return true;
	}
	public abstract void setBullet(int kind,DynamInteractable source);
	public abstract void setEffect(int kind,DynamInteractable source);
}