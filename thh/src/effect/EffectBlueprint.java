package effect;

import core.Dynam;
import core.GHQ;
import paint.DotPaint;

public class EffectBlueprint {
	private final static int 
		NONE = GHQ.NONE,
		MAX = GHQ.MAX;
	public static final EffectScript DEFAULT_SCRIPT = new EffectScript();
	public static EffectScript script;
	public static String name;
	public static int
		nowFrame,
		size,
		limitFrame,
		limitRange;
	public static final Dynam dynam = new Dynam();
	public static double accel;
	public static DotPaint
		paintScript;
	
	public static final void clear(EffectScript nextScript) {
		name = GHQ.NOT_NAMED;
		script = nextScript;
		size = NONE;
		nowFrame = GHQ.getNowFrame();
		limitFrame = MAX;
		limitRange = MAX;
		dynam.clear();
		accel = 0.0;
		paintScript = DotPaint.BLANK_SCRIPT;
	}
	public static final void clear(EffectScript nextScript, Dynam baseDynam) {
		name = GHQ.NOT_NAMED;
		script = nextScript;
		size = NONE;
		nowFrame = GHQ.getNowFrame();
		limitFrame = MAX;
		limitRange = MAX;
		dynam.initBySample(baseDynam);
		accel = 0.0;
		paintScript = DotPaint.BLANK_SCRIPT;
	}
}