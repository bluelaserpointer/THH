package effect.debugEffect;

import java.awt.Color;
import java.awt.Stroke;

import core.GHQ;
import effect.Effect;
import paint.ColorLine;

public class DebugEffect extends Effect{
	public static int lifeSpan = GHQ.MAX;
	public static Effect setLine(Color color, Stroke stroke, int x1, int y1, int x2, int y2) {
		final DebugEffect effect = new DebugEffect();
		effect.dynam.setXY(x1, y1);
		effect.paintScript = new ColorLine(color, stroke).convertToDotPaint(x2, y2);
		return GHQ.addEffect(effect);
	}
	public static Effect setLine(Color color, int x1, int y1, int x2, int y2) {
		return setLine(color, GHQ.stroke1, x1, y1, x2, y2);
	}
	/*public static Effect makeLine(HasCoordinate source, int dx, int dy) {
		if(source == null)
			return null;
		final Coordinate COORDINATE = source.getCoordinate();
		return makeLine(COORDINATE.getX(), COORDINATE.getY(), dx, dy);
	}*/
	//set x1: 150,y1: 50,x2: 150,y2: 10
	//paint x1: 50,y1: 0,x2: 150,y2: 10
}
