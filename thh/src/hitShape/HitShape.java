package hitShape;

import java.awt.Color;
import java.awt.Stroke;
import java.io.Serializable;

import physics.HitInteractable;
import physics.Point;

/**
 * A primal class for managing object hit area.Need {@link Point} to create this instance.<br>
 * The {@link HitShape#intersects(DoublePoint, HasHitShape, DoublePoint)} method provide geometry hit judge,
 * while {@link HitInteractable#intersects(HitInteractable)} can perform grouped hit judge.
 * 
 * @author bluelaserpointer
 * @since alpha1.0
 */
public abstract class HitShape implements Serializable{
	private static final long serialVersionUID = -4544439590281786292L;

	public abstract boolean intersects(Point p1, HitShape shape, Point p2);
	public final boolean intersects(Point p1, HasHitShape target, Point p2) {
		final HitShape SHAPE = target.hitShape();
		return SHAPE == null ? false : intersects(p1, SHAPE, p2);
	}
	public abstract boolean intersectsDot(int myX, int myY, int x, int y);
	public abstract boolean intersectsLine(int myX, int myY, int x1, int y1, int x2, int y2);
	
	//tool
	public abstract HitShape clone();
	public abstract void fill(Point point, Color color);
	public abstract void draw(Point point, Color color, Stroke stroke);
	
	//information
	public abstract int width();
	public abstract int height();
}
