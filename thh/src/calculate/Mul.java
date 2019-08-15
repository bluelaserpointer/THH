package calculate;

public class Mul extends CalculateStream{
	private static final long serialVersionUID = -4816841917364698371L;
	private final double MUL;
	public Mul(double mul) {
		MUL = mul;
	}
	@Override
	protected Number calculate(Number value) {
		return value.doubleValue()*MUL;
	}
}