package unit;

import action.Action;
import bullet.Bullet;
import core.Dynam;
import core.GHQ;

public class DummyUnit extends Unit{
	private static final long serialVersionUID = 411168207492887964L;

	private int dummyIID;
	public DummyUnit(Dynam dynam) {
		super(new Status(),GHQ.NONE);
		super.dynam.setAllBySample(dynam);
		dummyIID = GHQ.loadImage("gui_editor/Unit.png");
	}
	@Override
	public void loadImageData() {
	}
	@Override
	public void paint(boolean doAnimation) {
		GHQ.drawImageGHQ_center(dummyIID, (int)dynam.getX(), (int)dynam.getY());
	}
	@Override
	public boolean isMovable() {
		return false;
	}

	@Override
	public void respawn(int spawnX, int spawnY) {
		
		
	}

	@Override
	public void respawn(int spawnX, int spawnY, int hp) {
		
		
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
	public void loadActionPlan(Action action) {
		
		
	}

	@Override
	public boolean bulletEngage(Bullet bullet) {
		
		return false;
	}

	@Override
	public int damage_amount(int damage) {
		
		return 0;
	}

	@Override
	public int damage_rate(double rate) {
		
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
