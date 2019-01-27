package chara;

import static java.lang.Math.*;
import bullet.BulletAgent;
import bullet.BulletInfo;
import thh.THH;
import weapon.Weapon;
import weapon.WeaponInfo;

public class EnemyBulletLibrary extends BulletAgent{
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
	public static void inputBulletInfo(Chara source,int bulletKind,int bulletIID,int x,int y,int targetX,int targetY) {
		THH.prepareBulletInfo();
		BulletInfo.team = source.getTeam();
		switch(bulletKind) {
		case lightBall_S:
			BulletInfo.name = "lightBall_S";
			BulletInfo.kind = lightBall_S;
			BulletInfo.fastParaSet_SourceDSpd(source,10,3);
			BulletInfo.atk = 20;
			BulletInfo.imageID = bulletIID;
			THH.createBullet(source);
			break;
		case lightBall_ROUND:
			BulletInfo.name = "lightBall_ROUND";
			BulletInfo.kind = lightBall_ROUND;
			BulletInfo.atk = 20;
			BulletInfo.imageID = bulletIID;
			BulletInfo.createBullet_BurstDesign(source,x,y,10,5,5);
			break;
		case HEAL_SHOTGUN:
			BulletInfo.name = "HEAL_SHOTGUN";
			BulletInfo.kind = HEAL_SHOTGUN;
			BulletInfo.atk = -20;
			BulletInfo.limitRange = 150;
			BulletInfo.imageID = bulletIID;
			BulletInfo.createBullet_BurstDesign(source,x,y,10,16,15);
			break;
		case BLACK_SLASH_BURST:
			BulletInfo.name = "BLACK_SLASH_BURST";
			BulletInfo.kind = lightBall_ROUND;
			BulletInfo.size = 10;
			BulletInfo.atk = 20;
			BulletInfo.offSet = 3;
			BulletInfo.reflection = 1;
			BulletInfo.limitFrame = 200;
			BulletInfo.imageID = bulletIID;
			final double DEG10 = PI/18;
			BulletInfo.fastParaSet_SourceDSpd(source,10,20);
			THH.createBullet(source);
			BulletInfo.fastParaSet_SourceADSpd(source,-DEG10,10,20);
			THH.createBullet(source);
			BulletInfo.fastParaSet_SourceADSpd(source,DEG10,10,20);
			THH.createBullet(source);
			break;
		}
	}
}
