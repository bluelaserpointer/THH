package gui;

import static java.awt.event.KeyEvent.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.Collection;

import core.GHQ;
import core.GHQObject;
import core.GHQObjectList;
import core.HasBoundingBox;
import input.SingleKeyListener;
import paint.ColorFilling;
import paint.ColorFraming;
import paint.ImageFrame;
import paint.RectPaint;
import physics.Dynam;
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
	private final GHQObjectList<Tile> tileScripts = new GHQObjectList<Tile>();
	private final GHQObjectList<Terrain> terrainScripts = new GHQObjectList<Terrain>();
	
	//input
	private static final int inputKeys[] = 
	{
		VK_BACK_SPACE,
		VK_DELETE,
		VK_C,
		VK_V,
		VK_TAB,
		VK_CONTROL,
	};
	private static final SingleKeyListener keyListener = new SingleKeyListener(inputKeys);
	private static int nowSlot = 0,slot_max = 1;
	
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
		EDIT_MENU_GROUP;
	//GUI
	private static TitledLabel configLabel;
	private static CombinedButtons CB_placeKind;
	//init
	public DefaultStageEditor(String group,File stageFile) {
		super(group, RectPaint.BLANK_SCRIPT, 0, 0, GHQ.getScreenW(), GHQ.getScreenH());
		EDIT_MENU_GROUP = group + ">EDIT_MENU_GROUP>";
		final int SCREEN_W = GHQ.getScreenW(),SCREEN_H = GHQ.getScreenH();
		GHQ.addListenerEx(keyListener);
		super.addParts(new BasicButton(EDIT_MENU_GROUP,new ColorFraming(Color.WHITE,GHQ.stroke3),150,0,SCREEN_W - 150,SCREEN_H) {
			@Override
			public void idle() {
				super.idle();
				if(isMouseEntered()) {
					final Graphics2D G2 = GHQ.getGraphics2D();
					GHQ.translateForGUI(false);
					//guide
					switch(CB_placeKind.getSelection()) {
					case POINTING:
						mouseOveredObject = GHQ.getUnitList().forMouseOver();
						if(mouseOveredObject == null)
							mouseOveredObject = GHQ.getVegetationList().forMouseOver();
						if(mouseOveredObject == null)
							mouseOveredObject = GHQ.getStructureList().forMouseOver();
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
							if(!configLabel.activated && keyListener.pullEvent(VK_DELETE)) {
								if(selectObject instanceof GHQObject) {
									((GHQObject)selectObject).forceDelete();
								}else
									System.out.println("detected undeletable object.");
								selectObject = null;
								break;
							}
							/*//script install / name change
							if(selectObject instanceof Tile) {
								selectObject = tileScripts.forName(configLabel.getText());
							}else if(selectObject instanceof Terrain) {
								selectObject = terrainScripts.forName(configLabel.getText());
							}else if(selectObject instanceof Unit) {
								((Unit)selectObject).originalName = configLabel.getText();
							}*/
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
					GHQ.translateForGUI(true);
				}
			}
			@Override
			public void clicked() {
				if(CB_placeKind.isDefaultSelection()) { //not selected any placeKind = object select
					selectObject = mouseOveredObject;
					keyListener.enable();
					final String labelText;
					if(selectObject instanceof Tile) {
						labelText = ((Tile)selectObject).getName();
						configLabel.setTitle("script:");
					}else if(selectObject instanceof Terrain) {
						labelText = ((Terrain)selectObject).getName();
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
							GHQ.addStructure(Tile.blueprint_addEndPointAndFlush(GHQ.getMouseX(), GHQ.getMouseY()));
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
		super.addParts(CB_placeKind = new CombinedButtons(EDIT_MENU_GROUP + "CombinedButtons", POINTING, 25, 155, 85, 85));
		CB_placeKind.addButton(TILES, new ImageFrame("thhimage/gui_editor/Tiles.png"),25,155,40,40);
		CB_placeKind.addButton(TERRAIN, new ImageFrame("thhimage/gui_editor/FreeShape.png"),70,155,40,40);
		CB_placeKind.addButton(UNIT, new ImageFrame("thhimage/gui_editor/Unit.png"),25,200,40,40);
		CB_placeKind.addButton(VEGETATION, new ImageFrame("thhimage/gui_editor/Vegetation.png"),70,200,40,40);
		super.addParts(new BasicButton("SAVE_BUTTON", new ImageFrame("thhimage/gui_editor/Save.png"),25,500,85,40) {
			@Override
			public void clicked() {
				System.out.println("saving...");
				GHQ.saveData(GHQ.getEngine().getStageSaveData(),stageFile);
				System.out.println("complete!");
			}
		});
		super.addParts(configLabel = new TitledLabel(EDIT_MENU_GROUP + "CONFIG_LABEL",new ColorFilling(Color.WHITE),25,300,120,25){
			
		});
		super.addParts(new InputOptionList(configLabel)).addWord("WHITE_WALL", "ABCD", "ABNK");
	}
	//role
	@Override
	public void idle() {
		super.idle();
		final Graphics2D G2 = GHQ.getGraphics2D();
		GHQ.translateForGUI(false);
		//mouse
		if(true /*GHQ.key_shift*/) {
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
		}
		//originalName display
		G2.setColor(Color.GRAY);
		G2.setFont(GHQ.basicFont.deriveFont(20.0f));
		for(Unit unit : GHQ.getUnitList())
			G2.drawString(unit.originalName, unit.getDynam().intX(), unit.getDynam().intY());
		G2.setFont(GHQ.basicFont);

		GHQ.translateForGUI(true);
		//GUI
		G2.setColor(Color.BLACK);
		G2.drawString("EDIT_MODE", 20, 20);
	}
	
	//control
	public void addTileScript(Collection<Tile> script) {
		tileScripts.addAll(script);
	}
	public void addTerrainScript(Collection<Terrain> script) {
		terrainScripts.addAll(script);
	}
	@Override
	public void enable() {
		super.enable();
		GHQ.stopScreen();
		selectObject = mouseOveredObject = null;
	}
	@Override
	public void disable() {
		super.disable();
		GHQ.clearStopEvent();
	}
}
