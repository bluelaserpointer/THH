package engine;

import java.util.Arrays;

import stage.StageSaveData;
import structure.Structure;
import unit.Unit;
import vegetation.Vegetation;

public class Stage_THH1 extends StageSaveData{
	
	private static final long serialVersionUID = -7046067326609502254L;

	public final Unit[] UNITS;
	public final Structure[] STRUCTURES;
	public final Vegetation[] VEGETATIONS;

	public Stage_THH1(Unit[] units,Structure[] structures,Vegetation[] vegetations) {
		this.UNITS = Arrays.copyOf(units, units.length);
		this.STRUCTURES = Arrays.copyOf(structures, structures.length);
		this.VEGETATIONS = Arrays.copyOf(vegetations, structures.length);
	}
}
