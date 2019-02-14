package bullet;

import java.io.Serializable;

import core.Dynam;
import core.DynamInteractable;
import core.GHQ;

public class BulletScript implements Serializable{
	private static final long serialVersionUID = -6286259077659200260L;

	//generation
	public final void create(DynamInteractable user) {
		create(user,user.getDynam());
	}
	public void create(DynamInteractable user,Dynam baseDynam) {}
	
	//role
	public void bulletIdle(Bullet bullet) { //Include painting
		if(bullet.defaultIdle())
			bulletNoAnmPaint(bullet);
	}
	public void bulletPaint(Bullet bullet) {
		this.bulletNoAnmPaint(bullet);
	}
	public void bulletNoAnmPaint(Bullet bullet) {
		bullet.defaultPaint();
	}
	
	//event
	public boolean bulletOutOfLifeSpan(Bullet bullet) {
		return true;
	}
	public boolean bulletOutOfRange(Bullet bullet) {
		return true;
	}
	public boolean bulletOutOfDurability(Bullet bullet) {
		return true;
	}
	public void bulletHitObject(Bullet bullet) {}
	public boolean bulletIfHitLandscape(Bullet bullet,int x,int y){
		return GHQ.hitLandscape_Rect(bullet.team,x,y,bullet.SIZE,bullet.SIZE);
	}
	
	//judge
	public boolean deleteBullet(Bullet bullet){
		return true;
	}
}
