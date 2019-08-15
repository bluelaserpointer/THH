package unit;

import core.GHQ;
import hitShape.Rectangle;
import paint.ImageFrame;
import physics.Dynam;
import physics.Point;

public class DummyUnit extends Unit{
	private static final long serialVersionUID = 411168207492887964L;

	private ImageFrame imageFrame;
	public DummyUnit(Dynam dynam) {
		super(new Rectangle(Point.NULL_POINT, 0 ,0), GHQ.NONE);
		imageFrame = ImageFrame.create("thhimage/gui_editor/Unit.png");
	}
	@Override
	public void loadImageData() {
	}
	@Override
	public void paint(boolean doAnimation) {
		imageFrame.dotPaint(dynam);
	}

	@Override
	public DummyUnit respawn(int spawnX, int spawnY) {
		return this;
	}

	@Override
	public int damage_amount(int damage) {
		
		return 0;
	}
	
	@Override
	public boolean isAlive() {
		
		return true;
	}
	
	@Override
	public String getName() {
		return "DummyUnit";
	}
}
