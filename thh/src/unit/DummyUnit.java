package unit;

import core.GHQ;
import geom.HitShape;
import physicis.Dynam;

public class DummyUnit extends Unit{
	private static final long serialVersionUID = 411168207492887964L;

	private int dummyIID;
	public DummyUnit(Dynam dynam) {
		super(dynam, HitShape.NULL_HITSHAPE, GHQ.NONE);
		dummyIID = GHQ.loadImage("thhimage/gui_editor/Unit.png");
	}
	@Override
	public void loadImageData() {
	}
	@Override
	public void paint(boolean doAnimation) {
		GHQ.drawImageGHQ_center(dummyIID, (int)(getDynam().getX()), (int)(getDynam().getY()));
	}

	@Override
	public void respawn(int spawnX, int spawnY) {
		
		
	}

	@Override
	public void moveRel(int dx, int dy) {
		
		
	}

	@Override
	public void moveTo(int x, int y) {
		
		
	}

	@Override
	public void teleportRel(int dx, int dy) {
		
		
	}

	@Override
	public void teleportTo(int x, int y) {
		
		
	}

	@Override
	public int damage_amount(int damage) {
		
		return 0;
	}

	@Override
	public boolean kill(boolean force) {
		
		return false;
	}

	@Override
	public boolean isAlive() {
		
		return true;
	}
}
