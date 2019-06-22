package effect.debugEffect;

import java.awt.Color;
import java.awt.Stroke;

import core.GHQ;
import effect.Effect;
import paint.ColorLine;

public class DebugEffect extends Effect{
	public static int lifeSpan = 100;
	public static Color color = Color.RED;
	public static Stroke stroke = GHQ.stroke1;
	public static Effect setLine(int x1, int y1, int x2, int y2) {
		final DebugEffect effect = new DebugEffect();
		effect.dynam.setXY(x1, y1);
		effect.paintScript = new ColorLine(color, stroke) {
			private static final long serialVersionUID = -179338272273721936L;
			
			private final int lifeSpan = DebugEffect.lifeSpan;
			private final int INITIAL_FRAME = GHQ.getNowFrame();
			@Override
			public void linePaint(int x1, int y1, int x2, int y2) {
				GHQ.setImageAlpha((float)(1.0 - (double)GHQ.getPassedFrame(INITIAL_FRAME)/lifeSpan));
				super.linePaint(x1, y1, x2, y2);
			}
		}.convertToDotPaint(x2, y2);
		return effect;
	}
	/*public static Effect makeLine(HasCoordinate source, int dx, int dy) {
		if(source == null)
			return null;
		final Coordinate COORDINATE = source.getCoordinate();
		return makeLine(COORDINATE.getX(), COORDINATE.getY(), dx, dy);
	}*/
}
