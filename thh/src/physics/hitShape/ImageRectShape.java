package physics.hitShape;

import paint.dot.DotPaint;
import physics.HasPoint;

public class ImageRectShape extends HitShape{
	private static final long serialVersionUID = 1238409585756263924L;
	
	protected DotPaint paintScript;
	public ImageRectShape(HasPoint owner, DotPaint paintScript) {
		super(owner);
		this.paintScript = paintScript;
	}
	@Override
	public boolean intersects(HitShape shape) {
		if(!(shape instanceof ImageRectShape || shape instanceof RectShape || shape instanceof Square || shape instanceof Circle))
			System.out.println("unhandled type: " + this.getClass().getName() + " against " + shape.getClass().getName());
		return super.intersects(shape);
	}
	
	//tool
	@Override
	public HitShape clone(HasPoint newOwner) {
		return new ImageRectShape(newOwner, paintScript);
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
}
