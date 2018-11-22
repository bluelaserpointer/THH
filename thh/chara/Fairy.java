package chara;

import thh.DynamInteractable;
import thh.THH;
import weapon.Weapon;

public class Fairy extends UserChara{
	{
		charaSize = 70;
	}
	private int targetX,targetY;
	private final Weapon weaponController = EnemyBulletLibrary.getWeaponController(EnemyBulletLibrary.lightBall_S);
	private final int bulletIID[] = new int[10];
	@Override
	public final String getName() {
		return "FairyA";
	}
	
	@Override
	public final void loadImageData(){ //»­ÏñÕi¤ßÞz¤ß
		super.loadImageData();
		charaIID = thh.loadImage("YouseiA.png");
		bulletIID[0] = thh.loadImage("LightBallA.png");
	}
	@Override
	public void activeCons() {
		final double DX = targetX - charaX,DY = targetY - charaY;
		if(DX < -2 && 2 < DX) {
			charaX += DX/20;
			charaY += DY/20;
		}
		weaponController.defaultIdle();
		for(int i = 0;i < THH.getCharaAmount();i++) {
			if(THH.charaIsVisibleFrom(i,(int)charaX,(int)charaY)) {
				if(weaponController.trigger())
					EnemyBulletLibrary.inputBulletInfo(this,EnemyBulletLibrary.lightBall_ROUND,bulletIID[0],(int)charaX,(int)charaY,(int)THH.getCharaX(i),(int)THH.getCharaY(i));
			}
		}
	}
	@Override
	public void setEffect(int kind,DynamInteractable source) {}
	@Override
	public void setBullet(int kind,DynamInteractable source) {}
}
