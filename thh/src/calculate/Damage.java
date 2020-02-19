package calculate;

import static java.lang.Math.PI;

import core.GHQ;
import effect.Effect;
import paint.text.StringPaint;
import physics.HasPoint;
import unit.Unit;

public interface Damage {
	public static final Damage NULL_DAMAGE = new Damage() {
		@Override
		public void doDamage(Unit unit, Unit attacker) {}
		@Override
		public Damage compound(Damage anotherDamage) {
			return anotherDamage;
		}
	};
	public static Effect getDefaultDmgEffect(HasPoint damagedTarget, StringPaint stringPaint) {
		final Effect effect = new Effect();
		effect.setName("DefaultDamageNumberEffect");
		effect.paintScript = stringPaint;
		effect.limitFrame = 15;
		effect.point().setXY(damagedTarget);
		effect.point().addSpeed_DA(10, GHQ.random2(0, 2*PI));
		return effect;
	}
	
	public abstract void doDamage(Unit unit, Unit attacker);
	public default Damage compound(Damage anotherDamage) {
		return new CompoundDamage(this, anotherDamage);
	}
}
