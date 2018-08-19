package chara;

import static java.lang.Math.PI;
import static java.lang.Math.random;

import bullet.Bullet;
import bullet.BulletInfo;
import effect.Effect;
import effect.EffectInfo;
import thh.THH;

public class Marisa extends THHOriginal{
	@Override
	public final String getName() {
		return "Marisa";
	}
	
	//weapon&bullet kind name
	final int
		MILLKY_WAY = 0,NARROW_SPARK = 1,REUSE_BOMB = 2;
	
	//effect kind name
	private final int LIGHTNING = 0,NARROW_SPARK_HE = 1;
	
	//GUI
		
	//Sounds
	
	@Override
	public final void loadImageData(){ //ª≠œÒ’i§ﬂﬁz§ﬂ
		super.loadImageData();
		charaIID = thh.loadImage("Marisa.png");
		bulletIID[MILLKY_WAY] = thh.loadImage("MillkyWay.png");
		bulletIID[NARROW_SPARK] = thh.loadImage("NarrowSpark_2.png");
		bulletIID[REUSE_BOMB] = thh.loadImage("ReuseBomb.png");
		effectIID[LIGHTNING] = thh.loadImage("ReuseBomb_Effect.png");
		effectIID[NARROW_SPARK_HE] = thh.loadImage("NarrowSpark_HitEffect.png");
	}
	
	@Override
	public final void loadSoundData(){ //•µ•¶•Û•…’i§ﬂﬁz§ﬂ
	}
	
	//Initialization
	@Override
	public final void battleStarted(int charaID){
		//test area
		weaponSlot[0] = REUSE_BOMB;
		spellSlot[0] = NARROW_SPARK;
		spellSlot[1] = REUSE_BOMB;
		////
		slot_spell = 0;
	}
	@Override
	public final void spawn(int charaID,int charaTeam,int x,int y){ //≥ı∆⁄ªØÑI¿Ì
		super.spawn(charaID,charaTeam,x,y);
		charaHP = super.charaBaseHP = 10000;
		charaME = charaBaseME = 100;
		charaSize = 70;
	}
	@Override
	public final void turnStarted(){
		super.turnStarted();
	}
	
	//bullet
	@Override
	public final void bulletSpawn(int kind) {
		super.bulletSpawn(kind);
		BulletInfo.team = charaTeam;
		switch(kind){
		case MILLKY_WAY:
			BulletInfo.name = "MILLKY_WAY";
			BulletInfo.fastParaSet_ADSpd(charaShotAngle,10,20);
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
			//message
			THH.addMessage(charaID,"KOIFU [MasterSpark]");
			THH.addMessage(charaID,"TEST MESSAGE 2");
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
			BulletInfo.name = "REUSE_BOMB";
			BulletInfo.fastParaSet_ADSpd(charaShotAngle,10,40);
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
		}
	}
	@Override
	public final void effectSpawn(int kind,double x,double y) {
		super.effectSpawn(kind,x,y);
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
			while(THH.inStage((int)bullet.x,(int)bullet.y) && bullet.defaultIdle())
				bullet.defaultPaint();
			bullet.x = charaX;
			bullet.y = charaY;
			break;
		case REUSE_BOMB:
			bullet.defaultIdle();
			bullet.setSpeedY(bullet.getSpeedY() + 1.1);
			bullet.defaultPaint();
			if(random() < 0.2)
				effectSpawn(LIGHTNING,bullet.x,bullet.y);
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
