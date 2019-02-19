package structure;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;

import core.ErrorCounter;
import core.GHQ;

public class Terrain extends Structure{
	private static final long serialVersionUID = 7167638140442836310L;
	public static final StructureScript<Terrain> DEFAULT_SCRIPT = new StructureScript<Terrain>();
	public StructureScript<Terrain> script = DEFAULT_SCRIPT;
	protected Polygon polygon;
	private int px[],py[];
	public static ArrayList<Integer> bppx = new ArrayList<Integer>(),bppy = new ArrayList<Integer>();
	
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

	public void setScript(StructureScript<Terrain> script) {
		this.script = script;
	}
	public void setScriptIfBlank(StructureScript<Terrain> script) {
		if(this.script != DEFAULT_SCRIPT)
			this.script = script;
	}
	//role
	@Override
	public final void defaultPaint() {
		fill(Color.GRAY);
	}
	@Override
	public void paint(boolean doAnimation) {
		script.paint(this,doAnimation);
	}
	public void fill(Color color) {
		final Graphics2D G2 = GHQ.getGraphics2D();
		G2.setColor(color);
		G2.fill(polygon);
	}
	public void draw(Color color,Stroke stroke) {
		final Graphics2D G2 = GHQ.getGraphics2D();
		G2.setColor(color);
		G2.setStroke(stroke);
		G2.draw(polygon);
	}
	public static final void makeGuiding(Graphics2D g2) {
		if(bppx.size() == 0)
			return;
		//lines
		g2.setColor(Color.WHITE);
		g2.setStroke(GHQ.stroke1);
		for(int i = 0;i < bppx.size() - 1;i++)
			g2.drawLine(bppx.get(i), bppy.get(i), bppx.get(i + 1), bppy.get(i + 1));
		//dots
		g2.setColor(Color.ORANGE);
		g2.setStroke(GHQ.stroke3);
		for(int i = 0;i < bppx.size();i++)
			g2.drawRect(bppx.get(i) - 4, bppy.get(i) - 4, 8, 8);
	}
	//information
	@Override
	public boolean contains(int x,int y,int w,int h) {
		return polygon.intersects(x, y, w, h);
	}
	@Override
	public boolean contains(int x,int y) {
		return polygon.contains(x, y);
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
	@Override
	public Rectangle2D getBoundingBox() {
		return polygon.getBounds2D();
	}
	
	//create-with-blueprint
	public static boolean blueprint_isOriginPoint(int x,int y) {
		return bppx.size() >= 3 && x == bppx.get(0) && y == bppy.get(0);
	}
	public static void blueprint_addPoint(int x,int y) {
		bppx.add(x);
		bppy.add(y);
	}
	public static void blueprint_clear() {
		bppx.clear();
		bppy.clear();
	}
	public static Terrain blueprint_flush() {
		final int[] rx = new int[bppx.size()],ry = new int[rx.length];
		for(int i = 0;i < rx.length;i++) {
			rx[i] = bppx.get(i);
			ry[i] = bppy.get(i);
		}
		bppx.clear();
		bppy.clear();
		return new Terrain(rx,ry);
	}
}
