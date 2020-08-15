package troubleCrasher.person;

public enum SceneEnum {
    WORK_DAY("WORK_DAY", "thhimage/OfficeDay.png"), WORK_NIGHT("WORK_NIGHT", "thhimage/OfficeNight.png"),
    BAR("BAR", "thhimage/Bar.png");

    public final String name;
    public final String sceneImage;
    // public final String description;

    SceneEnum(final String name, final String sceneImage) {
        this.name = name;
        this.sceneImage = sceneImage;
    }
}
