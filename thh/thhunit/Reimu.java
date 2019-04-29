package thhunit;

import static java.lang.Math.*;

import core.GHQ;
import paint.ImageFrame;
import physicis.HasDynam;
import weapon.Weapon;
import weapon.WeaponInfo;

public class Reimu extends THH_BasicUnit{
	public Reimu(int initialGroup) {
		super(20, initialGroup);
	}

	private static final long serialVersionUID = 1669960313477709935L;
	@Override
	public final String getName() {
		return "Reimu";
	}
	
	//weapon&bullet kind name
	static final int
		FUDA_KOUHAKU = 0,FUDA_SHIROKURO = 1,FUDA_SOUHAKU = 2;
	//effect kind name
	private static final int LIGHTNING = 0,FUDA_HIT_EF = 1;
	
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
		weapon[FUDA_KOUHAKU] = new Weapon();
		//FUDA_SHIROKURO
		WeaponInfo.clear();
		WeaponInfo.name = "FUDA_SHIROKURO";
		WeaponInfo.coolTime = 2;
		weapon[FUDA_SHIROKURO] = new Weapon();
		//FUDA_SOUHAKU
		WeaponInfo.clear();
		WeaponInfo.name = "FUDA_SOUHAKU";
		WeaponInfo.coolTime = 12;
		weapon[FUDA_SOUHAKU] = new Weapon();
		/////////////////////
		slot_spell = 0;
	}
	@Override
	public final void respawn(int x,int y){
		super.respawn(x,y);
		for(Weapon ver : weapon) {
			if(ver != null)
				ver.reset();
		}
	}
	@Override
	public void activeCons() {
		super.activeCons();
		for(Weapon ver : weapon) {
			if(ver != null)
				ver.idle();
		}
	}
	//bullet
	@Override
	public final void setBullet(int kind,HasDynam user) {
		switch(kind){
		case FUDA_KOUHAKU:
			GHQ.addBullet(new THH_BulletLibrary.FudaKouhaku(this)).split_xMirror(6, 2);
			GHQ.addBullet(new THH_BulletLibrary.FudaKouhaku(this)).split_xMirror(6, 7);
			break;
		case FUDA_SHIROKURO:
			GHQ.addBullet(new THH_BulletLibrary.FudaShiroKuro(this)).split_NWay(10,PI/18,3,40);
			break;
		case FUDA_SOUHAKU:
			GHQ.addBullet(new THH_BulletLibrary.FudaSouhaku(this)).split_NWay(25,PI/10,8,40);
			break;
		}
	}
}
