package chara;

import thh.*;

import java.awt.*;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import bullet.Bullet2;
import bullet.BulletInfo;

import static java.lang.Math.*;

public class Reimu extends Chara{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3254558769L;
	//•–•»•Î•—©`•»ÈvﬂB
	private int
		charaID,charaTeam,
		charaHP,charaME,
		charaJumpLimit,
		charaSize,
		charaStatus,
		dodgeMarkerX,dodgeMarkerY;
	private double
		charaX,charaY,
		charaXSpeed,charaYSpeed,
		charaShotAngle,
		dodgeMarkerAngleRecord,
		dodgeMarkerDistanceRecord;
	private boolean
		charaOnLand,
		isDraggingDodgeMarker;
	private final int
		charaBaseHP = 10000,
		charaBaseME = 100,
		charaBaseSize = 50,
		dodgeMarkerLimitDistance = 100;
	
	//Weapon
	private int
		slot;
	private final int
		weaponSlot_max = 6,
		weapon_max = 3;
	private final int[]
		weaponSlot = new int[weaponSlot_max];
	final int
		MILLKY_WAY = 0,NARROW_SPARK = 1,REUSE_BOMB = 2;
	
	//bullet
	private Linker linker_bullet_effects = new Linker();
	
	//effect
	private final int LIGHTNING = 0,NARROW_SPARK_HE = 1;
	private HashMap<Integer,Double> effectAngle = new HashMap<Integer,Double>();
	
	//GUI
	private int dragGapX,dragGapY;
	
	//Resource
	//Images
	private int
		charaIID,
		dodgeMarkerIID,
		bulletIID[] = new int[weapon_max],
		effectIID[] = new int[10];
	private int
		blazingSID;
	
	//BulletImages
	@Override
	protected void loadImageData(){ //ª≠œÒ’i§ﬂﬁz§ﬂ
		charaIID = thh.loadImage("Reimu.png");
		dodgeMarkerIID = thh.loadImage("DodgeMarker.png");
		bulletIID[MILLKY_WAY] = thh.loadImage("MillkyWay.png");
		bulletIID[NARROW_SPARK] = thh.loadImage("NarrowSpark_2.png");
		bulletIID[REUSE_BOMB] = thh.loadImage("ReuseBomb.png");
		effectIID[LIGHTNING] = thh.loadImage("ReuseBomb_Effect.png");
		effectIID[NARROW_SPARK_HE] = thh.loadImage("NarrowSpark_HitEffect.png");
	}
	
	//KeyEvent
	private int VK_DODGE,
			VK_END_TURN = KeyEvent.VK_ENTER;
	
