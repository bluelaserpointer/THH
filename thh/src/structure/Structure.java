package structure;

import java.io.Serializable;

import core.GHQObject;
import physics.HitInteractable;

/**
 * A primal class for managing structure.
 * @author bluelaserpointer
 * @since alpha1.0
 */
public abstract class Structure extends GHQObject implements Serializable, HitInteractable{
	private static final long serialVersionUID = -641218813005671688L;
}