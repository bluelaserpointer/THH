package geom;

import java.io.Serializable;

import core.HitInteractable;
import physics.Point;

/**
 * A primal class for managing object hit area.Need {@link Point} to create this instance.<br>
 * The {@link HitShape#intersects(DoublePoint, HasHitShape, DoublePoint)} method provide geometry hit judge,
 * while {@link HitInteractable#isHit(HitInteractable)} can perform grouped hit judge.
 * 
 * @author bluelaserpointer
 * @since alpha1.0
 */
public abstract class HitShape implements Serializable{
	private static final long serialVersionUID = -4544439590281786292L;
	public static final HitShape NULL_HITSHAPE = new HitShape() {
		private static final long serialVersionUID = -7767897859147305018L;
		@Override
		public boolean intersects(Point coordinate1, HitShape shape, Point coordinate2) {
			return false;
		}
		@Override
		public HitShape clone() {
			return NULL_HITSHAPE;
		}
		@Override
		public int getWidth() {
			return 0;
		}
		@Override
		public int getHeight() {
			return 0;
		}
	};
	
	public final boolean intersects(Point coordinate1, HasHitShape target, Point coordinate2) {
		final HitShape SHAPE = target.getHitShape();
		return SHAPE == null ? false : intersects(coordinate1, SHAPE, coordinate2);
	}
	public abstract boolean intersects(Point coordinate1, HitShape shape, Point coordinate2);
	
	//tool
	public abstract HitShape clone();
	
	//information
	public abstract int getWidth();
	public abstract int getHeight();
}
