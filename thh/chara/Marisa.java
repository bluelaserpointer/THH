package chara;

import static java.lang.Math.PI;
import static java.lang.Math.random;

import bullet.*;
import effect.*;
import thh.DynamInteractable;
import thh.THH;
import weapon.Weapon;
import weapon.WeaponInfo;

public class Marisa extends UserChara{
	{
		charaSize = 20;
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
	public final void loadImageData(){ //ª≠œÒ’i§ﬂﬁz§ﬂ
		super.loadImageData();
		charaIID = thh.loadImage("Marisa.png");
		bulletIID[MILLKY_WAY] = thh.loadImage("MillkyWay.png");
		bulletIID[NARROW_SPARK] = thh.loadImage("NarrowSpark_2.png");
		bulletIID[REUSE_BOMB] = thh.loadImage("ReuseBomb.png");
		bulletIID[MAGIC_MISSILE] = thh.loadImage("MagicMissile.png");
		effectIID[LIGHTNING] = thh.loadImage("ReuseBomb_Effect.png");
		effectIID[SPARK_HIT_EF] = thh.loadImage("NarrowSpark_HitEffect.png");
		effectIID[MISSILE_TRACE1_EF] = thh.loadImage("StarEffect2.png");
		effectIID[MISSILE_TRACE2_EF] = thh.loadImage("MagicMissile.png");
		effectIID[MISSILE_HIT_EF] = thh.loadImage("MissileHitEffect.png");
	}
	
