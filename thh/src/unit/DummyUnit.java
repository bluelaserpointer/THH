package unit;

import paint.ImageFrame;
import physics.Dynam;

public class DummyUnit extends Unit{
	private static final long serialVersionUID = 411168207492887964L;

	private ImageFrame imageFrame;
	public DummyUnit(Dynam dynam) {
		imageFrame = ImageFrame.create("thhimage/gui_editor/Unit.png");
	}
	@Override
	public void loadImageData() {
	}
	@Override
	public void paint() {
		imageFrame.dotPaint(point());
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
	public String name() {
		return "DummyUnit";
	}
}
