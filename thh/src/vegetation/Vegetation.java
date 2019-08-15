package vegetation;

import java.io.Serializable;

import core.GHQObject;
import paint.dot.DotPaint;
import paint.dot.HasDotPaint;
import physics.HasBoundingBox;
import physics.Point;

/**
 * A primal class for managing vegetation object.
 * @author bluelaserpointer
 * @since alpha1.0
 */
public class Vegetation extends GHQObject implements Serializable, HasBoundingBox, HasDotPaint{
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
	public Point point() {
		return point;
	}
	@Override
	public int width() {
		return paintScript.width();
	}
	@Override
	public int height() {
		return paintScript.height();
	}
	@Override
	public final DotPaint getDotPaint() {
		return paintScript;
	}
}
