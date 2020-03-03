package stage;

import java.awt.Graphics2D;

import core.GHQ;
import physics.HasBoundingBox;
import physics.Point;

public class GridPainter {
	public final int W_GRIDS, H_GRIDS;
	public final int GRID_SIZE;
	
	public GridPainter(HasBoundingBox stage, int gridSize) {
		GRID_SIZE = gridSize;
		W_GRIDS = stage.width()/GRID_SIZE + 1;
		H_GRIDS = stage.height()/GRID_SIZE + 1;
	}
	//control
	public void drawGrid(Graphics2D g2, int xPos, int yPos) {
		g2.drawRect(xPos*GRID_SIZE, yPos*GRID_SIZE, GRID_SIZE, GRID_SIZE);
	}
	public void fillGrid(Graphics2D g2, int xPos, int yPos) {
		g2.fillRect(xPos*GRID_SIZE, yPos*GRID_SIZE, GRID_SIZE, GRID_SIZE);
	}
	//information
	public Point getPosPoint(int xPos, int yPos) {
		return new Point.DoublePoint(xPos*GRID_SIZE, yPos*GRID_SIZE);
	}
	public int screenLeftXPos() {
		return GHQ.getScreenLeftX_stageCod()/GRID_SIZE;
	}
	public int screenTopYPos() {
		return GHQ.getScreenTopY_stageCod()/GRID_SIZE;
	}
	public int gridsToFillScreenWidth() {
		return GHQ.getScreenW_stageCod()/GRID_SIZE + 2;
	}
	public int gridsToFillScreenHeight() {
		return GHQ.getScreenH_stageCod()/GRID_SIZE + 2;
	}
}
