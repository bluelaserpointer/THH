package physics.hitShape;

import java.awt.Color;
import java.awt.Stroke;
import java.util.LinkedList;
import java.util.List;

import physics.HasPoint;

public class UnionHitShape<T extends HasHitShape> extends HitShape {
	private static final long serialVersionUID = 1231284737748383681L;
	
	protected List<T> objects;
	protected int areaWidth, areaHeight;
	public UnionHitShape(List<T> objects, int areaWidth, int areaHeight) {
		this.objects = objects;
		setOwner(HasPoint.generate(0, 0));
		this.areaWidth = areaWidth;
		this.areaHeight = areaHeight;
	}
	public UnionHitShape(List<T> objects, int areaLeft, int areaTop, int areaWidth, int areaHeight) {
		this.objects = objects;
		setOwner(HasPoint.generate(areaLeft, areaTop));
		this.areaWidth = areaWidth;
		this.areaHeight = areaHeight;
	}
	public UnionHitShape(List<T> objects, HasPoint owner, int areaWidth, int areaHeight) {
		this.objects = objects;
		setOwner(owner);
		this.areaWidth = areaWidth;
		this.areaHeight = areaHeight;
	}
	//init
	public UnionHitShape<T> setObjects(List<T> objects) {
		this.objects = objects;
		return this;
	}
	
	//information;
	@Override
	public int width() {
		return areaWidth;
	}
	@Override
	public int height() {
		return areaHeight;
	}
	@Override
	public HitShape clone(HasPoint newOwner) {
		return new UnionHitShape<T>(objects, newOwner, areaWidth, areaHeight);
	}
	public T elementIntersects(HitShape shape) {
		for(T object : objects) {
			if(object.hitShape().intersects(shape)) {
				return object;
			}
		}
		return null;
	}
	public T elementIntersectsDot(int x, int y) {
		for(T object : objects) {
			if(object.hitShape().intersectsDot(x, y)) {
				return object;
			}
		}
		return null;
	}
	public T elementIntersectsLine(int x1, int y1, int x2, int y2) {
		for(T object : objects) {
			if(object.hitShape().intersectsLine(x1, y1, x2, y2)) {
				return object;
			}
		}
		return null;
	}
	public LinkedList<T> elementsIntersect(HitShape shape) {
		LinkedList<T> list = new LinkedList<>();
		for(T object : objects) {
			if(object.hitShape().intersects(shape)) {
				list.add(object);
			}
		}
		return list;
	}
	public LinkedList<T> elementsIntersectDot(int x, int y) {
		LinkedList<T> list = new LinkedList<>();
		for(T object : objects) {
			if(object.hitShape().intersectsDot(x, y)) {
				list.add(object);
			}
		}
		return list;
	}
	public LinkedList<T> elementsIntersectLine(int x1, int y1, int x2, int y2) {
		LinkedList<T> list = new LinkedList<>();
		for(T object : objects) {
			if(object.hitShape().intersectsLine(x1, y1, x2, y2)) {
				list.add(object);
			}
		}
		return list;
	}
	@Override
	public boolean intersects(HitShape shape) {
		return elementIntersects(shape) != null;
	}
	@Override
	public boolean intersectsDot(int x, int y) {
		return elementIntersectsDot(x, y) != null;
	}
	@Override
	public boolean intersectsLine(int x1, int y1, int x2, int y2) {
		return elementIntersectsLine(x1, y1, x2, y2) != null;
	}
	@Override
	public void fill(Color color) {
		for(T object : objects) {
			object.hitShape().fill(color);
		}
	}
	@Override
	public void draw(Color color, Stroke stroke) {
		for(T object : objects) {
			object.hitShape().draw(color, stroke);
		}
	}

}
