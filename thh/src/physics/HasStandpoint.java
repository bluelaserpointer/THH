package physics;

public interface HasStandpoint {
	public abstract Standpoint standpoint();
	public default boolean isFriend(Standpoint standpoint) {
		return standpoint().isFriend(standpoint);
	}
	public default boolean isFriend(HasStandpoint target) {
		return isFriend(target.standpoint());
	}
}
