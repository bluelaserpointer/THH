package exsampleGame.unit;

import bullet.Bullet;
import calculate.ConsumableEnergy;
import calculate.Damage;
import core.GHQ;
import item.ItemData;
import paint.dot.DotPaint;
import paint.rect.RectPaint;
import physics.Dynam;
import physics.HasPoint;
import physics.HitGroup;
import physics.Point;
import physics.hitShape.Square;
import storage.ItemStorage;
import storage.Storage;
import unit.Unit;
import weapon.Weapon;

public abstract class THH_BasicUnit extends Unit {
	private static final long serialVersionUID = 6736932836274080528L;
	public Point.IntPoint dstPoint = new Point.IntPoint();
	public double charaSpeed = 30;
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
	protected DotPaint charaPaint = DotPaint.BLANK_SCRIPT;
	public final DotPaint bulletPaint[] = new DotPaint[weapon_max], effectPaint[] = new DotPaint[10];

	//status
	public final ConsumableEnergy
		HP = new ConsumableEnergy(1).setMin(0).setMax(1).setDefaultToMax(),
		MP = new ConsumableEnergy(1).setMin(0).setDefaultToMax(),
		ATK = new ConsumableEnergy(1).setMin(0).setDefaultToMax(),
		AGI = new ConsumableEnergy(1).setMin(0).setDefaultToMax(),
		BLO = new ConsumableEnergy(1).setMin(0).setDefaultToMax(),
		STUN = new ConsumableEnergy(1).setMin(0).setDefaultToMax();
	//inventory
	public final ItemStorage inventory = def_inventory();
	protected ItemStorage def_inventory() {
		return new ItemStorage(new Storage<ItemData>());
	}
	
	public THH_BasicUnit(int charaSize, int initialGroup) {
		physics().setPoint(new Dynam());
		physics().setHitShape(new Square(this, charaSize));
		physics().setHitGroup(new HitGroup(initialGroup));
	}
	
	@Override
	public void loadImageData() {
	}

	@Override
	public THH_BasicUnit respawn(int x, int y) {
		resetOrder();
		HP.reset();
		if(HP.isMin()) {
			System.out.println(name() + " hp max is 0.");
			HP.setNumber(1);
		}
		point().stop();
		dstPoint.setXY(point().setXY(x, y));
		charaOnLand = false;
		slot_spell = 0;
		for(Weapon ver : weapon) {
			if(ver != null)
				ver.reset();
		}
		return this;
	}
	public void resetOrder() {
		weaponChangeOrder = 0;
		attackOrder = dodgeOrder = spellOrder = false;
	}
	public void resetSingleOrder() {
		weaponChangeOrder = 0;
		spellOrder = dodgeOrder = false;
	}
	@Override
	public void idle() {
		super.idle();
		////////////
		//weapon
		////////////
		for(Weapon ver : weapon) {
			if(ver != null)
				ver.idle();
		}
		////////////
		//dynam
		////////////
		point().moveIfNoObstacles(this);
		point().accelerate_MUL(0.9);
	}
	public int weaponChangeOrder;
	public boolean attackOrder, dodgeOrder, spellOrder;
	@Override
	public void paint() {
		charaPaint.dotPaint(point());
		GHQ.paintHPArc(point(), 20, HP);
	}
	protected final void paintMode_magicCircle(DotPaint paintScript) {
		paintScript.dotPaint_turn(point(), (double)GHQ.nowFrame()/35.0);
		charaPaint.dotPaint(point());
	}
	
	// control
	// HP
	protected final void dodge(double targetX, double targetY) {
		point().addSpeed_DA(40, point().angleTo(targetX, targetY));
		charaOnLand = false;
	}

	// decrease
	@Override
	public void damage(Damage damage, Bullet bullet) {
		damage.doDamage(this, null); //HP.consume(amount).intValue();
	}
	public final boolean kill(boolean force) {
		HP.setToMin();
		return true;
	}

	// information
	@Override
	public String name() {
		return GHQ.NOT_NAMED;
	}
	@Override
	public final boolean isAlive() {
		return !HP.isMin();
	}
	public boolean useWeapon(int kind) {
		return weapon[kind].trigger(this);
	}
	public abstract void setBullet(int kind, HasPoint source);
}