package core;

public interface DynamInteractable {
	public static final DynamInteractable BlankDI = new DynamInteractable() {
		private final Dynam nullDynam = new Dynam();
		@Override
		public Dynam getDynam() {
			return nullDynam;
		}
		@Override
		public boolean isMovable() {
			return false;
		}
	};
	
	public abstract Dynam getDynam();
	
	public abstract boolean isMovable();
}