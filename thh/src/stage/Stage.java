package stage;

import structure.Structure;

import java.awt.geom.Line2D;

import chara.Chara;
import core.GHQ;

public class Stage {
	//stage
	private String name;
	private double gravity;
	private int stageW,stageH;
	//structure
	private Structure[] primeStructures; //ｶ猝�ﾅﾉ
	private Structure[] subStructures; //ﾉﾙﾊ�ﾅﾉ
	//chara
	private Chara[] primeCharas; //ｶ猝�ﾅﾉ(筏､ﾊ､ﾉ)
	private Chara[] subCharas; //ﾉﾙﾊ�ﾅﾉ
	
	{
		reset();
	}
	//initialization
	public Stage(){
		load();
	}
	
	//role
	public final void load() {
		name = StageInfo.name;
		gravity = StageInfo.gravity;
		stageW = StageInfo.stageW;
		stageH = StageInfo.stageH;
		primeStructures = StageInfo.primeStructures;
		subStructures = StageInfo.subStructures;
		primeCharas = StageInfo.primeCharas;
		subCharas = StageInfo.subCharas;
	}
	public final void save() {
		StageInfo.name = name;
		StageInfo.gravity = gravity;
		StageInfo.stageW = stageW;
		StageInfo.stageH = stageH;
		StageInfo.primeStructures = primeStructures;
		StageInfo.subStructures = subStructures;
		StageInfo.primeCharas = primeCharas;
		StageInfo.subCharas = subCharas;
	}
	//control
	public final void reset() {
		primeStructures = subStructures = new Structure[0];
		primeCharas = subCharas = new Chara[0];
	}
	//information
	public final String getStageName() {
		return name;
	}
	public final Structure[] getPrimeStructures() {
		return primeStructures;
	}
	public final Structure[] getSubStructures() {
		return subStructures;
	}
	public final int getStageW() {
		return stageW;
	}
	public final int getStageH() {
		return stageH;
	}
	public final boolean hasGravity() {
		return gravity != 0;
	}
	public final double getGravity() {
		return gravity;
	}
	public final boolean inStage(int x,int y) {
		return 0 <= x && x < stageW && 0 <= y && y < stageH;
	}
	public final boolean checkLoS(Line2D line) {
		for(Structure ver : primeStructures) {
			if(ver.intersectsLine(line))
				return false;
		}
		for(Structure ver : subStructures) {
			if(ver.intersectsLine(line))
				return false;
		}
		return true;
	}
	public final boolean hitLandscape(int x,int y,int w,int h){
		for(Structure structure : primeStructures) {
			if(structure.contains(x, y, w, h))
				return true;
		}
		for(Structure structure : subStructures) {
			if(structure.contains(x, y, w, h))
				return true;
		}
		return false;
	}
	public final boolean hitLandscape(int team,int x,int y,int w,int h){
		if(primeStructures.length > 0 && GHQ.isRival(team,primeStructures[0].getTeam())) {
			for(Structure structure : primeStructures) {
				if(structure.contains(x, y, w, h))
					return true;
			}
		}
		for(Structure structure : subStructures) {
			if(structure.hit(team,x, y, w, h))
				return true;
		}
		return false;
	}
	//role
	final void loadStage(String url) {
		
	}
	final void writeStage(String url) {
		
	}
}
