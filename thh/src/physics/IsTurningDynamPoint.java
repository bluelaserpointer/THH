package physics;

public interface IsTurningDynamPoint extends HasDynam, IsTurningPoint{
	@Override
	public default Coordinate getCoordinate() {
		return getDynam();
	}
}
