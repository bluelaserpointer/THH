package core;

import bullet.Bullet;
import effect.Effect;
import structure.Structure;
import unit.Unit;
import vegetation.Vegetation;

public class GHQObject {
	private static int maxUniqueID = -1;
	public final int UNIQUE_ID;
	private boolean isDeleted;
	
	public GHQObject() {
		UNIQUE_ID = ++maxUniqueID;
	}
	
	public void claimDelete() {
		isDeleted = true;
	}
	public void cancelDelete() {
		isDeleted = false;
	}
	public void forceDelete() {
		if(this instanceof Unit)
			GHQ.stage().units.forceRemove((Unit)this);
		else if(this instanceof Bullet)
			GHQ.stage().bullets.forceRemove((Bullet)this);
		else if(this instanceof Effect)
			GHQ.stage().effects.forceRemove((Effect)this);
		else if(this instanceof Structure)
			GHQ.stage().structures.forceRemove((Structure)this);
		else if(this instanceof Vegetation)
			GHQ.stage().vegetations.forceRemove((Vegetation)this);
	}
	public final boolean hasDeleteClaim() {
		return isDeleted;
	}
	public void idle() {
		paint();
	}
	public void paint(boolean doAnimation) {
		
	}
	public final void paint() {
		paint(true);
	}
	public String getName() {
		return GHQ.NOT_NAMED;
	}
	public static int getTotalAmount() {
		return maxUniqueID + 1;
	}
	public static void initTotalAmount() {
		maxUniqueID = -1;
	}
}