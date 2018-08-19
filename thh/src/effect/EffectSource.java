package effect;

public interface EffectSource {
	
	//idle
	public default void effectIdle(Effect effect,boolean isCharaActive) { //Include painting
		effect.defaultIdle();
		effect.defaultPaint();
	}
	public default void effectAnimationPaint(Effect effect) {
		this.effectPaint(effect);
	}
	public default void effectPaint(Effect effect) {
		effect.defaultPaint();
	}
	
	//event
	public default void effectOutOfLifeSpan(Effect effect) {}
	public default void effectOutOfRange(Effect effect) {}
	
	//judge
	public default boolean deleteEffect(Effect effect){
		return true;
	}

}
