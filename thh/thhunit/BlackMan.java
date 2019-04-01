package thhunit;

import unit.Unit;
import core.GHQ;
import paint.ImageFrame;
import physicis.Dynam;
import physicis.HasDynam;
import thhunit.EnemyBulletLibrary;
import weapon.Weapon;

public class BlackMan extends THHUnit{
	private static final long serialVersionUID = -1004211237829934326L;
	{
		charaSpeed = 2;
	}
	
	public BlackMan(int initialGroup) {
		super(120, initialGroup);
	}
	private final Weapon weaponController = EnemyBulletLibrary.getWeaponController(EnemyBulletLibrary.lightBall_S);
	@Override
	public final String getName() {
		return "BlackMan";
	}
	
	@Override
	public final void loadImageData(){
		super.loadImageData();
		charaPaint = new ImageFrame("thhimage/BlackBall.png");
		bulletPaint[0] = new ImageFrame("thhimage/DarkNiddle3.png");
		bulletPaint[1] = new ImageFrame("thhimage/DodgeMarker.png");
	}
	@Override
	public void activeCons() {
		final int unitX = (int)dynam.getX(),unitY = (int)dynam.getY();
		weaponController.defaultIdle();
		final Unit targetEnemy = GHQ.getNearstVisibleEnemy(this);
		if(targetEnemy != null && weaponController.trigger()) {
			EnemyBulletLibrary.inputBulletInfo(this,EnemyBulletLibrary.BLACK_SLASH_BURST,bulletPaint[0],targetEnemy);
			EnemyBulletLibrary.inputBulletInfo(this,EnemyBulletLibrary.BLACK_SLASH_BURST,bulletPaint[1],targetEnemy);
		}
		Unit unit = GHQ.getNearstEnemy(this, (int)unitX, (int)unitY);
		if(unit != null) {
			final Dynam UNIT_DYNAM = unit.getDynam();
			dynam.setAngle(dynam.getAngle(charaDstX = UNIT_DYNAM.getX(),charaDstY = UNIT_DYNAM.getY()));
		}
		dynam.approach(charaDstX, charaDstY, charaSpeed);
	}
	@Override
	public void setEffect(int kind,HasDynam source) {}
	@Override
	public void setBullet(int kind,HasDynam source) {}
}
