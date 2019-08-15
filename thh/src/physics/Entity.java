package physics;

import core.GHQ;
import core.GHQObject;
import hitShape.HitShape;

public abstract class Entity extends GHQObject implements HitInteractable{
	public final int INITIAL_FRAME;
	public HitShape hitShape;
	public Standpoint standpoint;
	
	public Entity(HitShape hitShape, Standpoint standpoint) {
		INITIAL_FRAME = GHQ.nowFrame();
		this.hitShape = hitShape;
		this.standpoint = standpoint;
	}
	public Entity(HitShape hitShape, Standpoint standpoint, int nowFrame) {
		INITIAL_FRAME = nowFrame;
		this.hitShape = hitShape;
		this.standpoint = standpoint;
	}
	//information
	@Override
	public HitShape hitShape() {
		return hitShape;
	}
	@Override
	public Standpoint standpoint() {
		return standpoint;
	}
}