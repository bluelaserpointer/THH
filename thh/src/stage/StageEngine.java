package stage;

import java.awt.Graphics2D;

import bullet.Bullet;
import core.GHQ;
import unit.Unit;

public abstract class StageEngine{
	protected final static int
		//system
		NONE = GHQ.NONE,
		MAX = GHQ.MAX,
		MIN = GHQ.MIN;
	
	public static GHQ thh;
	
	//initialization
	public String getTitleName(){
		return GHQ.NOT_NAMED;
	}
	public abstract ControlExpansion getCtrl_ex();
	public abstract void loadResource();
	public abstract Unit[] charaSetup();
	public abstract Stage stageSetup();
	public abstract void openStage();
	
	//idle
	public abstract void idle(Graphics2D g2,int stopEventKind);
	
	//control
	public abstract void resetStage();
	public abstract Unit[] callBulletEngage(Unit[] characters,Bullet bullet);
	public abstract boolean deleteChara(Unit chara);
	
	//information
	public abstract int getGameFrame();
}