	@Override
	protected void loadSoundData(){ //•µ•¶•Û•…’i§ﬂﬁz§ﬂ
	
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
		//attack
		if(isActive){
			//death
			if(charaHP <= 0) {
				thh.rollCharaTurn();
				return;
			}
			//weaponChange
			int roll = this.detectMouseWheelMove();
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
			//dodgeMarker
			if(thh.getMouseLeftPress()) {
				if(isDraggingDodgeMarker) {
					dodgeMarkerX = mouseX + dragGapX;
					dodgeMarkerY = mouseY + dragGapY;
					final int distanceX = dodgeMarkerX - (int)charaX,
							distanceY = dodgeMarkerY - (int)charaY;
					final double ANGLE = atan2(distanceY,distanceX),
							DISTANCE = distanceX*distanceX + distanceY*distanceY;
					if(DISTANCE > dodgeMarkerLimitDistance*dodgeMarkerLimitDistance) {
						dodgeMarkerX = (int)(charaX + dodgeMarkerLimitDistance*cos(ANGLE));
						dodgeMarkerY = (int)(charaY + dodgeMarkerLimitDistance*sin(ANGLE));
						dodgeMarkerDistanceRecord = dodgeMarkerLimitDistance;
					}else
						dodgeMarkerDistanceRecord = sqrt(DISTANCE);
					dodgeMarkerAngleRecord = ANGLE;
				}
			}else
				isDraggingDodgeMarker = false;
			//attack
			if(this.detectMousePress_left()) {
				if(thh.isMouseOnImage(dodgeMarkerIID,dodgeMarkerX, dodgeMarkerY)) {
					isDraggingDodgeMarker = true;
					dragGapX = dodgeMarkerX - mouseX;
					dragGapY = dodgeMarkerY - mouseY;
				}else {
					charaShotAngle = mouseAngle;
					final BulletInfo tempBI = thh.getTempBulletInfo();
					tempBI.source = charaID;
					tempBI.team = charaTeam;
					tempBI.kind = weaponSlot[slot];
					switch(weaponSlot[slot]){
						case MILLKY_WAY:
							tempBI.name = "MILLKY_WAY";
							tempBI.fastParamSet_ADSpd(mouseAngle,10,20);
							tempBI.accel = 1.0;
							tempBI.size = 30;
							tempBI.atk = 40;
							tempBI.offSet = 20;
							tempBI.penetration = 0;
							tempBI.reflection = 0;
							tempBI.limitFrame = 200;
							tempBI.limitRange = MAX;
							tempBI.imageID = bulletIID[MILLKY_WAY];
							thh.createBullet_RoundDesign(8,charaX,charaY,50);
							break;
						case NARROW_SPARK:
							tempBI.name = "NARROW_SPARK";
							tempBI.fastParamSet_XYADSpd(charaX,charaY,mouseAngle,0,thh.getImageByID(bulletIID[NARROW_SPARK]).getWidth(null));
							tempBI.accel = 1.0;
							tempBI.size = 50;
							tempBI.atk = 5;
							tempBI.offSet = 20;
							tempBI.penetration = MAX;
							tempBI.reflection = 3;
							tempBI.limitFrame = 30;
							tempBI.limitRange = MAX;
							tempBI.imageID = bulletIID[NARROW_SPARK];
							thh.createBullet();
							break;
						case REUSE_BOMB:
							tempBI.name = "REUSE_BOMB";
							tempBI.fastParamSet_ADSpd(mouseAngle,10,40);
							tempBI.accel = 0.98;
							tempBI.size = 30;
							tempBI.atk = 40;
							tempBI.offSet = 20;
							tempBI.penetration = 0;
							tempBI.reflection = 0;
							tempBI.limitFrame = 200;
							tempBI.limitRange = MAX;
							tempBI.imageID = bulletIID[REUSE_BOMB];
							thh.createBullet_RoundDesign(3,charaX,charaY,50);
							break;
					}
				}
			}
			if(this.detectMousePress_right()) { //dodge
				if(charaJumpLimit > 0){
					charaJumpLimit--;
					dodge(mouseX,mouseY);
				}
			}
			//turn end
			if(detectKeyInput(this.VK_END_TURN)) {
				isDraggingDodgeMarker = false;
				detectedInputFrame_dodge = thh.getNowFrame();
				thh.rollCharaTurn();
			}
		}else { //inactive
			if(detectKeyInput(this.VK_DODGE)) {
				dodge(dodgeMarkerX,dodgeMarkerY);
			}
		}
		//paintChara
		thh.drawImageTHH(charaIID,(int)charaX,(int)charaY);
		thh.drawImageTHH(dodgeMarkerIID, (int)dodgeMarkerX, (int)dodgeMarkerY);
		thh.paintHPArc((int)charaX,(int)charaY,charaHP,charaBaseHP);
	}
	@Override
	protected void bulletIdle(Bullet2 bullet,boolean isCharaActive) {
		final int x_int = (int)bullet.x,y_int = (int)bullet.y;
		switch(bullet.KIND) {
		case MILLKY_WAY:
			bullet.defaultIdle(thh);
			bullet.defaultPaint(thh);
			break;
		case NARROW_SPARK: //laser action
			while(thh.inStage(x_int,y_int) && bullet.defaultIdle(thh))
				bullet.defaultPaint(thh);
			bullet.x = charaX;
			bullet.y = charaY;
			break;
		case REUSE_BOMB:
			bullet.defaultIdle(thh);
			bullet.setSpeedY(bullet.getSpeedY() + 1.1);
			bullet.defaultPaint(thh);
			if(random() < 0.2) {
				int effectID = thh.createEffectID_cut();
				if(effectID != NONE) {
					thh.effectChara[effectID] = charaID;
					thh.effectKind[effectID] = LIGHTNING;
					thh.effectX[effectID] = 0;
					thh.effectY[effectID] = 0;
					effectAngle.put(effectID, 2*PI*random());
					linker_bullet_effects.put(bullet.ID, effectID);
				}
			}
			final int[] effectIDs = linker_bullet_effects.get(bullet.ID);
			for(int effectID : effectIDs) {
				if(thh.effectKind[effectID] == LIGHTNING) {
					if(thh.isExpired(thh.effectAppearedFrame[effectID],8)) {
						linker_bullet_effects.remove_IntList(bullet.ID,effectID);
						thh.effectKind[effectID] = NONE;
						thh.deleteEffect(effectID);
					}else {
						final double ANGLE = effectAngle.get(effectID);
						thh.effectX[effectID] += 20*cos(ANGLE);
						thh.effectY[effectID] += 20*sin(ANGLE);
						thh.drawImageTHH_center(effectIID[LIGHTNING], x_int + thh.effectX[effectID], y_int + thh.effectY[effectID],ANGLE);
					}
				}else {
					linker_bullet_effects.remove_IntList(bullet.ID,effectID);
					thh.deleteEffect(effectID);
				}
			}
			break;
		}
	}
	protected void effectIdle(int id,int kind,boolean isCharaActive) {
		switch(kind) {
		case LIGHTNING:
			break;
		case NARROW_SPARK_HE:
			if(thh.isExpired(thh.effectAppearedFrame[id],10))
				thh.deleteEffect(id);
			else
				thh.drawImageTHH_center(effectIID[id], thh.effectX[id], thh.effectY[id], this.effectAngle.get(id));
			break;
		}
	}
	
