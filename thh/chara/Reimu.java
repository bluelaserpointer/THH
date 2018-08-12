package chara;

import static java.lang.Math.PI;
import static java.lang.Math.random;

import bullet.Bullet;
import bullet.BulletInfo;
import effect.Effect;
import effect.EffectInfo;

public class Reimu extends THHOriginal{
	@Override
	public final String getName() {
		return "Reimu";
	}
	
	//weapon&bullet kind name
	final int
		MILLKY_WAY = 0,NARROW_SPARK = 1,REUSE_BOMB = 2;
	
	//effect kind name
	private final int LIGHTNING = 0,NARROW_SPARK_HE = 1;
	
	//GUI
		
	//Sounds
	
	@Override
	final protected void loadImageData(){ //ª≠œÒ’i§ﬂﬁz§ﬂ
		super.loadImageData();
		charaIID = thh.loadImage("Reimu.png");
		bulletIID[MILLKY_WAY] = thh.loadImage("MillkyWay.png");
		bulletIID[NARROW_SPARK] = thh.loadImage("NarrowSpark_2.png");
		bulletIID[REUSE_BOMB] = thh.loadImage("ReuseBomb.png");
		effectIID[LIGHTNING] = thh.loadImage("ReuseBomb_Effect.png");
		effectIID[NARROW_SPARK_HE] = thh.loadImage("NarrowSpark_HitEffect.png");
	}
	
	@Override
	final protected void loadSoundData(){ //•µ•¶•Û•…’i§ﬂﬁz§ﬂ
	}
	
	//Initialization
	@Override
	final protected void battleStarted(int charaID){
		//test area
		weaponSlot[0] = MILLKY_WAY;
		weaponSlot[1] = NARROW_SPARK;
		weaponSlot[2] = REUSE_BOMB;
		////
		slot = 0;
	}
	@Override
	final protected void spawn(int charaID,int charaTeam,int x,int y){ //≥ı∆⁄ªØÑI¿Ì
		super.spawn(charaID,charaTeam,x,y);
		charaHP = super.charaBaseHP = 10000;
		charaME = charaBaseME = 100;
		charaSize = 70;
		charaJumpLimit = 300;
	}
	@Override
	final protected void turnStarted(){
		super.turnStarted();
		charaJumpLimit = 300;
	}
	
	//behavior
	@Override
	protected final void guardOrder(int targetX,int targetY) {
		
	}
	//bullet
	@Override
	final protected void bulletSpawn(int kind) {
		thh.prepareBulletInfo(charaID);
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
			thh.createBullet_RoundDesign(8,charaX,charaY,50);
			break;
		case NARROW_SPARK:
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
			thh.createBullet();
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
			thh.createBullet_RoundDesign(3,charaX,charaY,50);
			break;
		}
	}
	@Override
	final protected void effectSpawn(int kind) {
		thh.prepareEffectInfo(charaID);
		switch(kind){
		case LIGHTNING:
			EffectInfo.name = "LIGHTNING";
			EffectInfo.fastParaSet_XYADSpd(charaX,charaY,2*PI*random(),10,20);
			EffectInfo.accel = 1.0;
			EffectInfo.size = NONE;
			EffectInfo.limitFrame = 8;
			EffectInfo.limitRange = MAX;
			EffectInfo.imageID = effectIID[LIGHTNING];
			thh.createBullet();
			break;
		}
	}
	@Override
	final protected void bulletIdle(Bullet bullet,boolean isCharaActive) {
		switch(bullet.KIND) {
		case MILLKY_WAY:
			bullet.defaultIdle(thh);
			bullet.defaultPaint(thh);
			break;
		case NARROW_SPARK: //laser action
			while(thh.inStage((int)bullet.x,(int)bullet.y) && bullet.defaultIdle(thh))
				bullet.defaultPaint(thh);
			bullet.x = charaX;
			bullet.y = charaY;
			break;
		case REUSE_BOMB:
			bullet.defaultIdle(thh);
			bullet.setSpeedY(bullet.getSpeedY() + 1.1);
			bullet.defaultPaint(thh);
			if(random() < 0.2)
				effectSpawn(LIGHTNING);
			break;
		}
	}
	@Override
	final protected boolean deleteBullet(Bullet bullet) {
		switch(bullet.KIND) {
		case REUSE_BOMB:
			break;
		}
		return true;
	}
	//effect
	@Override
	final protected void effectIdle(Effect effect,boolean isCharaActive) {
		switch(effect.KIND) {
		case LIGHTNING:
			break;
		case NARROW_SPARK_HE:
			break;
		}
	}
	@Override
	final public boolean deleteEffect(Effect effect) {
		switch(effect.KIND) {
		case LIGHTNING:
			effect.name = "LIGHTNING_D";
			return false;
		case NARROW_SPARK_HE:
			return true;
		}
		return true;
	}
}
