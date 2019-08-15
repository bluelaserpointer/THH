package calculate;

import core.GHQ;

public class FixedSetter extends Setter{
	private static final long serialVersionUID = -4205400184362313242L;
	private final Number VALUE;

	public static final FixedSetter FixedNONE = new FixedSetter(GHQ.NONE);
	public FixedSetter(Number value) {
		VALUE = value;
	}
	@Override
	protected Number set() {
		return VALUE;
	}
}
