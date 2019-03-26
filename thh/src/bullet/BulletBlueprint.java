package bullet;

import core.GHQ;
import geom.HitShape;
import geom.Square;
import paint.DotPaint;
import physicis.Dynam;

/**
 * A class helping initialize Bullet parameters.
 * @author bluelaserpointer
 * @since alpha1.0
 */
public final class BulletBlueprint {
	private final static int
		NONE = GHQ.NONE,
		MAX = GHQ.MAX;
	public static final BulletScript DEFAULT_SCRIPT = new BulletScript();
	public static HitShape defaultHitShape = new Square(10);
	public static BulletScript script;
	public static HitShape hitshape;
	public static String name;
	public static int
		nowFrame,
		size,
		standpointGroup,
		atk,
		offSet,
		penetration,
		reflection,
		limitFrame,
		limitRange;
	public static final Dynam dynam = new Dynam();
	public static double accel;
	public static DotPaint
		paintScript;
	public static boolean
		isLaser;
	
	public static final void clear(BulletScript nextScript) {
		clear(nextScript,null);
	}
	public static final void clear(BulletScript nextScript, Dynam baseDynam) {
		name = GHQ.NOT_NAMED;
		script = nextScript;
		size = 2;
		standpointGroup = NONE;
		hitshape = defaultHitShape;
		atk = offSet = 0;
		penetration = reflection = 0;
		nowFrame = GHQ.getNowFrame();
		limitFrame = limitRange = MAX;
		if(baseDynam == null)
			dynam.clear();
		else
			dynam.initBySample(baseDynam);
		accel = 0.0;
		paintScript = DotPaint.BLANK_SCRIPT;
		isLaser = false;
	}
}