package geom;

import java.awt.geom.Line2D;

public class Circle implements HitShape{
	public final int RADIUS;
	
	public Circle(int radius) {
		RADIUS = radius;
	}
	@Override
	public boolean intersects(int x1, int y1, HitShape shape, int x2, int y2) {
		if(shape instanceof Circle) {
			final double DX = x1 - x2,DY = y1 - y2,DR = RADIUS + ((Circle)shape).RADIUS;
			return DX*DX + DY*DY < DR*DR;
		}else if(shape instanceof Square) {
			// TODO lacking strictness
			final int S2 = (RADIUS + ((Square)shape).SIDE)/2;
			return Math.abs(x1 - x2) < S2 && Math.abs(y1 - y2) < S2;
		}
		return false;
	}

	@Override
	public boolean intersects(int x1, int y1, Line2D line) {
		return line.ptLineDistSq(x1,y1) <= RADIUS;
	}

}
