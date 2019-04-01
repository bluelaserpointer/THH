package core;

import java.io.Serializable;

/**
 * A class for managing objects' friendly-or-hostile relationship.
 * @author bluelaserpointer
 * @since alpha1.0
 */
public class Standpoint implements Serializable{
	private static final long serialVersionUID = 4039529801782707123L;

	public static final Standpoint NULL_STANDPOINT = new Standpoint(GHQ.NONE);
	public static final int ALL = GHQ.NONE + 1;
	
	private int group;
	public final int INITIAL_GROUP;
	
	public Standpoint() {
		INITIAL_GROUP = GHQ.NONE;
	}
	public Standpoint(int initialGroup) {
		INITIAL_GROUP = group = initialGroup;
	}
	public boolean isFriend(Standpoint standpoint) {
		return group == standpoint.group && group != GHQ.NONE || group == ALL || standpoint.group == ALL;
	}
	public void set(int group) {
		this.group = group;
	}
	public int get() {
		return group;
	}
}
