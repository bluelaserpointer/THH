package core;

public interface HasStandpoint {
	public abstract Standpoint getStandpoint();
	public default boolean isFriend(HasStandpoint target) {
		return getStandpoint().isFriend(target.getStandpoint());
	}
}
