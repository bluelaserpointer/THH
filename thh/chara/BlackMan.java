package chara;

import engine.Engine_THH1;
import thh.DynamInteractable;
import thh.THH;
import weapon.Weapon;

public class BlackMan extends UserChara{
	{
		charaSize = 120;
		charaSpeed = 2;
	}
	private final Weapon weaponController = EnemyBulletLibrary.getWeaponController(EnemyBulletLibrary.lightBall_S);
	private final int bulletIID[] = new int[10];
	@Override
	public final String getName() {
		return "BlackMan";
	}
	
	@Override
	public final void loadImageData(){
		super.loadImageData();
		charaIID = THH.loadImage("BlackBall.png");
		bulletIID[0] = THH.loadImage("DarkNiddle3.png");
		bulletIID[1] = THH.loadImage("DodgeMarker.png");
	}
	@Override
	public void activeCons() {
		final int charaX = (int)dynam.getX(),charaY = (int)dynam.getY();
		weaponController.defaultIdle();
		final Chara targetEnemy = THH.getNearstVisibleEnemy(this);
		if(targetEnemy != null && weaponController.trigger()) {
			EnemyBulletLibrary.inputBulletInfo(this,EnemyBulletLibrary.BLACK_SLASH_BURST,bulletIID[0],charaX,charaY,(int)targetEnemy.dynam.getX(),(int)targetEnemy.dynam.getY());
			EnemyBulletLibrary.inputBulletInfo(this,EnemyBulletLibrary.BLACK_SLASH_BURST,bulletIID[1],charaX,charaY,(int)targetEnemy.dynam.getX(),(int)targetEnemy.dynam.getY());
		}
		Chara chara = THH.getNearstEnemy(Engine_THH1.FRIEND, (int)charaX, (int)charaY);
		if(chara != null) 
			dynam.setAngle(dynam.getAngle(charaDstX = chara.dynam.getX(),charaDstY = chara.dynam.getY()));
		dynam.approach(charaDstX, charaDstY, charaSpeed);
	}
	@Override
	public void setEffect(int kind,DynamInteractable source) {}
	@Override
	public void setBullet(int kind,DynamInteractable source) {}
}
