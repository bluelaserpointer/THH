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
	
	public Box(String boxName) {
		super(decideJigsawShape(boxName));
		this.boxName = boxName;
		
		switch(boxName)
		{
			case "一瓶啤酒":
				this.setTag("WEAPON");
				this.reuse = false;
				break;
			case "左轮手枪":
				this.setTag("WEAPON");
				this.reuse = true;
				break;
			case "医疗绷带":
				this.setTag("HEAL");
				this.reuse = false;
				break;
			case "伤口":
				this.setTag("WOUND");
				this.reuse = true;
				break;
			case "失足的铁匠":
			case "小镇银行大劫案":
			case "强酸与烈火":
			case "酗酒的后果":
				this.setTag("SCRIPT");
				this.reuse = false;
				break;
			
		}
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
			case "WOUND":
				return false;
			case "SCRIPT":
				
				return TCGame.scriptManager.boxNeeded(this.boxName);
			default:
				break;
		}
		return reuse;	
	}
}
