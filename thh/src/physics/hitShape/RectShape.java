package physics.hitShape;

import java.awt.Color;
import java.awt.Stroke;

import physics.HasPoint;

public class RectShape extends HitShape {
	private static final long serialVersionUID = 3869237032416439346L;
	
	protected int width, height;
	public RectShape(HasPoint owner, int w, int h) {
		super(owner);
		width = w;
		height = h;
	}
	public RectShape(int x, int y, int w, int h) {
		super(HasPoint.generate(x, y));
		width = w;
		height = h;
	}
	//init
	public RectShape setWidth(int w) {
		width = w;
		return this;
	}
	public RectShape setHeight(int h) {
		height = h;
		return this;
	}
	public RectShape setBoundsSize(int w, int h) {
		width = w;
		height = h;
		return this;
	}
	public RectShape setBoundsSize(HitShape sample) {
		return setBoundsSize(sample.width(), sample.height());
	}
	@Override
	public boolean intersects(HitShape shape) {
		if(!(shape instanceof RectShape || shape instanceof Square || shape instanceof Circle))
			System.out.println("unhandled type: " + this.getClass().getName() + " against " + shape.getClass().getName());
		return super.boundingBoxIntersects(shape);
	}
	
	//tool
	@Override
	public HitShape clone(HasPoint newOwner) {
		return new RectShape(newOwner, width, height);
	}
	
	//information
	@Override
	public int width() {
		return width;
	}
	@Override
	public int height() {
		return height;
	}
	@Override
	public boolean intersectsDot(int x, int y) {
		return super.boundingBoxIntersectsDot(x, y);
	}
	@Override
	public boolean intersectsLine(int x1, int y1, int x2, int y2) {
		return super.boundingBoxIntersectsLine(x1, y1, x2, y2);
	}
	@Override
	public void fill(Color color) {
		super.boundingBoxFill(color);
	}
	@Override
	public void draw(Color color, Stroke stroke) {
		super.boundingBoxDraw(color, stroke);
	}
}
