package stage;

import core.SaveData;

public abstract class StageSaveData extends SaveData{
	private static final long serialVersionUID = -2254494191005088297L;
	
	private String name;
	private int stageW,stageH;
	
	//initialization
	public StageSaveData(){
		name = "<Not named>";
		stageW = stageH = 5000;
	}
	
	//information
	public String getStageName() {
		return name;
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
}
