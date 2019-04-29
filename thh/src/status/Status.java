package status;

import java.io.Serializable;
import java.util.Arrays;

import core.GHQ;

public class Status implements Serializable{
	private static final long serialVersionUID = -239346790787280009L;

	protected final int parameters[];

	public Status(int parameterAmount) {
		parameters = new int[parameterAmount];
	}
	public Status(Status sample) {
		parameters = Arrays.copyOf(sample.parameters, sample.parameters.length);
	}
	//control
	public Status clone() {
		return new Status(this);
	}
	public int get(int index) {
		if(isLegalIndex(index))
			return parameters[index];
		return GHQ.NONE;
	}
	public int set(int index,int value) {
		if(isLegalIndex(index)) {
			final int OLD_VALUE = parameters[index];
			parameters[index] = value;
			return (parameters[index] = capCheck(index)) - OLD_VALUE;
		}
		return 0;
	}
	public void setAll(int value) {
		Arrays.fill(parameters, value);
	}
	public int add(int index,int value) {
		if(isLegalIndex(index)) {
			final int OLD_VALUE = parameters[index];
			parameters[index] += value;
			return (parameters[index] = capCheck(index)) - OLD_VALUE;
		}
		return 0;
	}
	public int pull(int index) {
		if(isLegalIndex(index) && parameters[index] > 0) {
			final int OLD_VALUE = parameters[index];
			parameters[index]--;
			return (parameters[index] = capCheck(index)) - OLD_VALUE;
		}
		return 0;
	}
	public int minLimitedPull(int index,int value,int minLimit) {
		if(isLegalIndex(index) && parameters[index] > minLimit) {
			final int OLD_VALUE = parameters[index];
			if(parameters[index] - value < minLimit)
				parameters[index] = minLimit;
			else
				parameters[index] -= value;
			return (parameters[index] = capCheck(index)) - OLD_VALUE;
		}
		return 0;
	}
	public int reduce_rate(int index,double rate) {
		if(isLegalIndex(index)) {
			final int OLD_VALUE = parameters[index];
			parameters[index] *= 1.0 - rate;
			return (parameters[index] = capCheck(index)) - OLD_VALUE;
		}
		return 0;
	}

	//information
	public boolean isLegalIndex(int index) {
		return 0 <= index && index < parameters.length;
	}
	/**
	 * Check if a parameter overs the cap and fix it.
	 * @param index index of parameter
	 * @since alpha1.0
	 */
	public int capCheck(int index) {
		return parameters[index] >= 0 ? parameters[index] : 0;
	}
	public final int getAmount() {
		return parameters.length;
	}
	public final boolean isBigger0(int index) {
		if(0 <= index && index < parameters.length)
			return parameters[index] > 0;
		return false;
	}
	public final boolean isBigger(int index,int value) {
		if(0 <= index && index < parameters.length)
			return parameters[index] > value;
		System.out.println("Status.isBigger is called illegally. index: " + index);
		return false;
	}
	public final boolean isSmaller(int index,int value) {
		if(0 <= index && index < parameters.length)
			return parameters[index] < value;
		System.out.println("Status.isSmaller is called illegally. index: " + index);
		return false;
	}
	public final int resetIfOver(int index, int value) {
		if(0 <= index && index < parameters.length) {
			final int EXCEED = parameters[index] - value;
			if(EXCEED > 0) {
				parameters[index] = value;
				return EXCEED;
			}
		}
		return 0;
	}
	public final int getExceed(int index, int value) {
		if(0 <= index && index < parameters.length)
			return value - parameters[index];
		return 0;
	}
}