	//contact
	protected boolean bulletEngage(Bullet2 bullet) {
		return thh.squreCollision((int)charaX, (int)charaY, charaSize, (int)bullet.x, (int)bullet.y, bullet.SIZE)
			&& (bullet.team == charaTeam ^ bullet.atk > 0);
	}
	
	//delete
	protected boolean deleteBullet(int kind,int id) {
		switch(kind) {
		case REUSE_BOMB:
			linker_bullet_effects.remove(id);
			break;
		}
		return true;
	}
	protected boolean deleteEffect(int kind,int id) {
		switch(kind) {
		case LIGHTNING:
			thh.effectKind[id] = EXIST;
			effectAngle.remove(id);
			return false;
		case NARROW_SPARK_HE:
			effectAngle.remove(id);
			return true;
		}
		return true;
	}
	//dodge
	@Override
	protected void battleStarted(int charaID){
		//test area
		weaponSlot[0] = MILLKY_WAY;
		weaponSlot[1] = NARROW_SPARK;
		weaponSlot[2] = REUSE_BOMB;
		////
		slot = 0;
	}
	@Override
	protected void spawn(int charaID,int charaTeam,int x,int y){ //≥ı∆⁄ªØÑI¿Ì
		this.charaID = charaID;
		this.charaTeam = charaTeam;
		charaX = x;
		charaY = y;
		dodgeMarkerX = x;
		dodgeMarkerY = y - 50;
		dodgeMarkerAngleRecord = -PI/2;
		dodgeMarkerDistanceRecord = 50;
		charaXSpeed = charaYSpeed = 0.0;
		charaHP = charaBaseHP;
		charaME = charaBaseME;
		charaSize = charaBaseSize;
		charaJumpLimit = 300;
		charaStatus = NONE;
		charaOnLand = false;
		slot = 0;
		VK_DODGE = KeyEvent.VK_1 + (charaID - charaTeam)/2;
	}
	@Override
	protected void turnStarted(){
		charaJumpLimit = 300;
		detectedMousePressedFrame_left
		 = detectedMousePressedFrame_right
		 = detectedInputFrame_endTurn
		 = detectedMouseWheelMovedFrame
		 = thh.getNowFrame();
		dodgeMarkerX = (int)(charaX + dodgeMarkerDistanceRecord*cos(dodgeMarkerAngleRecord));
		dodgeMarkerY = (int)(charaY + dodgeMarkerDistanceRecord*sin(dodgeMarkerAngleRecord));
	}
	
