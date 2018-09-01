package effect;

public interface EffectSource {
	
	//idle
	public default void effectIdle(Effect effect,boolean isCharaActive) { //Include painting
		effect.idle();
		effect.paint();
	}
	public default void effectAnimationPaint(Effect effect) {
		this.effectPaint(effect);
	}
	public default void effectPaint(Effect effect) {
		effect.paint();
	}
	
	//event
	public default void effectOutOfLifeSpan(Effect effect) {}
	public default void effectOutOfRange(Effect effect) {}
	
	//judge
	public default boolean deleteEffect(Effect effect){
		return true;
	}

}
