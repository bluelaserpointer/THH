package physics;

public interface HasHitGroup {
	public abstract HitGroup hitGroup();
	public default boolean hitableGroup(HitGroup hitGroup) {
		return hitGroup().hitableGroup(hitGroup);
	}
	public default boolean hitableGroup(HasHitGroup target) {
		return hitableGroup(target.hitGroup());
	}
}
