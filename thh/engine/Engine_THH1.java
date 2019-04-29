package engine;

import static java.awt.event.KeyEvent.*;
import static thhunit.THH_BasicUnit.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;

import core.GHQ;
import gui.DefaultStageEditor;
import gui.MessageSource;
import input.DoubleNumKeyListener;
import input.MouseListenerEx;
import input.SingleKeyListener;
import input.SingleNumKeyListener;
import paint.ImageFrame;
import stage.StageEngine;
import stage.StageSaveData;
import structure.Structure;
import thhunit.*;
import unit.Unit;
import thhunit.WhiteMan;
import vegetation.Vegetation;

public class Engine_THH1 extends StageEngine implements MessageSource{
	private static THH_BasicUnit[] friends;
	private static final Stage_THH1[] stages = new Stage_THH1[1];
	private int nowStage;
	private int stageW,stageH;
	
	public static final int FRIEND = 0,ENEMY = 100;
	final int F_MOVE_SPD = 6;
	
	int formationsX[],formationsY[];
	int formationCenterX,formationCenterY;

	public String getVersion() {
		return "alpha1.0.0";
	}
	//inputEvnet
	private final int inputKeys[] = 
	{
		VK_W,
		VK_A,
		VK_S,
		VK_D,
		VK_Q,
		VK_E,
		VK_R,
		VK_F,
		VK_G,
		VK_TAB,
		VK_SHIFT,
		VK_SPACE,
		VK_ESCAPE,
		VK_F6,
	};
	private final MouseListenerEx s_mouseL = new MouseListenerEx();
	private final SingleKeyListener s_keyL = new SingleKeyListener(inputKeys);
	private final SingleNumKeyListener s_numKeyL = new SingleNumKeyListener();
	private final DoubleNumKeyListener d_numKeyL = new DoubleNumKeyListener(20);
	
	//images
	private int focusIID,magicCircleIID;
	
	//editMode
	private static DefaultStageEditor editor;
	private static final String EDIT_GUI_GROUP = "EDIT_GUI_GROUP";
	
	//initialization
	@Override
	public String getTitleName() {
		return "touhouHachidanmakusetu";
	}
	public static void main(String args[]){
		new GHQ(new Engine_THH1());
	}
	
