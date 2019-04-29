package thhunit;

import core.GHQ;
import paint.DotPaint;
import paint.ImageFrame;
import physicis.HasDynam;
import thhunit.EnemyWeaponLibrary;
import unit.Unit;
import weapon.Weapon;

public class Fairy extends THH_BasicUnit{
	private static final long serialVersionUID = -8167654165444569286L;
	public Fairy(int initialGroup) {
		super(70, initialGroup);
	}
	private final Weapon weapon = EnemyWeaponLibrary.getWeaponController(EnemyWeaponLibrary.lightBall_S);
	private DotPaint magicCircleIID;
	@Override
	public final String getName() {
		return "FairyA";
	}
	
	@Override
	public final void loadImageData(){
		super.loadImageData();
		charaPaint = new ImageFrame("thhimage/YouseiA.png");
		magicCircleIID = new ImageFrame("thhimage/MagicCircleBlue.png");
		bulletPaint[0] = new ImageFrame("thhimage/LightBallA.png");
	}
	@Override
	public void activeCons() {
		weapon.idle();
		final Unit targetEnemy = GHQ.getNearstVisibleEnemy(this);
		if(targetEnemy != null && weapon.trigger(this))
			EnemyWeaponLibrary.inputBulletInfo(this,EnemyWeaponLibrary.lightBall_ROUND,bulletPaint[0],targetEnemy);
	}
	@Override
	public void paint(boolean doAnimation) {
		if(!isAlive())
			return;
		super.paintMode_magicCircle(magicCircleIID);
		GHQ.paintHPArc((int) dynam.getX(), (int) dynam.getY(), 20,status.get(HP), status.getDefault(HP));
	}
	@Override
	public void setBullet(int kind,HasDynam source) {}
}
