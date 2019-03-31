package thhunit;

import core.GHQ;
import paint.ImageFrame;
import physicis.Dynam;
import physicis.HasDynam;
import unit.Status;
import unit.Unit;

public class WhiteMan extends THHUnit{
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
	
	//Dynam
	private final Dynam dynam = new Dynam();
	@Override
	public final Dynam getDynam() {
		return dynam;
	}
	
	@Override
	public final void loadImageData(){
		super.loadImageData();
		charaPaint = new ImageFrame("thhimage/WhiteBall.png");
		bulletPaint[0] = new ImageFrame("thhimage/LightBallA.png");
	}
	@Override
	public void activeCons() {
		final Unit blackManAdress = GHQ.getChara("BlackMan");
		if(blackManAdress == null)
			return;
		final Dynam THE_DYNAM = blackManAdress.getDynam();
		charaDstX = THE_DYNAM.getX();
		charaDstY = THE_DYNAM.getY();
		final Status THE_STATUS = ((THHUnit)blackManAdress).status;
		if(THE_STATUS.isBigger0(HP) && THE_STATUS.isSmaller(HP,10000) && dynam.getDistance(charaDstX, charaDstY) < 200){
			THE_STATUS.add(HP, 100);
		}
		dynam.approach(charaDstX, charaDstY, charaSpeed);
	}
	@Override
	public void setEffect(int kind,HasDynam source) {}
	@Override
	public void setBullet(int kind,HasDynam source) {}
}
