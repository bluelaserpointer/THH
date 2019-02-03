package core;

public interface MessageSource {
	public default void eventNotice(int event) {}
}