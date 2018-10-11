package thh;

import java.awt.Polygon;
import java.util.ArrayList;

import structure.Structure;

public class Stage {
	private final int
		NONE = THH.NONE;
	//stage
	private String name;
	private double gravity;
	private int stageW,stageH;
	private int stageFrame;
	//structure
	private Structure[] primeStructures; //多数派
	private Structure[] subStructures; //少数派
	private int primeStructureTeam;
	//chara
	private Chara[] primeCharas; //多数派(敵など)
	private Chara[] subCharas; //少数派
	private int primeCharaTeam;
	//testStage
	private int[] poliX = {0,0,300,400,500,600,700,700,},poliY = {650,450,450,350,350,450,450,650};
	private Polygon landPolygon = new Polygon(poliX,poliY,poliX.length);
	
	{
		reset();
	}
	//initialization
	public Stage(){
		name = StageInfo.name;
		gravity = StageInfo.gravity;
		stageW = StageInfo.stageW;
		stageH = StageInfo.stageH;
	}
	
	//role
	public final void load() {
		StageInfo.name = name;
		StageInfo.gravity = gravity;
		StageInfo.stageW = stageW;
		StageInfo.stageH = stageH;
		StageInfo.primeStructures = primeStructures;
		StageInfo.subStructures = subStructures;
		if(primeStructures.length > 0)
			primeStructureTeam = primeStructures[0].getTeam();
		StageInfo.primeCharas = primeCharas;
		StageInfo.subCharas = subCharas;
		if(primeCharas.length > 0)
			primeCharaTeam = primeCharas[0].getTeam();
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
		//return landPolygon.intersects(x - w/2,y + h/2,w,h);
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
		if(THH.isRival(team,primeStructureTeam)) {
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
