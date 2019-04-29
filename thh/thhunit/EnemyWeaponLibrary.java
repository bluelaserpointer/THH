package thhunit;

import static java.lang.Math.*;

import core.GHQ;
import paint.DotPaint;
import unit.Unit;
import weapon.Weapon;
import weapon.WeaponInfo;

public class EnemyWeaponLibrary{
	public static final int lightBall_S = 0,lightBall_ROUND = 1,BLACK_SLASH_BURST = 2,HEAL_SHOTGUN = 3;
	
	public static Weapon getWeaponController(int bulletKind) {
		WeaponInfo.clear();
		switch(bulletKind) {
		case lightBall_S:
		case lightBall_ROUND:
			WeaponInfo.name = "lightBall_S";
			WeaponInfo.coolTime = 5;
			WeaponInfo.magazineSize = 6;
			WeaponInfo.reloadTime = 100;
			return new Weapon();
		case HEAL_SHOTGUN:
			WeaponInfo.name = "HEAL_SHOTGUN";
			WeaponInfo.coolTime = 5;
			WeaponInfo.magazineSize = 3;
			WeaponInfo.reloadTime = 20;
			return new Weapon();
		case BLACK_SLASH_BURST:
			WeaponInfo.name = "BLACK_SLASH_BURST";
			WeaponInfo.coolTime = 5;
			WeaponInfo.magazineSize = 1;
			WeaponInfo.reloadTime = 40;
			return new Weapon();
		default:
			return null;
		}
	}
	public static void inputBulletInfo(Unit user,int bulletKind,DotPaint paintScript,Unit targetChara) {
		switch(bulletKind) {
		case lightBall_S:
			GHQ.addBullet(new THH_BulletLibrary.LightBall(user));
			break;
		case lightBall_ROUND:
			GHQ.addBullet(new THH_BulletLibrary.LightBall(user)).split_Burst(5, 10, 12.0);
			break;
		case HEAL_SHOTGUN:
			GHQ.addBullet(new THH_BulletLibrary.HealShotgun(user)).split_Burst(10, 16, 15.0);
			break;
		case BLACK_SLASH_BURST:
			final double DEG10 = PI/18;
			GHQ.addBullet(new THH_BulletLibrary.BlackSlashBurst(user)).split_NWay(10,new double[] {-DEG10, 0.0, +DEG10},20);
			break;
		}
	}
}
