package calculate;

import java.io.Serializable;

public abstract class CalculateStream implements Serializable, HasNumber{
	private static final long serialVersionUID = -4939624713066550281L;
	private CalculateStream nextCalculation;
	
	public CalculateStream() {}
	
	public CalculateStream append(CalculateStream list) {
		return nextCalculation = list;
	}
	public boolean delete(CalculateStream list) {
		if(nextCalculation == list) {
			nextCalculation = nextCalculation.nextCalculation;
			return true;
		}else
			return nextCalculation.delete(list);
	}
	
	protected abstract Number calculate(Number value);
	public Number getResult(Number value) {
		if(nextCalculation == null)
			return calculate(value);
		else
			return nextCalculation.getResult(calculate(value));
	}
	
	@Override
	public Number getNumber() {
		return getResult(0);
	}
}
