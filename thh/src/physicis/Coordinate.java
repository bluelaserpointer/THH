package physicis;

import static java.lang.Math.sqrt;

import java.io.Serializable;

import core.GHQ;

public class Coordinate implements Serializable{
	private static final long serialVersionUID = 2768080843202856362L;
	protected double x,y;
	public Coordinate() {}
	public Coordinate(Coordinate coordinate) {
		x = coordinate.x;
		y = coordinate.y;
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
	public void setXY(HasCoordinate hasCoordinate) {
		if(hasCoordinate == null)
			return;
		setXY(hasCoordinate.getCoordinate());
	}
	public void setXY(Coordinate coordinate) {
		this.x = coordinate.x;
		this.y = coordinate.y;
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
		return GHQ.inStage((int)x, (int)y);
	}
	public double getDistance(HasCoordinate targetCI) {
		return targetCI == null ? GHQ.NONE : getDistance(targetCI.getCoordinate());
	}
	public double getDistance(Coordinate coordinate) {
		return getDistance(coordinate.x, coordinate.y);
	}
	public double getDistance(double x, double y) {
		return sqrt(getDistancePow(x, y));
	}
	public double getDistancePow(double x, double y) {
		final double DX = x - this.x,DY = y - this.y;
		return DX*DX + DY*DY;
	}
	public double getDistancePow(Coordinate coordinate) {
		return getDistancePow(coordinate.x, coordinate.y);
	}
	public double getDistancePow(HasCoordinate targetCI) {
		return targetCI == null ? GHQ.NONE : getDistancePow(targetCI.getCoordinate());
	}
	public double getDX(Coordinate coordinate) {
		return coordinate.x - x;
	}
	public double getDY(Coordinate coordinate) {
		return coordinate.y - y;
	}
	public double getAbsDX(Coordinate coordinate) {
		final double DX = coordinate.x - x;
		return DX > 0 ? DX : -DX;
	}
	public double getAbsDY(Coordinate coordinate) {
		final double DY = coordinate.y - y;
		return DY > 0 ? DY : -DY;
	}
	public boolean isCloser_DX(Coordinate coordinate, double dx) {
		final double DX = coordinate.x - x;
		return DX < 0 ? (-dx < DX) : (DX < dx);
	}
	public boolean isCloser_DY(Coordinate coordinate, double dy) {
		final double DY = coordinate.y - y;
		return DY < 0 ? (-dy < DY) : (DY < dy);
	}
	public boolean isCloser_DXDY(Coordinate coordinate, double d) {
		final double DX = coordinate.x - x,DY = coordinate.y - y;
		return (DX < 0 ? (-d < DX) : (DX < d)) && (DY < 0 ? (-d < DY) : (DY < d));
	}
	public boolean isCloser_DXDY(Coordinate coordinate, double dx, double dy) {
		final double DX = coordinate.x - x,DY = coordinate.y - y;
		return (DX < 0 ? (-dx < DX) : (DX < dx)) && (DY < 0 ? (-dy < DY) : (DY < dy));
	}
}
