package unit;

import core.GHQ;
import geom.HitShape;
import physics.Dynam;

public class DummyUnit extends Unit{
	private static final long serialVersionUID = 411168207492887964L;

	private int dummyIID;
	public DummyUnit(Dynam dynam) {
		super(HitShape.NULL_HITSHAPE, GHQ.NONE);
		dummyIID = GHQ.loadImage("thhimage/gui_editor/Unit.png");
	}
	@Override
	public void loadImageData() {
	}
	@Override
	public void paint(boolean doAnimation) {
		GHQ.drawImageGHQ_center(dummyIID, getDynam().intX(), getDynam().intY());
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
