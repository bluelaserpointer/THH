package core;

import java.io.Serializable;

public class Standpoint implements Serializable{
	private static final long serialVersionUID = 4039529801782707123L;

	public static Standpoint NULL_STANDPOINT = new Standpoint(GHQ.NONE);
	
	private int group;
	public final int INITIAL_GROUP;
	
	public Standpoint(int initialGroup) {
		INITIAL_GROUP = group = initialGroup;
	}
	public boolean isFriendly(Standpoint standpoint) {
		return group == standpoint.group && group != GHQ.NONE || group == GHQ.ALL || standpoint.group == GHQ.ALL;
	}
	public void set(int group) {
		this.group = group;
	}
	public int get() {
		return group;
	}
}
