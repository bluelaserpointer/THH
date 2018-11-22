package bullet;

import thh.DynamInteractable;

public interface BulletSource extends DynamInteractable{
	//bulletEvent
	public static final int IDLE = 0,PAINT = 1,ANIMATION_PAINT = 2,OUT_LIFE_SPAN = 3,OUT_RANGE = 4,OUT_PENETRATION = 5,OUT_REFLECTION = 6,HIT_LANDSCAPE = 7,HIT_TARGET = 8,DELETE = 9;
	//idle
	public default boolean bulletEvent(int event) {
		/*
		switch(event) {
		case IDLE:
		case PAINT:
		case OUT_LIFE_SPAN:
		case OUT_RANGE:
		case OUT_PENETRATION:
		case OUT_REFLECTION:
		case HIT_LANDSCAPE:
		case HIT_TARGET:
		case DELETE:
		}
		*/
		return true;
	}

	//control
	public void setBullet(int kind,DynamInteractable source);
}
