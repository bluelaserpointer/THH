package troubleCrasher.resource;

import troubleCrasher.engine.TCGame;
import troubleCrasher.jigsaw.Jigsaw;
import troubleCrasher.jigsaw.JigsawEnum;


public class Box extends Jigsaw {
	private String boxName;
	private String tag;
	
	
	static JigsawEnum decideJigsawShape(String boxName) {
		return JigsawEnum.BAR2;
	}
	
	public Box(String boxName) {
		super(decideJigsawShape(boxName));
		this.boxName = boxName;
	}
	
	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	
	public void setBoxName(String boxName)
	{
		this.boxName = boxName;
	}
	
	public String getBoxName() {
		return this.boxName;
	}
	
}
