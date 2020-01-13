package stage;

import java.awt.Graphics2D;

import core.GHQ;
import physics.Point;

public class Gridder{
	public final int W_DIV, H_DIV;
	public final double GRID_W, GRID_H;
	
	public Gridder(int widthDiv, int heightDiv) {
		W_DIV = widthDiv;
		H_DIV = heightDiv;
		GRID_W = (double)GHQ.stage().WIDTH/widthDiv;
		GRID_H = (double)GHQ.stage().HEIGHT/heightDiv;
	}
	
	public void drawGrid(Graphics2D g2, int xPos, int yPos) {
		g2.drawRect((int)(xPos*GRID_W), (int)(yPos*GRID_H), (int)GRID_W, (int)GRID_H);
	}
	public void fillGrid(Graphics2D g2, int xPos, int yPos) {
		g2.fillRect((int)(xPos*GRID_W), (int)(yPos*GRID_H), (int)GRID_W, (int)GRID_H);
	}
	public Point getPosPoint(int xPos, int yPos) {
		return new Point.DoublePoint(xPos*GRID_W, yPos*GRID_H);
	}

}
