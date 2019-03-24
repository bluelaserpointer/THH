package physicis;

import static java.lang.Math.sqrt;

import core.GHQ;

public class Coordinate {
	protected double x,y;
	public Coordinate() {}
	public Coordinate(Coordinate cod) {
		x = cod.x;
		y = cod.y;
	}
	public Coordinate(double x,double y) {
		this.x = x;
		this.y = y;
	}
	
	//control
	public void setXY(double x,double y) {
		this.x = x;
		this.y = y;
	}
	public void setXY(CoordinateInteractable targetCI) {
		if(targetCI == null)
			return;
		final Coordinate targetCoordinate = targetCI.getCoordinate();
		this.x = targetCoordinate.x;
		this.y = targetCoordinate.y;
	}
	public void setX(double x) {
		this.x = x;
	}
	public void setY(double y) {
		this.y = y;
	}
	public void addX(double dx) {
		this.x += dx;
	}
	public void addY(double dy) {
		this.y += dy;
	}
	public void addXY(double dx,double dy) {
		this.x += dx;
		this.y += dy;
	}
	
	//information
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public boolean inStage() {
		return GHQ.inStage((int)x,(int)y);
	}
	public double getDistance(CoordinateInteractable targetCI) {
		return targetCI == null ? GHQ.NONE : getDistance(targetCI.getCoordinate());
	}
	public double getDistance(Coordinate coordinate) {
		return getDistance(coordinate.x,coordinate.y);
	}
	public double getDistance(double x,double y) {
		final double DX = x - this.x,DY = y - this.y;
		if(DX == 0.0) {
			if(DY == 0.0)
				return 0.0;
			else
				return DY;
		}else {
			if(DY == 0.0)
				return DX;
			else
				return sqrt(DX*DX + DY*DY);
		}
	}
	public double getDX(Coordinate coordinate) {
		return coordinate.x - x;
	}
	public double getDY(Coordinate coordinate) {
		return coordinate.y - y;
	}
}
