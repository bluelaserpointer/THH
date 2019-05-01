package thhunit;

import static java.lang.Math.*;

import core.GHQ;
import paint.ImageFrame;
import physicis.HasDynam;
import weapon.Weapon;
import weapon.WeaponInfo;

public class Reimu extends THH_BasicPlayer{
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
	
	//Images
		
	//Sounds
	
	@Override
	public final void loadImageData(){
		super.loadImageData();
		charaPaint = new ImageFrame("thhimage/Reimu.png");
		iconPaint = new ImageFrame("thhimage/ReimuIcon.png");
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
	public final void extendIdle() {
		dynam.setAngle(dynam.getMouseAngle());
	}
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
