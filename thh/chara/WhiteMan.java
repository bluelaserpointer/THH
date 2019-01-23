package chara;

import thh.Chara;
import thh.DynamInteractable;
import thh.THH;

public class WhiteMan extends UserChara{
	{
		charaSize = 120;
		charaSpeed = 1;
	}
	private final int bulletIID[] = new int[10];
	@Override
	public final String getName() {
		return "WhiteMan";
	}
	
	@Override
	public final void loadImageData(){ //»­ÏñÕi¤ßÞz¤ß
		super.loadImageData();
		charaIID = thh.loadImage("WhiteBall.png");
		bulletIID[0] = thh.loadImage("LightBallA.png");
	}
	@Override
	public void activeCons() {
		final Chara blackManAdress = THH.getChara("BlackMan");
		charaDstX = blackManAdress.dynam.getX();
		charaDstY = blackManAdress.dynam.getY();
		if(0 < blackManAdress.getHP() && blackManAdress.getHP() < 10000 && dynam.getDistance(charaDstX, charaDstY) < 200){
			blackManAdress.setHP(blackManAdress.getHP() + 100);
		}
		dynam.approach(charaDstX, charaDstY, charaSpeed);
	}
	@Override
	public void setEffect(int kind,DynamInteractable source) {}
	@Override
	public void setBullet(int kind,DynamInteractable source) {}
}
