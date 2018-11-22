package effect;

import thh.DynamInteractable;

public interface EffectSource extends DynamInteractable{
	//effectEvent
	public static final int IDLE = 0,PAINT = 1,ANIMATION_PAINT = 2,OUT_LIFE_SPAN = 3,OUT_RANGE = 4,DELETE = 9;
	
	//idle
	public default boolean effectEvent(int event) {
		/*
		switch(event) {
		case IDLE:
		case PAINT:
		case OUT_LIFE_SPAN:
		case OUT_RANGE:
		case DELETE:
		}
		*/
		return true;
	}
	
	//control
	public void setEffect(int kind,DynamInteractable source);
}
