package thhunit;

import static java.lang.Math.PI;
import static java.lang.Math.random;

import bullet.*;
import core.GHQ;
import effect.*;
import paint.ImageFrame;
import physicis.HasDynam;
import weapon.Weapon;
import weapon.WeaponInfo;

public class Marisa extends THHUnit{
	private static final long serialVersionUID = -1533719907505962673L;
	
	public Marisa(int initialGroup) {
		super(20, initialGroup);
	}
	@Override
	public final String getName() {
		return "Marisa";
	}
	
	//weapon&bullet kind name
	static final int
		MILLKY_WAY = 0,NARROW_SPARK = 1,REUSE_BOMB = 2,MAGIC_MISSILE = 3;
	//effect kind name
	private static final int LIGHTNING = 0,SPARK_HIT_EF = 1,MISSILE_TRACE1_EF = 2,MISSILE_TRACE2_EF = 3,
			MISSILE_HIT_EF = 4;
	
	//GUI
		
	//Sounds
	
	@Override
	public final void loadImageData(){
		super.loadImageData();
		charaPaint = new ImageFrame("thhimage/Marisa.png");
		iconPaint = new ImageFrame("thhimage/MarisaIcon.png");
		bulletPaint[MILLKY_WAY] = new ImageFrame("thhimage/MillkyWay.png");
		bulletPaint[NARROW_SPARK] = new ImageFrame("thhimage/NarrowSpark_2.png");
		bulletPaint[REUSE_BOMB] = new ImageFrame("thhimage/ReuseBomb.png");
		bulletPaint[MAGIC_MISSILE] = new ImageFrame("thhimage/MagicMissile.png");
		effectPaint[LIGHTNING] = new ImageFrame("thhimage/ReuseBomb_Effect.png");
		effectPaint[SPARK_HIT_EF] = new ImageFrame("thhimage/NarrowSpark_HitEffect.png");
		effectPaint[MISSILE_TRACE1_EF] = new ImageFrame("thhimage/StarEffect2.png");
		effectPaint[MISSILE_TRACE2_EF] = new ImageFrame("thhimage/MagicMissile.png");
		effectPaint[MISSILE_HIT_EF] = new ImageFrame("thhimage/MissileHitEffect.png");
	}
	
	@Override
	public final void loadSoundData(){
	}
	
