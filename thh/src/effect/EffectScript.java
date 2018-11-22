package effect;

public class EffectScript {
	//idle
	public void effectIdle(Effect effect) { //Include painting
		if(effect.idle())
			effect.paint();
	}
	public void effectAnimationPaint(Effect effect) {
		this.effectPaint(effect);
	}
	public void effectPaint(Effect effect) {
		effect.paint();
	}
	
	//event
	public void effectOutOfLifeSpan(Effect effect) {}
	public void effectOutOfRange(Effect effect) {}
	
	//judge
	public boolean deleteEffect(Effect effect){
		return true;
	}
}
