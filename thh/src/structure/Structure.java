package structure;

import java.io.Serializable;

import core.GHQObject;
import hitShape.HitShape;
import physics.HasStandpoint;
import physics.HitInteractable;
import physics.Point;
import physics.Standpoint;

/**
 * A primal class for managing structure.
 * @author bluelaserpointer
 * @since alpha1.0
 */
public abstract class Structure extends GHQObject implements Serializable, HitInteractable{
	private static final long serialVersionUID = -641218813005671688L;
	
	protected final Point point;
	protected final HitShape hitShape;
	
	protected Standpoint standpoint;
	
	public Structure(HitShape hitShape, Standpoint standpoint) {
		this.hitShape = hitShape;
		this.point = hitShape.point();
		this.standpoint = standpoint;
	}
	
	//information
	@Override
	public final Standpoint standpoint() {
		return standpoint;
	}
	@Override
	public HitShape hitShape() {
		return hitShape;
	}
	@Override
	public boolean isFriend(HasStandpoint target) {
		return Standpoint.NULL_STANDPOINT.isFriend(target.standpoint());
	}
}