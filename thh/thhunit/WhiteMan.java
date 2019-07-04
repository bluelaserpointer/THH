package thhunit;

import core.GHQ;
import paint.ImageFrame;
import physics.HasDynam;
import status.StatusWithDefaultValue;
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
		charaPaint = new ImageFrame("thhimage/WhiteBall.png");
		bulletPaint[0] = new ImageFrame("thhimage/LightBallA.png");
	}
	@Override
	public void idle() {
		super.idle();
		final Unit blackManAdress = GHQ.getUnitList().forName("BlackMan");
		if(blackManAdress == null)
			return;
		dstPoint.setXY(blackManAdress);
		final StatusWithDefaultValue THE_STATUS = ((THH_BasicUnit)blackManAdress).status;
		if(THE_STATUS.isBigger0(HP) && THE_STATUS.isSmaller(HP,10000) && dynam.inRange(dstPoint, 200)){
			THE_STATUS.add(HP, 100);
		}
		dynam.approachIfNoObstacles(this, dstPoint, charaSpeed);
	}
	@Override
	public void setBullet(int kind,HasDynam source) {}
}
