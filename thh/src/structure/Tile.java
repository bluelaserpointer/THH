package structure;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.BitSet;

import core.GHQ;
import hitShape.HitShape;
import paint.rect.RectPaint;
import physics.Point;
import physics.Standpoint;

public class Tile extends Structure{
	private static final long serialVersionUID = -1364728656700080343L;
	public static int bp_ox = GHQ.NONE, bp_oy,bp_tileSize = 100;
	
	public static class TileHitShape extends HitShape{
		protected static final int TILE_SIZE = 100;
		protected final int
			ORIGIN_X,
			ORIGIN_Y,
			X_TILES,
			Y_TILES;
		protected final BitSet aliveTiles;
		public TileHitShape(int ox, int oy, int x_tiles, int y_tiles) {
			super(new Point.IntPoint(ox, oy));
			ORIGIN_X = ox;
			ORIGIN_Y = oy;
			X_TILES = x_tiles;
			Y_TILES = y_tiles;
			aliveTiles = new BitSet(X_TILES*Y_TILES);
			aliveTiles.set(0, aliveTiles.size());
		}
		private static final long serialVersionUID = -8708171125934571442L;
		@Override
		public boolean intersects(HitShape shape) {
			//TODO
			return intersectsDot(shape.point().intX(), shape.point().intY());
		}
		@Override
		public boolean intersectsDot(int x, int y) {
			x -= myPoint.intX();
			y -= myPoint.intY();
			return 0 < x && x < width() && 0 < y && y < height() && aliveTiles.get(x/TILE_SIZE + y/TILE_SIZE*X_TILES);
		}
		@Override
		public boolean intersectsLine(int x1, int y1, int x2, int y2) {
			final Line2D line = new Line2D.Double(x1, y1, x2, y2);
			if(!line.intersects(ORIGIN_X, ORIGIN_Y, X_TILES*TILE_SIZE, Y_TILES*TILE_SIZE))
				return false;
			for(int xi = 0;xi < X_TILES;xi++) {
				for(int yi = 0;yi < Y_TILES;yi++) {
					if(aliveTiles.get(xi + yi*X_TILES)){
						if(line.intersects(ORIGIN_X + xi*TILE_SIZE, ORIGIN_Y + yi*TILE_SIZE, TILE_SIZE, TILE_SIZE))
							return true;
					}
				}
			}
			return false;
		}
		@Override
		public void fill(Point point, Color color) {
			final Graphics2D G2 = GHQ.getGraphics2D();
			for(int xi = 0;xi < X_TILES;xi++) {
				for(int yi = 0;yi < Y_TILES;yi++) {
					if(aliveTiles.get(xi + yi*X_TILES)){
						final int PX = ORIGIN_X + xi*TILE_SIZE, PY = ORIGIN_Y + yi*TILE_SIZE;
						G2.setColor(color);
						G2.fillRect(PX, PY, TILE_SIZE, TILE_SIZE);
					}
				}
			}
		}
		@Override
		public void draw(Point point, Color color, Stroke stroke) {
			final Graphics2D G2 = GHQ.getGraphics2D();
			for(int xi = 0;xi < X_TILES;xi++) {
				for(int yi = 0;yi < Y_TILES;yi++) {
					if(aliveTiles.get(xi + yi*X_TILES)){
						final int PX = ORIGIN_X + xi*TILE_SIZE, PY = ORIGIN_Y + yi*TILE_SIZE;
						G2.setColor(color);
						G2.setStroke(stroke);
						G2.drawRect(PX, PY, TILE_SIZE, TILE_SIZE);
					}
				}
			}
		}
		@Override
		public HitShape clone(Point newPoint) {
			return new TileHitShape(newPoint.intX(), newPoint.intY(), X_TILES, Y_TILES);
		}
		public void putPaint(RectPaint paintScript) {
			for(int xi = 0;xi < X_TILES;xi++) {
				for(int yi = 0;yi < Y_TILES;yi++) {
					if(aliveTiles.get(xi + yi*X_TILES)){
						final int PX = ORIGIN_X + xi*TILE_SIZE, PY = ORIGIN_Y + yi*TILE_SIZE;
						paintScript.rectPaint(PX, PY, TILE_SIZE, TILE_SIZE);
					}
				}
			}
		}
		@Override
		public int width() {
			return X_TILES*TILE_SIZE;
		}
		@Override
		public int height() {
			return Y_TILES*TILE_SIZE;
		}
	}
	public Tile(int ox, int oy, int x_tiles, int y_tiles) {
		super(new TileHitShape(ox, oy, x_tiles, y_tiles), new Standpoint(GHQ.NONE));
	}
	public static Tile create(Rectangle2D rect) {
		return new Tile((int)rect.getX(), (int)rect.getY(), (int)rect.getWidth()/100, (int)rect.getHeight()/100);
	}
	//role
	@Override
	public void paint(boolean doAnimation) {
		hitShape.fill(point, Color.WHITE);
		hitShape.draw(point, Color.LIGHT_GRAY, GHQ.stroke3);
	}
	
	//information
	public int xTiles() {
		return ((TileHitShape)hitShape).X_TILES;
	}
	public int yTiles() {
		return ((TileHitShape)hitShape).Y_TILES;
	}
	@Override
	public String getName() {
		return "DefaultTile";
	}
	
	public static void blueprint_addOriginPoint(int x, int y) {
		bp_ox = x;
		bp_oy = y;
	}
	public static Tile blueprint_addEndPointAndFlush(int x, int y) {
		final int x_tiles = Math.abs((x - bp_ox)/bp_tileSize) + 1, y_tiles = Math.abs((y - bp_oy)/bp_tileSize) + 1;
		final Tile tile = new Tile(bp_ox < GHQ.mouseX() ? bp_ox : bp_ox - x_tiles*bp_tileSize, bp_oy < GHQ.mouseY() ? bp_oy : bp_oy - y_tiles*bp_tileSize, x_tiles, y_tiles);
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
			final int MOUSE_X = GHQ.mouseX(), MOUSE_Y = GHQ.mouseY();
			final int x_tiles = Math.abs((MOUSE_X - bp_ox)/bp_tileSize) + 1, y_tiles = Math.abs((MOUSE_Y - bp_oy)/bp_tileSize) + 1;
			final int TX = bp_ox < MOUSE_X ? 0 : -x_tiles*bp_tileSize, TY = bp_oy < MOUSE_Y ? 0 : -y_tiles*bp_tileSize;
			g2.translate(TX, TY);
			for(int xi = 0;xi < x_tiles;xi++) {
				for(int yi = 0;yi < y_tiles;yi++)
					g2.drawRect(bp_ox + xi*bp_tileSize, bp_oy + yi*bp_tileSize, bp_tileSize, bp_tileSize);
			}
			g2.translate(-TX, -TY);
			g2.setColor(Color.ORANGE);
			g2.setStroke(GHQ.stroke3);
			g2.drawLine(bp_ox, bp_oy, MOUSE_X, MOUSE_Y);
		}
	}
}
