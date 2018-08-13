package chara;

import static java.lang.Math.*;

import bullet.Bullet;
import bullet.BulletInfo;
import thh.Chara;
import thh.THH;

public abstract class THHOriginal extends Chara{
	
	//•–•»•Î•—©`•»ÈvﬂB
	protected int
		charaID,charaTeam,
		charaHP,charaME,
		charaBaseHP,charaBaseME,
		charaJumpLimit,
		charaSpellCharge,
		charaSize,
		charaStatus;
	protected double
		charaX,charaY,
		charaXSpeed,charaYSpeed,
		charaShotAngle;
	protected boolean
		charaOnLand;
	
	//Weapon
	protected int
		slot;
	protected final int
		weaponSlot_max = 6,
		weapon_max = 3;
	protected final int[]
		weaponSlot = new int[weaponSlot_max];

	//GUI
	
	//Resource
	//Images
	protected int
		charaIID;
	protected final int
		bulletIID[] = new int[weapon_max],
		effectIID[] = new int[10];
	
	@Override
	protected void loadImageData(){ //ª≠œÒ’i§ﬂﬁz§ﬂ
	}
	@Override
	protected void spawn(int charaID,int charaTeam,int x,int y){ //≥ı∆⁄ªØÑI¿Ì
		super.resetOrder();
		this.charaID = charaID;
		this.charaTeam = charaTeam;
		charaX = x;
		charaY = y;
		charaXSpeed = charaYSpeed = 0.0;
		charaStatus = NONE;
		charaOnLand = false;
		slot = 0;
	}
	@Override
	protected void idle(boolean isActive){
		final int mouseX = thh.getMouseX(),mouseY = thh.getMouseY();
		final double mouseAngle = atan2(mouseY - charaY,mouseX - charaX);
		//dynam
		charaX += charaXSpeed;
		charaY += charaYSpeed;
		if(charaXSpeed < -0.5 || 0.5 < charaXSpeed)
			charaXSpeed *= 0.9;
		else
			charaXSpeed = 0.0;
		if(charaYSpeed < -0.5 || 0.5 < charaYSpeed)
			charaYSpeed *= 0.9;
		else
			charaYSpeed = 0.0;
		if(!charaOnLand){
			charaYSpeed += 1.1;
			if(thh.hitLandscape((int)charaX - 10,(int)charaY + 40,20,20)){
				charaYSpeed = 0.0;
				do{
					charaY -= 1.0;
				}while(thh.hitLandscape((int)charaX - 10,(int)charaY + 30,20,10));
				if(charaXSpeed == 0.0)
					charaOnLand = true;
			}
		}
		//dodge
		if(super.dodgeOrder)
			dodge(mouseX,mouseY);
		//attack
		if(isActive){
			//death
			if(charaHP <= 0) {
				thh.rollCharaTurn();
				return;
			}
			//attack
			if(super.attackOrder) {
				charaShotAngle = mouseAngle;
				bulletSpawn(BulletInfo.kind = weaponSlot[slot]);
			}
			if(moveOrder) {
				charaX += (mouseX - charaX)/10;
				charaY += (mouseY - charaY)/10;
			}
			//weaponChange
			int roll = super.weaponChangeOrder;
			if(roll != 0) {
				int target = slot;
				if(roll > 0){
					while(target < weaponSlot_max - 1){
						if(weaponSlot[++target] != NONE) {
							if(--roll == 0)
								break;
						}
					}
				}else{
					while(target > 0){
						if(weaponSlot[--target] != NONE) {
							if(++roll == 0)
								break;
						}
					}
				}
				slot = target;
			}
		}
		super.resetOrder();
		//paintChara
		this.animationPaint();
	}
	@Override
	protected void animationPaint() {
		this.freezePaint();
	}
	@Override
	protected void freezePaint() {
		thh.drawImageTHH(charaIID,(int)charaX,(int)charaY);
		thh.paintHPArc((int)charaX,(int)charaY,charaHP,charaBaseHP);
	}
	
	//control
	//judge
	public final boolean bulletEngage(Bullet bullet) {
		return THH.squreCollision((int)charaX, (int)charaY, charaSize, (int)bullet.x, (int)bullet.y, bullet.SIZE)
			&& (bullet.team == charaTeam ^ bullet.atk > 0);
	}
	//acceleration
	@Override
	public final void addAccel(double xAccel,double yAccel){
		charaXSpeed += xAccel;
		charaYSpeed += yAccel;
	}
	public final void setAccel(double xAccel,double yAccel){
		charaXSpeed = xAccel;
		charaYSpeed = yAccel;
	}
	private final void dodge(double targetX,double targetY) {
		final double ANGLE = atan2(targetY - charaY,targetX - charaX);
		charaXSpeed += 40*cos(ANGLE);
		charaYSpeed += 40*sin(ANGLE);
		charaOnLand = false;
	}
	//decrease
	@Override
	public final int decreaseME_amount(int amount){
		charaME -= amount;
		return amount;
	}
	@Override
	public final int decreaseME_rate(double rate){
		final int value = (int)(charaME * rate);
		charaME -= value;
		return value;
	}
	@Override
	public final int damage_amount(int amount){
		charaHP -= amount;
		return amount;
	}
	@Override
	public final int damage_rate(double rate){
		final int damage = (int)(charaHP * rate);
		charaHP -= damage;
		return damage;
	}
	@Override
	public final boolean kill(){
		charaHP = 0;
		return true;
	}
	//information
	@Override
	public final int getHP(){
		return charaHP;
	}
	@Override
	public final double getHPRate(){
		return (double)charaHP/(double)charaBaseHP;
	}
	@Override
	public final int getME(){
		return charaME;
	}
	@Override
	public final double getMERate(){
		return (double)charaME/(double)charaBaseME;
	}
	@Override
	public final int getCharaStatus() {
		return charaStatus;
	}

	abstract protected void bulletSpawn(int kind);
	abstract protected void effectSpawn(int kind);
}