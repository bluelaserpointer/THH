package weapon;
/**
 * ��Ľy�ƥ����ƥ���Է֤����ꤿ���ʤ����ä����ɤ���ޤ�����
 * Bullet��Effect�ΤȤ���ͬ���褦�ˡ��ޤ�WeaponInfo�˥ץ�ѥƥ���ӛ�뤷�Ƥ��顢���Υ��饹�Υ��󥹥��󥹤����ɤ��Ƥ���������
 */
public class Weapon {
	protected final static int 
		NONE = thh.THH.NONE,
		MAX = thh.THH.MAX;
	protected final int
		COOL_TIME, //�����Υ���åȚ��˱�Ҫ�ʥ��`�륿����
		COOL_SPEED, //�����Υե�`�������`�낎
		RELOAD_TIME, //�����Υ��`�ɤ˱�Ҫ�ʕr�g
		RELOAD_SPEED, //�����Υե�`�������`�ɂ�
		MAGAZINE_SIZE, //�����Ώ��}����
		MAGAZINE_CONSUMPTION, //�����Υ���åȚ��Ώ��a���M
		RELOAD_OPTION;
	protected int
		coolProcess, //���`���M�Ђ�
		reloadProcess, //���`���M�Ђ�
		magazine; //���}�ڲЏ�
	public Weapon() {
		COOL_TIME = WeaponInfo.coolTime;
		COOL_SPEED = WeaponInfo.coolSpeed;
		RELOAD_TIME = WeaponInfo.reloadTime;
		RELOAD_SPEED = WeaponInfo.reloadSpeed;
		MAGAZINE_SIZE = WeaponInfo.magazineSize;
		MAGAZINE_CONSUMPTION = WeaponInfo.magazineConsumption;
		RELOAD_OPTION = WeaponInfo.reloadOption;
	}
	public void reset() {
		coolProcess = COOL_TIME;
		reloadProcess = RELOAD_TIME;
		magazine = MAGAZINE_SIZE;
	}
	//control
	public void defaultIdle() {
		if(reloadProcess == NONE) //cooling
			coolProcess += COOL_SPEED;
		else { //reloading
			reloadProcess += RELOAD_SPEED;
			if(reloadProcess > RELOAD_TIME) {
				reloadProcess = NONE;
				coolProcess = COOL_TIME;
				magazine = MAGAZINE_SIZE;
			}
		}
	}
	public boolean trigger(int magazineConsumption) {
		if(coolProcess >= COOL_TIME && reloadProcess == NONE && magazine > 0) { //fire
			coolProcess = 0;
			if(magazine != MAX) {
				magazine -= magazineConsumption;
				switch(RELOAD_OPTION) {
				case WeaponInfo.EMPTY:
					if(magazine == 0)
						this.reload();
					break;
				case WeaponInfo.ALWAYS:
					this.reload();
					break;
				}
			}
			return true;
		}
		return false;
	}
	public boolean trigger() {
		return this.trigger(MAGAZINE_CONSUMPTION);
	}
	/**
	 * �����Υ��`���������M��ޤ���
	 * @param cool ���`���������ゎ
	 * @return ���Υ᥽�åɤǥ��`����������ˤ�����
	 */
	public boolean coolAdd(int cool) {
		if(reloadProcess == NONE && coolProcess < COOL_TIME) {
			coolProcess += cool;
			return coolProcess >= COOL_TIME;
		}
		return false;
	}
	/**
	 * �����Υ��`�ɤ��M��ޤ���
	 * @param value ���`���M�жȼ��ゎ
	 * @return ���Υ᥽�åɤǥ��`�ɤ����ˤ�����
	 */
	public boolean reloadAdd(int value) {
		if(reloadProcess != NONE) {
			reloadProcess += RELOAD_SPEED;
			if(reloadProcess > RELOAD_TIME) {
				reloadProcess = NONE;
				coolProcess = COOL_TIME;
				magazine = MAGAZINE_SIZE;
				return true;
			}
		}
		return false;
	}
	/**
	 * �����βЏ���ȫ��ȡ������ޤ���
	 * @return ȡ�������������
	 */
	public int unload() {
		final int temp = magazine;
		magazine = 0;
		return temp;
	}
	/**
	 * �����Υ��`�ɤ��Ф��ޤ���
	 * @return װ���ʹ��줿������
	 */
	public int reload() {
		if(RELOAD_TIME == 0)
			return 0;
		final int requiredAmmo = MAGAZINE_SIZE - magazine;
		if(requiredAmmo > 0) {
			reloadProcess = 0;
			magazine = 0;
		}
		return requiredAmmo;
	}
	public int reload(int surplusAmmo) {
		if(RELOAD_TIME == 0)
			return 0;
		final int requiredAmmo = MAGAZINE_SIZE - magazine;
		if(requiredAmmo > 0) {
			reloadProcess = 0;
			magazine = 0;
		}
		return requiredAmmo;
	}
	public final int getMagazine() {
		return magazine;
	}
	public final double getMagazinePercentage() {
		return (double)magazine/(double)MAGAZINE_SIZE;
	}
}