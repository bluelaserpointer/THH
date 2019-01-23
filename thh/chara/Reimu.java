package chara;

import static java.lang.Math.*;

import bullet.Bullet;
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
		charaSize = 20;
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
	private final int LIGHTNING = 0,FUDA_HIT_EF = 1;
	
	//Images
		
	//Sounds
	
	@Override
	public final void loadImageData(){ //ª≠œÒ’i§ﬂﬁz§ﬂ
		super.loadImageData();
		charaIID = thh.loadImage("Reimu.png");
		bulletIID[FUDA_KOUHAKU] = thh.loadImage("KouhakuNoFuda.png");
		bulletIID[FUDA_SHIROKURO] = thh.loadImage("ShirokuroNoFuda.png");
		bulletIID[FUDA_SOUHAKU] = thh.loadImage("SouhakuNoFuda.png");
		effectIID[LIGHTNING] = thh.loadImage("ReuseBomb_Effect.png");
		effectIID[FUDA_HIT_EF] = thh.loadImage("FudaHitEffect.png");
	}
	
	@Override
	public final void loadSoundData(){ //•µ•¶•Û•…’i§ﬂﬁz§ﬂ
	}
	
	//Initialization
	@Override
	public final void battleStarted(){
		//test area
		weaponSlot[0] = FUDA_KOUHAKU;
		spellSlot[0] = FUDA_SOUHAKU;
		////weaponLoad
		//FUDA_KOUHAKU
		WeaponInfo.clear();
		WeaponInfo.name = "FUDA_KOUHAKU";
		WeaponInfo.coolTime = 6;
		weaponController[FUDA_KOUHAKU] = new Weapon();
		//FUDA_SHIROKURO
		WeaponInfo.clear();
		WeaponInfo.name = "FUDA_SHIROKURO";
		WeaponInfo.coolTime = 2;
		weaponController[FUDA_SHIROKURO] = new Weapon();
		//FUDA_SOUHAKU
		WeaponInfo.clear();
		WeaponInfo.name = "FUDA_SOUHAKU";
		WeaponInfo.coolTime = 12;
		weaponController[FUDA_SOUHAKU] = new Weapon();
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
		case FUDA_KOUHAKU:
			BulletInfo.name = "FUDA_KOUHAKU";
			BulletInfo.script = bulletScripts[FUDA_KOUHAKU];
			BulletInfo.size = 10;
			BulletInfo.atk = 50;
			BulletInfo.offSet = 5;
			BulletInfo.limitFrame = 200;
			BulletInfo.imageID = bulletIID[FUDA_KOUHAKU];
			BulletInfo.fastParaSet_SourceDXYSpd(source,7,16,28);
			THH.createBullet(this);
			BulletInfo.fastParaSet_SourceDXYSpd(source,5,21,28);
			THH.createBullet(this);
			BulletInfo.fastParaSet_SourceDXYSpd(source,-5,21,28);
			THH.createBullet(this);
			BulletInfo.fastParaSet_SourceDXYSpd(source,-7,16,28);
			THH.createBullet(this);
			break;
		case FUDA_SHIROKURO:
			BulletInfo.name = "FUDA_SHIROKURO";
			BulletInfo.script = bulletScripts[FUDA_SHIROKURO];
			BulletInfo.size = 10;
			BulletInfo.atk = 20;
			BulletInfo.offSet = 3;
			BulletInfo.reflection = 1;
			BulletInfo.limitFrame = 200;
			BulletInfo.imageID = bulletIID[FUDA_SHIROKURO];
			BulletInfo.fastParaSet_SourceDSpd(source,10,40);
			THH.createBullet(this);
			BulletInfo.fastParaSet_SourceADSpd(source,-PI/18,10,40);
			THH.createBullet(this);
			BulletInfo.fastParaSet_SourceADSpd(source,PI/18,10,40);
			THH.createBullet(this);
			break;
		case FUDA_SOUHAKU:
			BulletInfo.name = "FUDA_SOUHAKU";
			BulletInfo.script = bulletScripts[FUDA_SOUHAKU];
			BulletInfo.accel = 0.8;
			BulletInfo.size = 15;
			BulletInfo.atk = 25;
			BulletInfo.offSet = 25;
			BulletInfo.reflection = 1;
			BulletInfo.limitFrame = 40;
			BulletInfo.imageID = bulletIID[FUDA_SOUHAKU];
			for(int i = -4;i <= 4;i++) {
				BulletInfo.fastParaSet_SourceADSpd(source,i*PI/10,25,40);
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
			EffectInfo.fastParaSet_SourceADSpd(source,2*PI*random(),10,20);
			EffectInfo.accel = 1.0;
			EffectInfo.size = NONE;
			EffectInfo.limitFrame = 2;
			EffectInfo.limitRange = MAX;
			EffectInfo.imageID = effectIID[LIGHTNING];
			THH.createEffect(this);
			break;
		case FUDA_HIT_EF:
			EffectInfo.name = "FUDA_HIT_EF";
			EffectInfo.script = effectScripts[FUDA_HIT_EF];
			EffectInfo.accel = 0.5;
			EffectInfo.size = NONE;
			EffectInfo.limitFrame = 4;
			EffectInfo.limitRange = MAX;
			EffectInfo.imageID = effectIID[FUDA_HIT_EF];
			for(int i = 0;i < 15;i++) {
				EffectInfo.fastParaSet_SourceADSpd(source,2*PI*random(),10,THH.random2(0,22));
				THH.createEffect(this);
			}
			break;
		}
	}
	private final BulletScript[] bulletScripts = new BulletScript[10];
	{
		bulletScripts[FUDA_KOUHAKU] = new BulletScript() {
			@Override
			public final void bulletHitObject(Bullet bullet) {
				setEffect(FUDA_HIT_EF,(DynamInteractable)bullet);
			}
		};
		bulletScripts[FUDA_SHIROKURO] = BulletInfo.DEFAULT_SCRIPT;
		bulletScripts[FUDA_SOUHAKU] = BulletInfo.DEFAULT_SCRIPT;
	}
	private final EffectScript[] effectScripts = new EffectScript[10];
	{
		effectScripts[LIGHTNING] = EffectInfo.DEFAULT_SCRIPT;
		effectScripts[FUDA_HIT_EF] = EffectInfo.DEFAULT_SCRIPT;
	}
}
