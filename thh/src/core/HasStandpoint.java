package core;

public interface HasStandpoint {
	public abstract Standpoint getStandpoint();
	public default boolean isFriendly(HasStandpoint target) {
		return getStandpoint().isFriendly(target.getStandpoint());
	}
	public default boolean isEnemy(HasStandpoint target) {
		return !isFriendly(target);
	}
}
