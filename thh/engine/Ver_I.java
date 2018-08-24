package engine;

import java.awt.Color;
import java.awt.Graphics2D;

import chara.Marisa;
import chara.Reimu;
import thh.Chara;
import thh.MessageSource;
import thh.Stage;
import thh.StageEngine;

public class Ver_I extends StageEngine implements MessageSource{
	private final Chara[] battleCharaClass = {new Marisa(),new Reimu()};
	private final Stage[] stages = new Stage[1];
	private int nowStage;
	
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
		return battleCharaClass;
	}
	@Override
	public final Stage stageSetup() {
		return stages[0] = new Stage();
	}
	@Override
	public final void openStage() {
	}
	//idle
	@Override
	public final void idle(Graphics2D g2,int stopEventKind) {
		g2.setColor(Color.GRAY);
		g2.draw(stages[nowStage].getLandPolygon());
		switch(nowStage) {
		case 0:
			for(Chara chara : battleCharaClass)
				chara.gravity(1.1);
			break;
		}
	}
	
	//control
	@Override
	public final void resetStage() {
		
	}
	
	//information

}
