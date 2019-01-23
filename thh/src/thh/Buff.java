package thh;

public abstract class Buff {
	public String getName() {
		return "<Not named>";
	}
	public abstract void setBuff(VariableParameter vp);
	public abstract void removeBuff(VariableParameter vp);
}
