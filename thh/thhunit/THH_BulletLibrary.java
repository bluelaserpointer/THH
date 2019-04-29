package thhunit;

import static java.lang.Math.PI;

import bullet.Bullet;
import core.GHQ;
import core.Standpoint;
import geom.Circle;
import paint.DotPaint;
import paint.ImageFrame;
import physicis.HasDynam;
import unit.Unit;
import weapon.Weapon;

public abstract class THH_BulletLibrary extends Bullet{

	public static void loadResource() {
		/////////////////
		//Marisa's bullet
		/////////////////
		MillkyWay.paint = new ImageFrame("thhimage/MillkyWay.png");
		NarrowSpark.paint = new ImageFrame("thhimage/NarrowSpark_2.png");
		ReuseBomb.paint = new ImageFrame("thhimage/ReuseBomb.png");
		MagicMissile.paint = new ImageFrame("thhimage/MagicMissile.png");
		
		/////////////////
		//Reimu's bullet
		/////////////////
		FudaKouhaku.paint = ImageFrame.createNew("thhimage/KouhakuNoFuda.png");
		FudaShiroKuro.paint = ImageFrame.createNew("thhimage/ShirokuroNoFuda.png");
		FudaSouhaku.paint = ImageFrame.createNew("thhimage/SouhakuNoFuda.png");

		/////////////////
		//Enemy's bullet
		/////////////////
		LightBall.paint = ImageFrame.createNew("thhimage/LightBallA.png");
		HealShotgun.paint = ImageFrame.createNew("thhimage/DarkNiddle2.png");
		BlackSlashBurst.paint = ImageFrame.createNew("thhimage/DarkNiddle.png");
	}
	
