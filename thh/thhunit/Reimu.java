package thhunit;

import static java.lang.Math.*;

import bullet.Bullet;
import bullet.BulletBlueprint;
import bullet.BulletScript;
import core.DynamInteractable;
import core.GHQ;
import effect.EffectBlueprint;
import effect.EffectScript;
import paint.ImageFrame;
import weapon.Weapon;
import weapon.WeaponInfo;

public class Reimu extends THHUnit{
	public Reimu(int initialGroup) {
		super(initialGroup);
	}

	private static final long serialVersionUID = 1669960313477709935L;
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
	public final void loadImageData(){
		super.loadImageData();
		charaPaint = new ImageFrame("thhimage/Reimu.png");
		iconPaint = new ImageFrame("thhimage/ReimuIcon.png");
		bulletPaint[FUDA_KOUHAKU] = new ImageFrame("thhimage/KouhakuNoFuda.png");
		bulletPaint[FUDA_SHIROKURO] = new ImageFrame("thhimage/ShirokuroNoFuda.png");
		bulletPaint[FUDA_SOUHAKU] = new ImageFrame("thhimage/SouhakuNoFuda.png");
		effectPaint[LIGHTNING] = new ImageFrame("thhimage/ReuseBomb_Effect.png");
		effectPaint[FUDA_HIT_EF] = new ImageFrame("thhimage/FudaHitEffect.png");
	}
	
	@Override
	public final void loadSoundData(){
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
	public final void setBullet(int kind,DynamInteractable user) {
		BulletBlueprint.clear(bulletScripts[kind],user.getDynam());
		BulletBlueprint.standpointGroup = standpoint.get();
		switch(kind){
		case FUDA_KOUHAKU:
			BulletBlueprint.name = "FUDA_KOUHAKU";
			BulletBlueprint.size = 10;
			BulletBlueprint.atk = 50;
			BulletBlueprint.offSet = 5;
			BulletBlueprint.limitFrame = 200;
			BulletBlueprint.paintScript = bulletPaint[FUDA_KOUHAKU];
			BulletBlueprint.dynam.setSpeed(28);
			GHQ.createBullet(user).split_xMirror(7, 7);
			GHQ.createBullet(user).split_xMirror(7, 14);
			break;
		case FUDA_SHIROKURO:
			BulletBlueprint.name = "FUDA_SHIROKURO";
			BulletBlueprint.size = 10;
			BulletBlueprint.atk = 20;
			BulletBlueprint.offSet = 3;
			BulletBlueprint.reflection = 1;
			BulletBlueprint.limitFrame = 200;
			BulletBlueprint.paintScript = bulletPaint[FUDA_SHIROKURO];
			GHQ.createBullet(user).split_NWay(10,PI/18,3,40);
			break;
		case FUDA_SOUHAKU:
			BulletBlueprint.name = "FUDA_SOUHAKU";
			BulletBlueprint.accel = -2.0;
			BulletBlueprint.size = 15;
			BulletBlueprint.atk = 25;
			BulletBlueprint.offSet = 25;
			BulletBlueprint.reflection = 1;
			BulletBlueprint.limitFrame = 40;
			BulletBlueprint.paintScript = bulletPaint[FUDA_SOUHAKU];
			GHQ.createBullet(this).split_NWay(25,PI/10,8,40);
			break;
		}
	}
	@Override
	public final void setEffect(int kind,DynamInteractable user) {
		EffectBlueprint.clear(effectScripts[kind],user.getDynam());
		switch(kind){
		case LIGHTNING:
			EffectBlueprint.name = "LIGHTNING";
			EffectBlueprint.dynam.fastParaAdd_DASpd(10,2*PI*random(),20);
			EffectBlueprint.accel = 1.0;
			EffectBlueprint.limitFrame = 2;
			EffectBlueprint.paintScript = effectPaint[LIGHTNING];
			GHQ.createEffect(this);
			break;
		case FUDA_HIT_EF:
			EffectBlueprint.name = "FUDA_HIT_EF";
			EffectBlueprint.accel = 0.5;
			EffectBlueprint.limitFrame = 4;
			EffectBlueprint.paintScript = effectPaint[FUDA_HIT_EF];
			for(int i = 0;i < 15;i++) {
				EffectBlueprint.dynam.setXY(user);
				EffectBlueprint.dynam.fastParaAdd_DASpd(10,2*PI*random(),GHQ.random2(0,22));
				GHQ.createEffect(this);
			}
			break;
		}
	}
	private static transient final BulletScript[] bulletScripts = new BulletScript[10];
	{
		bulletScripts[FUDA_KOUHAKU] = new BulletScript() {
			@Override
			public final void bulletHitObject(Bullet bullet) {
				setEffect(FUDA_HIT_EF,(DynamInteractable)bullet);
			}
		};
		bulletScripts[FUDA_SHIROKURO] = BulletBlueprint.DEFAULT_SCRIPT;
		bulletScripts[FUDA_SOUHAKU] = BulletBlueprint.DEFAULT_SCRIPT;
	}
	private static transient final EffectScript[] effectScripts = new EffectScript[10];
	{
		effectScripts[LIGHTNING] = EffectBlueprint.DEFAULT_SCRIPT;
		effectScripts[FUDA_HIT_EF] = EffectBlueprint.DEFAULT_SCRIPT;
	}
}
