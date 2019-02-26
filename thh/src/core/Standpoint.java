package core;

public class Standpoint {
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
