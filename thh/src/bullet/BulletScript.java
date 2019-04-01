package bullet;

import java.io.Serializable;

import core.GHQ;
import physicis.Dynam;
import physicis.HasDynam;

public class BulletScript implements Serializable{
	private static final long serialVersionUID = -6286259077659200260L;

	//generation
	public final void create(HasDynam user) {
		create(user,user.getDynam());
	}
	public void create(HasDynam user,Dynam baseDynam) {}
	
	//role
	public void idle(Bullet bullet) { //Include painting
		if(bullet.defaultIdle())
			noAnmPaint(bullet);
	}
	public void paint(Bullet bullet) {
		this.noAnmPaint(bullet);
	}
	public void noAnmPaint(Bullet bullet) {
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
	public boolean bulletIfHitLandscape(Bullet bullet){
		return GHQ.hitStructure(bullet);
	}
	
	//judge
	public boolean deleteBullet(Bullet bullet){
		return true;
	}
}
