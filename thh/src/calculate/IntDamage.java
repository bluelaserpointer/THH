package calculate;

import core.GHQObject;

public class IntDamage extends Damage {
	protected final Consumables consumable;
	protected int damageValue;
	public IntDamage(Consumables consumable, int damageValue) {
		this.consumable = consumable;
		this.damageValue = damageValue;
	}
	public IntDamage(IntDamage intDamage) {
		super(intDamage);
		this.consumable = intDamage.consumable;
		this.damageValue = intDamage.damageValue;
	}
	@Override
	public void doDamage(GHQObject target) {
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
	@Override
	public IntDamage clone() {
		return new IntDamage(this);
	}
}
