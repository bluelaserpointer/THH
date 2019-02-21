package unit;

import java.io.Serializable;
import java.util.ArrayList;

import core.GHQ;

public class Status implements Serializable{
	private static final long serialVersionUID = 1194231397112685499L;
	private final int parameterDefaults[];
	private final int parameters[];
	public final ArrayList<Buff> skillEffects = new ArrayList<Buff>();
	public final ArrayList<Buff> buffs = new ArrayList<Buff>();

	//initialization
	public Status(int parameterAmount) {
		parameterDefaults = new int[parameterAmount];
		parameters = new int[parameterAmount];
	}
	public Status() {
		parameterDefaults = new int[0];
		parameters = new int[0];
	}
	public void reset() {
		buffs.clear();
		for(int i = 0;i < parameters.length;i++)
			parameters[i] = parameterDefaults[i];
		for(Buff buff : skillEffects)
			buff.reset(this);
	}
	
	//role
	/**
	 * Set a default value to a parameter.
	 * @since alpha1.0
	 */
	public void setDefault(int index,int defaultValue) {
		if(0 <= index && index < parameters.length)
			parameters[index]  = parameterDefaults[index] = defaultValue;
	}
	public int getDefault(int index) {
		if(0 <= index && index < parameters.length)
			return parameterDefaults[index];
		return GHQ.NONE;
	}
	public int get(int index) {
		if(0 <= index && index < parameters.length)
			return parameters[index];
		return GHQ.NONE;
	}
	public int set(int index,int value) {
		if(0 <= index && index < parameters.length) {
			final int OLD_VALUE = parameters[index];
			parameters[index] = value;
			return capCheckII(index) - OLD_VALUE;
		}
		return 0;
	}
	public int set0(int index) {
		if(0 <= index && index < parameters.length) {
			final int OLD_VALUE = parameters[index];
			parameters[index] = 0;
			return -OLD_VALUE;
		}
		return 0;
	}
	public int add(int index,int value) {
		if(0 <= index && index < parameters.length) {
			final int OLD_VALUE = parameters[index];
			parameters[index] += value;
			return capCheckII(index) - OLD_VALUE;
		}
		return 0;
	}
	public int pull(int index) {
		if(0 <= index && index < parameters.length && parameters[index] > 0) {
			final int OLD_VALUE = parameters[index];
			parameters[index]--;
			return capCheckII(index) - OLD_VALUE;
		}
		return 0;
	}
	public int minLimitedPull(int index,int value,int minLimit) {
		if(0 <= index && index < parameters.length && parameters[index] > minLimit) {
			final int OLD_VALUE = parameters[index];
			if(parameters[index] - value < minLimit)
				parameters[index] = minLimit;
			else
				parameters[index] -= value;
			return capCheckII(index) - OLD_VALUE;
		}
		return 0;
	}
	public int reduce_rate(int index,double rate) {
		if(0 <= index && index < parameters.length) {
			final int OLD_VALUE = parameters[index];
			parameters[index] *= 1.0 - rate;
			return capCheckII(index) - OLD_VALUE;
		}
		return 0;
	}
	public double getRate(int index) {
		if(0 <= index && index < parameters.length)
			return (double)parameters[index]/(double)parameterDefaults[index];
		System.out.println("Status.getRate is called illegally. index: " + index);
		return GHQ.NONE;
	}
	private final int capCheckII(int index) {
		if(parameters[index] < 0)
			parameters[index] = 0;
		capCheck(index);
		return parameters[index];
	}
	/**
	 * Check if a parameter overs the cap and fix it.
	 * This method is strongly recommended to be overloaded.
	 * @param index index of parameter
	 * @since alpha1.0
	 */
	public void capCheck(int index) {}
	
	//information
	public final int getSize() {
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
	public final int resetIfOverDefault(int index) {
		if(0 <= index && index < parameters.length) {
			final int EXCEED = parameters[index] - parameterDefaults[index];
			if(EXCEED > 0) {
				parameters[index] = parameterDefaults[index];
				return EXCEED;
			}
		}
		return 0;
	}
	public final int getDefaultExceed(int index) {
		if(0 <= index && index < parameters.length)
			return parameterDefaults[index] - parameters[index];
		return 0;
	}
}
