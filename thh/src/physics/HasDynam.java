package physics;

/**
 * Subclasses of this has a accessible {@link Dynam} instance.
 * Mainly this interface is implemented by moving stage objects.
 * @author bluelaserpointer
 * @since alpha1.0
 */
public interface HasDynam extends HasPoint{
	public static final HasDynam NULL_DYNAM_SOURCE = new HasDynam() {
		private final Dynam nullDynam = new Dynam();
		@Override
		public Dynam dynam() {
			return nullDynam;
		}
		@Override
		public Point point() {
			return nullDynam;
		}
	};

	/**
	 * Return the Dynam instance of this object.
	 * @return {@link Dynam}
	 */
	public abstract Dynam dynam();
	public default Point point() {
		return dynam();
	}
}