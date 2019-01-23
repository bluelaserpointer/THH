package chara;

import thh.DynamInteractable;
import thh.THH;
import weapon.Weapon;

public class Fairy extends UserChara{
	{
		charaSize = 70;
	}
	private final Weapon weaponController = EnemyBulletLibrary.getWeaponController(EnemyBulletLibrary.lightBall_S);
	private final int bulletIID[] = new int[10];
	private int magicCircleIID;
	@Override
	public final String getName() {
		return "FairyA";
	}
	
	@Override
	public final void loadImageData(){ //»­ÏñÕi¤ßÞz¤ß
		super.loadImageData();
		charaIID = thh.loadImage("YouseiA.png");
		magicCircleIID = thh.loadImage("MagicCircleBlue.png");
		bulletIID[0] = thh.loadImage("LightBallA.png");
	}
	@Override
	public void activeCons() {
		weaponController.defaultIdle();
		for(int i = 0;i < THH.getCharaAmount();i++) {
			if(THH.charaIsVisibleFrom(i,(int)dynam.getX(),(int)dynam.getY())) {
				if(weaponController.trigger())
					EnemyBulletLibrary.inputBulletInfo(this,EnemyBulletLibrary.lightBall_ROUND,bulletIID[0],(int)dynam.getX(),(int)dynam.getY(),(int)THH.getCharaX(i),(int)THH.getCharaY(i));
			}
		}
	}
	@Override
	public void paint(boolean doAnimation) {
		if(charaHP <= 0)
			return;
		super.paintMode_magicCircle(magicCircleIID);
		thh.paintHPArc((int) dynam.getX(), (int) dynam.getY(), 20,charaHP, charaBaseHP);
	}
	@Override
	public void setEffect(int kind,DynamInteractable source) {}
	@Override
	public void setBullet(int kind,DynamInteractable source) {}
}
