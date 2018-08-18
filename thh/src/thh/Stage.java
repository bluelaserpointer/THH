package thh;

import java.awt.Polygon;
import java.util.ArrayList;

public class Stage {
	//stage
	private String name;
	private boolean isSideView;
	private int stageW,stageH;
	//structure
	private final ArrayList<String>
			structureName = new ArrayList<String>(),
			structureKind = new ArrayList<String>();
	private final ArrayList<Integer>
			structureX = new ArrayList<Integer>(),
			structureY = new ArrayList<Integer>();
	//testStage
	private int[] poliX = {0,0,300,400,500,600,700,700,},poliY = {650,450,450,350,350,450,450,650};
	private Polygon landPolygon = new Polygon(poliX,poliY,poliX.length);
	
	//initialization
	public Stage(){
		name = StageInfo.name;
		isSideView = StageInfo.isSideView;
		stageW = StageInfo.stageW;
		stageH = StageInfo.stageH;
	}
	
	//control
	public final void reset() {
		structureName.clear();
		structureKind.clear();
		structureX.clear();
		structureY.clear();
	}
	public final void addStructure(String kind,int x,int y) {
		this.addStructure("<Not named>",kind,x,y);
	}
	public final void addStructure(String name,String kind,int x,int y) {
		structureName.add(name);
		structureKind.add(kind);
		structureX.add(x);
		structureY.add(y);
	}
	//information
	public final String getStageName() {
		return name;
	}
	public final boolean isSideView() {
		return isSideView;
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
		return landPolygon.intersects(x - w/2,y + h/2,w,h);
	}
	//role
	final void loadStage(String url) {
		
	}
	final void writeStage(String url) {
		
	}
}
