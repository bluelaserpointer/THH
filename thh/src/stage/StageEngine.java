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
	
	public abstract String getVersion();
	
	//initialization
	public String getTitleName(){
		return GHQ.NOT_NAMED;
	}
	public abstract ControlExpansion getCtrl_ex();
	public abstract void loadResource();
	public abstract void charaSetup();
	public abstract void stageSetup();
	public abstract void openStage();
	public abstract StageSaveData getStageSaveData();
	
	//idle
	public abstract void idle(Graphics2D g2,int stopEventKind);
	
	//control
	public abstract void resetStage();
	public abstract Unit[] callBulletEngage(Unit[] characters,Bullet bullet);
	
	//information
	public abstract int getGameFrame();
	public boolean inStage(int x,int y) {
		return 0 < x && x <= getStageW() && 0 < y && y <= getStageH();
	}
	public abstract int getStageW();
	public abstract int getStageH();
}
