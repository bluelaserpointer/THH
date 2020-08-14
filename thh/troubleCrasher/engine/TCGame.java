package troubleCrasher.engine;

import java.awt.Graphics2D;

import core.GHQ;
import core.Game;
import gui.GUIPartsSwitcher;
import physics.stage.GHQStage;

public class TCGame extends Game {
	
	public static void main(String args[]) {
		new GHQ(new TCGame(), 1000, 600);
	}
	
	public TCGame() {
		super(new GUIPartsSwitcher(0, 0)); //TODO: change it!!
	}
	@Override
	public GHQStage loadStage() {
		return null;
	}
	@Override
	public void idle(Graphics2D g2, int stopEventKind) {
		
	}

}
