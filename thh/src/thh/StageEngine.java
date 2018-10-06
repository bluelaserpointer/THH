package thh;

import java.awt.Graphics2D;

import bullet.Bullet;

public abstract class StageEngine{
	//•’•£©`•Î•…
	//•∑•π•∆•‡ÈvﬂB
	protected final static int
		//system
		NONE = THH.NONE,
		MAX = THH.MAX,
		MIN = THH.MIN;
	
	protected static final THH thh = THH.thh;
	
	
	//initialization
	public abstract Chara[] charaSetup();
	public abstract Stage stageSetup();
	public abstract void openStage();
	
	//idle
	public abstract void idle(Graphics2D g2,int stopEventKind);
	
	//control
	public abstract void resetStage();
	public abstract Chara[] callBulletEngage(Bullet bullet);
	
	//information
}
