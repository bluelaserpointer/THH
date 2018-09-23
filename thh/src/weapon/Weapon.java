package weapon;
/**
 * 射撃統制システムを自分で作りたくない人用に作成されました。
 * BulletやEffectのときと同じように、まずWeaponInfoにプロパティを記入してから、このクラスのインスタンスを生成してください。
 */
public class Weapon {
	protected final static int 
		NONE = thh.THH.NONE,
		MAX = thh.THH.MAX;
	protected final int
		COOL_TIME, //武器のショット毎に必要なクールタイム
		COOL_SPEED, //武器のフレーム毎クール値
		RELOAD_TIME, //武器のリロードに必要な時間
		RELOAD_SPEED, //武器のフレーム毎リロード値
		MAGAZINE_SIZE, //武器の弾倉容量
		MAGAZINE_CONSUMPTION, //武器のショット毎の弾薬消費
		RELOAD_OPTION;
	protected int
		coolProcess, //クール進行値
		reloadProcess, //リロード進行値
		magazine; //弾倉内残弾
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
	 * 武器のクールダウンを進めます。
	 * @param cool クールダウン加算値
	 * @return このメソッドでクールダウンが完了したか
	 */
	public boolean coolAdd(int cool) {
		if(reloadProcess == NONE && coolProcess < COOL_TIME) {
			coolProcess += cool;
			return coolProcess >= COOL_TIME;
		}
		return false;
	}
	/**
	 * 武器のリロードを進めます。
	 * @param value リロード進行度加算値
	 * @return このメソッドでリロードが完了したか
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
	 * 武器の残弾を全部取り出します。
	 * @return 取り出した弾の量
	 */
	public int unload() {
		final int temp = magazine;
		magazine = 0;
		return temp;
	}
	/**
	 * 武器のリロードを行います。
	 * @return 装填に使われた弾の量
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