package troubleCrasher.resource;

import troubleCrasher.engine.TCGame;
import troubleCrasher.jigsaw.Jigsaw;
import troubleCrasher.jigsaw.JigsawEnum;


public class Box extends Jigsaw {
	private String boxName;
	private String tag;
	private boolean reuse;	
	
	static JigsawEnum decideJigsawShape(String boxName) {
		return JigsawEnum.SQ1;
	}
	
	public Box(String boxName, boolean reuse) {
		super(decideJigsawShape(boxName));
		this.boxName = boxName;
		this.reuse = reuse;
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
	
	public boolean reusable() {
		
		String tag;
		Box box;
		
		switch(this.tag)
		{
			case "WEAPON":
				if(this.reuse)
				{
					return true;
				}else {
					return false;
				}
			case "HEAL":
				return false;
			case "SCRIPT":
				return TCGame.scriptManager.boxNeeded(this.boxName);
			default:
				break;
		}
		return reuse;	
	}
}
