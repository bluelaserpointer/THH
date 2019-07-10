package core;

import java.awt.Graphics2D;

import gui.GUIPartsSwitcher;
import stage.StageSaveData;

/**
 * A important class which is a frame of game main system.
 * @author bluelaserpointer
 * @since alpha1.0
 */
public abstract class Game{
	
	protected final GUIPartsSwitcher mainScreen;
	
	public Game(GUIPartsSwitcher screen) {
		mainScreen = screen;
	}
	public Game() {
		mainScreen = null;
	}
	
	public void loadResource() {}
	public abstract GHQStage loadStage();
	public abstract StageSaveData getStageSaveData();
	
	//idle
	public abstract void idle(Graphics2D g2,int stopEventKind);
	
	//information
	public String getTitleName(){
		return GHQ.NOT_NAMED;
	}
	public String getVersion(){
		return GHQ.NOT_NAMED;
	}
}
