package chara;

import static java.lang.Math.*;

import bullet.BulletInfo;
import bullet.BulletScript;
import effect.EffectInfo;
import effect.EffectScript;
import thh.DynamInteractable;
import thh.THH;
import weapon.Weapon;
import weapon.WeaponInfo;

public class Reimu extends UserChara{
	{
		charaSize = 70;
	}
	@Override
	public final String getName() {
		return "Reimu";
	}
	
	//weapon&bullet kind name
	final int
		FUDA_KOUHAKU = 0,FUDA_SHIROKURO = 1,FUDA_SOUHAKU = 2;
	private final Weapon weaponController[] = new Weapon[10];
	//effect kind name
	private final int LIGHTNING = 0,FUDA_SHIROKURO_HE = 1;
	
	//GUI
		
	//Sounds
	
	@Override
	public final void loadImageData(){ //ª≠œÒ’i§ﬂﬁz§ﬂ
		super.loadImageData();
		charaIID = thh.loadImage("Reimu.png");
		bulletIID[FUDA_KOUHAKU] = thh.loadImage("KouhakuNoFuda.png");
		bulletIID[FUDA_SHIROKURO] = thh.loadImage("ShirokuroNoFuda.png");
		bulletIID[FUDA_SOUHAKU] = thh.loadImage("SouhakuNoFuda.png");
		effectIID[LIGHTNING] = thh.loadImage("ReuseBomb_Effect.png");
		effectIID[FUDA_SHIROKURO_HE] = thh.loadImage("NarrowSpark_HitEffect.png");
	}
	
	@Override
	public final void loadSoundData(){ //•µ•¶•Û•…’i§ﬂﬁz§ﬂ
	}
	
	//Initialization
	@Override
	public final void battleStarted(){
		//test area
		weaponSlot[0] = FUDA_KOUHAKU;
		spellSlot[0] = FUDA_SHIROKURO;
		spellSlot[1] = FUDA_SOUHAKU;
		////weaponLoad
		//FUDA_KOUHAKU
		WeaponInfo.clear();
		WeaponInfo.name = "FUDA_KOUHAKU";
		WeaponInfo.coolTime = 1;
		WeaponInfo.reloadTime = 100;
		WeaponInfo.magazineSize = 180;
		WeaponInfo.magazineConsumption = 2;
		weaponController[FUDA_KOUHAKU] = new Weapon();
		//FUDA_SHIROKURO
		WeaponInfo.clear();
		WeaponInfo.name = "FUDA_SHIROKURO";
		WeaponInfo.coolTime = 2;
		WeaponInfo.reloadTime = 100;
		WeaponInfo.magazineSize = 180;
		WeaponInfo.magazineConsumption = 2;
		weaponController[FUDA_SHIROKURO] = new Weapon();
		//FUDA_SOUHAKU
		WeaponInfo.clear();
		WeaponInfo.name = "FUDA_SOUHAKU";
		WeaponInfo.coolTime = 12;
		WeaponInfo.magazineSize = MAX;
		weaponController[FUDA_SOUHAKU] = new Weapon();
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
		case FUDA_KOUHAKU:
			BulletInfo.name = "FUDA_KOUHAKU";
			BulletInfo.size = 20;
			BulletInfo.atk = 30;
			BulletInfo.offSet = 5;
			BulletInfo.limitFrame = 200;
			BulletInfo.limitRange = MAX;
			BulletInfo.imageID = bulletIID[FUDA_KOUHAKU];
			final double DX = 50*cos(ANGLE + PI/2),DY = 50*sin(ANGLE + PI/2);
			final double SHOT_ANGLE = ANGLE + THH.random2(-PI/50, PI/50);
			BulletInfo.fastParaSet_XYADSpd(X + DX,Y + DY,SHOT_ANGLE,10,40);
			THH.createBullet(this);
			BulletInfo.fastParaSet_XYADSpd(X - DX,Y - DY,SHOT_ANGLE,10,40);
			THH.createBullet(this);
			break;
		case FUDA_SHIROKURO:
			BulletInfo.name = "FUDA_SHIROKURO";
			BulletInfo.size = 10;
			BulletInfo.atk = 20;
			BulletInfo.offSet = 3;
			BulletInfo.reflection = 1;
			BulletInfo.limitFrame = 200;
			BulletInfo.imageID = bulletIID[FUDA_SHIROKURO];
			BulletInfo.fastParaSet_XYADSpd(X,Y,ANGLE,10,4);
			THH.createBullet(this);
			BulletInfo.fastParaSet_XYADSpd(X,Y,ANGLE - PI/18,10,4);
			THH.createBullet(this);
			BulletInfo.fastParaSet_XYADSpd(X,Y,ANGLE + PI/18,10,4);
			THH.createBullet(this);
			break;
		case FUDA_SOUHAKU:
			BulletInfo.name = "FUDA_SOUHAKU";
			BulletInfo.accel = 0.8;
			BulletInfo.size = 10;
			BulletInfo.atk = 25;
			BulletInfo.offSet = 25;
			BulletInfo.reflection = 1;
			BulletInfo.limitFrame = 40;
			BulletInfo.imageID = bulletIID[FUDA_SOUHAKU];
			for(int i = -4;i <= 4;i++) {
				BulletInfo.fastParaSet_XYADSpd(X,Y,ANGLE + i*PI/10,25,40);
				THH.createBullet(this);
			}
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
			EffectInfo.script = effectScripts[LIGHTNING];
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
		bulletScripts[FUDA_KOUHAKU] = BulletInfo.DEFAULT_SCRIPT;
		bulletScripts[FUDA_SHIROKURO] = BulletInfo.DEFAULT_SCRIPT;
		bulletScripts[FUDA_SOUHAKU] = BulletInfo.DEFAULT_SCRIPT;
	}
	private final EffectScript[] effectScripts = new EffectScript[10];
	{
		effectScripts[LIGHTNING] = EffectInfo.DEFAULT_SCRIPT;
	}
}
