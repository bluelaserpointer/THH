package engine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;

import bullet.Bullet;
import chara.*;
import stage.Stage;
import stage.StageEngine;
import thh.Chara;
import thh.MessageSource;
import thh.THH;

public class Engine_THH1 extends StageEngine implements MessageSource{
	private final UserChara[] battleCharaClass = {new Marisa(),new Reimu()};
	private final ArrayList<Chara> enemyCharaClass = new ArrayList<Chara>();
	private final Stage[] stages = new Stage[1];
	private int nowStage;
	
	public final int ENEMY = 100;
	final int FORMATION_MOVE_SPD = 20;
	
	int formationsX[],formationsY[];
	int formationX,formationY;
	
	private final CtrlEx_THH1 ctrlEx = new CtrlEx_THH1(this);
	
	//initialization
	@Override
	public final Chara[] charaSetup() {
		THH.addControlExpansion(ctrlEx);
		for(Chara chara : battleCharaClass) {
			chara.loadImageData();
			chara.loadSoundData();
		}
		battleCharaClass[0].battleStarted();
		battleCharaClass[1].battleStarted();
		battleCharaClass[0].spawn(0,0,50,200);
		battleCharaClass[1].spawn(1,0,400,200);
		formationX = 225;formationY = 200;
		formationsX = new int[2];
		formationsY = new int[2];
		formationsX[0] = -175;formationsY[0] = 0;
		formationsX[1] = +175;formationsY[1] = 0;
		//enemy
		Chara enemy = new Fairy();
		enemy.loadImageData();
		enemy.loadSoundData();
		enemy.battleStarted();
		enemy.spawn(0, ENEMY, 400, THH.random2(100, 150),1000);
		enemyCharaClass.add(enemy);
		
		return battleCharaClass;
	}
	@Override
	public final Stage stageSetup() {
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
		g2.draw(stages[nowStage].getLandPolygon());
		if(stopEventKind == NONE) {
			//scroll by key
			if(doScrollView) {
				final int SCROLL_SPEED = 8;
				if(ctrlEx.getCommandBool(CtrlEx_THH1.UP)) {
					THH.moveView(0,-SCROLL_SPEED);
				}else if(ctrlEx.getCommandBool(CtrlEx_THH1.DOWN)) {
					THH.moveView(0,SCROLL_SPEED);
				}
				if(ctrlEx.getCommandBool(CtrlEx_THH1.LEFT)) {
					THH.moveView(-SCROLL_SPEED,0);
				}else if(ctrlEx.getCommandBool(CtrlEx_THH1.RIGHT)) {
					THH.moveView(SCROLL_SPEED,0);
				}
			}
			//gravity
			if(doGravity) {
				for(Chara chara : battleCharaClass)
					chara.gravity(1.1);
			}
			//others
			switch(nowStage) {
			case 0:
				for(int i = 0;i < enemyCharaClass.size();i++) {
					final Chara enemy = enemyCharaClass.get(i);
					if(enemy.getHP() <= 0) {
						enemyCharaClass.remove(enemy);
						continue;
					}
					enemy.idle();
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
				//formation
				if(ctrlEx.getCommandBool(CtrlEx_THH1.UP))
					formationY -= FORMATION_MOVE_SPD;
				else if(ctrlEx.getCommandBool(CtrlEx_THH1.DOWN))
					formationY += FORMATION_MOVE_SPD;
				if(ctrlEx.getCommandBool(CtrlEx_THH1.LEFT))
					formationX -= FORMATION_MOVE_SPD;
				else if(ctrlEx.getCommandBool(CtrlEx_THH1.RIGHT))
					formationX += FORMATION_MOVE_SPD;
				for(int i = 0;i < battleCharaClass.length;i++)
					battleCharaClass[i].moveTo(formationX + formationsX[i], formationY + formationsY[i]);
				g2.setColor(Color.RED);
				g2.setStroke(THH.stroke5);
				g2.drawOval(formationX, formationY, 50, 50);
				//shot
				for(Chara chara : battleCharaClass)
					chara.attackOrder = ctrlEx.getCommandBool(CtrlEx_THH1.SHOT);
				//spell
				if(ctrlEx.pullCommandBool(CtrlEx_THH1.SPELL)) {
					battleCharaClass[ctrlEx.spellUser].spellOrder = true;
					ctrlEx.spellUser = NONE;
				}
				break;
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
	public final Chara[] callBulletEngage(Bullet bullet) {
		final Chara[] result = new Chara[battleCharaClass.length];
		int searched = 0;
		for(int i = 0;i < battleCharaClass.length;i++) {
			final Chara chara = battleCharaClass[i];
			if(chara.bulletEngage(bullet))
				result[searched++] = chara;
		}
		for(int i = 0;i < enemyCharaClass.size();i++) {
			final Chara chara = enemyCharaClass.get(i);
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
		return battleCharaClass;
	}
	private boolean doScrollView = false;
	private boolean doGravity = false;
}
