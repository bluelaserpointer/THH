package thhunit;

import core.GHQ;
import paint.ImageFrame;
import physics.HasDynam;
import weapon.Weapon;

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
		charaPaint = ImageFrame.create("thhimage/Marisa.png");
		iconPaint = ImageFrame.create("thhimage/MarisaIcon.png");

		//test area
		//weaponSlot[0] = REUSE_BOMB;
		weaponSlot[0] = MAGIC_MISSILE;
		spellSlot[0] = NARROW_SPARK;
		spellSlot[1] = REUSE_BOMB;
		////weaponLoad
		//MILLKY_WAY
		weapon[MILLKY_WAY] = new Weapon();
		weapon[MILLKY_WAY].name = "MILLKY_WAY";
		weapon[MILLKY_WAY].coolTime = 10;
		//NARROW_SPARK
		weapon[NARROW_SPARK] = new Weapon();
		weapon[NARROW_SPARK].name = "NARROW_SPARK";
		weapon[NARROW_SPARK].coolTime = 150;
		weapon[NARROW_SPARK].magazineSize = 1;
		//REUSE_BOMB
		weapon[REUSE_BOMB] = new Weapon();
		weapon[REUSE_BOMB].name = "REUSE_BOMB";
		weapon[REUSE_BOMB].coolTime = 10;
		//MAGIC_MISSILE
		weapon[MAGIC_MISSILE] = new Weapon();
		weapon[MAGIC_MISSILE].name = "MAGIC_MISSILE";
		weapon[MAGIC_MISSILE].coolTime = 25;
		/////////////////////
		slot_spell = 0;
	}
	@Override
	public void idle() {
		super.idle();
		dynam.setMoveAngle(dynam.angleToMouse());
	}
	@Override
	public final void setBullet(int kind,HasDynam user) {
		switch(kind){
		case MILLKY_WAY:
			GHQ.stage().addBullet(new THH_BulletLibrary.MillkyWay(this)).split_Round(50, 8);
			break;
		case NARROW_SPARK:
			//message
			//if(this == source) {
			//	THH.addMessage(this,charaID,"KOIFU [MasterSpark]");
			//	THH.addMessage(this,charaID,"TEST MESSAGE 2");
			//}
			GHQ.stage().addBullet(new THH_BulletLibrary.NarrowSpark(this));
			break;
		case REUSE_BOMB:
			GHQ.stage().addBullet(new THH_BulletLibrary.ReuseBomb(this)).split_Round(50, 3);
			break;
		case MAGIC_MISSILE:
			GHQ.stage().addBullet(new THH_BulletLibrary.MagicMissile(this));
			break;
		}
	}
}
