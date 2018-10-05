package chara;

import thh.EnemyControl;
import thh.THH;
import weapon.Weapon;

public class Fairy extends THHOriginal implements EnemyControl{
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
	public void Espawn_IDTeamXYHP(int id,int team,int x,int y,int hp) {
		super.spawn(id, team, x, y);
		super.charaBaseHP = super.charaHP = hp;
		targetX = x;targetY = y;
	}
	@Override
	public void Emove(int x,int y) {
		targetX = x;targetY = y;
	}
	@Override
	public void idle(boolean isActive) {
		final double DX = targetX - charaX,DY = targetY - charaY;
		if(DX < -2 && 2 < DX) {
			charaX += DX/20;
			charaY += DY/20;
		}
		for(int i = 0;i < THH.getCharaAmount();i++) {
			if(THH.charaIsVisibleFrom(i,(int)charaX,(int)charaY)) {
				if(weaponController.trigger()) {
					EnemyBulletLibrary.inputBulletInfo(EnemyBulletLibrary.lightBall_S,bulletIID[0],(int)charaX,(int)charaY,(int)THH.getCharaX(i),(int)THH.getCharaY(i));
				}
			}
		}
	}
}
