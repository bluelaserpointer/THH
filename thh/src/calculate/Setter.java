package calculate;

public abstract class Setter extends CalculateStream{
	private static final long serialVersionUID = -7228107695838692555L;

	protected abstract Number set();
	
	@Override
	protected final Number calculate(Number value) {
		return set();
	}
}
