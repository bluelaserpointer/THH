package geom;

import java.awt.geom.Line2D;

import core.Dynam;
import core.DynamInteractable;

public abstract class HitShape {
	public static final int RECTANGLE = 0,OVAL = 1;
	
	public abstract boolean intersects(int x1, int y1, HitShape shape, int x2, int y2);
	public abstract boolean intersects(int x1, int y1, Line2D line);
	

	public final boolean intersects(DynamInteractable sourceDI, HitShape shape, DynamInteractable targetDI) {
		final Dynam D1 = sourceDI.getDynam(),D2 = targetDI.getDynam();
		return intersects((int)D1.getX(),(int)D1.getY(),shape,(int)D2.getX(),(int)D2.getY());
	}
	public final boolean intersects(DynamInteractable sourceDI, Line2D line) {
		final Dynam D1 = sourceDI.getDynam();
		return intersects((int)D1.getX(),(int)D1.getY(),line);
	}
	
}
