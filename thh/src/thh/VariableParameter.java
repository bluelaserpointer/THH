package thh;

import java.util.ArrayList;

public class VariableParameter {
	public final int BASE_HP;
	public final int BASE_SPD;
	public final int BASE_ATK;
	public final int BASE_AGI;
	
	public int hp;
	public int hpChange;
	public int spd;
	public int atk;
	public int agi;
	public int agiGage;
	public final int HP = 0,HP_CHANGE = 1,SPD = 2,ATK = 3,AGI = 4,AGI_GAGE = 5;
	public final ArrayList<Buff> skillEffect = new ArrayList<Buff>();
	public final ArrayList<Buff> buffs = new ArrayList<Buff>();
	
	public VariableParameter(int hp,int spd,int atk,int agi) {
		BASE_HP = this.hp = hp;
		this.hpChange = 0;
		BASE_SPD = this.spd = spd;
		BASE_ATK = this.atk = atk;
		BASE_AGI = this.agi = agi;
		this.agiGage = 0;
		buffs.clear();
	}
	
	public void reset() {
		hp = BASE_HP;
		spd = BASE_SPD;
		atk = BASE_ATK;
		agi = BASE_AGI;
		buffs.clear();
		agiGage = hpChange = 0;
		for(Buff buff : skillEffect)
			buff.setBuff(this);
	}
}
