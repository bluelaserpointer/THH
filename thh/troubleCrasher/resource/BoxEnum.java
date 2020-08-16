package troubleCrasher.resource;

import paint.ImageFrame;

public enum BoxEnum {
	WINE("一瓶啤酒", BoxTagEnum.WEAPON),
	REBOLVER("左轮手枪", BoxTagEnum.WEAPON, true),
	BANDAGE("医疗绷带", BoxTagEnum.HEAL),
	WOUND_SMALL("伤口", BoxTagEnum.WOUND),
	//scripts
	SHI_ZU_TIE_JIANG("失足的铁匠"),
	YING_HANG_QIANG_JIE("小镇银行大劫案"),
	QIANG_SUAN_LIE_HUO("强酸与烈火"),
	XU_JIU_HOU_GUO("酗酒的后果"),
	;

	public final String boxName;
	public final BoxTagEnum tag;
	public final boolean reuse;
	public final ImageFrame image;
	BoxEnum(String boxName, String imageUrl) {
		this.boxName = boxName;
		this.tag = BoxTagEnum.SCRIPT;
		this.reuse = false;
		image = ImageFrame.create(imageUrl);
	}
	BoxEnum(String boxName, BoxTagEnum tag, String imageUrl) {
		this.boxName = boxName;
		this.tag = tag;
		this.reuse = false;
		image = ImageFrame.create(imageUrl);
	}
	BoxEnum(String boxName, BoxTagEnum tag, boolean reuse, String imageUrl) {
		this.boxName = boxName;
		this.tag = tag;
		this.reuse = reuse;
		image = ImageFrame.create(imageUrl);
	}
}
