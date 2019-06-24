package thhunit;

import core.GHQ;
import paint.ImageFrame;
import physics.HasDynam;
import weapon.Weapon;
import weapon.WeaponInfo;

public class Marisa extends THH_BasicPlayer{
	private static final long serialVersionUID = -1533719907505962673L;
	
	public Marisa(int initialGroup) {
		super(20, initialGroup);
	}
	@Override
	public final String getName() {
		return "Marisa";
	}
	
	//weapon&bullet kind name
	static final int
		MILLKY_WAY = 0,NARROW_SPARK = 1,REUSE_BOMB = 2,MAGIC_MISSILE = 3;
	
	//GUI
		
	//Sounds
	
	@Override
	public final void loadImageData(){
		super.loadImageData();
		charaPaint = new ImageFrame("thhimage/Marisa.png");
		iconPaint = new ImageFrame("thhimage/MarisaIcon.png");
	}
	
	@Override
	public final void loadSoundData(){
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
		weapon[MILLKY_WAY] = new Weapon();
		//NARROW_SPARK
		WeaponInfo.clear();
		WeaponInfo.name = "NARROW_SPARK";
		WeaponInfo.coolTime = 150;
		WeaponInfo.magazineSize = 1;
		weapon[NARROW_SPARK] = new Weapon();
		//REUSE_BOMB
		WeaponInfo.clear();
		WeaponInfo.name = "REUSE_BOMB";
		WeaponInfo.coolTime = 10;
		weapon[REUSE_BOMB] = new Weapon();
		//MAGIC_MISSILE
		WeaponInfo.clear();
		WeaponInfo.name = "MAGIC_MISSILE";
		WeaponInfo.coolTime = 25;
		weapon[MAGIC_MISSILE] = new Weapon();
		/////////////////////
		slot_spell = 0;
	}
	@Override
	public final void extendIdle() {
		dynam.setMoveAngle(dynam.angleToMouse());
	}
	@Override
	public final void setBullet(int kind,HasDynam user) {
		switch(kind){
		case MILLKY_WAY:
			GHQ.addBullet(new THH_BulletLibrary.MillkyWay(this)).split_Round(50, 8);
			break;
		case NARROW_SPARK:
			//message
			//if(this == source) {
			//	THH.addMessage(this,charaID,"KOIFU [MasterSpark]");
			//	THH.addMessage(this,charaID,"TEST MESSAGE 2");
			//}
			GHQ.addBullet(new THH_BulletLibrary.NarrowSpark(this));
			break;
		case REUSE_BOMB:
			GHQ.addBullet(new THH_BulletLibrary.ReuseBomb(this)).split_Round(50, 3);
			break;
		case MAGIC_MISSILE:
			GHQ.addBullet(new THH_BulletLibrary.MagicMissile(this));
			break;
		}
	}
}
