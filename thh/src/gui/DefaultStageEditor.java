package gui;

import static java.awt.event.KeyEvent.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.ArrayList;

import core.GHQ;
import core.HasBoundingBox;
import input.SingleKeyListener;
import paint.ColorFilling;
import paint.ColorFraming;
import paint.ImageFrame;
import paint.RectPaint;
import physicis.Dynam;
import structure.Structure;
import structure.StructureScript;
import structure.Terrain;
import structure.Tile;
import unit.DummyUnit;
import unit.Unit;
import vegetation.Vegetation;

/**
 * A class provides a default stage editor.
 * @author bluelaserpointer
 * @since alpha1.0
 */
public class DefaultStageEditor extends GUIGroup{
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
	private final String
		EDIT_MENU_GROUP,
		OBJECT_CONFIG_GROUP;
	//GUI
	private static TitledLabel configLabel;
	private static CombinedButtons CB_placeKind;
	//init
	public DefaultStageEditor(String group,File stageFile) {
		super(group, RectPaint.BLANK_SCRIPT, 0, 0, GHQ.getScreenW(), GHQ.getScreenH());
		EDIT_MENU_GROUP = group + ">EDIT_MENU_GROUP";
		OBJECT_CONFIG_GROUP = group + ">OBJECT_CONFIG_GROUP";
		final int SCREEN_W = GHQ.getScreenW(),SCREEN_H = GHQ.getScreenH();
		GHQ.addListenerEx(keyListener);
		super.addParts(new BasicButton(EDIT_MENU_GROUP,new ColorFraming(Color.WHITE,GHQ.stroke3),150,0,SCREEN_W - 150,SCREEN_H) {
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
						GHQ.addVegetation(new Vegetation(new ImageFrame("thhimage/gui_editor/Vegetation.png"), placeX, placeY));
						break;
					case ITEM:
						break;
					}
				}
			}
		});
		super.addParts(CB_placeKind = new CombinedButtons(EDIT_MENU_GROUP, POINTING, 0, 0, 150, SCREEN_H));
		CB_placeKind.addButton(TILES, new ImageFrame("thhimage/gui_editor/Tiles.png"),25,155,40,40);
		CB_placeKind.addButton(TERRAIN, new ImageFrame("thhimage/gui_editor/FreeShape.png"),70,155,40,40);
		CB_placeKind.addButton(UNIT, new ImageFrame("thhimage/gui_editor/Unit.png"),25,200,40,40);
		CB_placeKind.addButton(VEGETATION, new ImageFrame("thhimage/gui_editor/Vegetation.png"),70,200,40,40);
		super.addParts(new BasicButton(EDIT_MENU_GROUP,new ImageFrame("thhimage/gui_editor/Save.png"),25,500,85,40) {
			@Override
			public void clicked() {
				System.out.println("saving...");
				GHQ.saveData(GHQ.getEngine().getStageSaveData(),stageFile);
				System.out.println("complete!");
			}
		});
		super.addParts(configLabel = new TitledLabel(OBJECT_CONFIG_GROUP,new ColorFilling(Color.WHITE),25,300,120,25){
			
		});
		super.addParts(new InputOptionList(configLabel)).addWord("WHITE_WALL", "ABCD", "ABNK");
	}
	//role
	@Override
	public void idle() {
		final Graphics2D G2 = GHQ.getGraphics2D();
		GHQ.translateForGUI(false);
		//mouse
		if(GHQ.key_shift) {
			G2.setColor(Color.RED);
			final int N = 100;
			int S = 4;
			final int SX = (GHQ.getMouseX() + N/2)/N,SY = (GHQ.getMouseY() + N/2)/N;
			for(int xi = -1;xi <= +1;xi++) {
				for(int yi = -1;yi <= +1;yi++)
					G2.fillOval((SX + xi)*N - S/2, (SY + yi)*N - S/2, S, S);
			}
			S += 6;
			G2.drawOval(SX*N - S/2, SY*N - S/2, S, S);
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
				G2.setColor(Color.WHITE);
				G2.setStroke(GHQ.stroke5);
				G2.draw(RECT);
				G2.drawOval((int)RECT.getX() - 9,(int)RECT.getY() - 9,18,18);
				G2.drawOval(GHQ.getMouseX() - 5,GHQ.getMouseY() - 5,10,10);
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
				G2.setColor(Color.WHITE);
				G2.setStroke(GHQ.stroke5);
				G2.draw(RECT);
				G2.setColor(Color.RED);
				G2.setStroke(GHQ.stroke3);
				G2.draw(RECT);
				break;
			}
		case TERRAIN:
			Terrain.makeGuiding(G2);
			break;
		case TILES:
			Tile.makeGuiding(G2);
			break;
		}
		//originalName display
		G2.setColor(Color.GRAY);
		G2.setFont(GHQ.basicFont.deriveFont(20.0f));
		for(Unit unit : GHQ.getCharacterList())
			G2.drawString(unit.originalName, (int)unit.dynam.getX(), (int)unit.dynam.getY());
		G2.setFont(GHQ.basicFont);

		GHQ.translateForGUI(true);
		//GUI
		G2.setColor(Color.WHITE);
		G2.drawString("EDIT_MODE", 20, 20);
	}
	
	//control
	public static void addTileScript(StructureScript<Tile> script) {
		tileScripts.add(script);
	}
	public static void addTerrainScript(StructureScript<Terrain> script) {
		terrainScripts.add(script);
	}
	@Override
	public void enable() {
		super.enable();
		GHQ.stopScreen_noAnm();
		selectObject = mouseOveredObject = null;
	}
	@Override
	public void disable() {
		super.disable();
		GHQ.clearStopEvent();
	}
}
