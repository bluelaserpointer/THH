package bullet;

import thh.THH;

public class BulletScript {

	public void bulletIdle(Bullet bullet) { //Include painting
		if(bullet.idle())
			bullet.paint();
	}
	public void bulletAnimationPaint(Bullet bullet) {
		this.bulletPaint(bullet);
	}
	public void bulletPaint(Bullet bullet) {
		bullet.paint();
	}
	
	//event
	public boolean bulletOutOfLifeSpan(Bullet bullet) {
		return true;
	}
	public boolean bulletOutOfRange(Bullet bullet) {
		return true;
	}
	public boolean bulletOutOfPenetration(Bullet bullet) {
		return true;
	}
	public boolean bulletOutOfReflection(Bullet bullet) {
		return true;
	}
	public void bulletHitObject(Bullet bullet) {}
	public boolean bulletIfHitLandscape(Bullet bullet,int x,int y){
		return THH.hitLandscape(x,y,10,10);
	}
	
	//judge
	public boolean deleteBullet(Bullet bullet){
		return true;
	}
}