	//acceleration
	@Override
	protected void addAccel(double xAccel,double yAccel){
		charaXSpeed += xAccel;
		charaYSpeed += yAccel;
	}
	protected void setAccel(double xAccel,double yAccel){
		charaXSpeed = xAccel;
		charaYSpeed = yAccel;
	}
	private void dodge(double targetX,double targetY) {
		final double ANGLE = atan2(targetY - charaY,targetX - charaX);
		charaXSpeed += 40*cos(ANGLE);
		charaYSpeed += 40*sin(ANGLE);
		charaOnLand = false;
	}
	//decrease
	@Override
	protected int decreaseME_amount(int amount){
		charaME -= amount;
		return amount;
	}
	@Override
	protected int decreaseME_rate(double rate){
		final int value = (int)(charaME * rate);
		charaME -= value;
		return value;
	}
	@Override
	public int damage_amount(int amount){
		charaHP -= amount;
		return amount;
	}
	@Override
	public int damage_rate(double rate){
		final int damage = (int)(charaHP * rate);
		charaHP -= damage;
		return damage;
	}
	@Override
	protected boolean kill(){
		charaHP = 0;
		return true;
	}
	//infomation
	@Override
	protected int getHP(){
		return charaHP;
	}
	@Override
	protected double getHPRate(){
		return (double)charaHP/(double)charaBaseHP;
	}
	private int
		detectedMousePressedFrame_left,
		detectedMousePressedFrame_right,
		detectedMouseWheelMovedFrame;
	private int
		detectedInputFrame_dodge,
		detectedInputFrame_endTurn;
	private int detectMouseWheelMove() {
		final int newFrame = thh.getMouseWheelMovedFrame();
		if(detectedMouseWheelMovedFrame < newFrame) {
			detectedMouseWheelMovedFrame = newFrame;
			return thh.getMouseWheelMoveAmount();
		}
		return 0;
	}
	private boolean detectMousePress_left() {
		final int newFrame = thh.getMousePressedFrame_left();
		if(detectedMousePressedFrame_left < newFrame) {
			detectedMousePressedFrame_left = newFrame;
			return true;
		}
		return false;
	}
	private boolean detectMousePress_right() {
		final int newFrame = thh.getMousePressedFrame_right();
		if(detectedMousePressedFrame_right < newFrame) {
			detectedMousePressedFrame_right = newFrame;
			return true;
		}
		return false;
	}
	private boolean detectKeyInput(int keyEvent) {
		final int newFrame = thh.getKeyInputFrame(keyEvent);
		if(keyEvent == this.VK_DODGE) {
			if(detectedInputFrame_dodge < newFrame) {
				detectedInputFrame_dodge = newFrame;
				return true;
			}
		}else if(keyEvent == this.VK_END_TURN) {
			if(detectedInputFrame_endTurn < newFrame) {
				detectedInputFrame_endTurn = newFrame;
				return true;
			}
		}
		return false;
	}
}