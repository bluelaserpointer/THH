package vegetation;

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
public class Vegetation extends GHQObject implements HasBoundingBox, HasDotPaint {
	protected final DotPaint paintScript;

	//init
	public Vegetation(DotPaint paintScript, int x, int y) {
		super(new Point.IntPoint(x, y));
		this.paintScript = paintScript;
	}
	public Vegetation(DotPaint paintScript, Point point) {
		super(new Point.IntPoint(point));
		this.paintScript = paintScript;
	}
	public Vegetation(Vegetation sample) {
		super(new Point.IntPoint(sample.point()));
		this.paintScript = sample.paintScript;
	}
	//idle
	@Override
	public void paint() {
		super.paint();
		paintScript.dotPaint(point());
	}
	//control
	@Override
	public Vegetation clone() {
		return new Vegetation(this);
	}
	
	//information
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
