package structure;

import java.util.ArrayList;
import java.util.Arrays;

import core.ErrorCounter;
import core.GHQ;

public abstract class Tile extends Structure{
	protected ArrayList<Integer> tileX = new ArrayList<Integer>();
	protected ArrayList<Integer> tileY = new ArrayList<Integer>();
	protected final int TILE_SIZE,
		IMAGE_ID;
	
	public Tile(int[] tileX,int[] tileY,int tileSize,int imageIID) {
		if(tileX.length != tileY.length) {
			ErrorCounter.put("Tile's constructer called Illegally: tileX.length = " + tileX.length + ", tileY.length = " + tileY.length);
			if(tileX.length < tileY.length)
				tileY = Arrays.copyOf(tileY, tileX.length);
			else
				tileX = Arrays.copyOf(tileX, tileY.length);
		}
		for(int x : tileX)
			this.tileX.add(x);
		for(int y : tileY)
			this.tileY.add(y);
		TILE_SIZE = tileSize;
		IMAGE_ID = imageIID;
	}
	@Override
	public boolean contains(int x,int y,int w,int h) {
		final int LENGTH = tileX.size();
		for(int i = 0;i < LENGTH;i++) {
			if(GHQ.rectangleCollision(tileX.get(i), tileY.get(i), TILE_SIZE, TILE_SIZE, x, y, w, h))
				return true;
		}
		return false;
	}
}
