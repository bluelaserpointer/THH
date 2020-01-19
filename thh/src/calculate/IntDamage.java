package calculate;

import unit.Unit;

public class IntDamage implements Damage{
	protected final Consumables consumable;
	protected int damageValue;
	public IntDamage(Consumables consumable, int damageValue) {
		this.consumable = consumable;
		this.damageValue = damageValue;
	}
	@Override
	public void doDamage(Unit unit) {
		consumable.consume(damageValue);
	}
	public void setDamage(int value) {
		damageValue = value;
	}
	public int damageValue() {
		return damageValue;
	}
	public IntDamage unify(IntDamage anotherDamage) {
		damageValue += anotherDamage.damageValue;
		return this;
	}
}
