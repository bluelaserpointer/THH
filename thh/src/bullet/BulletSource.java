package bullet;

import thh.THH;

public interface BulletSource {
	
	//idle
	public default void bulletIdle(Bullet bullet,boolean isCharaActive) { //Include painting
		bullet.defaultIdle();
		bullet.defaultPaint();
	}
	public default void bulletAnimationPaint(Bullet bullet) {
		this.bulletPaint(bullet);
	}
	public default void bulletPaint(Bullet bullet) {
		bullet.defaultPaint();
	}
	
	//event
	public default void bulletOutOfLifeSpan(Bullet bullet) {}
	public default void bulletOutOfRange(Bullet bullet) {}
	public default void bulletOutOfPenetration(Bullet bullet) {
		if(!bullet.IS_LASER)
			THH.deleteBullet(bullet);
	}
	public default void bulletOutOfReflection(Bullet bullet) {
		if(!bullet.IS_LASER)
			THH.deleteBullet(bullet);
	}
	public default void bulletHitObject(Bullet bullet) {}
	public default boolean bulletIfHitLandscape(Bullet bullet,int x,int y){
		return THH.hitLandscape(x,y,10,10);
	}
	
	//judge
	public default boolean deleteBullet(Bullet bullet){
		return true;
	}
}
