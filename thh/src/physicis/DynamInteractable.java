package physicis;

public interface DynamInteractable {
	public static final DynamInteractable BlankDI = new DynamInteractable() {
		private final Dynam nullDynam = new Dynam();
		@Override
		public Dynam getDynam() {
			return nullDynam;
		}
	};
	
	public abstract Dynam getDynam();
}