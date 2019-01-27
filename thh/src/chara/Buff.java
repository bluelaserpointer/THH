package chara;

public abstract class Buff {
	public String getName() {
		return "<Not named>";
	}
	public abstract void setBuff(CharaParameters vp);
	public abstract void removeBuff(CharaParameters vp);
}
