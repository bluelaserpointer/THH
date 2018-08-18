package thh;

public class StageInfo {
	public static String name;
	public static boolean isSideView;
	public static int stageW,stageH;
	//testStage
	public static int[] poliX,poliY;
	
	static {
		clear();
	}
	public static void clear() {
		name = "<Not named>";
		isSideView = false;
		stageW = stageH = 2000;
		poliX = new int[]{0,0,300,400,500,600,700,700};
		poliY = new int[]{650,450,450,350,350,450,450,650};
	}
}
