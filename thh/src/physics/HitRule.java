package physics;

import java.io.Serializable;

/**
 * A class for managing objects' friendly-or-hostile relationship.
 * @author bluelaserpointer
 * @since alpha1.0
 */
public class HitRule implements Serializable {
	private static final long serialVersionUID = 4039529801782707123L;

	public static final int ALL = -1, NONE = -2;
	public static final HitRule HIT_ALL = new HitRule(ALL);
	public static final HitRule HIT_NONE = new HitRule(NONE);
	
	private int group;
	
	public HitRule() {
		group = ALL;
	}
	public HitRule(int initialGroup) {
		group = initialGroup;
	}
	public boolean hitableGroup(HitRule hitGroup) {
		return group != NONE && hitGroup.group != NONE && (group == ALL || hitGroup.group == ALL || group != hitGroup.group);
	}
	public void set(int group) {
		this.group = group;
	}
	public int get() {
		return group;
	}
}
