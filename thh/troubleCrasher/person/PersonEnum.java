package troubleCrasher.person;

public enum PersonEnum {
	BANKER("银行家", "thhimage/Focus.png", "没胡子。", ""),

	CAPTAIN("警长", "thhimage/Captain.png", "代填", ""), FARMER("农夫", "thhimage/Farmer.png", "代填", ""),
	DOCTOR("医生", "thhimage/Doctor.png", "代填", ""), ANNOUNCER("播报员", "thhimage/Announcer.png", "代填", "");

	public final String name;
	public final String personImage;
	public final String description;
	public final String personIcon;

	PersonEnum(final String name, final String personImage, final String description, final String personIcon) {
		this.name = name;
		this.personImage = personImage;
		this.description = description;
		this.personIcon = personIcon;
	}
}
