package physics;

public interface HasHitGroup {
	public abstract HitRule hitGroup();
	public default boolean hitableGroup(HitRule hitGroup) {
		return hitGroup().hitableGroup(hitGroup);
	}
	public default boolean hitableGroup(HasHitGroup target) {
		return hitableGroup(target.hitGroup());
	}
}
