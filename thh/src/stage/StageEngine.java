package stage;

import java.awt.Graphics2D;
import core.GHQ;

/**
 * A important class which is a frame of game main system.
 * @author bluelaserpointer
 * @since alpha1.0
 */
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
	public abstract void loadResource();
	public abstract void openStage();
	public abstract StageSaveData getStageSaveData();
	
	//idle
	public abstract void idle(Graphics2D g2,int stopEventKind);
	
	//control
	public abstract void resetStage();
	
	//information
	/**
	 * Return how many times the idle method of this class is called.
	 * @return Times the idle class called.
	 */
	public abstract int getEngineGameFrame();
	/**
	 * Judge if a coordinate were in permitted area.
	 * @return true - in stage / false - in outside of stage
	 */
	public boolean inStage(int x,int y) {
		return 0 < x && x <= getStageW() && 0 < y && y <= getStageH();
	}
	/**
	 * Return the maximum width of the stage.
	 * @return maximum width
	 */
	public abstract int getStageW();
	/**
	 * Return the maximum height of the stage.
	 * @return maximum height
	 */
	public abstract int getStageH();
}