	//Initialization
	@Override
	public final void battleStarted(){
		//test area
		//weaponSlot[0] = REUSE_BOMB;
		weaponSlot[0] = MAGIC_MISSILE;
		spellSlot[0] = NARROW_SPARK;
		spellSlot[1] = REUSE_BOMB;
		////weaponLoad
		//MILLKY_WAY
		WeaponInfo.clear();
		WeaponInfo.name = "MILLKY_WAY";
		WeaponInfo.coolTime = 10;
		weaponController[MILLKY_WAY] = new Weapon();
		//NARROW_SPARK
		WeaponInfo.clear();
		WeaponInfo.name = "NARROW_SPARK";
		WeaponInfo.reloadTime = 150;
		WeaponInfo.magazineSize = 1;
		weaponController[NARROW_SPARK] = new Weapon();
		//REUSE_BOMB
		WeaponInfo.clear();
		WeaponInfo.name = "REUSE_BOMB";
		WeaponInfo.coolTime = 10;
		weaponController[REUSE_BOMB] = new Weapon();
		//MAGIC_MISSILE
		WeaponInfo.clear();
		WeaponInfo.name = "MAGIC_MISSILE";
		WeaponInfo.coolTime = 25;
		weaponController[MAGIC_MISSILE] = new Weapon();
		/////////////////////
		slot_spell = 0;
	}
	@Override
	public final void respawn(int x,int y){
		super.respawn(x,y);
		for(Weapon ver : weaponController) {
			if(ver != null)
				ver.reset();
		}
	}	
	@Override
	public void activeCons() {
		super.activeCons();
		for(Weapon ver : weaponController) {
			if(ver != null)
				ver.defaultIdle();
		}
	}
	//bullet
	@Override
	public final boolean useWeapon(int kind) {
		return weaponController[kind].trigger();
	}
	@Override
	public final void setBullet(int kind,HasDynam user) {
		BulletBlueprint.clear(bulletScripts[kind],user.getDynam());
		BulletBlueprint.standpointGroup = standpoint.get();
		switch(kind){
		case MILLKY_WAY:
			BulletBlueprint.name = "MILLKY_WAY";
			BulletBlueprint.dynam.setSpeed(20);
			BulletBlueprint.size = 30;
			BulletBlueprint.atk = 40;
			BulletBlueprint.offSet = 20;
			BulletBlueprint.limitFrame = 200;
			BulletBlueprint.paintScript = bulletPaint[MILLKY_WAY];
			GHQ.createBullet(user).split_Round(50, 8);
			break;
		case NARROW_SPARK:
			//message
			//if(this == source) {
			//	THH.addMessage(this,charaID,"KOIFU [MasterSpark]");
			//	THH.addMessage(this,charaID,"TEST MESSAGE 2");
			//}
			BulletBlueprint.name = "NARROW_SPARK";
			BulletBlueprint.dynam.setSpeed(bulletPaint[NARROW_SPARK].getDefaultW());
			BulletBlueprint.size = 15;
			BulletBlueprint.atk = 8;
			BulletBlueprint.offSet = 20;
			BulletBlueprint.penetration = MAX;
			BulletBlueprint.reflection = 3;
			BulletBlueprint.limitFrame = 40;
			BulletBlueprint.paintScript = bulletPaint[NARROW_SPARK];
			BulletBlueprint.isLaser = true;
			GHQ.createBullet(user);
			break;
		case REUSE_BOMB:
			BulletBlueprint.name = "REUSE_BOMB";
			BulletBlueprint.dynam.setSpeed(40);
			BulletBlueprint.accel = 0.98;
			BulletBlueprint.size = 30;
			BulletBlueprint.atk = 40;
			BulletBlueprint.offSet = 20;
			BulletBlueprint.limitFrame = 200;
			BulletBlueprint.paintScript = bulletPaint[REUSE_BOMB];
			GHQ.createBullet(user).split_Round(50, 3);
			break;
		case MAGIC_MISSILE:
			BulletBlueprint.name = "MAGIC_MISSILE";
			BulletBlueprint.accel = 1.07;
			BulletBlueprint.size = 20;
			BulletBlueprint.atk = 500;
			BulletBlueprint.offSet = 100;
			BulletBlueprint.limitFrame = 2000;
			BulletBlueprint.paintScript = bulletPaint[MAGIC_MISSILE];
			BulletBlueprint.dynam.fastParaAdd_DASpd(10,GHQ.random2(PI/36),10.0);
			GHQ.createBullet(user);
			break;
		}
	}
	@Override
	public final void setEffect(int kind,HasDynam user) {
		EffectBlueprint.clear(effectScripts[kind],user.getDynam());
		switch(kind){
		case LIGHTNING:
			EffectBlueprint.name = "LIGHTNING";
			EffectBlueprint.dynam.fastParaAdd_DASpd(10,2*PI*random(),20);
			EffectBlueprint.limitFrame = 2;
			EffectBlueprint.paintScript = effectPaint[LIGHTNING];
			GHQ.createEffect(this);
			break;
		case SPARK_HIT_EF:
			EffectBlueprint.name = "SPARK_HIT_EF";
			EffectBlueprint.limitFrame = 3;
			EffectBlueprint.paintScript = effectPaint[SPARK_HIT_EF];
			EffectBlueprint.dynam.fastParaAdd_DASpd(10,2*PI*random(),GHQ.random2(0,12));
			GHQ.createEffect(this);
			break;
		case MISSILE_TRACE1_EF:
			EffectBlueprint.name = "MISSILE_TRACE1_EF";
			EffectBlueprint.accel = -0.2;
			EffectBlueprint.limitFrame = 7;
			EffectBlueprint.paintScript = effectPaint[kind];
			for(int i = 0;i < 4;i++) {
				EffectBlueprint.dynam.fastParaAdd_DASpd(10,2*PI*random(),GHQ.random2(0,12));
				GHQ.createEffect(this);
			}
			break;
		case MISSILE_TRACE2_EF:
			EffectBlueprint.name = "MISSILE_TRACE2_EF";
			EffectBlueprint.limitFrame = 7;
			EffectBlueprint.dynam.stop();
			EffectBlueprint.paintScript = effectPaint[kind];
			GHQ.createEffect(this);
			break;
		case MISSILE_HIT_EF:
			EffectBlueprint.name = "MISSILE_HIT_EF";
			EffectBlueprint.accel = -0.1;
			EffectBlueprint.paintScript = effectPaint[kind];
			for(int i = 0;i < 30;i++) {
				EffectBlueprint.limitFrame = GHQ.random2(10,40);
				GHQ.createEffect(this).getDynam().fastParaAdd_DASpd(30,2*PI*random(),GHQ.random2(2,5));
			}
			break;
		}
	}
	private static transient final BulletScript[] bulletScripts = new BulletScript[10];
	{
		bulletScripts[MILLKY_WAY] = BulletBlueprint.DEFAULT_SCRIPT;
		bulletScripts[NARROW_SPARK] = new BulletScript() {
			@Override
			public final void idle(Bullet bullet) {
				bullet.lifeSpanCheck();
				int count = 0;
				while(bullet.getDynam().inStage()) {
					bullet.dynam(++count % 5 == 0);
					bullet.defaultPaint();
				}
				final HasDynam BULLET_SOURCE = bullet.source;
				bullet.getDynam().setXY(BULLET_SOURCE);
				bullet.getDynam().setAngle(BULLET_SOURCE.getDynam().getAngle());
			}
			@Override
			public final void bulletHitObject(Bullet bullet) {
				setEffect(SPARK_HIT_EF,bullet);
			}
		};
		bulletScripts[REUSE_BOMB] = new BulletScript() {
			@Override
			public final void idle(Bullet bullet) {
				bullet.defaultIdle();
				bullet.getDynam().addSpeed(0.0,1.1);
				bullet.defaultPaint();
				if(random() < 0.2)
					setEffect(LIGHTNING,(HasDynam)bullet);
			}
		};
		bulletScripts[MAGIC_MISSILE] = new BulletScript() {
			@Override
			public final void idle(Bullet bullet) {
				for(int i = 0;i < 2;i++)
					setEffect(MISSILE_TRACE1_EF,bullet);
				setEffect(MISSILE_TRACE2_EF,bullet);
				super.idle(bullet);
			}
			@Override
			public final void bulletHitObject(Bullet bullet) {
				setEffect(MISSILE_HIT_EF,bullet);
			}
		};
	}
	private static transient final EffectScript[] effectScripts = new EffectScript[10];
	{
		effectScripts[MISSILE_TRACE1_EF] = new EffectScript() {
			@Override
			public final void noAnmPaint(Effect effect) {
				effectFadePaint(effect);
			}
		};
		effectScripts[MISSILE_TRACE2_EF] = new EffectScript() {
			@Override
			public final void noAnmPaint(Effect effect) {
				GHQ.setImageAlpha((float)(1.0 - (double)GHQ.getPassedFrame(effect.INITIAL_FRAME)/effect.LIMIT_FRAME));
				super.noAnmPaint(effect);
				GHQ.setImageAlpha();
			}
		};
		effectScripts[MISSILE_HIT_EF] = new EffectScript() {
			@Override
			public final void noAnmPaint(Effect effect) {
				GHQ.setImageAlpha((float)(1.0 - (double)GHQ.getPassedFrame(effect.INITIAL_FRAME)/effect.LIMIT_FRAME));
				super.noAnmPaint(effect);
				GHQ.setImageAlpha();
			}
		};
	}
}
