package troubleCrasher.person;

public enum PersonEnum {
	BANKER("银行家", "thhimage/Focus.png", "没胡子。");
	
	public final String name;
	public final String personImage;
	public final String description;
	PersonEnum(String name, String personImage, String description) {
		this.name = name;
		this.personImage = personImage;
		this.description = description;
	}
}
