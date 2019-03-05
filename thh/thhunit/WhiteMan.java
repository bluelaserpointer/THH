package thhunit;

import core.DynamInteractable;
import core.GHQ;
import paint.ImageFrame;
import unit.Unit;

public class WhiteMan extends THHUnit{
	private static final long serialVersionUID = -3224085275647002850L;
	public WhiteMan(int initialGroup) {
		super(initialGroup);
	}
	{
		charaSize = 120;
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
	public void activeCons() {
		final Unit blackManAdress = GHQ.getChara("BlackMan");
		if(blackManAdress == null)
			return;
		charaDstX = blackManAdress.dynam.getX();
		charaDstY = blackManAdress.dynam.getY();
		if(blackManAdress.status.isBigger0(HP) && blackManAdress.status.isSmaller(HP,10000) && dynam.getDistance(charaDstX, charaDstY) < 200){
			blackManAdress.status.add(HP, 100);
		}
		dynam.approach(charaDstX, charaDstY, charaSpeed);
	}
	@Override
	public void setEffect(int kind,DynamInteractable source) {}
	@Override
	public void setBullet(int kind,DynamInteractable source) {}
}
