package core;

import physics.Dynam;
import physics.HasDynam;

public abstract class Entity implements HasDynam{
	public final int INITIAL_FRAME;
	public final Dynam dynam = def_dynam();
	
	public Entity() {
		INITIAL_FRAME = GHQ.getNowFrame();
	}
	public Entity(int nowFrame) {
		INITIAL_FRAME = nowFrame;
	}
	
	abstract public boolean idle();
	abstract public void defaultPaint();
	
	@Override
	public final Dynam getDynam() {
		return dynam;
	}
	protected Dynam def_dynam() {
		return new Dynam();
	}
}