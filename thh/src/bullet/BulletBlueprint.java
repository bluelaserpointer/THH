package bullet;

import core.Dynam;
import core.GHQ;

public final class BulletBlueprint {
	private final static int
		NONE = GHQ.NONE,
		MAX = GHQ.MAX;
	public static final BulletScript DEFAULT_SCRIPT = new BulletScript();
	public static BulletScript script;
	public static String name;
	public static int
		nowFrame,
		size,
		team,
		atk,
		offSet,
		penetration,
		reflection,
		limitFrame,
		limitRange;
	public static final Dynam dynam = new Dynam();
	public static double accel;
	public static int
		imageID;
	public static boolean
		hitEnemy,
		isLaser;
	
	public static final void clear(BulletScript nextScript) {
		name = GHQ.NOT_NAMED;
		script = nextScript;
		size = 2;
		team = NONE;
		atk = offSet = 0;
		penetration = reflection = 0;
		nowFrame = GHQ.getNowFrame();
		limitFrame = limitRange = MAX;
		dynam.clear();
		accel = 0.0;
		imageID = NONE;
		hitEnemy = true;
		isLaser = false;
	}
	public static final void clear(BulletScript nextScript, Dynam baseDynam) {
		name = GHQ.NOT_NAMED;
		script = nextScript;
		size = 2;
		team = NONE;
		atk = offSet = 0;
		penetration = reflection = 0;
		nowFrame = GHQ.getNowFrame();
		limitFrame = limitRange = MAX;
		dynam.initBySample(baseDynam);
		accel = 0.0;
		imageID = NONE;
		hitEnemy = true;
		isLaser = false;
	}
}