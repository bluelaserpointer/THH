package status;

import java.util.ArrayList;
import java.util.Arrays;

import buff.Buff;
import core.GHQ;

public class StatusWithDefaultValue extends Status{
	private static final long serialVersionUID = 1194231397112685499L;
	protected final int parameterDefaults[];
	public final ArrayList<Buff> skillEffects = new ArrayList<Buff>();
	public final ArrayList<Buff> buffs = new ArrayList<Buff>();
	
	public static final int UNCHANGE = -1;

	//initialization
	public StatusWithDefaultValue(StatusWithDefaultValue sample) {
		super(sample);
		parameterDefaults = Arrays.copyOf(sample.parameterDefaults, sample.parameterDefaults.length);
	}
	public StatusWithDefaultValue(int parameterAmount) {
		super(parameterAmount);
		parameterDefaults = new int[parameterAmount];
	}
	public void reset() {
		buffs.clear();
		for(int i = 0;i < parameters.length;i++)
			parameters[i] = getDefault(i);
		for(Buff buff : skillEffects)
			buff.reset();
	}
	
	//role
	/**
	 * Set a default value to a parameter.
	 * @since alpha1.0
	 */
	public void setDefault(int index,int defaultValue) {
		if(isLegalIndex(index))
			parameters[index]  = parameterDefaults[index] = defaultValue;
	}
	public int getDefault(int index) {
		if(isLegalIndex(index))
			return parameterDefaults[index];
		return GHQ.NONE;
	}
	public double getRate(int index) {
		if(isLegalIndex(index))
			return (double)parameters[index]/(double)getDefault(index);
		System.out.println("Status.getRate is called illegally. index: " + index);
		return GHQ.NONE;
	}
	
	//control
	public StatusWithDefaultValue clone() {
		return new StatusWithDefaultValue(this);
	}
	
	//information
	public final int resetIfOverDefault(int index) {
		if(0 <= index && index < parameters.length) {
			final int EXCEED = parameters[index] - getDefault(index);
			if(EXCEED > 0) {
				parameters[index] = getDefault(index);
				return EXCEED;
			}
		}
		return 0;
	}
	public final int getDefaultExceed(int index) {
		if(0 <= index && index < parameters.length)
			return getDefault(index) - parameters[index];
		return 0;
	}
}
