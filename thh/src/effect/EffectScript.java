package effect;

import thh.THH;

public class EffectScript {
	//idle
	public void effectIdle(Effect effect) { //Include painting
		if(effect.defaultIdle())
			effectPaint(effect);
	}
	public void effectAnimationPaint(Effect effect) {
		this.effectPaint(effect);
	}
	public void effectPaint(Effect effect) {
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
		THH.setImageAlpha((float)(1.0 - (double)THH.getPassedFrame(effect.INITIAL_FRAME)/effect.LIMIT_FRAME));
		effect.defaultPaint();
		THH.setImageAlpha();
	}
}
