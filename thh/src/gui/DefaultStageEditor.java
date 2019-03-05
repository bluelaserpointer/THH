package gui;

import static java.awt.event.KeyEvent.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.ArrayList;

import core.Dynam;
import core.GHQ;
import core.HasBoundingBox;
import input.SingleKeyListener;
import paint.ColorFilling;
import paint.ColorFraming;
import paint.ImageFrame;
import structure.Structure;
import structure.StructureScript;
import structure.Terrain;
import structure.Tile;
import unit.DummyUnit;
import unit.Unit;
import vegetation.Vegetation;

public class DefaultStageEditor {
	private static ArrayList<StructureScript<Tile>> tileScripts = new ArrayList<StructureScript<Tile>>();
	private static ArrayList<StructureScript<Terrain>> terrainScripts = new ArrayList<StructureScript<Terrain>>();
	//private static ArrayList<StructureScript<Terrain>> unitScripts = new ArrayList<StructureScript<Terrain>>();
	
	static {
		//tileScripts
		tileScripts.add(new StructureScript<Tile>() {
			private static final long serialVersionUID = -1082801436030177586L;
			@Override
			public String getName() {
				return "WHITE_WALL";
			}
			@Override
			public void paint(Tile tile,boolean doAnimation) {
				tile.fill(Color.WHITE);
				tile.draw(Color.LIGHT_GRAY, GHQ.stroke3);
			}
		});
		//terrainScripts
		terrainScripts.add(new StructureScript<Terrain>() {
			private static final long serialVersionUID = -1082801436030177586L;
			@Override
			public String getName() {
				return "WHITE_WALL";
			}
			@Override
			public void paint(Terrain terrain,boolean doAnimation) {
				terrain.fill(Color.WHITE);
				terrain.draw(Color.LIGHT_GRAY, GHQ.stroke3);
			}
		});
	}
	//input
	private static final int inputKeys[] = 
	{
		VK_BACK_SPACE,
		VK_C,
		VK_V,
		VK_TAB,
		VK_CONTROL,
	};
	private static final SingleKeyListener keyListener = new SingleKeyListener(inputKeys);
	private static int nowSlot = 0,slot_max = 1;
	//private static final int arrowIID = GHQ.loadImage("gui_editor/arrow.png");
	
	private static int placeX,placeY;
	
