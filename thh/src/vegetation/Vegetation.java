package vegetation;

import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import core.GHQObject;
import core.HasBoundingBox;
import paint.HasDotPaint;
import physics.HasPoint;
import physics.Point;
import paint.DotPaint;

/**
 * A primal class for managing vegetation object.
 * @author bluelaserpointer
 * @since alpha1.0
 */
public class Vegetation extends GHQObject implements Serializable, HasPoint, HasBoundingBox, HasDotPaint{
	private static final long serialVersionUID = -5536970507937704287L;
	
	protected final DotPaint paintScript;
	protected final Point.IntPoint point;

	//init
	public Vegetation(DotPaint paintScript, int x, int y) {
		this.paintScript = paintScript;
		point = new Point.IntPoint(x, y);
	}
	public Vegetation(DotPaint paintScript, Point point) {
		this.paintScript = paintScript;
		this.point = new Point.IntPoint(point);
	}
	public Vegetation(Vegetation sample) {
		this.paintScript = sample.paintScript;
		point = new Point.IntPoint();
	}
	//idle
	public void paint(boolean doAnimation) {
		paintScript.dotPaint(point);
	}
	//control
	@Override
	public Vegetation clone() {
		return new Vegetation(this);
	}
	
	//information
	@Override
	public Point getPoint() {
		return point;
	}
	@Override
	public Rectangle2D getBoundingBox() {
		final int W = paintScript.getDefaultW(), H = paintScript.getDefaultH();
		return new Rectangle2D.Double(point.intX() - W/2, point.intY() - H/2, W, H);
	}
	@Override
	public final DotPaint getPaintScript() {
		return paintScript;
	}
}
