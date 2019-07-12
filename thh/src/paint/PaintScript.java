package paint;

import java.io.Serializable;

/**
 * A primal interface for managing paint process.
 * @author bluelaserpointer
 * @since alpha1.0
 */
public interface PaintScript extends Serializable{
	public static final PaintScript BLANK_SCRIPT = new PaintScript() {
		private static final long serialVersionUID = -1201824004902133813L;
	};
}