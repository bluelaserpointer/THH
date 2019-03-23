package physicis;

/**
 * Subclasses of this has a accessible {@link Dynam} instance.
 * Mainly this interface is implemented by moving stage objects.
 * @author bluelaserpointer
 * @since alpha1.0
 */
public interface DynamInteractable {
	public static final DynamInteractable BlankDI = new DynamInteractable() {
		private final Dynam nullDynam = new Dynam();
		@Override
		public Dynam getDynam() {
			return nullDynam;
		}
	};

	/**
	 * Return the Dynam instance of this object.
	 * @return {@link Dynam}
	 */
	public abstract Dynam getDynam();
}