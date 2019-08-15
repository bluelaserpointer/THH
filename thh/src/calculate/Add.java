package calculate;

public class Add extends CalculateStream{
	private static final long serialVersionUID = -7020264920780470221L;
	private final Number ADD;
	public Add(Number add) {
		ADD = add;
	}
	@Override
	protected Number calculate(Number value) {
		return value.intValue() + ADD.intValue();
	}
}