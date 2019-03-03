package structure;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.BitSet;

import core.GHQ;
import paint.RectPaint;

public class Tile extends Structure{
	private static final long serialVersionUID = -1364728656700080343L;
	public static final StructureScript<Tile> DEFAULT_SCRIPT = new StructureScript<Tile>();
	public StructureScript<Tile> script = DEFAULT_SCRIPT;
	protected final int
		ORIGIN_X,
		ORIGIN_Y,
		TILE_SIZE,
		X_TILES,
		Y_TILES;
	protected final BitSet aliveTiles;
	public static int bp_ox = GHQ.NONE,bp_oy,bp_tileSize = 100;
	
	public Tile(int ox,int oy,int x_tiles,int y_tiles) {
		ORIGIN_X = ox;
		ORIGIN_Y = oy;
		TILE_SIZE = 100;
		X_TILES = x_tiles;
		Y_TILES = y_tiles;
		aliveTiles = new BitSet(x_tiles*y_tiles);
		aliveTiles.set(0,aliveTiles.size());
	}

	public void setScript(StructureScript<Tile> script) {
		this.script = script;
	}
	public void setScriptIfBlank(StructureScript<Tile> script) {
		if(this.script != DEFAULT_SCRIPT)
			this.script = script;
	}
	@Override
	public boolean contains(int x,int y,int w,int h) {
		x -= ORIGIN_X;
		y -= ORIGIN_Y;
		if(x < 0 || X_TILES*TILE_SIZE < x || y < 0 || Y_TILES*TILE_SIZE < y)
			return false;
		final int x1 = x/TILE_SIZE,x2 = (x + w)/TILE_SIZE;
		final int y1 = y/TILE_SIZE,y2 = (y + w)/TILE_SIZE;
		for(int xi = x1;xi <= x2;xi++) {
			for(int yi = y1;yi <= y2;yi++) {
				if(aliveTiles.get(xi + yi*X_TILES))
					return true;
			}
		}
		return false;
	}
	@Override
	public boolean contains(int x,int y) {
		x -= ORIGIN_X;
		y -= ORIGIN_Y;
		return 0 < x && x < X_TILES*TILE_SIZE && 0 < y && y < Y_TILES*TILE_SIZE && aliveTiles.get(x/TILE_SIZE + y/TILE_SIZE*X_TILES);
	}
	@Override
	public boolean intersectsLine(Line2D line) {
		if(!line.intersects(ORIGIN_X,ORIGIN_Y,X_TILES*TILE_SIZE,X_TILES*TILE_SIZE))
			return false;
		for(int xi = 0;xi < X_TILES;xi++) {
			for(int yi = 0;yi < Y_TILES;yi++) {
				if(aliveTiles.get(xi + yi*X_TILES)){
					final int X = ORIGIN_X + xi*TILE_SIZE,Y = ORIGIN_Y + xi*TILE_SIZE;
					if(line.intersects(X,Y,TILE_SIZE,TILE_SIZE));
					return true;
				}
			}
		}
		return false;
	}
	@Override
	public Rectangle2D getBoundingBox() {
		return new Rectangle2D.Double(ORIGIN_X,ORIGIN_Y,X_TILES*TILE_SIZE,Y_TILES*TILE_SIZE);
	}
	//role
	@Override
	public final void defaultPaint() {
		draw(Color.WHITE,GHQ.stroke5);
		draw(Color.GRAY,GHQ.stroke3);
	}
	@Override
	public void paint(boolean doAnimation) {
		script.paint(this,doAnimation);
	}
	public void fill(Color color) {
		final Graphics2D G2 = GHQ.getGraphics2D();
		for(int xi = 0;xi < X_TILES;xi++) {
			for(int yi = 0;yi < Y_TILES;yi++) {
				if(aliveTiles.get(xi + yi*X_TILES)){
					final int PX = ORIGIN_X + xi*TILE_SIZE,PY = ORIGIN_Y + yi*TILE_SIZE;
					G2.setColor(color);
					G2.fillRect(PX,PY,TILE_SIZE,TILE_SIZE);
				}
			}
		}
	}
	public void draw(Color color,Stroke stroke) {
		final Graphics2D G2 = GHQ.getGraphics2D();
		for(int xi = 0;xi < X_TILES;xi++) {
			for(int yi = 0;yi < Y_TILES;yi++) {
				if(aliveTiles.get(xi + yi*X_TILES)){
					final int PX = ORIGIN_X + xi*TILE_SIZE,PY = ORIGIN_Y + yi*TILE_SIZE;
					G2.setColor(color);
					G2.setStroke(stroke);
					G2.fillRect(PX,PY,TILE_SIZE,TILE_SIZE);
				}
			}
		}
	}
	public void putPaint(RectPaint paintScript) {
		for(int xi = 0;xi < X_TILES;xi++) {
			for(int yi = 0;yi < Y_TILES;yi++) {
				if(aliveTiles.get(xi + yi*X_TILES)){
					final int PX = ORIGIN_X + xi*TILE_SIZE,PY = ORIGIN_Y + yi*TILE_SIZE;
					paintScript.paint(PX,PY,TILE_SIZE,TILE_SIZE);
				}
			}
		}
	}
	public static void blueprint_addOriginPoint(int x,int y) {
		bp_ox = x;
		bp_oy = y;
	}
	public static Tile blueprint_addEndPointAndFlush(int x,int y) {
		final int x_tiles = Math.abs((x - bp_ox)/bp_tileSize) + 1,y_tiles = Math.abs((y - bp_oy)/bp_tileSize) + 1;
		final int MOUSE_X = GHQ.getMouseX(),MOUSE_Y = GHQ.getMouseY();
		final Tile tile = new Tile(bp_ox < MOUSE_X ? bp_ox : bp_ox - x_tiles*bp_tileSize,bp_oy < MOUSE_Y ? bp_oy : bp_oy - y_tiles*bp_tileSize,x_tiles,y_tiles);
		blueprint_clear();
		return tile;
	}
	public static boolean blueprint_hasOriginPoint() {
		return bp_ox != GHQ.NONE;
	}
	public static void blueprint_clear() {
		bp_ox = GHQ.NONE;
		bp_tileSize = 100;
	}
	public static void makeGuiding(Graphics2D g2) {
		if(bp_ox != GHQ.NONE) {
			g2.setColor(Color.WHITE);
			g2.setStroke(GHQ.stroke1);
			final int MOUSE_X = GHQ.getMouseX(),MOUSE_Y = GHQ.getMouseY();
			final int x_tiles = Math.abs((MOUSE_X - bp_ox)/bp_tileSize) + 1,y_tiles = Math.abs((MOUSE_Y - bp_oy)/bp_tileSize) + 1;
			final int TX = bp_ox < MOUSE_X ? 0 : -x_tiles*bp_tileSize,TY = bp_oy < MOUSE_Y ? 0 : -y_tiles*bp_tileSize;
			g2.translate(TX,TY);
			for(int xi = 0;xi < x_tiles;xi++) {
				for(int yi = 0;yi < y_tiles;yi++)
					g2.drawRect(bp_ox + xi*bp_tileSize, bp_oy + yi*bp_tileSize, bp_tileSize, bp_tileSize);
			}
			g2.translate(-TX,-TY);
			g2.setColor(Color.ORANGE);
			g2.setStroke(GHQ.stroke3);
			g2.drawLine(bp_ox, bp_oy, MOUSE_X, MOUSE_Y);
		}
	}
}
