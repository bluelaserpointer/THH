package physics;

public class GridPoint extends Point.IntPoint {
	private static final long serialVersionUID = -7403163749353622067L;

	private final int gridSize;
	public GridPoint(int gridSize) {
		this.gridSize = gridSize;
	}
	@Override
	public void setX(int x) {
		super.setX(x - x%gridSize);
	}
	@Override
	public void setY(int y) {
		super.setY(y - y%gridSize);
	}
	public GridPoint setPos(int xPos, int yPos) {
		setXPos(xPos);
		setYPos(yPos);
		return this;
	}
	public GridPoint setXPos(int xPos) {
		super.setX(xPos*gridSize + gridSize/2);
		return this;
	}
	public GridPoint setYPos(int yPos) {
		super.setY(yPos*gridSize + gridSize/2);
		return this;
	}
	
	//information
	public int gridSize() {
		return gridSize;
	}
	public int xPos() {
		return super.intX()/gridSize;
	}
	public int yPos() {
		return super.intY()/gridSize;
	}
}
