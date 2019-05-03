package thhunit;

import core.GHQ;
import paint.DotPaint;
import paint.ImageFrame;
import physics.HasDynam;
import thhunit.EnemyWeaponLibrary;
import unit.Unit;

public class Fairy extends THH_BasicEnemy{
	private static final long serialVersionUID = -8167654165444569286L;
	public Fairy(int initialGroup) {
		super(70, initialGroup);
	}
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
	public final void battleStarted() {
		weapon[0] = EnemyWeaponLibrary.getWeaponController(EnemyWeaponLibrary.lightBall_S);
	}
	@Override
	public void extendIdle() {
		final Unit targetEnemy = GHQ.getNearstVisibleEnemy(this);
		if(targetEnemy != null && weapon[0].trigger(this))
			EnemyWeaponLibrary.inputBulletInfo(this,EnemyWeaponLibrary.lightBall_ROUND,bulletPaint[0],targetEnemy);
	}
	@Override
	public void paint(boolean doAnimation) {
		super.paintMode_magicCircle(magicCircleIID);
		GHQ.paintHPArc((int) dynam.getX(), (int) dynam.getY(), 20,status.get(HP), status.getDefault(HP));
	}
	@Override
	public void setBullet(int kind,HasDynam source) {}
}
