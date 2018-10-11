package structure;

import java.awt.Polygon;

import thh.ErrorCounter;

public abstract class Terrain extends Structure{
	protected Polygon polygon;
	
	public Terrain() {
		polygon = new Polygon();
	}
	public Terrain(Polygon polygon) {
		this.polygon = polygon;
	}
	public Terrain(int[] pointX,int[] pointY) {
		if(pointX.length != pointY.length)
			ErrorCounter.put("Terrainの不正使用：pointX.length = " + pointX.length + ", pointY.length = " + pointY.length);
		if(pointX.length < pointY.length)
			polygon = new Polygon(pointX,pointY,pointX.length);
		else
			polygon = new Polygon(pointX,pointY,pointY.length);
	}
	@Override
	public boolean hitLandscape(int x,int y,int w,int h) {
		return polygon.intersects(x, y, w, h);
	}
}
