package thh;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import bullet.Bullet;
import effect.Effect;

public abstract class Chara implements KeyListener,MouseListener,MouseWheelListener{
	//•’•£©`•Î•…
	//•∑•π•∆•‡ÈvﬂB
	protected final static int
		//system
		NONE = THH.NONE,
		MAX = THH.MAX,
		MIN = THH.MIN,
		EXIST = THH.EXIST;
	
	protected static THH thh;
	
	//•·•Ω•√•…
	
	//Initialization
	protected void battleStarted(int charaID){}
	protected abstract void spawn(int charaID,int charaTeam,int spawnX,int spawnY);
	protected void turnStarted(){}
	protected void loadImageData(){} //ª≠œÒ’i§ﬂﬁz§ﬂ
	protected void loadSoundData(){} //•µ•¶•Û•…’i§ﬂﬁz§ﬂ
	
	//idle
	protected abstract void idle(boolean isActive); //Include painting
	protected abstract void bulletIdle(Bullet bullet,boolean IsCharaActive); //Include painting
	protected abstract void effectIdle(Effect effect,boolean IsCharaActive); //Include painting
	
	//control
	//judge
	public abstract boolean bulletEngage(Bullet bullet);
	//decrease
	public abstract int decreaseME_amount(int amount);
	public abstract int decreaseME_rate(double rate);
	public abstract int damage_amount(int damage);
	public abstract int damage_rate(double rate);
	public abstract boolean kill();
	//information
	public abstract String getName();
	public abstract int getHP();
	public abstract double getHPRate();
	public abstract int getME();
	public abstract double getMERate();
	public abstract int getCharaStatus();
	//acceleration
	public abstract void addAccel(double xAccel,double yAccel);
	public abstract void setAccel(double xAccel,double yAccel);

	//bullet
	public void bulletOutOfLifeSpan(Bullet bullet){}
	public void bulletOutOfRange(Bullet bullet){}
	public void bulletOutOfPenetration(Bullet bullet) {
		if(!bullet.IS_LASER)
			thh.deleteBullet(bullet);
	}
	public void bulletOutOfReflection(Bullet bullet) {
		if(!bullet.IS_LASER)
			thh.deleteBullet(bullet);
	}
	public void bulletHitObject(Bullet bullet){}
	public boolean bulletIfHitLandscape(Bullet bullet,int x,int y){
		return thh.hitLandscape(x,y,10,10);
	}
	protected boolean deleteBullet(Bullet bullet){
		return true;
	}
	
	//effect
	public void effectOutOfLifeSpan(Effect effect) {}
	public void effectOutOfRange(Effect effect) {}
	protected boolean deleteEffect(Effect effect){
		return true;
	}
	
	//keyEvent
	@Override
	public void keyPressed(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {}
	@Override
	public void keyTyped(KeyEvent e) {}	
	//mouseEvent
	@Override
	public void mouseWheelMoved(MouseWheelEvent e){}
	@Override
	public void mouseEntered(MouseEvent e){}
	@Override
	public void mouseExited(MouseEvent e){}
	@Override
	public void mousePressed(MouseEvent e){}
	@Override
	public void mouseReleased(MouseEvent e){}
	@Override
	public void mouseClicked(MouseEvent e){}
	//specialEvent
	protected abstract void attackOrder(int targetX,int targetY);
	protected abstract void jumpOrder(int targetX,int targetY);
	protected abstract void dodgeOrder(int targetX,int targetY);
	protected abstract void guardOrder(int targetX,int targetY);
}