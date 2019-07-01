package thhunit;

import core.GHQ;

public abstract class THH_BasicPlayer extends THH_BasicUnit{
	private static final long serialVersionUID = -8336591617807677807L;
	public THH_BasicPlayer(int charaSize, int initialGroup) {
		super(charaSize, initialGroup);
	}

	@Override
	public final void baseIdle() {
		super.baseIdle();
		final int mouseX = GHQ.getMouseX(), mouseY = GHQ.getMouseY();
		////////////
		//main
		////////////
		// angle
		baseAngle.set(dynam.angleToMouse());
		// dodge
		if (dodgeOrder)
			dodge(mouseX, mouseY);
		// attack
		if (attackOrder) {
			final int weapon = weaponSlot[slot_weapon];
			if (weapon != NONE && useWeapon(weapon))
				setBullet(weapon,this);
		}
		// spell
		if (spellOrder) {
			final int spell = spellSlot[slot_spell];
			if (spell != NONE && useWeapon(spell))
				setBullet(spell,this);
		}
		dynam.approachIfNoObstacles(this, charaDstX, charaDstY, charaSpeed);
		// weaponChange
		int roll = weaponChangeOrder;
		if (roll != 0) {
			int target = slot_spell;
			if (roll > 0) {
				while (target < spellSlot_max - 1) {
					if (spellSlot[++target] != NONE) {
						if (--roll == 0)
							break;
					}
				}
			} else {
				while (target > 0) {
					if (spellSlot[--target] != NONE) {
						if (++roll == 0)
							break;
					}
				}
			}
			slot_spell = target;
		}
		////////////
		//input
		////////////
		resetSingleOrder();
	}
}
