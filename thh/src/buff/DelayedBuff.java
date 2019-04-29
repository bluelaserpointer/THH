package buff;

import core.GHQ;
import status.StatusWithDefaultValue;

public class DelayedBuff extends Buff{
	private static final long serialVersionUID = -8761397095697706630L;
	public final int delay;
	protected final String NAME;
	protected final int AFFECT_KIND;
	protected int affectAmount;
	public DelayedBuff(StatusWithDefaultValue targetStatus,String buffName,int affectKind,int affectAmount,int delay) {
		NAME = buffName;
		AFFECT_KIND = affectKind;
		this.affectAmount = affectAmount;
		this.delay = delay;
	}
	@Override
	public final String getName() {
		return NAME;
	}
	@Override
	public void idle() {
		if(GHQ.isExpired_frame(INITIAL_FRAME, delay))
			targetStatus.add(AFFECT_KIND, affectAmount);
	}

}
