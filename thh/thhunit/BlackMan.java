package thhunit;

import unit.Unit;
import core.GHQ;
import paint.ImageFrame;
import physics.Dynam;
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
		charaPaint = new ImageFrame("thhimage/BlackBall.png");
		bulletPaint[0] = new ImageFrame("thhimage/DarkNiddle.png");
		bulletPaint[1] = new ImageFrame("thhimage/DodgeMarker.png");
	}
	@Override
	public final void battleStarted() {
		weapon[0] = EnemyWeaponLibrary.getWeaponController(EnemyWeaponLibrary.lightBall_S);
	}
	@Override
	public void extendIdle() {
		final Unit targetEnemy = GHQ.getNearstVisibleEnemy(this);
		if(targetEnemy != null) {
			final Dynam UNIT_DYNAM = targetEnemy.getDynam();
			dynam.setMoveAngle(dynam.angleTo(charaDstX = UNIT_DYNAM.doubleX(),charaDstY = UNIT_DYNAM.doubleY()));
			baseAngle.set(dynam.angleTo(targetEnemy));
			if(weapon[0].trigger(this)) {
				EnemyWeaponLibrary.inputBulletInfo(this,EnemyWeaponLibrary.BLACK_SLASH_BURST,bulletPaint[0],targetEnemy);
				EnemyWeaponLibrary.inputBulletInfo(this,EnemyWeaponLibrary.BLACK_SLASH_BURST,bulletPaint[1],targetEnemy);
			}
		}
		dynam.approachIfNoObstacles(this, charaDstX, charaDstY, charaSpeed);
	}
	@Override
	public void setBullet(int kind,HasDynam source) {}
}
