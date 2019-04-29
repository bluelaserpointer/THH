package thhunit;

import action.Action;
import action.ActionInfo;
import core.GHQ;
import geom.Square;
import item.Item;
import paint.DotPaint;
import paint.RectPaint;
import physicis.Dynam;
import physicis.HasDynam;
import status.StatusWithDefaultValue;
import storage.ItemStorage;
import storage.Storage;
import unit.Unit;
import weapon.Weapon;

public abstract class THH_BasicUnit extends Unit {
	private static final long serialVersionUID = 6736932836274080528L;
	public double charaDstX, charaDstY, charaSpeed = 30;
	public boolean charaOnLand;

	// Weapon
	public int slot_spell, slot_weapon;
	public final int spellSlot_max = 6, weaponSlot_max = 6, weapon_max = 10;
	public final int[] spellSlot = new int[spellSlot_max], weaponSlot = new int[weaponSlot_max];
	protected final Weapon weapon[] = new Weapon[10];

	// GUI
	public RectPaint iconPaint;

	// Resource
	// Images
	protected DotPaint charaPaint;
	public final DotPaint bulletPaint[] = new DotPaint[weapon_max], effectPaint[] = new DotPaint[10];

	//status
	public static final int PARAMETER_AMOUNT = 6;
	public static final int HP = 0,MP = 1,ATK = 2,AGI = 3,BLO = 4,STUN = 5;
	private static final String names[] = new String[PARAMETER_AMOUNT];
	static {
		names[HP] = "HP";
		names[MP] = "MP";
		names[ATK] = "ATK";
		names[AGI] = "AGI";
		names[BLO] = "BLO";
		names[STUN] = "STUN";
	}
	public final StatusWithDefaultValue status = new StatusWithDefaultValue(PARAMETER_AMOUNT);
	
	//inventory
	public final ItemStorage inventory;
	
	public THH_BasicUnit(int charaSize, int initialGroup) {
		super(new Square(charaSize), initialGroup);
		inventory = new ItemStorage(new Storage<Item>());
	}
	public THH_BasicUnit(int charaSize, int initialGroup, Storage<Item> itemStorageKind) {
		super(new Square(charaSize), initialGroup);
		inventory = new ItemStorage(itemStorageKind);
	}
	
	@Override
	public void loadImageData() {
	}

	@Override
	public void respawn(int x, int y) {
		resetOrder();
		status.reset();
		dynam.clear();
		dynam.setXY(charaDstX = x, charaDstY = y);
		charaOnLand = false;
		slot_spell = 0;
	}
	public void resetOrder() {
		weaponChangeOrder = 0;
		attackOrder = moveOrder = dodgeOrder = spellOrder = false;
	}
	public void resetSingleOrder() {
		weaponChangeOrder = 0;
		spellOrder = dodgeOrder = false;
	}
	@Override
	public void dynam() {
		Dynam DYNAM = getDynam();
		DYNAM.move();
		DYNAM.accelerate_MUL(0.9);
	}
	public int weaponChangeOrder;
	public boolean attackOrder,moveOrder,dodgeOrder,spellOrder;
	@Override
	public void passiveCons() {
		resetSingleOrder();
	}
	@Override
	public void activeCons() {
		// death
		if (status.get(HP) <= 0) {
			return;
		}
		final int mouseX = GHQ.getMouseX(), mouseY = GHQ.getMouseY();
		Dynam DYNAM = getDynam();
		DYNAM.setAngle(DYNAM.getMouseAngle());
		// dodge
		if (dodgeOrder)
			dodge(mouseX, mouseY);
		// attack
		if (attackOrder) {
			final int weapon = weaponSlot[slot_weapon];
			if (weapon != NONE && useWeapon(weapon))
				setBullet(weapon,this);
		}
		// spell
		if (spellOrder) {
			final int spell = spellSlot[slot_spell];
			if (spell != NONE && useWeapon(spell))
				setBullet(spell,this);
		}
		// move
		if (moveOrder) {
			//under edit
		}
		DYNAM.approach(charaDstX, charaDstY, charaSpeed);
		// weaponChange
		int roll = weaponChangeOrder;
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
	public void paint(boolean doAnimation) {
		if(status.get(HP) <= 0)
			return;
		final Dynam DYNAM = getDynam();
		final int X = (int) DYNAM.getX(),Y = (int) DYNAM.getY();
		charaPaint.dotPaint(X, Y);
		GHQ.paintHPArc(X, Y, 20,status.get(HP), status.getDefault(HP));
	}
	protected final void paintMode_magicCircle(DotPaint paintScript) {
		final Dynam DYNAM = getDynam();
		final int X = (int) DYNAM.getX(),Y = (int) DYNAM.getY();
		paintScript.dotPaint_turn(X, Y, (double)GHQ.getNowFrame()/35.0);
		charaPaint.dotPaint(X, Y);
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
		getDynam().addXY(x, y);
		charaDstX += x;
		charaDstY += y;
	}
	@Override
	public void teleportTo(int x,int y) {
		getDynam().setXY(charaDstX = x, charaDstY = y);
	}
	private Action actionPlan;
	private int initialFrame;
	protected void doActionPlan() {
		int countedFrame = 0;
		final Dynam DYNAM = getDynam();
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
					charaDstX = DYNAM.getX() + X;
					charaDstY = DYNAM.getY() + Y;
					break;
				case ActionInfo.ATTACK:
					setBullet(weaponSlot[slot_weapon],this);
					break;
				case ActionInfo.SPEED:
					DYNAM.addXY(X,Y);
					break;
				}
			}
			
		}
	}
	// HP
	private final void dodge(double targetX, double targetY) {
		final Dynam DYNAM = getDynam();
		DYNAM.addSpeed_DA(40, DYNAM.getAngle(targetX,targetY));
		charaOnLand = false;
	}

	// decrease
	@Override
	public final int damage_amount(int amount) {
		return status.add(HP, -amount);
	}

	@Override
	public final boolean kill(boolean force) {
		status.set(HP, 0);
		return true;
	}

	// information
	@Override
	public String getName() {
		return GHQ.NOT_NAMED;
	}
	@Override
	public final boolean isAlive() {
		return status.get(HP) > 0;
	}
	public boolean useWeapon(int kind) {
		return weapon[kind].trigger(this);
	}
	public abstract void setBullet(int kind,HasDynam source);
}