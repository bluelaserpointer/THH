package paint;

import java.io.Serializable;

import core.GHQ;

/**
 * A primal interface for managing paint process.
 * @author bluelaserpointer
 * @since alpha1.0
 */
public interface PaintScript extends Serializable{
	public static final PaintScript BLANK_SCRIPT = new PaintScript() {
		private static final long serialVersionUID = -1201824004902133813L;
		@Override
		public int getDefaultW() {
			return 0;
		}
		@Override
		public int getDefaultH() {
			return 0;
		}
	};
	public default int getDefaultW() {
		return GHQ.NONE;
	}
	public default int getDefaultH() {
		return GHQ.NONE;
	}
}