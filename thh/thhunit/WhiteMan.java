package thhunit;

import calculate.ConsumableEnergy;
import core.GHQ;
import paint.ImageFrame;
import physics.HasDynam;
import unit.Unit;

public class WhiteMan extends THH_BasicEnemy{
	private static final long serialVersionUID = -3224085275647002850L;
	public WhiteMan(int initialGroup) {
		super(120, initialGroup);
	}
	{
		charaSpeed = 1;
	}
	@Override
	public final String getName() {
		return "WhiteMan";
	}
	
	@Override
	public final void loadImageData(){
		super.loadImageData();
		charaPaint = ImageFrame.create("thhimage/WhiteBall.png");
		bulletPaint[0] = ImageFrame.create("thhimage/LightBallA.png");
	}
	@Override
	public void idle() {
		super.idle();
		final Unit blackManAdress = GHQ.stage().units.forName("BlackMan");
		if(blackManAdress == null)
			return;
		dstPoint.setXY(blackManAdress);
		final ConsumableEnergy THE_HP = ((THH_BasicUnit)blackManAdress).HP;
		if(!THE_HP.isMin() && !THE_HP.isMax() && dynam.inRange(dstPoint, 200)){
			THE_HP.consume(-100);
		}
		dynam.approachIfNoObstacles(this, dstPoint, charaSpeed);
	}
	@Override
	public void setBullet(int kind,HasDynam source) {}
}
