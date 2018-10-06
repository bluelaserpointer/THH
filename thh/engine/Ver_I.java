package engine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;

import bullet.Bullet;
import chara.*;
import thh.Chara;
import thh.MessageSource;
import thh.Stage;
import thh.StageEngine;
import thh.THH;

public class Ver_I extends StageEngine implements MessageSource{
	private final Chara[] battleCharaClass = {new Marisa(),new Reimu()};
	private final ArrayList<Chara> enemyCharaClass = new ArrayList<Chara>();
	private final Stage[] stages = new Stage[1];
	private int nowStage;
	
	public final int ENEMY = 100;
	//initialization
	@Override
	public final Chara[] charaSetup() {
		for(Chara chara : battleCharaClass) {
			chara.loadImageData();
			chara.loadSoundData();
		}
		battleCharaClass[0].battleStarted();
		battleCharaClass[1].battleStarted();
		battleCharaClass[0].spawn(0,0,50,200);
		battleCharaClass[1].spawn(1,0,400,200);
		//enemy
		Chara enemy = new Fairy();
		enemy.loadImageData();
		enemy.loadSoundData();
		enemy.battleStarted();
		enemy.spawn(0, ENEMY, 700, THH.random2(100, 150),1000);
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
			switch(nowStage) {
			case 0:
				for(Chara chara : battleCharaClass)
					chara.gravity(1.1);
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
				break;
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
}
