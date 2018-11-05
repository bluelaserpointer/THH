package stage;

import structure.Structure;
import structure.Terrain;
import thh.Chara;

public class StageInfo {
	public static String name;
	public static double gravity;
	public static int stageW,stageH;
	
	public static Structure[] primeStructures;
	public static Structure[] subStructures;
	
	public static Chara[] primeCharas;
	public static Chara[] subCharas;
	//testStage
	public static int[] poliX,poliY;
	
	static {
		clear();
	}
	public static void clear() {
		name = "<Not named>";
		gravity = 0.0;
		stageW = stageH = 2000;
		primeStructures = subStructures = new Structure[0];
		primeCharas = subCharas = new Chara[0];
		poliX = new int[]{0,0,300,400,500,600,700,700};
		poliY = new int[]{650,450,450,350,350,450,450,650};
		primeStructures = new Structure[] {new BlankTerrain(poliX,poliY)};
	}
	
}
class BlankTerrain extends Terrain{
	public BlankTerrain(int[] pointX,int[] pointY) {
		super(pointX,pointY);
	}
	@Override
	public int getTeam() {
		return NONE;
	}
}
