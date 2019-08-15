package thhunit;

import core.GHQ;
import paint.ImageFrame;
import paint.dot.DotPaint;
import physics.HasDynam;
import thhunit.EnemyWeaponLibrary;
import unit.Unit;

public class Fairy extends THH_BasicEnemy{
	private static final long serialVersionUID = -8167654165444569286L;
	public Fairy(int initialGroup) {
		super(70, initialGroup);
	}
	private DotPaint magicCircleIID = ImageFrame.create("thhimage/MagicCircleBlue.png");
	@Override
	public final String getName() {
		return "FairyA";
	}
	
	@Override
	public final void loadImageData(){
		super.loadImageData();
		charaPaint = ImageFrame.create("thhimage/YouseiA.png");
		bulletPaint[0] = ImageFrame.create("thhimage/LightBallA.png");
		weapon[0] = EnemyWeaponLibrary.getWeaponController(EnemyWeaponLibrary.lightBall_S);
	}
	@Override
	public void idle() {
		super.idle();
		final Unit targetEnemy = GHQ.stage().getNearstVisibleEnemy(this);
		if(targetEnemy != null && weapon[0].trigger(this))
			EnemyWeaponLibrary.inputBulletInfo(this,EnemyWeaponLibrary.lightBall_ROUND,bulletPaint[0],targetEnemy);
	}
	@Override
	public void paint(boolean doAnimation) {
		super.paintMode_magicCircle(magicCircleIID);
		GHQ.paintHPArc(dynam.intX(), dynam.intY(), 20, HP);
	}
	@Override
	public void setBullet(int kind,HasDynam source) {}
}