	@Override
	public final void loadResource() {
		/////////////////////////////////
		//images this engine required
		/////////////////////////////////
		focusIID = GHQ.loadImage("thhimage/focus.png");
		magicCircleIID = GHQ.loadImage("thhimage/MagicCircle.png");
		/////////////////////////////////
		//GUI
		/////////////////////////////////
		GHQ.addGUIParts(editor = new DefaultStageEditor(EDIT_GUI_GROUP, new File("stage/saveData1.txt")));
		/////////////////////////////////
		//input
		/////////////////////////////////
		GHQ.addListenerEx(s_mouseL);
		GHQ.addListenerEx(s_keyL);
		GHQ.addListenerEx(s_numKeyL);
		GHQ.addListenerEx(d_numKeyL);
		/////////////////////////////////
		//bullets && effects
		/////////////////////////////////
		THH_BulletLibrary.loadResource();
		THH_EffectLibrary.loadResource();
		/////////////////////////////////
		//units
		/////////////////////////////////
		//formation
		formationCenterX = GHQ.getScreenW()/2;formationCenterY = GHQ.getScreenH() - 100;
		formationsX = new int[2];
		formationsY = new int[2];
		formationsX[0] = -15;formationsY[0] = 0;
		formationsX[1] = +15;formationsY[1] = 0;
		//friend
		friends = new THH_BasicUnit[2];
		friends[0] = Unit.initialSpawn(new Marisa(FRIEND),formationCenterX + formationsX[0],formationCenterY + formationsY[0]);
		friends[0].status.set(HP, 4000);
		friends[1] = Unit.initialSpawn(new Reimu(FRIEND),formationCenterX + formationsX[1],formationCenterY + formationsY[1]);
		friends[1].status.set(HP, 4000);
		for(Unit friend : friends)
			GHQ.addUnit(friend);
		//action
		//ActionInfo.clear();
		//ActionInfo.addDstPlan(1000, GHQ.getScreenW() - 200, GHQ.getScreenH() + 100);
		//ActionInfo.addDstPlan(1000, GHQ.getScreenW() + 200, GHQ.getScreenH() + 100);
		//final Action moveLeftToRight200 = new Action(this);
		//enemy
		GHQ.addUnit(Unit.initialSpawn(new Fairy(ENEMY),300, 100)).status.setDefault(HP, 2500);
		GHQ.addUnit(Unit.initialSpawn(new Fairy(ENEMY),700, 20)).status.setDefault(HP, 2500);
		GHQ.addUnit(Unit.initialSpawn(new Fairy(ENEMY),1200, 300)).status.setDefault(HP, 2500);
		GHQ.addUnit(Unit.initialSpawn(new Fairy(ENEMY),1800, 700)).status.setDefault(HP, 2500);
		GHQ.addUnit(Unit.initialSpawn(new WhiteMan(ENEMY),400, GHQ.random2(100, 150))).status.setDefault(HP, 50000);
		GHQ.addUnit(Unit.initialSpawn(new BlackMan(ENEMY),200, GHQ.random2(100, 150))).status.setDefault(HP, 10000);
		//vegetation
		GHQ.addVegetation(new Vegetation(new ImageFrame("thhimage/veg_leaf.png"),1172,886));
		GHQ.addVegetation(new Vegetation(new ImageFrame("thhimage/veg_flower.png"),1200,800));
		GHQ.addVegetation(new Vegetation(new ImageFrame("thhimage/veg_leaf2.png"),1800,350));
		GHQ.addVegetation(new Vegetation(new ImageFrame("thhimage/veg_stone.png"),1160,870));
		GHQ.addVegetation(new Vegetation(new ImageFrame("thhimage/veg_leaf3.png"),1102,830));
		GHQ.addVegetation(new Vegetation(new ImageFrame("thhimage/veg_leaf3.png"),1122,815));
		GHQ.addVegetation(new Vegetation(new ImageFrame("thhimage/veg_leaf3.png"),822,886));
		stageW = stageH = 5000;
	}
	@Override
	public final void openStage() {
		stages[0] = (Stage_THH1)GHQ.loadData(new File("stage/saveData1.txt"));
		if(stages[0] != null) {
			for(Structure structure : stages[0].STRUCTURES) {
				GHQ.addStructure(structure);
			}
		}
		GHQ.addMessage(this,"This is a prototype stage.");
		s_mouseL.enable();
		s_keyL.enable();
		s_numKeyL.enable();
		d_numKeyL.enable();
	}
	@Override
	public final StageSaveData getStageSaveData() {
		return new Stage_THH1(GHQ.getUnits(),GHQ.getStructures(),GHQ.getVegetations());
	}
	//idle
	private int gameFrame;
	@Override
	public final void idle(Graphics2D g2,int stopEventKind) {
		gameFrame++;
		//stagePaint
		//background
		g2.setColor(new Color(112,173,71));
		g2.fillRect(0,0,stageW,stageH);
		//vegetation
		for(Vegetation ver : GHQ.getVegetationList())
			ver.paint();
		//landscape
		for(Structure ver : GHQ.getStructureList())
			ver.paint();
		////////////////
		GHQ.drawImageGHQ_center(magicCircleIID, formationCenterX, formationCenterY, (double)GHQ.getNowFrame()/35.0);
		g2.setColor(Color.RED);
		g2.fillOval(formationCenterX - 2, formationCenterY - 2, 5, 5);
		////////////////
		final int MOUSE_X = GHQ.getMouseX(),MOUSE_Y = GHQ.getMouseY();
		if(stopEventKind == NONE) {
			//gravity
			if(doGravity) {
				//<editting>
			}
			//others
			switch(nowStage) {
			case 0:
				for(int i = 0;i < friends.length;i++)
					friends[i].teleportTo(formationCenterX + formationsX[i], formationCenterY + formationsY[i]);
				//enemy
				for(Unit enemy : GHQ.getUnitList()) {
					if(!enemy.isAlive())
						continue;
					GHQ.defaultUnitIdle(enemy);
					if(enemy.getName() == "FairyA") {
						final int FRAME = gameFrame % 240;
						if(FRAME < 100)
							enemy.getDynam().setSpeed(-5, 0);
						else if(FRAME < 120)
							enemy.getDynam().setSpeed(0, 0);
						else if(FRAME < 220)
							enemy.getDynam().setSpeed(5, 0);
						else
							enemy.getDynam().setSpeed(0, 0);
					}
				}
				//leap
				if(s_keyL.hasEvent(VK_SHIFT)){
					//final int DX = MOUSE_X - formationCenterX,DY = MOUSE_Y - formationCenterY;
					//if(DX*DX + DY*DY < 5000) {
						formationCenterX = MOUSE_X;formationCenterY = MOUSE_Y;
					//}else {
						//formationCenterX += (double)DX/10.0;formationCenterY += (double)DY/10.0;
					//}
					for(int i = 0;i < friends.length;i++)
						friends[i].teleportTo(formationCenterX + formationsX[i], formationCenterY + formationsY[i]);
				}
				//shot
				for(THH_BasicUnit chara : friends)
					chara.attackOrder = s_mouseL.hasButton1Event();
				//spell
				{
					int spellUser;
					while((spellUser = d_numKeyL.pullHasEventKeyNum()) != NONE) {
						spellUser -= 1;
						if(spellUser < friends.length)
							friends[spellUser].spellOrder = true;
					}
				}
				break;
			}
		}else if(stopEventKind == GHQ.STOP || stopEventKind == GHQ.STOP)
			GHQ.defaultCharaIdle(GHQ.getUnitList());
		GHQ.defaultEntityIdle();
		//focus
		g2.setColor(new Color(200,120,10,100));
		g2.setStroke(GHQ.stroke3);
		g2.drawLine(formationCenterX,formationCenterY,MOUSE_X,MOUSE_Y);
		GHQ.drawImageGHQ_center(focusIID,MOUSE_X,MOUSE_Y);
		//editor
		if(s_keyL.pullEvent(VK_F6)) {
			editor.flit();
		}
		if(!editor.isEnabled()){ //game GUI
			GHQ.translateForGUI(true);
			int pos = 1;
			for(THH_BasicUnit chara : friends) 
				chara.iconPaint.rectPaint(pos++*90 + 10, GHQ.getScreenH() - 40, 80, 30);
			GHQ.translateForGUI(false);
		}
		if(stopEventKind == NONE || editor.isEnabled()) { //scroll
			//scroll by keys
			if(s_keyL.hasEvent(VK_W)) {
				formationCenterY -= F_MOVE_SPD;
				GHQ.viewTargetMove(0,-F_MOVE_SPD);
				GHQ.pureViewMove(0,-F_MOVE_SPD);
			}else if(s_keyL.hasEvent(VK_S)) {
				formationCenterY += F_MOVE_SPD;
				GHQ.viewTargetMove(0,F_MOVE_SPD);
				GHQ.pureViewMove(0,F_MOVE_SPD);
			}
			if(s_keyL.hasEvent(VK_A)) {
				formationCenterX -= F_MOVE_SPD;
				GHQ.viewTargetMove(-F_MOVE_SPD,0);
				GHQ.pureViewMove(-F_MOVE_SPD,0);
			}else if(s_keyL.hasEvent(VK_D)) {
				formationCenterX += F_MOVE_SPD;
				GHQ.viewTargetMove(F_MOVE_SPD,0);
				GHQ.pureViewMove(F_MOVE_SPD,0);
			}
			//scroll by mouse
			if(doScrollView) {
				GHQ.viewTargetTo((MOUSE_X + formationCenterX)/2,(MOUSE_Y + formationCenterY)/2);
				GHQ.viewApproach_rate(10);
			}
		}
	}
	
	//control
	@Override
	public final void resetStage() {
		
	}
	//information
	@Override
	public final int getEngineGameFrame() {
		return gameFrame;
	}
	@Override
	public final boolean inStage(int x,int y) {
		return 0 < x && x <= stageW && 0 < y && y <= stageH;
	}
	@Override
	public final int getStageW() {
		return stageW;
	}
	@Override
	public final int getStageH() {
		return stageH;
	}
	private boolean doScrollView = true;
	private boolean doGravity = false;
}
