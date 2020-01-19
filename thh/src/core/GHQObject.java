package core;

import bullet.Bullet;
import effect.Effect;
import gui.GUIParts;
import gui.stageEditor.GHQObjectHashMap;
import paint.HasPaint;
import physics.HasPhysics;
import physics.Physics;
import physics.Point;
import physics.StdPhysics;
import physics.hitShape.HitShape;
import physics.hitShape.RectShape;
import structure.Structure;
import unit.Unit;
import vegetation.Vegetation;

public class GHQObject implements HasName, HasPaint, HasPhysics  {
	public final int INITIAL_FRAME;
	private static int maxUniqueID = -1;
	public final int UNIQUE_ID;
	private boolean isDeleted;
	private int paintOrder = -1;
	public static int nowPaintOrder = -1;
	
	protected String name = this.getClass().getName() + GHQ.NOT_NAMED;
	
	protected GHQObject outerContainer = null;
	
	protected Physics physics;
	
	public static final GHQObject NULL_GHQ_OBJECT = new GHQObject();
	
	//init
	public GHQObject() {
		INITIAL_FRAME = GHQ.nowFrame();
		UNIQUE_ID = ++maxUniqueID;
		physics = new StdPhysics();
	}
	public GHQObject(Physics physics) {
		INITIAL_FRAME = GHQ.nowFrame();
		UNIQUE_ID = ++maxUniqueID;
		this.physics = physics;
	}
	public GHQObject(Point point) {
		INITIAL_FRAME = GHQ.nowFrame();
		UNIQUE_ID = ++maxUniqueID;
		physics = new StdPhysics().setPoint(point);
	}
	public GHQObject(Point point, HitShape hitShape) {
		INITIAL_FRAME = GHQ.nowFrame();
		UNIQUE_ID = ++maxUniqueID;
		physics = new StdPhysics().setPoint(point).setHitShape(hitShape);
	}
	public GHQObject(int x, int y, int w, int h) {
		INITIAL_FRAME = GHQ.nowFrame();
		UNIQUE_ID = ++maxUniqueID;
		physics = new StdPhysics().setPoint(new Point.IntPoint(x + w/2, y + h/2)).setHitShape(new RectShape(physics, w, h));
	}
	public GHQObject setName(String name) {
		this.name = name;
		return this;
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
		else if(this instanceof GUIParts)
			System.out.println("Not supported to delete GUIParts.");
	}
	public final boolean hasDeleteClaim() {
		return isDeleted;
	}
	public void idle() {
		paintOrder = ++nowPaintOrder;
		paint();
	}
	@Override
	public void paint() {
	}
	public int getPaintOrder() {
		return paintOrder;
	}
	public boolean upperThen(GHQObject object) {
		return object == null || paintOrder > object.paintOrder;
	}
	public void tellNewFrameStart() {
		nowPaintOrder = -1;
	}
	public GHQObject uiAtCursur() {
		return this;
	}
	//information
	@Override
	public String name() {
		return name;
	}
	public boolean nameIs(String name) {
		return this.name == name;
	}
	public void setOuterContainer(GHQObject container) {
		outerContainer = container;
	}
	public GHQObject getOuterContainer() {
		return outerContainer;
	}
	public static int getTotalAmount() {
		return maxUniqueID + 1;
	}
	public static void initTotalAmount() {
		maxUniqueID = -1;
	}
	public GHQObjectHashMap getKindDataHashMap() {
		return new GHQObjectHashMap();
	}
	public GHQObject readKindDataHashMap(GHQObjectHashMap kindDataMap) {
		return this;
	}
	@Override
	public Physics physics() {
		return physics;
	}
}