package troubleCrasher.engine;

import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;

import core.GHQ;
import core.Game;
import gui.GUIPartsSwitcher;
import physics.stage.GHQStage;
import troubleCrasher.story.*;

public class TCGame extends Game {
	
	
	public static void main(String args[]) throws IOException {
		new GHQ(new TCGame(), 1000, 600);
	}
	
	public TCGame() throws IOException {
		super(new GUIPartsSwitcher(0, 0)); //TODO: change it!!
		
		ScriptManager scriptManager = new ScriptManager("1");
	}
		
	@Override
	public GHQStage loadStage() {
		return null;
	}
	
	@Override
	public void idle(Graphics2D g2, int stopEventKind) {
		
	}

}