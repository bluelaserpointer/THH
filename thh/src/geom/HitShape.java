package geom;

import java.awt.geom.Line2D;

import physicis.Coordinate;
import physicis.CoordinateInteractable;

/**
 * 
 * @author bluelaserpointer
 * @since alpha1.0
 */
public abstract class HitShape implements CoordinateInteractable{
	public static final int RECTANGLE = 0,OVAL = 1;
	protected final Coordinate COORDINATE;
	
	public HitShape(Coordinate coordinate) {
		COORDINATE = coordinate;
	}
	public final boolean intersects(HasHitShape target) {
		final HitShape SHAPE = target.getHitShape();
		return SHAPE == null ? false : intersects(SHAPE);
	}
	public abstract boolean intersects(HitShape shape);
	public abstract boolean intersects(Line2D line);
	
	@Override
	public Coordinate getCoordinate() {
		return COORDINATE;
	}
}
