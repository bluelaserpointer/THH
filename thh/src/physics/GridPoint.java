package physics;

public class GridPoint extends Point.IntPoint{
	private static final long serialVersionUID = -8901821167720670703L;
	public Direction4 direction;
	protected final int GRID_SIZE;
	
	public GridPoint(int gridSize) {
		direction = Direction4.D;
		GRID_SIZE = gridSize;
	}
	public GridPoint(int x, int y, int gridSize, Direction4 initialDirection) {
		super(x, y);
		direction = initialDirection;
		GRID_SIZE = gridSize;
	}
	public GridPoint move(Direction4 direction4) {
		switch(direction4) {
		case W:
			y -= GRID_SIZE;
			break;
		case D:
			x += GRID_SIZE;
			break;
		case S:
			y += GRID_SIZE;
			break;
		case A:
			x -= GRID_SIZE;
			break;
		}
		return this;
	}
	public GridPoint moveFoward() {
		this.move(direction);
		return this;
	}
	public GridPoint moveBack() {
		this.move(direction.getBack());
		return this;
	}
	public GridPoint turnLeft() {
		direction = direction.getLeft();
		return this;
	}
	public GridPoint turnRight() {
		direction = direction.getRight();
		return this;
	}
	public void turnBack() {
		direction = direction.getBack();
	}
}
