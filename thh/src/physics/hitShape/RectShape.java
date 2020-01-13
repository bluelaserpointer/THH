package physics.hitShape;

import physics.HasPoint;

public class RectShape extends HitShape{
	private static final long serialVersionUID = 3869237032416439346L;
	
	protected int width, height;
	public RectShape(HasPoint owner, int w, int h) {
		super(owner);
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
	public RectShape setSize(int w, int h) {
		width = w;
		height = h;
		return this;
	}
	public RectShape setSize(HitShape sample) {
		return setSize(sample.width(), sample.height());
	}
	@Override
	public boolean intersects(HitShape shape) {
		if(!(shape instanceof RectShape || shape instanceof Square || shape instanceof Circle))
			System.out.println("unhandled type: " + this.getClass().getName() + " against " + shape.getClass().getName());
		return super.intersects(shape);
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
}
