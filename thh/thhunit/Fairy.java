package thhunit;

import core.DynamInteractable;
import core.GHQ;
import paint.DotPaint;
import paint.ImageFrame;
import thhunit.EnemyBulletLibrary;
import unit.Unit;
import weapon.Weapon;

public class Fairy extends THHUnit{
	private static final long serialVersionUID = -8167654165444569286L;
	public Fairy(int initialGroup) {
		super(initialGroup);
	}
	{
		charaSize = 70;
	}
	private final Weapon weaponController = EnemyBulletLibrary.getWeaponController(EnemyBulletLibrary.lightBall_S);
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
		weaponController.defaultIdle();
		final Unit targetEnemy = GHQ.getNearstVisibleEnemy(this);
		if(targetEnemy != null && weaponController.trigger())
			EnemyBulletLibrary.inputBulletInfo(this,EnemyBulletLibrary.lightBall_ROUND,bulletPaint[0],targetEnemy);
	}
	@Override
	public void paint(boolean doAnimation) {
		if(!isAlive())
			return;
		super.paintMode_magicCircle(magicCircleIID);
		GHQ.paintHPArc((int) dynam.getX(), (int) dynam.getY(), 20,status.get(HP), status.getDefault(HP));
	}
	@Override
	public void setEffect(int kind,DynamInteractable source) {}
	@Override
	public void setBullet(int kind,DynamInteractable source) {}
}
