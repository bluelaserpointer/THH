package stage;

import structure.Structure;

import java.awt.geom.Line2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import core.GHQ;

public class Stage implements Serializable{
	private static final long serialVersionUID = -2254494191005088297L;
	//stage
	private String name;
	private int stageW,stageH;
	//structure
	private ArrayList<Structure> structures = new ArrayList<Structure>();
	
	{
		reset();
	}
	//initialization
	public Stage(){
		name = "<Not named>";
		stageW = stageH = 5000;
		structures.add(new BlankTerrain(new int[]{0,0,300,400,500,600,700,700},new int[]{650,450,450,350,350,450,450,650}));
	}
	
	//control
	public final void reset() {
		structures.clear();
	}
	//information
	public final String getStageName() {
		return name;
	}
	public final ArrayList<Structure> getStructures() {
		return structures;
	}
	public final Structure[] getStructures(int team,boolean white) {
		Structure result[] = new Structure[structures.size()];
		int searched = 0;
		for(Structure structure : structures) {
			if(white == (structure.getTeam() == team))
				result[searched++] = structure;
		}
		return Arrays.copyOf(result, searched);
	}
	public final Structure getStructure(int index) {
		return structures.get(index);
	}
	public final int getStructureAmount() {
		return structures.size();
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
	public final boolean checkLoS(Line2D line) {
		for(Structure structure : structures) {
			if(structure.intersectsLine(line))
				return false;
		}
		return true;
	}
	public final boolean hitLandscape(int x,int y,int w,int h){
		for(Structure structure : structures) {
			if(structure.contains(x, y, w, h))
				return true;
		}
		return false;
	}
	public final boolean hitLandscape(int team,int x,int y,int w,int h){
		for(Structure structure : structures) {
			if(GHQ.isRival(team,structure.getTeam()) && structure.contains(x, y, w, h))
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
