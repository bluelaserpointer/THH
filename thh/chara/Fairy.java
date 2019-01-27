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
	public final void loadImageData(){
		super.loadImageData();
		charaIID = THH.loadImage("YouseiA.png");
		magicCircleIID = THH.loadImage("MagicCircleBlue.png");
		bulletIID[0] = THH.loadImage("LightBallA.png");
	}
	@Override
	public void activeCons() {
		final int charaX = (int)dynam.getX(),charaY = (int)dynam.getY();
		weaponController.defaultIdle();
		final Chara targetEnemy = THH.getNearstVisibleEnemy(this);
		if(targetEnemy != null && weaponController.trigger())
			EnemyBulletLibrary.inputBulletInfo(this,EnemyBulletLibrary.lightBall_ROUND,bulletIID[0],charaX,charaY,(int)targetEnemy.dynam.getX(),(int)targetEnemy.dynam.getY());
	}
	@Override
	public void paint(boolean doAnimation) {
		if(charaHP <= 0)
			return;
		super.paintMode_magicCircle(magicCircleIID);
		THH.paintHPArc((int) dynam.getX(), (int) dynam.getY(), 20,charaHP, charaBaseHP);
	}
	@Override
	public void setEffect(int kind,DynamInteractable source) {}
	@Override
	public void setBullet(int kind,DynamInteractable source) {}
}
