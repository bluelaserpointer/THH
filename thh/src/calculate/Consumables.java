package calculate;

public interface Consumables {
	public void consume(Number amount);
	
	public void setLestConsumable(Number amount);
	
	public Number intLeftConsumable();

	public default void consumeRate(double rate) {
		consume(intLeftConsumable().doubleValue()*rate);
	}
}
