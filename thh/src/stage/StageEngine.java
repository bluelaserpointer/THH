package stage;

import java.awt.Graphics2D;

import bullet.Bullet;
import thh.Chara;
import thh.THH;

public abstract class StageEngine{
	//•’•£©`•Î•…
	//•∑•π•∆•‡ÈvﬂB
	protected final static int
		//system
		NONE = THH.NONE,
		MAX = THH.MAX,
		MIN = THH.MIN;
	
	public static THH thh;
	
	
	//initialization
	public abstract void loadResource();
	public abstract Chara[] charaSetup();
	public abstract Stage stageSetup();
	public abstract void openStage();
	
	//idle
	public abstract void idle(Graphics2D g2,int stopEventKind);
	
	//control
	public abstract void resetStage();
	public abstract Chara[] callBulletEngage(Chara[] characters,Bullet bullet);
	public abstract boolean deleteChara(Chara chara);
	
	//information
	public abstract int getGameFrame();
}
