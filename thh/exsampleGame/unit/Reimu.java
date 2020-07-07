package exsampleGame.unit;

import static java.lang.Math.*;

import core.GHQ;
import paint.ImageFrame;
import physics.HasPoint;
import weapon.Weapon;

public class Reimu extends THH_BasicPlayer {
	public Reimu(int initialGroup) {
		super(20, initialGroup);
	}
	@Override
	public final String name() {
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
		charaPaint = ImageFrame.create("thhimage/Reimu.png");
		iconPaint = ImageFrame.create("thhimage/ReimuIcon.png");
		
		//test area
		weaponSlot[0] = FUDA_KOUHAKU;
		spellSlot[0] = FUDA_SOUHAKU;
		////weaponLoad
		//FUDA_KOUHAKU
		weapon[FUDA_KOUHAKU] = new Weapon();
		weapon[FUDA_KOUHAKU].name = "FUDA_KOUHAKU";
		weapon[FUDA_KOUHAKU].setCoolTime(6);
		//FUDA_SHIROKURO
		weapon[FUDA_SHIROKURO] = new Weapon();
		weapon[FUDA_SHIROKURO].name = "FUDA_SHIROKURO";
		weapon[FUDA_SHIROKURO].setCoolTime(2);
		//FUDA_SOUHAKU
		weapon[FUDA_SOUHAKU] = new Weapon();
		weapon[FUDA_SOUHAKU].name = "FUDA_SOUHAKU";
		weapon[FUDA_SOUHAKU].setCoolTime(12);
		/////////////////////
		slot_spell = 0;
	}
	@Override
	public final void setBullet(int kind, HasPoint user) {
		switch(kind){
		case FUDA_KOUHAKU:
			GHQ.stage().addBullet(new THH_BulletLibrary.FudaKouhaku(this)).split_xMirror(6, 2);
			GHQ.stage().addBullet(new THH_BulletLibrary.FudaKouhaku(this)).split_xMirror(6, 7);
			break;
		case FUDA_SHIROKURO:
			GHQ.stage().addBullet(new THH_BulletLibrary.FudaShiroKuro(this)).split_NWay(10,PI/18,3,40);
			break;
		case FUDA_SOUHAKU:
			GHQ.stage().addBullet(new THH_BulletLibrary.FudaSouhaku(this)).split_NWay(25,PI/10,8,40);
			break;
		}
	}
}
