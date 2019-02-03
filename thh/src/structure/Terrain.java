package structure;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.util.Arrays;

import core.ErrorCounter;
import core.GHQ;

public class Terrain extends Structure{
	protected Polygon polygon;
	private int px[],py[];
	
	public Terrain() {
		polygon = new Polygon();
	}
	public Terrain(Polygon polygon) {
		this.polygon = polygon;
	}
	public Terrain(int[] pointX,int[] pointY) {
		if(pointX.length < pointY.length) {
			ErrorCounter.put("Terrain's constructer called Illegally: pointX.length = " + pointX.length + ", pointY.length = " + pointY.length);
			polygon = new Polygon(pointX,pointY,pointX.length);
			px = pointX;py = Arrays.copyOf(pointY, pointX.length);
		}else if(pointX.length > pointY.length){
			ErrorCounter.put("Terrain's constructer called Illegally: pointX.length = " + pointX.length + ", pointY.length = " + pointY.length);
			polygon = new Polygon(pointX,pointY,pointY.length);
			px = Arrays.copyOf(pointX, pointY.length);py = pointY;
		}else {
			polygon = new Polygon(pointX,pointY,pointX.length);
			px = pointX;py = pointY;
		}
	}
	
	//role
	@Override
	public void doFill(Graphics2D g2) {
		g2.fill(polygon);
	}
	@Override
	public void doDraw(Graphics2D g2) {
		g2.draw(polygon);
	}
	
	//information
	@Override
	public boolean contains(int x,int y,int w,int h) {
		return polygon.intersects(x, y, w, h);
	}
	@Override
	public boolean intersectsLine(Line2D line) {
		for(int i = 0;i < px.length - 1;i++) {
			if(line.intersectsLine(px[i], py[i], px[i + 1], py[i + 1]))
				return true;
		}
		if(line.intersectsLine(px[px.length - 1], py[px.length - 1], px[0], py[0]))
			return true;
		return false;
		
	}
	@Override
	public int getTeam() {
		return GHQ.NONE;
	}
}
