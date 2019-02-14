package unit;

import java.io.Serializable;

import core.GHQ;

public abstract class Buff implements Serializable{
	private static final long serialVersionUID = -6546281620292643179L;
	final int INITIAL_FRAME;
	
	public Buff() {
		INITIAL_FRAME = GHQ.getNowFrame();
	}
	public Buff(int initialFrame) {
		INITIAL_FRAME = initialFrame;
	}
	
	public String getName() {
		return "<Not named>";
	}
	public abstract void idle(Status status);
	public void reset(Status status) {};
	
	public static Buff getSpanTypeBuff(String buffName,int affectKind,int changes,int spanFrame) {
		return new Buff() {
			private static final long serialVersionUID = 9062557653968203855L;
			@Override
			public String getName() {
				return buffName;
			}
			@Override
			public void idle(Status status) {
				if(GHQ.getNowFrame() - INITIAL_FRAME % spanFrame == 0)
					status.add(affectKind, changes);
			}
		};
	}
}
