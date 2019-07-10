package physics;

public interface HasAngleDynam extends HasDynam, HasAnglePoint{
	@Override
	public default Point point() {
		return dynam();
	}
}
