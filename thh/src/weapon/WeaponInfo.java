package weapon;

import java.io.BufferedWriter;

import thh.Config;
import thh.ErrorCounter;
import thh.THH;

public class WeaponInfo {
	private final static int
		NONE = thh.THH.NONE,
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
	public static final boolean readConfig(String str) {
		final String[] _str = THH.split2(str, "=");
		if(_str.length == 2)
			return readConfig(_str[0],_str[1]);
		else
			return false;
	}
	public static final boolean readConfig(String property,String str) {
		switch(property) {
		case "coolTime":
			coolTime = Config.parseInt2(str, 0);
			break;
		case "coolSpeed":
			coolSpeed = Config.parseInt2(str, 1);
			break;
		case "reloadTime":
			reloadTime = Config.parseInt2(str, 0);
			break;
		case "reloadSpeed":
			reloadSpeed = Config.parseInt2(str, 1);
			break;
		case "magazineSize":
			magazineSize = Config.parseInt2(str,MAX);
			break;
		case "magazineConsumption":
			magazineConsumption = Config.parseInt2(str,1);
			break;
		case "reloadOption":
			if(str.equalsIgnoreCase("ALWAYS"))
				reloadOption = ALWAYS;
			else if(str.equalsIgnoreCase("EMPTY"))
				reloadOption = EMPTY;
			else if(str.equalsIgnoreCase("NONE"))
				reloadOption = NONE;
			else {
				ErrorCounter.putWithPrint("WeaponConfig.readConfig�β���ʹ�ã�(" + property + ", " + str + ")");
				reloadOption = EMPTY;
			}
		default:
			return false;
		}
		return true;
	}
	public static final void writeConfig(BufferedWriter bw) {
		final StringBuilder sb = new StringBuilder();
		final String ENDL = "\r\n";
		sb.append("coolTime = ").append(coolTime).append(ENDL);
		sb.append("coolSpeed = ").append(coolSpeed).append(ENDL);
		sb.append("reloadTime = ").append(reloadTime).append(ENDL);
		sb.append("reloadSpeed = ").append(reloadSpeed).append(ENDL);
		sb.append("magazineSize = ").append(magazineSize).append(ENDL);
		sb.append("magazineConsumption = ").append(magazineConsumption).append(ENDL);
		sb.append("reloadOption = ");
		switch(reloadOption) {
		case ALWAYS:
			sb.append("ALWAYS");
			break;
		case EMPTY:
			sb.append("EMPTY");
			break;
		case NONE:
			sb.append("NONE");
			break;
		}
		sb.append(ENDL);
	}
}