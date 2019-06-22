package vegetation;

import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import core.Deletable;
import core.GHQ;
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
public class Vegetation implements Serializable, HasPoint, HasBoundingBox, HasDotPaint, Deletable{
	private static final long serialVersionUID = -5536970507937704287L;
	public final int UNIQUE_ID;
	public static int nowMaxUniqueID = -1;
	
	protected final DotPaint paintScript;
	protected final Point.IntPoint point;
	private boolean isDeleted;

	//init
	public Vegetation(DotPaint paintScript,int x,int y) {
		UNIQUE_ID = ++nowMaxUniqueID;
		this.paintScript = paintScript;
		point = new Point.IntPoint(x, y);
	}
	public Vegetation(DotPaint paintScript,Point point) {
		UNIQUE_ID = ++nowMaxUniqueID;
		this.paintScript = paintScript;
		this.point = new Point.IntPoint(point);
	}
	public Vegetation(Vegetation sample) {
		UNIQUE_ID = ++nowMaxUniqueID;
		this.paintScript = sample.paintScript;
		point = new Point.IntPoint();
	}
	//idle
	public void idle() {
		
	}
	public void paint() {
		if(paintScript == null)
			return;
		paintScript.dotPaint(point);
	}
	//control
	@Override
	public Vegetation clone() {
		return new Vegetation(this);
	}
	public void beforeDelete() {
		isDeleted = true;
	}
	@Override
	public void delete() {
		GHQ.deleteVegetation(this);
	}
	
	//information
	@Override
	public Point getPoint() {
		return point;
	}
	@Override
	public Rectangle2D getBoundingBox() {
		final int W = paintScript.getDefaultW(),H = paintScript.getDefaultH();
		return new Rectangle2D.Double(point.intX() - W/2, point.intY() - H/2, W, H);
	}
	@Override
	public final DotPaint getPaintScript() {
		return paintScript;
	}
	@Override
	public final boolean isDeleted() {
		return isDeleted;
	}
}
