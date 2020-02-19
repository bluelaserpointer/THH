package core;

import java.util.Iterator;
import java.util.LinkedList;

import physics.HasBoundingBox;
import physics.HasPoint;
import physics.HitInteractable;
import physics.Point;

/**
 * Note: This class does not used for class GUIParts.
 * @author bluelaserpointer
 *
 * @param <T>
 */
public class GHQObjectList<T extends GHQObject> extends LinkedList<T>{
	private static final long serialVersionUID = 1774337313239922412L;
	
	public void traverseIdle() {
		for(GHQObject element : this) {
			element.idle();
		}
		//check delete claim
		final Iterator<? extends GHQObject> iterator = descendingIterator();
		while(iterator.hasNext()) {
			GHQObject object = iterator.next();
			if(object.hasDeleteClaim()) {
				iterator.remove();
			}
		}
	}
	public void traversePaint() {
		for(GHQObject element : this)
			element.paint();
	}
	public final void defaultTraverse() {
		if(GHQ.isNoStopEvent())
			traverseIdle();
		else
			traversePaint();
	}
	public void forceRemove(T element) {
		remove(element);
	}
	public T forName(String name) {
		for(T element : this) {
			if(element.name().equals(name))
				return element;
		}
		return null;
	}
	public T forMouseOver() {
		if(peek() instanceof HasBoundingBox) {
			for(T element : this) {
				if(((HasBoundingBox)element).isMouseOveredBoundingBox())
					return element;
			}
		}
		return null;
	}
	public T forIntersects(HitInteractable object){
		for(T element : this) {
			if(element instanceof HitInteractable && ((HitInteractable)element).intersects(object))
				return element;
		}
		return null;
	}
	public T forIntersects_atNewPoint(HitInteractable object, double dx, double dy){
		object.point().addXY(dx, dy);
		for(T element : this) {
			if(element instanceof HitInteractable && object.intersects((HitInteractable)element)) {
				object.point().addXY(-dx, -dy);
				return element;
			}
		}
		object.point().addXY(-dx, -dy);
		return null;
	}
	public T forIntersectsDot(int x, int y) {
		for(T element : this) {
			if(element instanceof HitInteractable && ((HitInteractable)element).intersectsDot(x, y))
				return element;
		}
		return null;
	}
	public T forIntersectsLine(int x1, int y1, int x2, int y2) {
		for(T element : this) {
			if(element instanceof HitInteractable && ((HitInteractable)element).intersectsLine(x1, y1, x2, y2))
				return element;
		}
		return null;
	}
	public T forIntersectsRect(int x, int y, int w, int h) {
		for(T element : this) {
			if(element instanceof HitInteractable && ((HitInteractable)element).intersectsRect(x, y, w, h))
				return element;
		}
		return null;
	}
	public boolean intersected(HitInteractable object) {
		return forIntersects(object) != null;
	}
	public boolean intersected_atNewPoint(HitInteractable object, double dx, double dy) {
		return forIntersects_atNewPoint(object, dx, dy) != null;
	}
	public boolean intersected_dot(int x, int y) {
		return forIntersectsDot(x, y) != null;
	}
	public boolean intersected_dot(Point point) {
		return intersected_dot(point.intX(), point.intY());
	}
	public boolean intersected_dot(HasPoint hasPoint) {
		return intersected_dot(hasPoint.point());
	}
	public boolean intersected_line(int x1, int y1, int x2, int y2) {
		return forIntersectsLine(x1, y1, x2, y2) != null;
	}
	public boolean intersected_line(Point p1, Point p2) {
		return intersected_line(p1.intX(), p1.intY(), p2.intX(), p2.intY());
	}
	public boolean intersected_line(HasPoint p1, HasPoint p2) {
		return intersected_line(p1.point(), p2.point());
	}
}
