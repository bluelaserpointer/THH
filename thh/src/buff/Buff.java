package buff;

import java.io.Serializable;

import core.GHQ;
import status.StatusWithDefaultValue;

public abstract class Buff implements Serializable{
	private static final long serialVersionUID = -6546281620292643179L;
	final int INITIAL_FRAME;
	protected StatusWithDefaultValue targetStatus;
	
	public Buff() {
		INITIAL_FRAME = GHQ.nowFrame();
	}
	public Buff(int initialFrame) {
		INITIAL_FRAME = initialFrame;
	}
	
	public String getName() {
		return "<Not named>";
	}
	public abstract void idle();
	public void reset() {};
	
	public static Buff getSpanTypeBuff(String buffName,int affectKind,int affectAmount,int spanFrame) {
		return new Buff() {
			private static final long serialVersionUID = 9062557653968203855L;
			@Override
			public String getName() {
				return buffName;
			}
			@Override
			public void idle() {
				if(GHQ.nowFrame() - INITIAL_FRAME % spanFrame == 0)
					targetStatus.add(affectKind, affectAmount);
			}
		};
	}
}
