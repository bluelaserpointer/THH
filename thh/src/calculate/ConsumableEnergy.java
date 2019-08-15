package calculate;

import core.GHQ;

public class ConsumableEnergy implements Consumables, HasNumber{
	protected CalculateStream value, min, max, defaultValue;
	protected int lastDecreased;
	
	public ConsumableEnergy() {
		value = new FixedSetter(0);
	}
	public ConsumableEnergy(Number initialValue) {
		value = new FixedSetter(initialValue);
	}
	public ConsumableEnergy(CalculateStream formula) {
		value = formula;
	}
	public ConsumableEnergy(Number initialValue, Number min, Number max) {
		value = new FixedSetter(initialValue);
		this.defaultValue = this.min = new FixedSetter(min);
		this.max = new FixedSetter(max);
	}
	public ConsumableEnergy(Number initialValue, CalculateStream min, CalculateStream max) {
		value = new FixedSetter(initialValue);
		this.defaultValue = this.min = min;
		this.max = max;
	}
	public Number setWithCapCheck(CalculateStream newValue) {
		return setWithCapCheck_getResult(newValue.getNumber());
	}
	public Number setWithCapCheck_getResult(Number newValue) {
		final double OLD_VALUE = value.doubleValue();
		if(max != null && max.doubleValue() < newValue.doubleValue())
			value = max;
		else if(min != null && newValue.doubleValue() < min.doubleValue())
			value = min;
		else
			value = new FixedSetter(newValue);
		if(OLD_VALUE > value.doubleValue())
			lastDecreased = GHQ.nowFrame();
		return value.getNumber();
	}
	public Number setWithCapCheck_getDiff(CalculateStream newValue) {
		return setWithCapCheck_getDiff(newValue.getNumber());
	}
	public Number setWithCapCheck_getDiff(Number newValue) {
		final Number OLD_VALUE = value.getNumber();
		return setWithCapCheck_getResult(newValue).doubleValue() - OLD_VALUE.doubleValue();
	}
	public void doCapCheck() {
		setWithCapCheck(value);
	}
	public ConsumableEnergy setMin(Number min) {
		this.min = new FixedSetter(min);
		return this;
	}
	public ConsumableEnergy setMin(CalculateStream min) {
		this.min = min;
		return this;
	}
	public ConsumableEnergy setToMin() {
		if(!isMin())
			lastDecreased = GHQ.nowFrame();
		value = min;
		return this;
	}
	public Number getMin() {
		return min.getNumber();
	}
	public CalculateStream getMinFormula() {
		return min;
	}
	public ConsumableEnergy setMax(Number max) {
		this.max = new FixedSetter(max);
		return this;
	}
	public ConsumableEnergy setMax(CalculateStream max) {
		this.max = max;
		return this;
	}
	public ConsumableEnergy setToMax() {
		value = max;
		return this;
	}
	public Number getMax() {
		return max.getNumber();
	}
	public CalculateStream getMaxFormula() {
		return max;
	}
	public ConsumableEnergy setMinMax(Number min, Number max) {
		return setMin(min).setMax(max);
	}
	public ConsumableEnergy setDefault(Number defaultValue) {
		this.defaultValue = new FixedSetter(defaultValue);
		return this;
	}
	public ConsumableEnergy setDefault(CalculateStream defaultValue) {
		this.defaultValue = defaultValue;
		return this;
	}
	public ConsumableEnergy setDefaultToMin() {
		defaultValue = min;
		return this;
	}
	public ConsumableEnergy setDefaultToMax() {
		defaultValue = max;
		return this;
	}
	public Number getDefault() {
		return defaultValue != null ? defaultValue.getNumber() : null;
	}
	public ConsumableEnergy reset() {
		if(defaultValue != null)
			setWithCapCheck_getResult(defaultValue.getNumber());
		return this;
	}
	public double getRate() {
		if(max == null || min == null) {
			System.out.println("! --  getRate() called with null max or min");
			return 0.0;
		}
		return (double)(value.doubleValue() - min.doubleValue())/(max.doubleValue() - min.doubleValue());
	}
	public boolean isMin() {
		return value.doubleValue() == min.getNumber().doubleValue();
	}
	public boolean isMax() {
		return value.doubleValue() == max.getNumber().doubleValue();
	}
	public int lastDecreasedFrame() {
		return lastDecreased;
	}
	public int lastDecreasedPassedFrame() {
		return GHQ.passedFrame(lastDecreased);
	}
	public Number consume_getEffect(Number amount) {
		return setWithCapCheck_getDiff(value.doubleValue() - amount.doubleValue());
	}
	@Override
	public void consume(Number amount) {
		setWithCapCheck_getResult(value.doubleValue() - amount.doubleValue());
	}
	@Override
	public void setLestConsumable(Number newValue) {
		setWithCapCheck_getResult(newValue);
	}
	@Override
	public Number intLeftConsumable() {
		return value.getNumber();
	}
	@Override
	public Number getNumber() {
		return value.getNumber();
	}
	public Number getValue() {
		return getNumber();
	}
	public CalculateStream getValueFormula() {
		return value;
	}
}
