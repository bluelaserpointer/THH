package physics.stage;

import java.util.ArrayList;
import java.util.LinkedList;

import physics.HasBoundingBox;

public class GridArrayList<T> extends Grids {
	final ArrayList<T> list;
	public GridArrayList(HasBoundingBox stage, int gridSize) {
		super(stage, gridSize);
		final int gridAmount = super.gridAmount();
		list = new ArrayList<T>(gridAmount);
		for(int i = 0; i < gridAmount; ++i)
			list.add(null);
	}
	
	//control
	public void set_cellPos(int xPos, int yPos, T element) {
		if(0 <= xPos && xPos < xGrids && 0 <= yPos && yPos < yGrids)
			list.set(xPos + yPos*xGrids, element);
	}
	public void set_stageCod(int x, int y, T element) {
		set_cellPos(x/gridSize, y/gridSize, element);
	}
	public void clear() {
		list.clear();
	}
	
	//information
	public final LinkedList<T> getIntersected(HasBoundingBox object) {
		final LinkedList<T> liquids = new LinkedList<T>();
		final int startX = (object.intX() - object.width()/2)/gridSize;
		final int startY = (object.intY() - object.height()/2)/gridSize;
		final int endX = (object.intX() + object.width()/2)/gridSize;
		final int endY = (object.intY() + object.height()/2)/gridSize;
		for(int posX = startX; posX <= endX; ++posX) {
			for(int posY = startY; posY <= endY; ++posY) {
				final T element = get_cellPos(posX, posY);
				if(element != null)
					liquids.add(get_cellPos(posX, posY));
			}
		}
		return liquids;
	}
	public ArrayList<T> list() {
		return list;
	}
	public T get_cellPos(int xPos, int yPos) {
		if(0 <= xPos && xPos < xGrids && 0 <= yPos && yPos < yGrids)
			return list.get(xPos + yPos*xGrids);
		else
			return null;
	}
	public T get_stageCod(int x, int y) {
		return get_cellPos(x/gridSize, y/gridSize);
	}
}
