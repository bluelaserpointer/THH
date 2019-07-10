package structure;

import java.io.Serializable;

import core.GHQObject;
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
	
	protected final Point point = new Point.IntPoint();
	
	protected Standpoint standpoint = Standpoint.NULL_STANDPOINT;
	
	//information
	@Override
	public final Standpoint standpoint() {
		return Standpoint.NULL_STANDPOINT;
	}
	@Override
	public final Point point() {
		return point;
	}
	@Override
	public boolean isFriend(HasStandpoint target) {
		return Standpoint.NULL_STANDPOINT.isFriend(target.standpoint());
	}
}