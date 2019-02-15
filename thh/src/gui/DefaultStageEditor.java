package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;

import core.GHQ;
import paint.ColorFraming;
import paint.ImageFrame;
import paint.PaintScript;
import structure.Terrain;

public class DefaultStageEditor {
	private static int placeX,placeY;
	
	private static final byte
		NOTHING = -1,
		TERRAIN = 0,
		TILES = 1,
		ENEMY = 2,
		ITEM = 3;
	private static byte placeKind = NOTHING;
	//GUI_GROUP_ID
	public static final String
		EDIT_MODE_GROUP = "EDIT_MODE_GROUP";
	//PaintScripts
	private static final PaintScript RED_FRAMING = new ColorFraming(Color.RED,GHQ.stroke3);
	//loadResource
	public static void init(File stageFile) {
		final int SCREEN_W = GHQ.getScreenW(),SCREEN_H = GHQ.getScreenH();
		GHQ.addGUIParts(new BasicButton(EDIT_MODE_GROUP,null,150 + (SCREEN_W - 150)/2,SCREEN_H/2,SCREEN_W - 150,SCREEN_H) {
			@Override
			public void clicked() {
				switch(placeKind) {
				case TERRAIN:
					if(Terrain.blueprint_isOriginPoint(placeX, placeY))
						GHQ.addStructure(Terrain.blueprint_flush());
					else
						Terrain.blueprint_addPoint(placeX, placeY);
					break;
				case TILES:
					break;
				case ENEMY:
					break;
				case ITEM:
					break;
				}
			}
		});
		GHQ.addGUIParts(new BasicButton(EDIT_MODE_GROUP,new ImageFrame("gui_editor/Tiles.png"),55,155,40,40) {
			@Override
			public void clicked() {
				placeKind = (placeKind == TILES ? NOTHING : TILES);
			}
			@Override
			public void paint() {
				super.paint();
				if(placeKind == TILES)
					RED_FRAMING.paint(x, y, w, h);
			}
		});
		GHQ.addGUIParts(new BasicButton(EDIT_MODE_GROUP,new ImageFrame("gui_editor/FreeShape.png"),100,155,40,40) {
			@Override
			public void clicked() {
				placeKind = (placeKind == TERRAIN ? NOTHING : TERRAIN);
			}
			@Override
			public void paint() {
				super.paint();
				if(placeKind == TERRAIN)
					RED_FRAMING.paint(x, y, w, h);
			}
		});
		GHQ.addGUIParts(new BasicButton(EDIT_MODE_GROUP,new ImageFrame("gui_editor/Save.png"),77,500,85,40) {
			@Override
			public void clicked() {
				System.out.println("saving...");
				GHQ.saveData(GHQ.getEngine().getStageSaveData(),stageFile);
				System.out.println("complete!");
			}
		});
	}
	//role
	public static void idle(Graphics2D g2) {
		//mouse
		if(GHQ.key_shift) {
			g2.setColor(Color.RED);
			final int N = 100;
			int S = 4;
			final int SX = (GHQ.getMouseX() + N/2)/N,SY = (GHQ.getMouseY() + N/2)/N;
			for(int xi = -1;xi <= +1;xi++) {
				for(int yi = -1;yi <= +1;yi++)
					g2.fillOval((SX + xi)*N - S/2, (SY + yi)*N - S/2, S, S);
			}
			S += 6;
			g2.drawOval(SX*N - S/2, SY*N - S/2, S, S);
			placeX = SX*N;
			placeY = SY*N;
		}else {
			placeX = GHQ.getMouseX();
			placeY = GHQ.getMouseY();
		}
		//origin
		switch(placeKind) {
		case TERRAIN:
			Terrain.blueprint_markPoints(new ColorFraming(Color.ORANGE,GHQ.stroke1,4,4));
			Terrain.blueprint_markOrigin(new ColorFraming(Color.ORANGE,GHQ.stroke1,8,8));
			break;
		}
		//GUI
		GHQ.translateForGUI(true);
		g2.setColor(Color.WHITE);
		g2.drawString("EDIT_MODE", 20, 20);
		GHQ.translateForGUI(false);
	}
}
