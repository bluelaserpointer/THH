package troubleCrasher.resource;

import troubleCrasher.jigsaw.Jigsaw;
import troubleCrasher.jigsaw.JigsawEnum;


public abstract class Box extends Jigsaw {
	private String boxName;
	
	public Box(String boxName, JigsawEnum type) {
		super(type);
		this.boxName = boxName;
	}
	
	public void setBoxName(String boxName) {
		this.boxName = boxName;
	}
	
	public String getBoxName() {
		return this.boxName;
	}
	
	public abstract void use();
}
