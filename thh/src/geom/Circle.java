package geom;

import java.awt.geom.Line2D;

public class Circle extends HitShape{
	public final int RADIUS;
	
	public Circle(int radius) {
		RADIUS = radius;
	}
	@Override
	public boolean intersects(int x1, int y1, HitShape shape, int x2, int y2) {
		if(shape instanceof Circle) {
			final double DX = x1 - x2,DY = y1 - y2,DR = RADIUS + ((Circle)shape).RADIUS;
			return DX*DX + DY*DY < DR*DR;
		}
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean intersects(int x1, int y1, Line2D line) {
		// TODO Auto-generated method stub
		return false;
	}

}
