package core;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import gui.GUIPartsSwitcher;
import stage.GHQStage;

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
	
	public void loadResource() {}
	public abstract GHQStage loadStage();
	
	//idle
	public abstract void idle(Graphics2D g2,int stopEventKind);
	
	//event
	public void mousePressed(MouseEvent e) {
		GHQ.mouseHoveredUI().clicked();
	}
	public void mouseReleased(MouseEvent e) {
		GHQ.mouseHoveredUI().released();
	}
	public void mouseMoved(MouseEvent e) {
		GHQ.mouseHoveredUI().mouseOver();
	}
	public void mouseDragged(MouseEvent e) {
		GHQ.mouseHoveredUI().mouseOver();
	}
	
	//information
	public String getTitleName(){
		return GHQ.NOT_NAMED;
	}
	public String getVersion(){
		return GHQ.NOT_NAMED;
	}
}
