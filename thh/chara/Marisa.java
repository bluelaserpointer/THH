package chara;

import static java.lang.Math.PI;
import static java.lang.Math.random;

import bullet.Bullet;
import bullet.BulletInfo;
import bullet.BulletScript;
import effect.EffectInfo;
import effect.EffectScript;
import thh.DynamInteractable;
import thh.THH;
import weapon.Weapon;
import weapon.WeaponInfo;

public class Marisa extends UserChara{
	{
		charaSize = 10;
	}
	@Override
	public final String getName() {
		return "Marisa";
	}
	
	//weapon&bullet kind name
	static final int
		MILLKY_WAY = 0,NARROW_SPARK = 1,REUSE_BOMB = 2,MAGIC_MISSILE = 3;
	//effect kind name
	private static final int LIGHTNING = 0,NARROW_SPARK_HE = 1;
	
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
		effectIID[NARROW_SPARK_HE] = thh.loadImage("NarrowSpark_HitEffect.png");
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
		WeaponInfo.coolTime = 30;
		weaponController[MAGIC_MISSILE] = new Weapon();
		/////////////////////
		slot_spell = 0;
	}
	@Override
	public final void spawn(int charaID,int charaTeam,int x,int y){ //≥ı∆⁄ªØÑI¿Ì
		super.spawn(charaID,charaTeam,x,y);
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
		final double X,Y,ANGLE;
		if(source == this) {
			X = charaX;
			Y = charaY;
			ANGLE = charaShotAngle;
		}else {
			X = source.getX();
			Y = source.getY();
			ANGLE = source.getAngle();
		}
		switch(kind){
		case MILLKY_WAY:
			BulletInfo.name = "MILLKY_WAY";
			BulletInfo.script = bulletScripts[MILLKY_WAY];
			BulletInfo.fastParaSet_ASpd(ANGLE,20);
			BulletInfo.accel = 1.0;
			BulletInfo.size = 30;
			BulletInfo.atk = 40;
			BulletInfo.offSet = 20;
			BulletInfo.penetration = 0;
			BulletInfo.reflection = 0;
			BulletInfo.limitFrame = 200;
			BulletInfo.limitRange = MAX;
			BulletInfo.imageID = bulletIID[MILLKY_WAY];
			THH.createBullet_RoundDesign(source,8,X,Y,50);
			break;
		case NARROW_SPARK:
			//message
			//if(this == source) {
			//	THH.addMessage(this,charaID,"KOIFU [MasterSpark]");
			//	THH.addMessage(this,charaID,"TEST MESSAGE 2");
			//}
			BulletInfo.name = "NARROW_SPARK";
			BulletInfo.script = bulletScripts[NARROW_SPARK];
			BulletInfo.fastParaSet_XYADSpd(X,Y,ANGLE,0,thh.getImageByID(bulletIID[NARROW_SPARK]).getWidth(null));
			BulletInfo.accel = 1.0;
			BulletInfo.size = 15;
			BulletInfo.atk = 10;
			BulletInfo.offSet = 20;
			BulletInfo.penetration = MAX;
			BulletInfo.reflection = 3;
			BulletInfo.limitFrame = 80;
			BulletInfo.limitRange = MAX;
			BulletInfo.imageID = bulletIID[NARROW_SPARK];
			BulletInfo.isLaser = true;
			THH.createBullet(source);
			break;
		case REUSE_BOMB:
			BulletInfo.name = "REUSE_BOMB";
			BulletInfo.script = bulletScripts[REUSE_BOMB];
			BulletInfo.fastParaSet_ASpd(ANGLE,40);
			BulletInfo.accel = 0.98;
			BulletInfo.size = 30;
			BulletInfo.atk = 40;
			BulletInfo.offSet = 20;
			BulletInfo.penetration = 0;
			BulletInfo.reflection = 0;
			BulletInfo.limitFrame = 200;
			BulletInfo.limitRange = MAX;
			BulletInfo.imageID = bulletIID[REUSE_BOMB];
			THH.createBullet_RoundDesign(source,3,X,Y,50);
			break;
		case MAGIC_MISSILE:
			BulletInfo.name = "MAGIC_MISSILE";
			BulletInfo.script = bulletScripts[MAGIC_MISSILE];
			BulletInfo.fastParaSet_XYADSpd(X,Y,ANGLE + THH.random2(-PI/36, PI/36),10,10);
			BulletInfo.accel = 1.2;
			BulletInfo.size = 20;
			BulletInfo.atk = 200;
			BulletInfo.offSet = 100;
			BulletInfo.penetration = 0;
			BulletInfo.reflection = 0;
			BulletInfo.limitFrame = 2000;
			BulletInfo.limitRange = MAX;
			BulletInfo.imageID = bulletIID[MAGIC_MISSILE];
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
			EffectInfo.fastParaSet_XYADSpd(source.getX(),source.getY(),2*PI*random(),10,20);
			EffectInfo.accel = 1.0;
			EffectInfo.size = NONE;
			EffectInfo.limitFrame = 2;
			EffectInfo.limitRange = MAX;
			EffectInfo.imageID = effectIID[LIGHTNING];
			THH.createEffect(this);
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
				while(THH.inStage((int)bullet.getX(),(int)bullet.getY())) {
					bullet.dynam();
					bullet.paint();
				}
				bullet.setX(bullet.SOURCE.getX());
				bullet.setY(bullet.SOURCE.getY());
				bullet.setAngle(bullet.SOURCE.getAngle());
			}
		};
		bulletScripts[REUSE_BOMB] = new BulletScript() {
			@Override
			public final void bulletIdle(Bullet bullet) {
				bullet.idle();
				bullet.addSpeed(0.0,1.1);
				bullet.paint();
				if(random() < 0.2)
					setEffect(LIGHTNING,(DynamInteractable)bullet);
			}
		};
		bulletScripts[MAGIC_MISSILE] = BulletInfo.DEFAULT_SCRIPT;
	}
	private final EffectScript[] effectScripts = new EffectScript[10];
	{
		effectScripts[LIGHTNING] = EffectInfo.DEFAULT_SCRIPT;
	}
}
