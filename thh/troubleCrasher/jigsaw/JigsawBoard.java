package troubleCrasher.jigsaw;

import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;

public class JigsawBoard {
	private final LinkedList<Jigsaw> jigsaws = new LinkedList<>();
	
	final int xGrids, yGrids;
	final BitSet bits;
	
	public JigsawBoard(int xGrids, int yGrids) {
		this.xGrids = xGrids;
		this.yGrids = yGrids;
		bits = new BitSet(xGrids*yGrids);
	}
	
	public boolean jigsawsIntersects(Jigsaw jigSaw) {
		for(int xi = 0; xi < jigSaw.xGrids(); ++xi) {
			for(int yi = 0; yi < jigSaw.yGrids(); ++yi) {
				if(bits.get((jigSaw.gridX() + xi) + (jigSaw.gridY() + yi)*jigSaw.xGrids())) {
//					System.out.println("fail at " + xi + ", " + yi);
					return true;
				}
			}
		}
//		System.out.println("acc");
		return false;
	}
	
	public boolean setJigsaw(Jigsaw jigsaw) {
		if(jigsawsIntersects(jigsaw))
			return false;
		for(int xi = 0; xi < jigsaw.xGrids(); ++xi) {
			for(int yi = 0; yi < jigsaw.yGrids(); ++yi) {
				bits.set((jigsaw.gridX() + xi) + (jigsaw.gridY() + yi)*jigsaw.xGrids());
			}
		}
		jigsaws.add(jigsaw);
		return true;
	}
	public boolean removeJigsaw(Jigsaw jigsaw) {
		return jigsaws.remove(jigsaw);
	}
	
	public boolean hasTile(int gridX, int gridY) {
		return bits.get(gridX + gridY*xGrids());
	}
	public int xGrids() {
		return xGrids;
	}
	public int yGrids() {
		return yGrids;
	}
	public List<Jigsaw> jigsaws() {
		return jigsaws;
	}
}
