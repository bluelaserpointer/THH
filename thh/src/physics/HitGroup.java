package physics;

import java.io.Serializable;

import core.GHQ;
import loading.ObjectSavable;
import loading.ObjectSaveTree;

/**
 * A class for managing objects' friendly-or-hostile relationship.
 * @author bluelaserpointer
 * @since alpha1.0
 */
public class HitGroup implements Serializable, ObjectSavable{
	private static final long serialVersionUID = 4039529801782707123L;

	public static final int ALL = GHQ.NONE + 1;
	public static final HitGroup HIT_ALL = new HitGroup(HitGroup.ALL);
	public static final HitGroup HIT_NONE = new HitGroup(GHQ.NONE);
	
	private int group;
	
	public HitGroup() {
		group = GHQ.NONE;
	}
	public HitGroup(int initialGroup) {
		group = initialGroup;
	}
	public HitGroup(ObjectSaveTree tree) {
		group = tree.pollInt();
	}
	@Override
	public ObjectSaveTree save() {
		return new ObjectSaveTree(0, group);
	}
	public boolean hitableGroup(HitGroup hitGroup) {
		return group != GHQ.NONE && hitGroup.group != GHQ.NONE && (group == ALL || hitGroup.group == ALL || group != hitGroup.group);
	}
	public void set(int group) {
		this.group = group;
	}
	public int get() {
		return group;
	}
}
