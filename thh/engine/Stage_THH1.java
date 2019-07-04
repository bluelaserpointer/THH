package engine;

import java.util.LinkedList;

import stage.StageSaveData;
import structure.Structure;
import unit.Unit;
import vegetation.Vegetation;

public class Stage_THH1 extends StageSaveData{
	
	private static final long serialVersionUID = -7046067326609502254L;

	public final LinkedList<Unit> UNITS;
	public final LinkedList<Structure> STRUCTURES;
	public final LinkedList<Vegetation> VEGETATIONS;

	public Stage_THH1(LinkedList<Unit> units, LinkedList<Structure> structures, LinkedList<Vegetation> vegetations) {
		this.UNITS = units;
		this.STRUCTURES = structures;
		this.VEGETATIONS = vegetations;
	}
}
