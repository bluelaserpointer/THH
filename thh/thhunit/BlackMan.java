package thhunit;

import unit.Unit;
import core.GHQ;
import paint.ImageFrame;
import physics.HasDynam;
import thhunit.EnemyWeaponLibrary;

public class BlackMan extends THH_BasicEnemy{
	private static final long serialVersionUID = -1004211237829934326L;
	{
		charaSpeed = 2;
	}
	
	public BlackMan(int initialGroup) {
		super(120, initialGroup);
	}
	@Override
	public final String getName() {
		return "BlackMan";
	}
	
	@Override
	public final void loadImageData(){
		super.loadImageData();
		charaPaint = ImageFrame.create("thhimage/BlackBall.png");
		bulletPaint[0] = ImageFrame.create("thhimage/DarkNiddle.png");
		bulletPaint[1] = ImageFrame.create("thhimage/DodgeMarker.png");
		weapon[0] = EnemyWeaponLibrary.getWeaponController(EnemyWeaponLibrary.lightBall_S);
	}
	@Override
	public void idle() {
		super.idle();
		final Unit targetEnemy = GHQ.stage().getNearstVisibleEnemy(this);
		if(targetEnemy != null) {
			final double ANGLE = dynam.angleTo(dstPoint.setXY(targetEnemy));
			dynam.setMoveAngle(ANGLE);
			baseAngle.set(ANGLE);
			if(weapon[0].trigger(this)) {
				EnemyWeaponLibrary.inputBulletInfo(this,EnemyWeaponLibrary.BLACK_SLASH_BURST,bulletPaint[0],targetEnemy);
				EnemyWeaponLibrary.inputBulletInfo(this,EnemyWeaponLibrary.BLACK_SLASH_BURST,bulletPaint[1],targetEnemy);
			}
		}
		dynam.approachIfNoObstacles(this, dstPoint, charaSpeed);
	}
	@Override
	public void setBullet(int kind,HasDynam source) {}
}
