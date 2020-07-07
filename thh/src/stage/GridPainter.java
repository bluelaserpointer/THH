package stage;

import java.awt.Graphics2D;

import calculate.HasGrid;
import core.GHQ;
import paint.rect.RectPaint;
import physics.HasBoundingBox;
import physics.Point;

public class GridPainter implements HasGrid {
	protected final int xGrids, yGrids;
	protected final int gridSize;
	
	public GridPainter(HasBoundingBox stage, int gridSize) {
		this.gridSize = gridSize;
		xGrids = stage.width()/gridSize + 1;
		yGrids = stage.height()/gridSize + 1;
	}
	//control
	public void drawGrid(Graphics2D g2, int xPos, int yPos) {
		g2.drawRect(xPos*gridSize, yPos*gridSize, gridSize, gridSize);
	}
	public void fillGrid(Graphics2D g2, int xPos, int yPos) {
		g2.fillRect(xPos*gridSize, yPos*gridSize, gridSize, gridSize);
	}
	public void paintGrid(RectPaint rectPaint, int xPos, int yPos) {
		rectPaint.rectPaint(xPos*gridSize, yPos*gridSize, gridSize, gridSize);
	}
	//information
	public Point getPosPoint(int xPos, int yPos) {
		return new Point.DoublePoint(xPos*gridSize, yPos*gridSize);
	}
	public int screenLeftXPos() {
		return GHQ.getScreenLeftX_stageCod()/gridSize;
	}
	public int screenTopYPos() {
		return GHQ.getScreenTopY_stageCod()/gridSize;
	}
	public int gridsToFillScreenWidth() {
		return GHQ.getScreenW_stageCod()/gridSize + 2;
	}
	public int gridsToFillScreenHeight() {
		return GHQ.getScreenH_stageCod()/gridSize + 2;
	}
	@Override
	public int gridSize() {
		return gridSize;
	}
	@Override
	public int xGrids() {
		return xGrids;
	}
	@Override
	public int yGrids() {
		return yGrids;
	}
}
