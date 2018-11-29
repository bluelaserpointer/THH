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
import stage.Stage;
import stage.StageEngine;
import stage.StageInfo;
import thh.Chara;
import thh.MessageSource;
import thh.THH;

public class Engine_THH1 extends StageEngine implements MessageSource,ActionSource{
	private final UserChara[] friendCharaClass = {new Marisa(),new Reimu()};
	private final ArrayList<Chara> enemyCharaClass = new ArrayList<Chara>();
	private final Stage[] stages = new Stage[1];
	private int nowStage;
	
	public static final int FRIEND = 0,ENEMY = 100;
	final int F_MOVE_SPD = 20;
	
	int formationsX[],formationsY[];
	int formationCenterX,formationCenterY;
	
	private final CtrlEx_THH1 ctrlEx = new CtrlEx_THH1(this);
	
	int focusIID;
	
	//initialization
	@Override
	public final void loadResource() {
		focusIID = thh.loadImage("focus.png");
	}
	@Override
	public final Chara[] charaSetup() {
		THH.addControlExpansion(ctrlEx);
		for(Chara chara : friendCharaClass) {
			chara.loadImageData();
			chara.loadSoundData();
		}
		//formation
		formationCenterX = THH.getScreenW()/2;formationCenterY = THH.getScreenH()/2;
		formationsX = new int[2];
		formationsY = new int[2];
		formationsX[0] = -15;formationsY[0] = 0;
		formationsX[1] = +15;formationsY[1] = 0;
		//friend
		friendCharaClass[0].battleStarted();
		friendCharaClass[1].battleStarted();
		friendCharaClass[0].spawn(0,FRIEND,formationCenterX + formationsX[0],THH.getScreenH());
		friendCharaClass[1].spawn(1,FRIEND,formationCenterX + formationsX[1],THH.getScreenH());
		//action
		ActionInfo.clear();
		ActionInfo.addDstPlan(1000, THH.getScreenW() - 200, THH.getScreenH() + 100);
		ActionInfo.addDstPlan(1000, THH.getScreenW() + 200, THH.getScreenH() + 100);
		final Action moveLeftToRight200 = new Action(this);
		//enemy
		Chara enemy = new Fairy();
		enemy.loadImageData();
		enemy.loadSoundData();
		enemy.battleStarted();
		enemy.spawn(0, ENEMY, 300, THH.random2(100, 150),1000);
		enemyCharaClass.add(enemy);
		Chara enemy2 = new WhiteMan();
		enemy2.loadImageData();
		enemy2.loadSoundData();
		enemy2.battleStarted();
		enemy2.spawn(1, ENEMY, 400, THH.random2(100, 150),50000);
		enemyCharaClass.add(enemy2);
		Chara enemy3 = new BlackMan();
		enemy3.loadImageData();
		enemy3.loadSoundData();
		enemy3.battleStarted();
		enemy3.spawn(1, ENEMY, 200, THH.random2(100, 150),10000);
		enemyCharaClass.add(enemy3);
		
		return new Chara[]{friendCharaClass[0],friendCharaClass[1],enemy,enemy2,enemy3};
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
		g2.setColor(Color.GRAY);
		g2.setStroke(THH.stroke3);
		g2.draw(stages[nowStage].getLandPolygon());
		if(stopEventKind == NONE) {
			final int MOUSE_X = THH.getMouseX(),MOUSE_Y = THH.getMouseY();
			//gravity
			if(doGravity) {
				for(Chara chara : friendCharaClass)
					chara.gravity(1.1);
			}
			//others
			switch(nowStage) {
			case 0:
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
				for(int i = 0;i < friendCharaClass.length;i++)
					friendCharaClass[i].moveTo(formationCenterX + formationsX[i], formationCenterY + formationsY[i]);
				g2.setColor(Color.RED);
				g2.setStroke(THH.stroke3);
				g2.drawOval(formationCenterX - 5, formationCenterY - 5, 10, 10);
				//shot
				for(Chara chara : friendCharaClass)
					chara.attackOrder = ctrlEx.getCommandBool(CtrlEx_THH1.SHOT);
				//spell
				if(ctrlEx.pullCommandBool(CtrlEx_THH1.SPELL)) {
					friendCharaClass[ctrlEx.spellUser].spellOrder = true;
					ctrlEx.spellUser = NONE;
				}
				//entity
				THH.defaultEntityIdle();
				break;
			}
			//focus
			thh.drawImageTHH(focusIID,MOUSE_X,MOUSE_Y);
			//scroll by mouse
			THH.viewTargetTo((MOUSE_X + formationCenterX)/2,(MOUSE_Y + formationCenterY)/2);
			THH.viewApproach_rate(40);
			/*scroll by key
			if(doScrollView) {
				final int SCROLL_SPEED = 8;
				if(ctrlEx.getCommandBool(CtrlEx_THH1.UP)) {
					THH.viewMove(0,-SCROLL_SPEED);
				}else if(ctrlEx.getCommandBool(CtrlEx_THH1.DOWN)) {
					THH.viewMove(0,SCROLL_SPEED);
				}
				if(ctrlEx.getCommandBool(CtrlEx_THH1.LEFT)) {
					THH.viewMove(-SCROLL_SPEED,0);
				}else if(ctrlEx.getCommandBool(CtrlEx_THH1.RIGHT)) {
					THH.viewMove(SCROLL_SPEED,0);
				}
			}*/
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
		}else if(stopEventKind == THH.STOP) {
			for(int i = 0;i < enemyCharaClass.size();i++) {
				final Chara enemy = enemyCharaClass.get(i);
				enemy.idle(Chara.PASSIVE_CONS);
			}
		}else if(stopEventKind == THH.FREEZE) {
			for(int i = 0;i < enemyCharaClass.size();i++) {
				final Chara enemy = enemyCharaClass.get(i);
				enemy.idle(Chara.PAINT_FREEZED);
			}
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
	final Chara[] getUserChara() {
		return friendCharaClass;
	}
	private boolean doScrollView = false;
	private boolean doGravity = false;
}
