package stage;

import java.awt.Graphics2D;

import bullet.Bullet;
import chara.Chara;
import core.GHQ;

public abstract class StageEngine{
	protected final static int
		//system
		NONE = GHQ.NONE,
		MAX = GHQ.MAX,
		MIN = GHQ.MIN;
	
	public static GHQ thh;
	
	
	//initialization
	public abstract ControlExpansion getCtrl_ex();
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
