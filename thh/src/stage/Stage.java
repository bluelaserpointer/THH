package stage;

import java.awt.Polygon;

import structure.Structure;
import thh.Chara;
import thh.THH;

public class Stage {
	//stage
	private String name;
	private double gravity;
	private int stageW,stageH;
	//structure
	private Structure[] primeStructures; //多数派
	private Structure[] subStructures; //少数派
	//chara
	private Chara[] primeCharas; //多数派(敵など)
	private Chara[] subCharas; //少数派
	//testStage
	private int[] poliX,poliY;
	private Polygon landPolygon;
	
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
		poliX = StageInfo.poliX;
		poliY = StageInfo.poliY;
		landPolygon = new Polygon(poliX,poliY,poliX.length);
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
		StageInfo.poliX = poliX;
		StageInfo.poliY = poliY;
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
	public final boolean hasGravity() {
		return gravity != 0;
	}
	public final double getGravity() {
		return gravity;
	}
	public final int getStageW() {
		return stageW;
	}
	public final int getStageH() {
		return stageH;
	}
	public final boolean inStage(int x,int y) {
		return 0 <= x && x < stageW && 0 <= y && y < stageH;
	}
	public final Polygon getLandPolygon() {
		return landPolygon;
	}
	public final boolean hitLandscape(int x,int y,int w,int h){
		for(Structure structure : primeStructures) {
			if(structure.hitLandscape(x, y, w, h))
				return true;
		}
		for(Structure structure : subStructures) {
			if(structure.hitLandscape(x, y, w, h))
				return true;
		}
		return false;
	}
	public final boolean hitLandscape(int team,int x,int y,int w,int h){
		if(primeStructures.length > 0 && THH.isRival(team,primeStructures[0].getTeam())) {
			for(Structure structure : primeStructures) {
				if(structure.hitLandscape(x, y, w, h))
					return true;
			}
		}
		for(Structure structure : subStructures) {
			if(structure.hitLandscape(team,x, y, w, h))
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
