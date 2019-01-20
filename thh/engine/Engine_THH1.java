package engine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;

import action.Action;
import action.ActionInfo;
import action.ActionSource;
import bullet.Bullet;
import chara.*;
import stage.ControlExpansion;
import stage.Stage;
import stage.StageEngine;
import stage.StageInfo;
import structure.Structure;
import thh.Chara;
import thh.MessageSource;
import thh.THH;

public class Engine_THH1 extends StageEngine implements MessageSource,ActionSource{
	private static final UserChara[] friendCharaClass = new UserChara[2];
	private static final ArrayList<Chara> enemyCharaClass = new ArrayList<Chara>();
	private static final Stage[] stages = new Stage[1];
	private int nowStage;
	
	public static final int FRIEND = 0,ENEMY = 100;
	final int F_MOVE_SPD = 8;
	
	int formationsX[],formationsY[];
	int formationCenterX,formationCenterY;
	
	//images
	//stageObject
	private int vegImageIID[] = new int[5];
	
	private static final CtrlEx_THH1 ctrlEx = new CtrlEx_THH1();
	
	int focusIID;
	
	//editMode
	static boolean editMode;
	
	//initialization
	@Override
	public final ControlExpansion getCtrl_ex() {
		return ctrlEx;
	}
	@Override
	public final void loadResource() {
		focusIID = thh.loadImage("focus.png");
		vegImageIID[0] = thh.loadImage("veg_leaf.png");
		vegImageIID[1] = thh.loadImage("veg_flower.png");
		vegImageIID[2] = thh.loadImage("veg_leaf2.png");
		vegImageIID[3] = thh.loadImage("veg_stone.png");
		vegImageIID[4] = thh.loadImage("veg_leaf3.png");
		Editor.freeShapeIID = thh.loadImage("gui_editor/FreeShape.png");
	}
	@Override
	public final Chara[] charaSetup() {
		//formation
		formationCenterX = THH.getScreenW()/2;formationCenterY = THH.getScreenH()/2;
		formationsX = new int[2];
		formationsY = new int[2];
		formationsX[0] = -15;formationsY[0] = 0;
		formationsX[1] = +15;formationsY[1] = 0;
		//friend
		friendCharaClass[0] = (UserChara)new Marisa().initialSpawn(0,FRIEND,formationCenterX + formationsX[0],THH.getScreenH());
		friendCharaClass[1] = (UserChara)new Reimu().initialSpawn(1,FRIEND,formationCenterX + formationsX[1],THH.getScreenH());
		//action
		ActionInfo.clear();
		ActionInfo.addDstPlan(1000, THH.getScreenW() - 200, THH.getScreenH() + 100);
		ActionInfo.addDstPlan(1000, THH.getScreenW() + 200, THH.getScreenH() + 100);
		final Action moveLeftToRight200 = new Action(this);
		//enemy
		enemyCharaClass.add(new Fairy().initialSpawn(0, ENEMY, 300, 100,2500));
		enemyCharaClass.add(new Fairy().initialSpawn(0, ENEMY, 700, 20,2500));
		enemyCharaClass.add(new Fairy().initialSpawn(0, ENEMY, 1200, 300,2500));
		enemyCharaClass.add(new Fairy().initialSpawn(0, ENEMY, 1800, 700,2500));
		enemyCharaClass.add(new WhiteMan().initialSpawn(1, ENEMY, 400, THH.random2(100, 150),50000));
		enemyCharaClass.add(new BlackMan().initialSpawn(1, ENEMY, 200, THH.random2(100, 150),10000));
		
		//result
		final ArrayList<Chara> allCharaClass = new ArrayList<Chara>();
		allCharaClass.add(friendCharaClass[0]);
		allCharaClass.add(friendCharaClass[1]);
		allCharaClass.addAll(enemyCharaClass);
		return allCharaClass.toArray(new Chara[0]);
	}
	@Override
	public final Stage stageSetup() {
		StageInfo.clear();
		StageInfo.name = "stage1_1";
		StageInfo.stageW = StageInfo.stageH = 2000;
		return stages[0] = new Stage();
	}
	@Override
	public final void openStage() {
		THH.addMessage(this,"This is a prototype stage.");
	}
	//idle
	private int gameFrame;
	@Override
	public final void idle(Graphics2D g2,int stopEventKind) {
		gameFrame++;
		//stagePaint
		//background
		g2.setColor(new Color(112,173,71));
		g2.fillRect(0,0,stages[nowStage].getStageW(),stages[nowStage].getStageH());
		//landscape
		g2.setColor(Color.LIGHT_GRAY);
		for(Structure ver : stages[nowStage].getPrimeStructures())
			ver.doFill(g2);
		for(Structure ver : stages[nowStage].getSubStructures())
			ver.doFill(g2);
		g2.setColor(Color.GRAY);
		g2.setStroke(THH.stroke3);
		for(Structure ver : stages[nowStage].getPrimeStructures())
			ver.doDraw(g2);
		for(Structure ver : stages[nowStage].getSubStructures())
			ver.doDraw(g2);
		//vegitation
		THH.drawImageTHH_center(vegImageIID[3], 1172, 886,1.3);
		THH.drawImageTHH_center(vegImageIID[0], 1200, 800,1.0);
		THH.drawImageTHH_center(vegImageIID[0], 1800, 350,1.4);
		THH.drawImageTHH_center(vegImageIID[0], 1160, 870,1.7);
		THH.drawImageTHH_center(vegImageIID[1], 1180, 830,1.3);
		THH.drawImageTHH_center(vegImageIID[2], 1102, 815,1.3);
		THH.drawImageTHH_center(vegImageIID[2], 1122, 826,1.3);
		THH.drawImageTHH_center(vegImageIID[4], 822, 886,1.3);
		////////////////
		final int MOUSE_X = THH.getMouseX(),MOUSE_Y = THH.getMouseY();
		if(stopEventKind == NONE) {
			//gravity
			if(doGravity) {
				for(Chara chara : friendCharaClass)
					chara.gravity(1.1);
			}
			//others
			switch(nowStage) {
			case 0:
				for(int i = 0;i < friendCharaClass.length;i++)
					friendCharaClass[i].teleportTo(formationCenterX + formationsX[i], formationCenterY + formationsY[i]);
				//friend
				THH.defaultCharaIdle(friendCharaClass);
				//enemy
				for(int i = 0;i < enemyCharaClass.size();i++) {
					final Chara enemy = enemyCharaClass.get(i);
					if(enemy.getHP() <= 0) {
						enemyCharaClass.remove(enemy);
						continue;
					}
					THH.defaultCharaIdle(enemy);
					if(enemy.getName() == "FairyA") {
						final int FRAME = gameFrame % 240;
						if(FRAME < 100)
							enemyCharaClass.get(i).setSpeed(-5, 0);
						else if(FRAME < 120)
							enemyCharaClass.get(i).setSpeed(0, 0);
						else if(FRAME < 220)
							enemyCharaClass.get(i).setSpeed(5, 0);
						else
							enemyCharaClass.get(i).setSpeed(0, 0);
					}
				}
				g2.setColor(Color.RED);
				g2.setStroke(THH.stroke3);
				g2.drawOval(formationCenterX - 5, formationCenterY - 5, 10, 10);
				//leap
				if(ctrlEx.pullCommandBool(CtrlEx_THH1.LEAP)){
					formationCenterX = MOUSE_X;formationCenterY = MOUSE_Y;
					for(int i = 0;i < friendCharaClass.length;i++)
						friendCharaClass[i].teleportTo(formationCenterX + formationsX[i], formationCenterY + formationsY[i]);
				}
				//shot
				for(Chara chara : friendCharaClass)
					chara.attackOrder = ctrlEx.getCommandBool(CtrlEx_THH1.SHOT);
				//spell
				{
					int spellUser;
					while((spellUser = ctrlEx.pullSpellUser()) != NONE) {
						if(spellUser < friendCharaClass.length)
							friendCharaClass[spellUser].spellOrder = true;
					}
				}
				break;
			}
			//formation
			if(ctrlEx.getCommandBool(CtrlEx_THH1.UP)) {
				formationCenterY -= F_MOVE_SPD;
				THH.viewTargetMove(0,-F_MOVE_SPD);
				THH.pureViewMove(0,-F_MOVE_SPD);
			}else if(ctrlEx.getCommandBool(CtrlEx_THH1.DOWN)) {
				formationCenterY += F_MOVE_SPD;
				THH.viewTargetMove(0,F_MOVE_SPD);
				THH.pureViewMove(0,F_MOVE_SPD);
			}
			if(ctrlEx.getCommandBool(CtrlEx_THH1.LEFT)) {
				formationCenterX -= F_MOVE_SPD;
				THH.viewTargetMove(-F_MOVE_SPD,0);
				THH.pureViewMove(-F_MOVE_SPD,0);
			}else if(ctrlEx.getCommandBool(CtrlEx_THH1.RIGHT)) {
				formationCenterX += F_MOVE_SPD;
				THH.viewTargetMove(F_MOVE_SPD,0);
				THH.pureViewMove(F_MOVE_SPD,0);
			}
		}else if(stopEventKind == THH.STOP || stopEventKind == THH.NO_ANM_STOP) {
			THH.defaultCharaIdle(friendCharaClass);
			THH.defaultCharaIdle(enemyCharaClass);
		}
		THH.defaultEntityIdle();
		//focus
		g2.setColor(new Color(10,200,10,100));
		g2.setStroke(THH.stroke1);
		g2.drawLine(formationCenterX,formationCenterY,MOUSE_X,MOUSE_Y);
		THH.drawImageTHH_center(focusIID,MOUSE_X,MOUSE_Y);
		//scroll by mouse
		if(stopEventKind == NONE && doScrollView) {
			THH.viewTargetTo((MOUSE_X + formationCenterX)/2,(MOUSE_Y + formationCenterY)/2);
			THH.viewApproach_rate(10);
		}
		//editor
		if(editMode) {
			Editor.doEditorPaint(g2);
		}
	}
	
	//control
	@Override
	public final void resetStage() {
		
	}
	@Override
	public final Chara[] callBulletEngage(Chara[] characters,Bullet bullet) {
		final Chara[] result = new Chara[characters.length];
		int searched = 0;
		for(int i = 0;i < characters.length;i++) {
			final Chara chara = characters[i];
			if(chara.bulletEngage(bullet))
				result[searched++] = chara;
		}
		return Arrays.copyOf(result, searched);
	}
	@Override
	public boolean deleteChara(Chara chara) {
		return enemyCharaClass.remove(chara);
	}
	//information
	@Override
	public final int getGameFrame() {
		return gameFrame;
	}
	final static Chara[] getUserChara() {
		return friendCharaClass;
	}
	private boolean doScrollView = true;
	private boolean doGravity = false;
	
	private static final class Editor{
		//gui
		private static int freeShapeIID;
		//role
		static void doEditorPaint(Graphics2D g2) {
			THH.translate(true);
			g2.setColor(Color.WHITE);
			g2.drawString("EDIT_MODE", 20, 20);
			THH.drawImageTHH_center(freeShapeIID,80,200);
			THH.translate(false);
		}
	}
}
