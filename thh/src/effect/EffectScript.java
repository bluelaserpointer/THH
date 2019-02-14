package effect;

import java.io.Serializable;

import core.Dynam;
import core.DynamInteractable;
import core.GHQ;

public class EffectScript implements Serializable{
	private static final long serialVersionUID = -7528140745782034400L;

	//generation
	public final void create(DynamInteractable user) {
		create(user,user.getDynam());
	}
	public void create(DynamInteractable user,Dynam baseDynam) {}
	
	//idle
	public void effectIdle(Effect effect) { //Include painting
		if(effect.defaultIdle())
			effectNoAnmPaint(effect);
	}
	public void effectPaint(Effect effect) {
		this.effectNoAnmPaint(effect);
	}
	public void effectNoAnmPaint(Effect effect) {
		effect.defaultPaint();
	}
	
	//event
	public void effectOutOfLifeSpan(Effect effect) {}
	public void effectOutOfRange(Effect effect) {}
	
	//judge
	public boolean deleteEffect(Effect effect){
		return true;
	}
	
	//library
	public final void effectFadePaint(Effect effect) {
		GHQ.setImageAlpha((float)(1.0 - (double)GHQ.getPassedFrame(effect.INITIAL_FRAME)/effect.LIMIT_FRAME));
		effect.defaultPaint();
		GHQ.setImageAlpha();
	}
}