	public THH_BulletLibrary(Weapon sourceWeapon, HasDynam shooter, Standpoint standpoint) {
		super(sourceWeapon, shooter, standpoint);
	}
	/////////////////
	/*	<Parameters and their default values of Bullet>
	 * 
	 *	name = GHQ.NOT_NAMED;
	 *	hitShape = new Square(10);
	 *	damage = 0;
	 *	limitFrame = GHQ.MAX;
	 *	limitRange = GHQ.MAX;
	 *	penetration = 1;
	 *	reflection = 0;
	 *	accel = 1.0;
	 *	paintScript = DotPaint.BLANK_SCRIPT;
	 *	isLaser = false;
	 */
	/////////////////
	//Reimu - 1
	/////////////////
	public static class FudaKouhaku extends THH_BulletLibrary{
		private static DotPaint paint;
		public FudaKouhaku(Unit shooterUnit) {
			super(Weapon.NULL_WEAPON, shooterUnit, shooterUnit.standpoint);
			name = "FudaKouhaku";
			dynam.setSpeed(20);
			hitShape = new Circle(10);
			damage = 50;
			limitFrame = 200;
			paintScript = paint;
		}
		@Override
		public FudaKouhaku getOriginal() {
			return new FudaKouhaku((Unit)SHOOTER);
		}
		@Override
		public final void hitObject() {
			GHQ.addEffect(new THH_EffectLibrary.FudaHitEF(this, true));
		}
	}
	/////////////////
	//Reimu - 2
	/////////////////
	public static class FudaShiroKuro extends THH_BulletLibrary{
		private static DotPaint paint;
		public FudaShiroKuro(Unit shooterUnit) {
			super(Weapon.NULL_WEAPON, shooterUnit, shooterUnit.standpoint);
			name = "FudaShiroKuro";
			dynam.setSpeed(20);
			hitShape = new Circle(10);
			damage = 20;
			limitFrame = 200;
			paintScript = paint;
		}
		@Override
		public Bullet getOriginal() {
			return new FudaShiroKuro((Unit)SHOOTER);
		}
	}
	/////////////////
	//Reimu - 3
	/////////////////
	public static class FudaSouhaku extends THH_BulletLibrary{
		private static DotPaint paint;
		public FudaSouhaku(Unit shooterUnit) {
			super(Weapon.NULL_WEAPON, shooterUnit, shooterUnit.standpoint);
			name = "FudaSouhaku";
			dynam.setSpeed(20);
			hitShape = new Circle(15);
			damage = 25;
			reflection = 1;
			limitFrame = 40;
			accel = -2.0;
			paintScript = paint;
		}
		@Override
		public Bullet getOriginal() {
			return new FudaSouhaku((Unit)SHOOTER);
		}
	}
	/////////////////
	//Marisa - 1
	/////////////////
	public static class MillkyWay extends THH_BulletLibrary{
		private static DotPaint paint;
		public MillkyWay(Unit shooterUnit) {
			super(Weapon.NULL_WEAPON, shooterUnit, shooterUnit.standpoint);
			name = "MillkyWay";
			dynam.setSpeed(20);
			hitShape = new Circle(30);
			damage = 40;
			limitFrame = 200;
			paintScript = paint;
		}
		@Override
		public Bullet getOriginal() {
			return new MillkyWay((Unit)SHOOTER);
		}
	}
	/////////////////
	//Marisa - 2
	/////////////////
	public static class NarrowSpark extends THH_BulletLibrary{
		private static DotPaint paint;
		public NarrowSpark(Unit shooterUnit) {
			super(Weapon.NULL_WEAPON, shooterUnit, shooterUnit.standpoint);
			name = "NarrowSpark";
			dynam.setSpeed(2);
			hitShape = new Circle(15);
			damage = 8;
			penetration = GHQ.MAX;
			reflection = 3;
			limitFrame = 40;
			paintScript = paint;
		}
		@Override
		public Bullet getOriginal() {
			return new NarrowSpark((Unit)SHOOTER);
		}
		@Override
		public boolean idle() {
			if(defaultDeleteCheck())
				return false;
			while(dynam.inStage()) {
				paint();
				if(!dynamIdle())
					break;
			}
			dynam.setXY(SHOOTER);
			dynam.setAngle(SHOOTER);
			return true;
		}
		@Override
		public void hitObject() {
			GHQ.addEffect(new THH_EffectLibrary.SparkHitEF(this));
		}
	}
	/////////////////
	//Marisa - 3
	/////////////////
	public static class ReuseBomb extends THH_BulletLibrary{
		private static DotPaint paint;
		public ReuseBomb(Unit shooterUnit) {
			super(Weapon.NULL_WEAPON, shooterUnit, shooterUnit.standpoint);
			name = "ReuseBomb";
			hitShape = new Circle(30);
			dynam.setSpeed(40);
			accel = -1.0;
			damage = 10;
			limitFrame = 200;
			paintScript = paint;
		}
		@Override
		public Bullet getOriginal() {
			return new ReuseBomb((Unit)SHOOTER);
		}
		@Override
		public boolean idle() {
			if(!super.idle())
				return false;
			if(Math.random() < 0.2)
				GHQ.addEffect(new THH_EffectLibrary.LightningEF(this));
			return true;
		}
	}
	/////////////////
	//Marisa - 4
	/////////////////
	public static class MagicMissile extends THH_BulletLibrary{
		private static DotPaint paint;
		public MagicMissile(Unit shooterUnit) {
			super(Weapon.NULL_WEAPON, shooterUnit, shooterUnit.standpoint);
			name = "MAGIC_MISSILE";
			hitShape = new Circle(20);
			accel = 1.07;
			damage = 500;
			limitFrame = 2000;
			paintScript = paint;
			dynam.fastParaAdd_DASpd(10,GHQ.random2(PI/36),10.0);
		}
		@Override
		public Bullet getOriginal() {
			return new MagicMissile((Unit)SHOOTER);
		}
		@Override
		public boolean idle() {
			if(!super.idle())
				return false;
			for(int i = 0;i < 2;i++)
				GHQ.addEffect(new THH_EffectLibrary.MissileTraceA_EF(this, true));
			GHQ.addEffect(new THH_EffectLibrary.MissileTraceB_EF(this));
			return true;
		}
		@Override
		public void hitObject() {
			GHQ.addEffect(new THH_EffectLibrary.MissileHitEF(this, true));
		}
	}
	/////////////////
	//Enemy - 1
	/////////////////
	public static class LightBall extends THH_BulletLibrary{
		private static DotPaint paint;
		public LightBall(Unit shooterUnit) {
			super(Weapon.NULL_WEAPON, shooterUnit, shooterUnit.standpoint);
			name = "LightBall";
			hitShape = new Circle(10);
			dynam.fastParaAdd_DSpd(10,3);
			damage = 20;
			paintScript = paint;
		}
		@Override
		public Bullet getOriginal() {
			return new LightBall((Unit)SHOOTER);
		}
	}
	/////////////////
	//Enemy - 2
	/////////////////
	public static class HealShotgun extends THH_BulletLibrary{
		private static DotPaint paint;
		public HealShotgun(Unit shooterUnit) {
			super(Weapon.NULL_WEAPON, shooterUnit, shooterUnit.standpoint);
			name = "HealShotgun";
			hitShape = new Circle(10);
			dynam.fastParaAdd_DSpd(10,3);
			damage = -20;
			limitRange = 150;
			paintScript = paint;
		}
		@Override
		public Bullet getOriginal() {
			return new HealShotgun((Unit)SHOOTER);
		}
	}
	/////////////////
	//Enemy - 2
	/////////////////
	public static class BlackSlashBurst extends THH_BulletLibrary{
		private static DotPaint paint;
		public BlackSlashBurst(Unit shooterUnit) {
			super(Weapon.NULL_WEAPON, shooterUnit, shooterUnit.standpoint);
			name = "BlackSlashBurst";
			hitShape = new Circle(10);
			damage = 20;
			reflection = 1;
			limitFrame = 200;
			paintScript = paint;
		}
		@Override
		public BlackSlashBurst getOriginal() {
			return new BlackSlashBurst((Unit)SHOOTER);
		}
	}
}
