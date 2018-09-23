package weapon;

public class WeaponInfo {
	private final static int 
		MAX = thh.THH.MAX;
	public static String name;
	public static int
		coolTime,
		coolSpeed,
		reloadTime,
		reloadSpeed,
		magazineSize,
		magazineConsumption,
		reloadOption;
	public static final int
		EMPTY = 0,ALWAYS = 1;
	
	public static final void clear() {
		name = "<Not named>";
		coolTime = 0;
		coolSpeed = 1;
		reloadTime = 0;
		reloadSpeed = 1;
		magazineSize = MAX;
		magazineConsumption = 1;
		reloadOption = EMPTY;
	}
}
