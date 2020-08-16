package troubleCrasher.resource;

import troubleCrasher.engine.TCGame;
import troubleCrasher.jigsaw.Jigsaw;
import troubleCrasher.jigsaw.JigsawEnum;


public class Box extends Jigsaw {
	public final BoxEnum boxEnum;
	
	static JigsawEnum decideJigsawShape(String boxName) {
		return JigsawEnum.SQ1;
	}
	public Box(BoxEnum boxEnum) {
		super(decideJigsawShape(boxEnum.boxName));
		this.boxEnum = boxEnum;
	}
	
	@Override
	public void paint(int left, int top) {
		boxEnum.image.rectPaint(left + gridX()*JigsawEnum.JIGSAW_GRID_SIZE,top + gridY()*JigsawEnum.JIGSAW_GRID_SIZE,JigsawEnum.JIGSAW_GRID_SIZE,JigsawEnum.JIGSAW_GRID_SIZE);
	}
	
	public BoxTagEnum getTag() {
		return boxEnum.tag;
	}
	public String getBoxName() {
		return boxEnum.boxName;
	}
	public boolean usable() {
		switch(boxEnum.tag) {
			case WEAPON:
			case HEAL:
				return true;
			case SCRIPT:
				return TCGame.scriptManager.boxNeeded(getBoxName());
			case WOUND:
				return false;
		}
		return false;
	}
}
