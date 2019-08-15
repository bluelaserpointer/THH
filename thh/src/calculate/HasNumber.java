package calculate;

import core.GHQ;

public interface HasNumber {
	public abstract Number getNumber();
	
	public default int intValue() {
		return getNumber().intValue();
	}
	public default double doubleValue() {
		return getNumber().doubleValue();
	}

	public default boolean isNONE() {
		return getNumber().intValue() == GHQ.NONE;
	}
}