	private static final int
		POINTING = -1,
		TERRAIN = 0,
		TILES = 1,
		UNIT = 2,
		VEGETATION = 3,
		ITEM = 4;
	private static HasBoundingBox selectObject,mouseOveredObject;
	//GUI_GROUP_ID
	public static final String
		EDIT_MENU_GROUP = "EDIT_MENU_GROUP",
		OBJECT_CONFIG_GROUP = "OBJECT_CONFIG_GROUP";
	//GUI
	private static TitledLabel configLabel;
	private static CombinedButtons CB_placeKind;
	//loadResource
	public static void init(File stageFile) {
		final int SCREEN_W = GHQ.getScreenW(),SCREEN_H = GHQ.getScreenH();
		GHQ.addListenerEx(keyListener);
		GHQ.addGUIParts(new BasicButton(EDIT_MENU_GROUP,new ColorFraming(Color.WHITE,GHQ.stroke3),150,0,SCREEN_W - 150,SCREEN_H) {
			@Override
			public void clicked() {
				if(CB_placeKind.isDefaultSelection()) { //not selected any placeKind = object select
					GHQ.enableGUIs(OBJECT_CONFIG_GROUP);
					selectObject = mouseOveredObject;
					keyListener.enable();
					final String labelText;
					if(selectObject instanceof Tile) {
						labelText = ((Tile)selectObject).script.getName();
						configLabel.setTitle("script:");
					}else if(selectObject instanceof Terrain) {
						labelText = ((Terrain)selectObject).script.getName();
						configLabel.setTitle("script:");
					}else if(selectObject instanceof Unit) {
						labelText = ((Unit)selectObject).originalName;
						configLabel.setTitle("orignal_name:");
					}else if(selectObject instanceof Vegetation) {
						labelText = "image";//((Vegetation)selectObject).getImageURL();
						configLabel.setTitle("image_URL:");
					}else
						labelText = "";
					configLabel.setText(labelText.equals(GHQ.NOT_NAMED) ? "" : labelText);
				}else { //object deselect
					GHQ.disableGUIs(OBJECT_CONFIG_GROUP);
					selectObject = null;
					keyListener.disable();
					switch(CB_placeKind.getSelection()) {
					case TERRAIN:
						if(Terrain.blueprint_isOriginPoint(placeX, placeY))
							GHQ.addStructure(Terrain.blueprint_flush());
						else
							Terrain.blueprint_addPoint(placeX, placeY);
						break;
					case TILES:
						if(Tile.blueprint_hasOriginPoint())
							GHQ.addStructure(Tile.blueprint_addEndPointAndFlush(placeX, placeY));
						else
							Tile.blueprint_addOriginPoint(placeX, placeY);
						break;
					case UNIT:
						GHQ.addUnit(new DummyUnit(new Dynam(placeX, placeY)));
						break;
					case VEGETATION:
						GHQ.addVegetation(new Vegetation(new ImageFrame("gui_editor/Vegetation.png"), placeX, placeY));
						break;
					case ITEM:
						break;
					}
				}
			}
		});
		GHQ.addGUIParts(CB_placeKind = new CombinedButtons(EDIT_MENU_GROUP, POINTING, 0, 0, 150, SCREEN_H));
		CB_placeKind.addButton(TILES, new ImageFrame("thhimage/gui_editor/Tiles.png"),25,155,40,40);
		CB_placeKind.addButton(TERRAIN, new ImageFrame("thhimage/gui_editor/FreeShape.png"),70,155,40,40);
		CB_placeKind.addButton(UNIT, new ImageFrame("thhimage/gui_editor/Unit.png"),25,200,40,40);
		CB_placeKind.addButton(VEGETATION, new ImageFrame("thhimage/gui_editor/Vegetation.png"),70,200,40,40);
		GHQ.addGUIParts(new BasicButton(EDIT_MENU_GROUP,new ImageFrame("thhimage/gui_editor/Save.png"),25,500,85,40) {
			@Override
			public void clicked() {
				System.out.println("saving...");
				GHQ.saveData(GHQ.getEngine().getStageSaveData(),stageFile);
				System.out.println("complete!");
			}
		});
		GHQ.addGUIParts(configLabel = new TitledLabel(OBJECT_CONFIG_GROUP,new ColorFilling(Color.WHITE),25,300,120,25){
			
		});
		GHQ.<InputOptionList>addGUIParts(new InputOptionList(configLabel)).addWord("WHITE_WALL", "ABCD", "ABNK");
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
		//guide
		switch(CB_placeKind.getSelection()) {
		case POINTING:
			mouseOveredObject = GHQ.getMouseOverChara();
			if(mouseOveredObject == null)
				mouseOveredObject = GHQ.getMouseOverVegetation();
			if(mouseOveredObject == null)
				mouseOveredObject = GHQ.getMouseOverStructure();
			if(mouseOveredObject != null && mouseOveredObject != selectObject) {
				final Rectangle2D RECT = mouseOveredObject.getBoundingBox();
				g2.setColor(Color.WHITE);
				g2.setStroke(GHQ.stroke5);
				g2.draw(RECT);
				g2.drawOval((int)RECT.getX() - 9,(int)RECT.getY() - 9,18,18);
				g2.drawOval(GHQ.getMouseX() - 5,GHQ.getMouseY() - 5,10,10);
			}
			if(selectObject != null) {
				//changeSlot(Left side menu bar)
				if(keyListener.pullEvent(VK_TAB)) {
					if(++nowSlot > slot_max)
						nowSlot = 0;
					if(nowSlot == 1) { //select script
						configLabel.clicked();
					}else {
						configLabel.outsideClicked();
					}
				}
				//delete
				if(!configLabel.activated && keyListener.pullEvent(VK_BACK_SPACE)) {
					if(selectObject instanceof Unit)
						GHQ.deleteUnit((Unit)selectObject);
					else if(selectObject instanceof Structure)
						GHQ.deleteStructure((Structure)selectObject);
					else if(selectObject instanceof Vegetation)
						GHQ.deleteVegetation((Vegetation)selectObject);
					selectObject = null;
					break;
				}
				//script install / name change
				scriptInstall:{
					if(selectObject instanceof Tile) {
						for(StructureScript<Tile> script : tileScripts) {
							if(configLabel.textEquals(script.getName())) {
								((Tile)selectObject).setScript(script);
								break scriptInstall;
							}
						}
						((Tile)selectObject).setScript(new StructureScript<Tile>());
					}else if(selectObject instanceof Terrain) {
						for(StructureScript<Terrain> script : terrainScripts) {
							if(configLabel.textEquals(script.getName())) {
								((Terrain)selectObject).setScript(script);
								break scriptInstall;
							}
						}
						((Terrain)selectObject).setScript(new StructureScript<Terrain>());
					}else if(selectObject instanceof Unit) {
						((Unit)selectObject).originalName = configLabel.getText();
					}
				}
				//draw selection guide
				final Rectangle2D RECT = selectObject.getBoundingBox();
				RECT.setRect(RECT.getX(),RECT.getY(),RECT.getWidth() + 4,RECT.getHeight() + 4);
				g2.setColor(Color.WHITE);
				g2.setStroke(GHQ.stroke5);
				g2.draw(RECT);
				g2.setColor(Color.RED);
				g2.setStroke(GHQ.stroke3);
				g2.draw(RECT);
				break;
			}
		case TERRAIN:
			Terrain.makeGuiding(g2);
			break;
		case TILES:
			Tile.makeGuiding(g2);
			break;
		}
		//originalName display
		g2.setColor(Color.GRAY);
		g2.setFont(GHQ.basicFont.deriveFont(20.0f));
		for(Unit unit : GHQ.getCharacterList())
			g2.drawString(unit.originalName, (int)unit.dynam.getX(), (int)unit.dynam.getY());
		g2.setFont(GHQ.basicFont);
		//GUI
		GHQ.translateForGUI(true);
		g2.setColor(Color.WHITE);
		g2.drawString("EDIT_MODE", 20, 20);
		GHQ.translateForGUI(false);
	}
	
	//control
	public static void addTileScript(StructureScript<Tile> script) {
		tileScripts.add(script);
	}
	public static void addTerrainScript(StructureScript<Terrain> script) {
		terrainScripts.add(script);
	}
	public static void enable() {
		GHQ.enableGUIs(DefaultStageEditor.EDIT_MENU_GROUP);
		GHQ.enableGUIs(DefaultStageEditor.OBJECT_CONFIG_GROUP);
		selectObject = mouseOveredObject = null;
	}
	public static void disable() {
		GHQ.disableGUIs(DefaultStageEditor.EDIT_MENU_GROUP);
		GHQ.disableGUIs(DefaultStageEditor.OBJECT_CONFIG_GROUP);
	}
}
