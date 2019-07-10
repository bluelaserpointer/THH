package physics;

import core.GHQ;
import core.GHQObject;

public abstract class Entity extends GHQObject implements HasDynam{
	public final int INITIAL_FRAME;
	public final Dynam dynam = def_dynam();
	
	public Entity() {
		INITIAL_FRAME = GHQ.nowFrame();
	}
	public Entity(int nowFrame) {
		INITIAL_FRAME = nowFrame;
	}
	
	@Override
	public final Dynam dynam() {
		return dynam;
	}
	protected Dynam def_dynam() {
		return new Dynam();
	}
}