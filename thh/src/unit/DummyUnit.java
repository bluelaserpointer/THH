package unit;

import core.GHQ;
import hitShape.Rectangle;
import physics.Dynam;

public class DummyUnit extends Unit{
	private static final long serialVersionUID = 411168207492887964L;

	private int dummyIID;
	public DummyUnit(Dynam dynam) {
		super(new Rectangle(0,0), GHQ.NONE);
		dummyIID = GHQ.loadImage("thhimage/gui_editor/Unit.png");
	}
	@Override
	public void loadImageData() {
	}
	@Override
	public void paint(boolean doAnimation) {
		GHQ.drawImageGHQ_center(dummyIID, dynam().intX(), dynam().intY());
	}

	@Override
	public void respawn(int spawnX, int spawnY) {
		
		
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
