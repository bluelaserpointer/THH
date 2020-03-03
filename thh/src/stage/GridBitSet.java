package stage;

import java.util.BitSet;

import physics.HasBoundingBox;

public class GridBitSet extends GridPainter {
	final BitSet bitSet;
	public GridBitSet(HasBoundingBox stage, int gridSize) {
		super(stage, gridSize);
		bitSet = new BitSet(super.W_GRIDS*super.H_GRIDS);
	}
	
	//control
	public void set_cellPos(int xPos, int yPos, boolean bool) {
		if(0 <= xPos && xPos < W_GRIDS && 0 <= yPos && yPos < H_GRIDS)
			bitSet.set(xPos + yPos*W_GRIDS, bool);
	}
	public void set_stageCod(int x, int y, boolean bool) {
		set_cellPos(x/GRID_SIZE, y/GRID_SIZE, bool);
	}
	public void set_cellPos(int xPos, int yPos) {
		set_cellPos(xPos, yPos, true);
	}
	public void set_stageCod(int x, int y) {
		set_stageCod(x, y, true);
	}
	public void clear() {
		bitSet.clear();
	}
	
	//information
	public BitSet bitSet() {
		return bitSet;
	}
	public boolean get_cellPos(int xPos, int yPos, boolean valueWhenOutOfBounds) {
		if(0 <= xPos && xPos < W_GRIDS && 0 <= yPos && yPos < H_GRIDS)
			return bitSet.get(xPos + yPos*W_GRIDS);
		else
			return valueWhenOutOfBounds;
	}
	public boolean get_stageCod(int x, int y, boolean valueWhenOutOfBounds) {
		return get_cellPos(x/GRID_SIZE, y/GRID_SIZE, valueWhenOutOfBounds);
	}
}
