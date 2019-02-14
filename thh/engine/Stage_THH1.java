package engine;

import java.util.Arrays;

import stage.StageSaveData;
import structure.Structure;
import unit.Unit;

public class Stage_THH1 extends StageSaveData{
	
	private static final long serialVersionUID = -7046067326609502254L;

	public final Unit[] UNITS;
	public final Structure[] STRUCTURES;

	public Stage_THH1(Unit[] units,Structure[] structures) {
		this.UNITS = Arrays.copyOf(units, units.length);
		this.STRUCTURES = Arrays.copyOf(structures, structures.length);
	}
}
