package core;

import physicis.HasDynam;

public abstract class Entity_double implements HasDynam{
	public final HasDynam source; //an information source of user
	public final int INITIAL_FRAME;
	
	public Entity_double() {
		source = HasDynam.NULL_DYNAM_SOURCE;
		INITIAL_FRAME = GHQ.getNowFrame();
	}
	public Entity_double(HasDynam source) {
		this.source = source;
		INITIAL_FRAME = GHQ.getNowFrame();
	}
	public Entity_double(HasDynam source, int nowFrame) {
		this.source = source;
		INITIAL_FRAME = nowFrame;
	}
	abstract public boolean defaultIdle();
	abstract public void defaultPaint();
}