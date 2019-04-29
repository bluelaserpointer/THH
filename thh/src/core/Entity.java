package core;

import physicis.Dynam;
import physicis.HasDynam;

public abstract class Entity implements HasDynam{
	public final int INITIAL_FRAME;
	public final Dynam dynam;
	
	public Entity() {
		dynam = new Dynam();
		INITIAL_FRAME = GHQ.getNowFrame();
	}
	public Entity(Dynam dynam) {
		this.dynam = dynam;
		INITIAL_FRAME = GHQ.getNowFrame();
	}
	public Entity(int nowFrame) {
		dynam = new Dynam();
		INITIAL_FRAME = nowFrame;
	}
	public Entity(Dynam dynam, int nowFrame) {
		this.dynam = dynam;
		INITIAL_FRAME = nowFrame;
	}
	
	abstract public boolean idle();
	abstract public void defaultPaint();
	
	@Override
	public final Dynam getDynam() {
		return dynam;
	}
}