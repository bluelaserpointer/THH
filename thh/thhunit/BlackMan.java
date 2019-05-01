package thhunit;

import unit.Unit;
import core.GHQ;
import paint.ImageFrame;
import physicis.Dynam;
import physicis.HasDynam;
import thhunit.EnemyWeaponLibrary;
import weapon.Weapon;

public class BlackMan extends THH_BasicUnit{
	private static final long serialVersionUID = -1004211237829934326L;
	{
		charaSpeed = 2;
	}
	
	public BlackMan(int initialGroup) {
		super(120, initialGroup);
	}
	private final Weapon weapon = EnemyWeaponLibrary.getWeaponController(EnemyWeaponLibrary.lightBall_S);
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
	public void activeCons() {
		super.activeCons();
		if(!isAlive())
			return;
		final int unitX = (int)dynam.getX(),unitY = (int)dynam.getY();
		weapon.idle();
		final Unit targetEnemy = GHQ.getNearstVisibleEnemy(this);
		if(targetEnemy != null && weapon.trigger(this)) {
			EnemyWeaponLibrary.inputBulletInfo(this,EnemyWeaponLibrary.BLACK_SLASH_BURST,bulletPaint[0],targetEnemy);
			EnemyWeaponLibrary.inputBulletInfo(this,EnemyWeaponLibrary.BLACK_SLASH_BURST,bulletPaint[1],targetEnemy);
		}
		Unit unit = GHQ.getNearstEnemy(this, (int)unitX, (int)unitY);
		if(unit != null) {
			final Dynam UNIT_DYNAM = unit.getDynam();
			dynam.setAngle(dynam.getAngle(charaDstX = UNIT_DYNAM.getX(),charaDstY = UNIT_DYNAM.getY()));
		}
	}
	@Override
	public void setBullet(int kind,HasDynam source) {}
}
