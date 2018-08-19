package bullet;

import thh.THH;

public class EnemyBulletLibrary extends BulletAgent{
	public static final int lightBall_S = 0;
	private static final EnemyBulletLibrary instance = new EnemyBulletLibrary();
	public static void inputBulletInfo(int bulletKind,int x,int y) {
		switch(bulletKind) {
		case lightBall_S:
			BulletInfo.name = "lightBall_S";
			BulletInfo.kind = lightBall_S;
			BulletInfo.x = x;
			BulletInfo.y = y;
			THH.createBullet(instance);
			break;
		}
	}
}
