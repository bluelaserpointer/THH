package chara;

import static java.lang.Math.sqrt;

import engine.Engine_THH1;
import thh.Chara;
import thh.DynamInteractable;
import thh.THH;
import weapon.Weapon;

public class BlackMan extends UserChara{
	{
		charaSize = 120;
		charaSpeed = 3;
	}
	private final Weapon weaponController = EnemyBulletLibrary.getWeaponController(EnemyBulletLibrary.lightBall_S);
	private final int bulletIID[] = new int[10];
	@Override
	public final String getName() {
		return "BlackMan";
	}
	
	@Override
	public final void loadImageData(){ //»­ÏñÕi¤ßÞz¤ß
		super.loadImageData();
		charaIID = thh.loadImage("BlackBall.png");
		bulletIID[0] = thh.loadImage("FMJv2.png");
		bulletIID[1] = thh.loadImage("RPGv2.png");
	}
	@Override
	public void activeCons() {
		weaponController.defaultIdle();
		for(int i = 0;i < THH.getCharaAmount();i++) {
			if(THH.charaIsVisibleFrom(i,(int)charaX,(int)charaY)) {
				if(weaponController.trigger()) {
					EnemyBulletLibrary.inputBulletInfo(this,EnemyBulletLibrary.BLACK_SLASH_BURST,bulletIID[0],(int)charaX,(int)charaY,(int)THH.getCharaX(i),(int)THH.getCharaY(i));
					EnemyBulletLibrary.inputBulletInfo(this,EnemyBulletLibrary.BLACK_SLASH_BURST,bulletIID[1],(int)charaX,(int)charaY,(int)THH.getCharaX(i),(int)THH.getCharaY(i));
				}
			}
		}
		Chara chara = THH.getNearstEnemy(Engine_THH1.FRIEND, (int)charaX, (int)charaY);
		if(chara != null) {
			final double X = chara.getX(),Y = chara.getY();
			charaDstX = X;
			charaDstY = Y;
			charaAngle = Math.atan2(Y - charaY,X - charaX); 
		}
		/*dynam*/ {
			final double DX = charaDstX - charaX,DY = charaDstY - charaY;
			final double DISTANCE = sqrt(DX*DX + DY*DY);
			if(DISTANCE <= charaSpeed) {
				charaX = charaDstX;
				charaY = charaDstY;
			}else {
				final double RATE = charaSpeed/DISTANCE;
				charaX += DX*RATE;
				charaY += DY*RATE;
			}
		}
	}
	@Override
	public void setEffect(int kind,DynamInteractable source) {}
	@Override
	public void setBullet(int kind,DynamInteractable source) {}
}
