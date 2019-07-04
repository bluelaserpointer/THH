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
	public void forceDelete() {
		if(this instanceof Unit)
			GHQ.getUnitList().forceRemove((Unit)this);
		else if(this instanceof Bullet)
			GHQ.getBulletList().forceRemove((Bullet)this);
		else if(this instanceof Effect)
			GHQ.getEffectList().forceRemove((Effect)this);
		else if(this instanceof Structure)
			GHQ.getStructureList().forceRemove((Structure)this);
		else if(this instanceof Vegetation)
			GHQ.getVegetationList().forceRemove((Vegetation)this);
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