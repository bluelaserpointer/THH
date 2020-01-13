package core;


public interface T_Verifier<T> {
	abstract T objectToT(Object object);
	default boolean objectToTAccepts(Object object) {
		return objectToT(object) != null;
	}
}
