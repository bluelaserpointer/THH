package effect.debugEffect;

import java.awt.Color;
import java.awt.Stroke;

import core.GHQ;
import core.GHQObject;
import effect.Effect;
import paint.ColorLine;
import physics.Point;

public class DebugEffect extends Effect{
	public static int lifeSpan = GHQ.MAX;
	public static Effect setLine(Color color, Stroke stroke, int x1, int y1, int x2, int y2) {
		final DebugEffect effect = new DebugEffect();
		effect.point().setXY(x1, y1);
		effect.paintScript = new ColorLine(color, stroke).convertToDotPaint(GHQObject.NULL_GHQ_OBJECT, x2, y2);
		return GHQ.stage().addEffect(effect);
	}
	public static Effect setLine(Color color, Stroke stroke, Point p1, Point p2) {
		return setLine(color, stroke, p1.intX(), p1.intY(), p2.intX(), p2.intY());
	}
	public static Effect setLine(Color color, int x1, int y1, int x2, int y2) {
		return setLine(color, GHQ.stroke1, x1, y1, x2, y2);
	}
	public static Effect setLine(Color color, Point p1, Point p2) {
		return setLine(color, p1.intX(), p1.intY(), p2.intX(), p2.intY());
	}
}
