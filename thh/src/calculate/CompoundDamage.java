package calculate;

import java.util.ArrayList;

import unit.Unit;

public class CompoundDamage implements Damage {
	protected final ArrayList<Damage> dmgElements = new ArrayList<Damage>();
	public CompoundDamage() {}
	public CompoundDamage(Damage...damages) {
		for(Damage ver : damages)
			dmgElements.add(ver);
	}
	@Override
	public void doDamage(Unit unit) {
		for(Damage ver : dmgElements)
			ver.doDamage(unit);
	}
	@Override
	public Damage compound(Damage anotherDamage) {
		dmgElements.add(anotherDamage);
		return this;
	}
	@Override
	public String toString() {
		return "";
	}
}
