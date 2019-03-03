package paint;

import java.io.Serializable;

import core.GHQ;

public interface PaintScript extends Serializable{
	public static final PaintScript BLANK_SCRIPT = new PaintScript() {
		private static final long serialVersionUID = -1201824004902133813L;
	};
	public default int getDefaultW() {
		return GHQ.NONE;
	}
	public default int getDefaultH() {
		return GHQ.NONE;
	}
}
