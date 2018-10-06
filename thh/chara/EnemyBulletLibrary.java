package chara;

import static java.lang.Math.*;
import bullet.BulletAgent;
import bullet.BulletInfo;
import bullet.BulletSource;
import thh.THH;
import weapon.Weapon;
import weapon.WeaponInfo;

public class EnemyBulletLibrary extends BulletAgent{
	public static final int lightBall_S = 0;
	
	private static final EnemyBulletLibrary instance = new EnemyBulletLibrary();
	public static Weapon getWeaponController(int bulletKind) {
		WeaponInfo.clear();
		switch(bulletKind) {
		case lightBall_S:
			WeaponInfo.name = "lightBall_S";
			WeaponInfo.coolTime = 5;
			WeaponInfo.magazineSize = 6;
			WeaponInfo.reloadTime = 100;
			return new Weapon();
		default:
			return null;
		}
	}
	public static void inputBulletInfo(int bulletKind,int bulletIID,int x,int y,int targetX,int targetY) {
		inputBulletInfo(instance,bulletKind,bulletIID,x,y,targetX,targetY);
	}
	public static void inputBulletInfo(BulletSource source,int bulletKind,int bulletIID,int x,int y,int targetX,int targetY) {
		final double ANGLE = atan2(targetY - y,targetX - x);
		switch(bulletKind) {
		case lightBall_S:
			BulletInfo.name = "lightBall_S";
			BulletInfo.kind = lightBall_S;
			BulletInfo.fastParaSet_XYADSpd(x,y,ANGLE,10,15);
			BulletInfo.atk = 20;
			BulletInfo.imageID = bulletIID;
			THH.createBullet(source);
			break;
		}
	}
}
