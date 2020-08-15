package troubleCrasher.engine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;

import camera.Camera;
import core.GHQ;
import core.Game;
import gui.GUIPartsSwitcher;
import gui.UIShowHideButton;
import physics.Point;
import physics.stage.GHQStage;

import troubleCrasher.story.*;

import troubleCrasher.jigsaw.Jigsaw;
import troubleCrasher.jigsaw.JigsawEnum;
import troubleCrasher.jigsaw.JigsawViewer;

import troubleCrasher.resource.Resource;


public class TCGame extends Game {
	public static Resource resource;
	public static StoryMechanicManager storyMechanicManager;
	public static ScriptManager scriptManager;

	public static JigsawViewer jigsawViewer;
	private Jigsaw testJigsaw;
	public static void main(String args[]) throws IOException {
		new GHQ(new TCGame(), 1000, 600);
	}
	
	public TCGame() throws IOException {
		super(new GUIPartsSwitcher(0, 0)); //TODO: change it!!
		
    // Scripts
		 try {
			ScriptManager scriptManager = new ScriptManager("1");
//			System.out.println()
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
		//UI
//		jigsawViewer = new JigsawViewer(5, 5);
//		jigsawViewer.setBGColor(Color.BLACK);
//		jigsawViewer.setBounds(0, 0, 75*5, 75*5);
//		jigsawViewer.board().setJigsaw(testJigsaw = new Jigsaw(JigsawEnum.L32).setGridPos(1, 1));
//		GHQ.addGUIParts(jigsawViewer);
//		GHQ.addGUIParts(new UIShowHideButton(jigsawViewer)).setBounds(750, 50, 100, 100).setBGColor(Color.LIGHT_GRAY);
	}
	@Override
	public String getTitleName() {
		return "TroubleCrusher";
	}
		
	public static Resource getResource() {
		return resource;
	}

	public static JigsawViewer getJigsawViewer() {
		return jigsawViewer;
	}

	public Jigsaw getTestJigsaw() {
		return testJigsaw;
	}

	public static void setScriptManager(ScriptManager scriptManager) {
		TCGame.scriptManager = scriptManager;
	}

	@Override
	public GHQStage loadStage() {
		return null;
	}
	
	@Override
	public void idle(Graphics2D g2, int stopEventKind) {
		if(GHQ.nowFrame() % 10 == 0) {
			testJigsaw.setDirection(testJigsaw.direction().right());
		}
	}
	protected Camera starterCamera() {
		return null;
	}

}