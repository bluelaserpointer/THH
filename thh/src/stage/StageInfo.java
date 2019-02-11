package stage;

import structure.Structure;
import structure.Terrain;
import unit.Unit;

public class StageInfo {
	public static String name;
	public static int stageW,stageH;
	
	public static Structure[] primeStructures;
	public static Structure[] subStructures;
	
	public static Unit[] primeCharas;
	public static Unit[] subCharas;
	//testStage
	public static int[] poliX,poliY;
	
	static {
		clear();
	}
	public static void clear() {
		name = "<Not named>";
		stageW = stageH = 5000;
		primeStructures = subStructures = new Structure[0];
		primeCharas = subCharas = new Unit[0];
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