	@Override
	public final void loadSoundData(){ //•µ•¶•Û•…’i§ﬂﬁz§ﬂ
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
	public final void respawn(int charaID,int charaTeam,int x,int y){ //≥ı∆⁄ªØÑI¿Ì
		super.respawn(charaID,charaTeam,x,y);
		charaHP = super.charaBaseHP = 10000;
		charaME = charaBaseME = 100;
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
	public final void setBullet(int kind,DynamInteractable source) {
		THH.prepareBulletInfo();
		BulletInfo.kind = kind;
		BulletInfo.team = charaTeam;
		switch(kind){
		case MILLKY_WAY:
			BulletInfo.name = "MILLKY_WAY";
			BulletInfo.script = bulletScripts[MILLKY_WAY];
			BulletInfo.fastParaSet_onlySpd_SourceSpd(source,20);
			BulletInfo.size = 30;
			BulletInfo.atk = 40;
			BulletInfo.offSet = 20;
			BulletInfo.limitFrame = 200;
			BulletInfo.imageID = bulletIID[MILLKY_WAY];
			BulletInfo.createBullet_RoundDesign(source,50,8);
			break;
		case NARROW_SPARK:
			//message
			//if(this == source) {
			//	THH.addMessage(this,charaID,"KOIFU [MasterSpark]");
			//	THH.addMessage(this,charaID,"TEST MESSAGE 2");
			//}
			BulletInfo.name = "NARROW_SPARK";
			BulletInfo.script = bulletScripts[NARROW_SPARK];
			BulletInfo.fastParaSet_SourceSpd(source,thh.getImageByID(bulletIID[NARROW_SPARK]).getWidth(null));
			BulletInfo.size = 15;
			BulletInfo.atk = 8;
			BulletInfo.offSet = 20;
			BulletInfo.penetration = MAX;
			BulletInfo.reflection = 3;
			BulletInfo.limitFrame = 40;
			BulletInfo.imageID = bulletIID[NARROW_SPARK];
			BulletInfo.isLaser = true;
			THH.createBullet(source);
			break;
		case REUSE_BOMB:
			BulletInfo.name = "REUSE_BOMB";
			BulletInfo.script = bulletScripts[REUSE_BOMB];
			BulletInfo.fastParaSet_onlySpd_SourceSpd(source,40);
			BulletInfo.accel = 0.98;
			BulletInfo.size = 30;
			BulletInfo.atk = 40;
			BulletInfo.offSet = 20;
			BulletInfo.limitFrame = 200;
			BulletInfo.imageID = bulletIID[REUSE_BOMB];
			BulletInfo.createBullet_RoundDesign(source,50,3);
			break;
		case MAGIC_MISSILE:
			BulletInfo.name = "MAGIC_MISSILE";
			BulletInfo.script = bulletScripts[MAGIC_MISSILE];
			BulletInfo.accel = 1.07;
			BulletInfo.size = 20;
			BulletInfo.atk = 500;
			BulletInfo.offSet = 100;
			BulletInfo.limitFrame = 2000;
			BulletInfo.imageID = bulletIID[MAGIC_MISSILE];
			BulletInfo.fastParaSet_SourceADSpd(source,THH.random2(PI/36),10,10);
			THH.createBullet(source);
			break;
		}
	}
	@Override
	public final void setEffect(int kind,DynamInteractable source) {
		THH.prepareEffectInfo();
		EffectInfo.kind = kind;
		switch(kind){
		case LIGHTNING:
			EffectInfo.name = "LIGHTNING";
			EffectInfo.fastParaSet_SourceADSpd(source,2*PI*random(),10,20);
			EffectInfo.accel = 1.0;
			EffectInfo.size = NONE;
			EffectInfo.limitFrame = 2;
			EffectInfo.imageID = effectIID[LIGHTNING];
			THH.createEffect(this);
			break;
		case SPARK_HIT_EF:
			EffectInfo.name = "SPARK_HIT_EF";
			EffectInfo.script = effectScripts[SPARK_HIT_EF];
			EffectInfo.accel = 1.0;
			EffectInfo.size = NONE;
			EffectInfo.limitFrame = 3;
			EffectInfo.imageID = effectIID[SPARK_HIT_EF];
			EffectInfo.fastParaSet_SourceADSpd(source,2*PI*random(),10,THH.random2(0,12));
			THH.createEffect(this);
			break;
		case MISSILE_TRACE1_EF:
			EffectInfo.name = "MISSILE_TRACE1_EF";
			EffectInfo.script = effectScripts[MISSILE_TRACE1_EF];
			EffectInfo.accel = 0.8;
			EffectInfo.size = NONE;
			EffectInfo.limitFrame = 7;
			EffectInfo.imageID = effectIID[kind];
			for(int i = 0;i < 4;i++) {
				EffectInfo.fastParaSet_SourceADSpd(source,2*PI*random(),10,THH.random2(0,12));
				THH.createEffect(this);
			}
			break;
		case MISSILE_TRACE2_EF:
			EffectInfo.name = "MISSILE_TRACE2_EF";
			EffectInfo.script = effectScripts[MISSILE_TRACE2_EF];
			EffectInfo.size = NONE;
			EffectInfo.limitFrame = 7;
			EffectInfo.x = source.getX();
			EffectInfo.y = source.getY();
			EffectInfo.angle = source.getAngle();
			EffectInfo.imageID = effectIID[kind];
			THH.createEffect(this);
			break;
		case MISSILE_HIT_EF:
			EffectInfo.name = "MISSILE_HIT_EF";
			EffectInfo.script = effectScripts[MISSILE_HIT_EF];
			EffectInfo.size = NONE;
			EffectInfo.limitFrame = THH.random2(10,40);
			EffectInfo.imageID = effectIID[kind];
			for(int i = 0;i < 30;i++) {
				EffectInfo.fastParaSet_SourceADSpd(source,2*PI*random(),10,THH.random2(0,5));
				THH.createEffect(this);
			}
			break;
		}
	}
	private final BulletScript[] bulletScripts = new BulletScript[10];
	{
		bulletScripts[MILLKY_WAY] = BulletInfo.DEFAULT_SCRIPT;
		bulletScripts[NARROW_SPARK] = new BulletScript() {
			@Override
			public final void bulletIdle(Bullet bullet) {
				bullet.lifeSpanCheck();
				int count = 0;
				while(THH.inStage((int)bullet.getX(),(int)bullet.getY())) {
					bullet.dynam(++count % 5 == 0);
					bullet.defaultPaint();
				}
				bullet.setX(bullet.SOURCE.getX());
				bullet.setY(bullet.SOURCE.getY());
				bullet.setAngle(bullet.SOURCE.getAngle());
			}
			@Override
			public final void bulletHitObject(Bullet bullet) {
				setEffect(SPARK_HIT_EF,bullet);
			}
		};
		bulletScripts[REUSE_BOMB] = new BulletScript() {
			@Override
			public final void bulletIdle(Bullet bullet) {
				bullet.defaultIdle();
				bullet.addSpeed(0.0,1.1);
				bullet.defaultPaint();
				if(random() < 0.2)
					setEffect(LIGHTNING,(DynamInteractable)bullet);
			}
		};
		bulletScripts[MAGIC_MISSILE] = new BulletScript() {
			@Override
			public final void bulletIdle(Bullet bullet) {
				for(int i = 0;i < 2;i++)
					setEffect(MISSILE_TRACE1_EF,bullet);
				setEffect(MISSILE_TRACE2_EF,bullet);
				bullet.spin(0.5);
				super.bulletIdle(bullet);
				/*int count = (int)(bullet.getSpeed()/bullet.SIZE);
				do{
					bullet.dynam();
				}while(--count >= 0);*/
			}
			@Override
			public final void bulletHitObject(Bullet bullet) {
				Splash.clear();
				Splash.amount = 20;
				Splash.accel = 0.9;
				Splash.limitFrame = 11;
				Splash.maxSpeed = 15;
				Splash.imageID = effectIID[MISSILE_HIT_EF];
				Splash.doImageRotate = false;
				Splash.setEffect(bullet);
			}
		};
	}
	private final EffectScript[] effectScripts = new EffectScript[10];
	{
		effectScripts[MISSILE_TRACE1_EF] = new EffectScript() {
			@Override
			public final void effectNoAnmPaint(Effect effect) {
				effectFadePaint(effect);
			}
		};
		effectScripts[MISSILE_TRACE2_EF] = new EffectScript() {
			@Override
			public final void effectNoAnmPaint(Effect effect) {
				THH.setImageAlpha((float)(1.0 - (double)THH.getPassedFrame(effect.INITIAL_FRAME)/effect.LIMIT_FRAME));
				super.effectNoAnmPaint(effect);
				THH.setImageAlpha();
			}
		};
		effectScripts[MISSILE_HIT_EF] = new EffectScript() {
			@Override
			public final void effectNoAnmPaint(Effect effect) {
				THH.setImageAlpha((float)(1.0 - (double)THH.getPassedFrame(effect.INITIAL_FRAME)/effect.LIMIT_FRAME));
				super.effectNoAnmPaint(effect);
				THH.setImageAlpha();
			}
		};
	}
}
