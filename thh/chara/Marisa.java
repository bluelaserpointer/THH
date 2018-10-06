package chara;

import static java.lang.Math.PI;
import static java.lang.Math.random;

import bullet.Bullet;
import bullet.BulletInfo;
import effect.Effect;
import effect.EffectInfo;
import thh.THH;
import weapon.Weapon;
import weapon.WeaponInfo;

public class Marisa extends THHOriginal{
	@Override
	public final String getName() {
		return "Marisa";
	}
	
	//weapon&bullet kind name
	static final int
		MILLKY_WAY = 0,NARROW_SPARK = 1,REUSE_BOMB = 2,MAGIC_MISSILE = 3;
	private final Weapon weaponController[] = new Weapon[10];
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
		WeaponInfo.reloadTime = 100;
		WeaponInfo.magazineSize = 180;
		WeaponInfo.magazineConsumption = 6;
		weaponController[MILLKY_WAY] = new Weapon();
		//NARROW_SPARK
		WeaponInfo.clear();
		WeaponInfo.name = "NARROW_SPARK";
		WeaponInfo.coolTime = 10;
		WeaponInfo.reloadTime = 1000;
		WeaponInfo.magazineSize = 2;
		WeaponInfo.magazineConsumption = 1;
		weaponController[NARROW_SPARK] = new Weapon();
		//REUSE_BOMB
		WeaponInfo.clear();
		WeaponInfo.name = "REUSE_BOMB";
		WeaponInfo.coolTime = 10;
		WeaponInfo.reloadTime = 100;
		WeaponInfo.magazineSize = 90;
		WeaponInfo.magazineConsumption = 3;
		weaponController[REUSE_BOMB] = new Weapon();
		//MAGIC_MISSILE
		WeaponInfo.clear();
		WeaponInfo.name = "MAGIC_MISSILE";
		WeaponInfo.coolTime = 30;
		WeaponInfo.magazineSize = MAX;
		weaponController[MAGIC_MISSILE] = new Weapon();
		/////////////////////
		slot_spell = 0;
	}
	@Override
	public final void spawn(int charaID,int charaTeam,int x,int y){ //≥ı∆⁄ªØÑI¿Ì
		super.spawn(charaID,charaTeam,x,y);
		charaHP = super.charaBaseHP = 10000;
		charaME = charaBaseME = 100;
		charaSize = 70;
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
	public final void useWeapon(int kind) {
		super.useWeapon(kind);
		BulletInfo.team = charaTeam;
		switch(kind){
		case MILLKY_WAY:
			if(!weaponController[MILLKY_WAY].trigger())
				break;
			BulletInfo.name = "MILLKY_WAY";
			BulletInfo.fastParaSet_ASpd(charaShotAngle,20);
			BulletInfo.accel = 1.0;
			BulletInfo.size = 30;
			BulletInfo.atk = 40;
			BulletInfo.offSet = 20;
			BulletInfo.penetration = 0;
			BulletInfo.reflection = 0;
			BulletInfo.limitFrame = 200;
			BulletInfo.limitRange = MAX;
			BulletInfo.imageID = bulletIID[MILLKY_WAY];
			THH.createBullet_RoundDesign(this,8,charaX,charaY,50);
			break;
		case NARROW_SPARK:
			if(!weaponController[NARROW_SPARK].trigger())
				break;
			//message
			THH.addMessage(this,charaID,"KOIFU [MasterSpark]");
			THH.addMessage(this,charaID,"TEST MESSAGE 2");
			BulletInfo.name = "NARROW_SPARK";
			BulletInfo.fastParaSet_XYADSpd(charaX,charaY,charaShotAngle,0,thh.getImageByID(bulletIID[NARROW_SPARK]).getWidth(null));
			BulletInfo.accel = 1.0;
			BulletInfo.size = 50;
			BulletInfo.atk = 5;
			BulletInfo.offSet = 20;
			BulletInfo.penetration = MAX;
			BulletInfo.reflection = 3;
			BulletInfo.limitFrame = 30;
			BulletInfo.limitRange = MAX;
			BulletInfo.imageID = bulletIID[NARROW_SPARK];
			BulletInfo.isLaser = true;
			THH.createBullet(this);
			break;
		case REUSE_BOMB:
			if(!weaponController[REUSE_BOMB].trigger())
				break;
			BulletInfo.name = "REUSE_BOMB";
			BulletInfo.fastParaSet_ASpd(charaShotAngle,40);
			BulletInfo.accel = 0.98;
			BulletInfo.size = 30;
			BulletInfo.atk = 40;
			BulletInfo.offSet = 20;
			BulletInfo.penetration = 0;
			BulletInfo.reflection = 0;
			BulletInfo.limitFrame = 200;
			BulletInfo.limitRange = MAX;
			BulletInfo.imageID = bulletIID[REUSE_BOMB];
			THH.createBullet_RoundDesign(this,3,charaX,charaY,50);
			break;
		case MAGIC_MISSILE:
			if(!weaponController[MAGIC_MISSILE].trigger())
				break;
			BulletInfo.name = "MAGIC_MISSILE";
			BulletInfo.fastParaSet_XYADSpd(charaX,charaY,charaShotAngle + THH.random2(-PI/36, PI/36),10,10);
			BulletInfo.accel = 1.2;
			BulletInfo.size = 50;
			BulletInfo.atk = 200;
			BulletInfo.offSet = 100;
			BulletInfo.penetration = 0;
			BulletInfo.reflection = 0;
			BulletInfo.limitFrame = 2000;
			BulletInfo.limitRange = MAX;
			BulletInfo.imageID = bulletIID[MAGIC_MISSILE];
			THH.createBullet(this);
			break;
		}
	}
	@Override
	public final void useEffect(int kind,double x,double y) {
		super.useEffect(kind,x,y);
		switch(kind){
		case LIGHTNING:
			EffectInfo.name = "LIGHTNING";
			EffectInfo.fastParaSet_XYADSpd(x,y,2*PI*random(),10,20);
			EffectInfo.accel = 1.0;
			EffectInfo.size = NONE;
			EffectInfo.limitFrame = 2;
			EffectInfo.limitRange = MAX;
			EffectInfo.imageID = effectIID[LIGHTNING];
			THH.createEffect(this);
			break;
		}
	}
	@Override
	public final void bulletIdle(Bullet bullet,boolean isCharaActive) {
		switch(bullet.KIND) {
		case MILLKY_WAY:
			super.bulletIdle(bullet, isCharaActive);
			break;
		case NARROW_SPARK: //laser action
			while(THH.inStage((int)bullet.x,(int)bullet.y) && bullet.idle())
				bullet.paint();
			bullet.x = charaX;
			bullet.y = charaY;
			break;
		case REUSE_BOMB:
			bullet.idle();
			bullet.addSpeed(0.0,1.1);
			bullet.paint();
			if(random() < 0.2)
				useEffect(LIGHTNING,bullet.x,bullet.y);
			break;
		case MAGIC_MISSILE:
			super.bulletIdle(bullet, isCharaActive);
			break;
		}
	}
	@Override
	public final void effectIdle(Effect effect,boolean isCharaActive) {
		switch(effect.KIND) {
		default:
			super.effectIdle(effect, isCharaActive);
		}
	}
}
