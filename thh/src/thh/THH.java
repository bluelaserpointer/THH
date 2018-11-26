package thh;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import static java.awt.event.KeyEvent.*;
import javax.swing.*;

import bullet.Bullet;
import bullet.BulletInfo;
import effect.Effect;
import effect.EffectInfo;
import engine.Engine_THH1;
import stage.ControlExpansion;
import stage.Stage;
import stage.StageEngine;

import java.io.*;
import java.net.*;
import java.util.*;
import static java.lang.Math.*;

/**
 * 
 * @author bluelaserpointer
 * @version alpha1.0
 *
 */
public final class THH extends JPanel implements MouseListener,MouseMotionListener,MouseWheelListener,KeyListener,Runnable{
	private static final long serialVersionUID = 123412351L;

	static final String
		GAME_VERSION = "Ver beta1.0.0";
	public static final int
		NONE = -999999999, //値なし
		ALL = -NONE,
		MAX = Integer.MAX_VALUE, //最大
		MIN = Integer.MIN_VALUE; //最小

	public static final String
		NOT_NAMED = "<Not Named>";
	
	//システム関連
	//File Pass
	final String
		ASSETS_URL = "assets",
		CHARA_FOLDERS_URL = "assets/chara",
		LOCAL_IMAGE_URL = "assets/image",
		LOCAL_SOUND_URL = "assets/sound",
		LOCAL_FONT_URL = "assets/font";
	
	//debug
	private static boolean freezeScreen;

	private boolean debugMode;
	private long loadTime_total;
	
	public static String errorPoint = "NONE";
	
	//イベント関連
	public final int 
		OPENING = 1000,
		TITLE = 2000,
		LOAD = 3000,
		SAVE = 4000,
		OPTION = 5000,OPTION_sound = 5100,OPTION_grahics = 5200,
		ACTION_PART = 6000,
		BATTLE = 7000,BATTLE_PAUSE = 7001,
		EVENT_PART = 8000;
	private int mainEvent = OPENING; //イベント複合情報(イベント種類+イベントデータ)
	
	//stopEvent
	private static int stopEventKind = NONE;
	public static final int STOP = 0,FREEZE = 1;
	private static int stopEventReason;
	public static final int MESSAGE = 0,SPELL = 1;
	
	//ウィンドウ関連
	private final int defaultScreenW = 1000,defaultScreenH = 600; //デフォルトウィンドサイズ
	private static int screenW = 1000,screenH = 600;
	private static double viewX,viewY,viewDstX,viewDstY;
	
	private int page,page_max; //ページ機能
	
	//タイム情報
	private int clearFrame; //ゲーム関係時間
	private static int systemFrame; //現在フレーム
	private static int gameFrame;
	
	//キャラクター情報
	private static Chara[] characters = new Chara[0];
	
	//stage
	private static StageEngine engine = new Engine_THH1();
	private static Stage stage;
	
	//bullet data
	private static final ArrayListEx<Bullet> bullets = new ArrayListEx<Bullet>();
	
	//effect data
	private static final ArrayListEx<Effect> effects = new ArrayListEx<Effect>();
	
	//event data
	private static final ArrayDeque<String> messageStr = new ArrayDeque<String>();
	private static final ArrayDeque<MessageSource> messageSource = new ArrayDeque<MessageSource>();
	private static final ArrayDeque<Integer> messageEvent = new ArrayDeque<Integer>();
	private int messageIterator;

	//ResourceSystem
	private Image[] arrayImage = new Image[128];
	private String[] arrayImageURL = new String[128];
	private int arrayImage_maxID = -1;
	private SoundClip[] arraySound = new SoundClip[128];
	private String[] arraySoundURL = new String[128];
	private int arraySound_maxID = -1;
	
	public static THH thh;
	
	//Initialize methods/////////////
	public static void main(String args[]){
		new THH();
	}
	private final JFrame myFrame;
	private final MediaTracker tracker;
	public THH(){
		thh = this;
		Chara.thh = thh;
		final long loadTime = System.currentTimeMillis();
		//window setup
		myFrame = new JFrame("東方六弾幕説");
		myFrame.add(this,BorderLayout.CENTER);
		myFrame.addWindowListener(new MyWindowAdapter());
		addMouseMotionListener(this);
		addMouseListener(this);
		addMouseWheelListener(this);
		myFrame.addKeyListener(this);
		myFrame.setBackground(Color.BLACK);
		myFrame.setBounds(120,80,1006,628);
		myFrame.setResizable(false);
		myFrame.setVisible(true);
		//load assets
		tracker = new MediaTracker(this);
		//setup
		resetStage();
		//System.out.println("loaded chara class: " + charaLibrary.length);
		//image & sound length fit
		arrayImage = Arrays.copyOf(arrayImage, arrayImage_maxID + 1);
		arraySound = Arrays.copyOf(arraySound, arrayImage_maxID + 1);
		try{
			tracker.waitForAll(); //夋憸儘乕僪
		}catch(InterruptedException | NullPointerException e){}
		
		System.out.println("loadTimeReslut: " + (System.currentTimeMillis() - loadTime));//儘乕僪強梫帪娫寢壥昞帵
		new Thread(this).start();
		loadComplete = true;
	}

	//mainLoop///////////////
	public final void run(){
		//titleBGM.loop();
		//try{
			while(true){
				try{
					Thread.sleep(25L);
				}catch(InterruptedException e){}
				if(freezeScreen)
					continue;
				systemFrame++;
				if(stopEventKind == NONE)
					gameFrame++;
				repaint();
			}
		//}catch(Exception e){
			//JOptionPane.showMessageDialog(null, "怽偟栿偁傝傑偣傫偑丄僄儔乕偑敪惗偟傑偟偨丅\n僄儔乕僐乕僪丗" + e.toString(),"僄儔乕",JOptionPane.ERROR_MESSAGE);
		//}
	}
	
	private boolean loadComplete;
	private final BufferedImage offImage = new BufferedImage(defaultScreenW,defaultScreenH,BufferedImage.TYPE_INT_ARGB_PRE); //僟僽儖僶僢僼傽僉儍儞僶僗
	private Graphics2D g2;
	private final Font basicFont = createFont("font/upcibi.ttf").deriveFont(Font.BOLD + Font.ITALIC,30.0f),commentFont = createFont("font/HGRGM.TTC").deriveFont(Font.PLAIN,15.0f);
	public static final BasicStroke stroke1 = new BasicStroke(1f),stroke3 = new BasicStroke(3f),stroke5 = new BasicStroke(5f);
	private static final Color HPWarningColor = new Color(255,120,120),debugTextColor = new Color(200,200,200,160);
	private final Rectangle2D screenRect = new Rectangle2D.Double(0,0,defaultScreenW,defaultScreenH);
	
	public void paintComponent(Graphics g){
		final long LOAD_TIME_PAINTCOMPONENT = System.currentTimeMillis();
		final int mouseX = THH.mouseX,mouseY = THH.mouseY;
		super.paintComponent(g);
		if(g2 == null){
			g2 = offImage.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
			g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_COLOR_RENDER_SPEED);
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			g2.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_SPEED);
		}
		if(!loadComplete)
			return;
		g2.setColor(Color.WHITE);
		g2.fill(screenRect);
		final int TRANSLATE_X = (int)viewX,TRANSLATE_Y = (int)viewY;
		g2.translate(TRANSLATE_X, TRANSLATE_Y);
		////////////////////////////////////////////////////////////////////////
		//stageAction
		engine.idle(g2,stopEventKind);
		////////////////////////////////////////////////////////////////////////
		//GUIAction
		g2.translate(-TRANSLATE_X, -TRANSLATE_Y);
		{
			//message system///////////////
			if(stopEventKind != NONE && stopEventReason == MESSAGE) {
				if(messageStr.size() > 0) {
					if(messageIterator > messageStr.getFirst().length() - 1){
						if(key_enter) { //next message order
							messageStr.remove();
							final MessageSource SOURCE = messageSource.remove();
							final int EVENT = messageEvent.remove();
							if(EVENT != NONE)
								SOURCE.eventNotice(EVENT);
							messageIterator = 0;
							key_enter = false;
						}
					}else if(key_enter) {
						messageIterator = messageStr.getFirst().length();
						key_enter = false;
					}else if(systemFrame % 2 == 0) //reading
						messageIterator++;
					//show message
					if(messageStr.size() > 0) {
						g2.setFont(basicFont);
						g2.setStroke(stroke5);
						g2.setColor(Color.CYAN);
						g2.drawString(messageStr.getFirst().substring(0, messageIterator), 150, 450);
					}
				}else { //finished reading
					stopEventKind = stopEventReason = NONE;
					messageIterator = 0;
					messageStr.clear();
					messageSource.clear();
					messageEvent.clear();
				}
			}
			//debug ////////////////////////
			if(debugMode){
				//grid
				g2.setColor(debugTextColor);
				g2.setFont(basicFont);
				g2.setStroke(stroke1);
				for(int i = 100;i < defaultScreenW;i += 100)
					g2.drawLine(i,0,i,defaultScreenH);
				for(int i = 100;i < defaultScreenH;i += 100)
					g2.drawLine(0,i,defaultScreenW,i);
				//Origin
				g2.setColor(debugTextColor);
				g2.setStroke(stroke1);
				g2.drawOval(TRANSLATE_X - 35, TRANSLATE_Y - 35, 70, 70);
				g2.drawOval(TRANSLATE_X - 25, TRANSLATE_Y - 25, 50, 50);
				g2.drawOval(TRANSLATE_X - 15, TRANSLATE_Y - 15, 30, 30);
				//mouseCoordinate
				g2.setColor(debugTextColor);
				g2.setStroke(stroke5);
				g2.drawString(mouseX + "," + mouseY,mouseX + 20,mouseY + 20);
				//entityInfo
				g2.drawString("Chara:" + characters.length + " EF:" + effects.size() + " B:" + bullets.size(),30,100);
				g2.drawString("LoadTime(ms):" + loadTime_total,30,120);
				//g2.drawString("EM:" + loadTime_enemy + " ET:" + loadTime_entity + " G:" + loadTime_gimmick + " EF:" + loadTime_effect + " B:" + loadTime_bullet + " I:" + loadTime_item + " W: " + loadTime_weapon + " Other: " + loadTime_other,30,140);
				g2.drawString("GameTime(ms):" + gameFrame,30,160);
				//mouseInfo
				g2.setStroke(stroke1);
				g2.drawLine(mouseX - 15, mouseY, mouseX + 15, mouseY);
				g2.drawLine(mouseX, mouseY - 15, mouseX, mouseY + 15);
				g2.setStroke(stroke5);
				g2.drawString("(" + (mouseX - viewX) + "," + (mouseY - viewY) + ")",mouseX + 20,mouseY + 40);
				//charaInfo
				for(Chara chara : characters) {
					final int SCREEN_X = (int)chara.getX() + (int)viewX,SCREEN_Y = (int)chara.getY() + (int)viewY;
					g2.setStroke(stroke1);
					g2.drawRect(SCREEN_X - 50, SCREEN_Y - 50, 100,100);
					g2.drawLine(SCREEN_X + 50, SCREEN_Y - 50, SCREEN_X + 60, SCREEN_Y - 60);
					g2.setStroke(stroke5);
					g2.drawString(chara.getName(), SCREEN_X + 62, SCREEN_Y - 68);
				}
			}
		}
		//UI昤夋
		g2.setColor(Color.GRAY);
		g2.fillRect(0,0,defaultScreenW,40);
		g.drawImage(offImage,0,0,screenW,screenH,this);
		loadTime_total = System.currentTimeMillis() - LOAD_TIME_PAINTCOMPONENT;
	}
	
	public static final Chara[] callBulletEngage(Bullet bullet) {
		return engine.callBulletEngage(getCharacters_team(bullet.team,false),bullet);
	}
	public static final Chara[] callBulletEngage(Chara[] characters,Bullet bullet) {
		return engine.callBulletEngage(characters,bullet);
	}
	//idle-character
	public static final void defaultCharaIdle(Chara[] characters) {
		switch(stopEventKind) {
		case STOP:
			for(Chara chara : characters)
				chara.idle(Chara.PASSIVE_CONS);
			break;
		case FREEZE:
			for(Chara chara : characters)
				chara.idle(Chara.PAINT_FREEZED);
			break;
		default:
			for(Chara chara : characters) {
				chara.idle();
				chara.resetSingleOrder();
			}
		}
	}
	//idle-entity
	public static final void defaultEntityIdle() {
		bullets.setIterator(0);
		effects.setIterator(0);
		switch(stopEventKind) {
		case STOP:
			while(bullets.hasNext()) {
				final Bullet bullet = bullets.next();
				bullet.SCRIPT.bulletAnimationPaint(bullet);
			}
			while(effects.hasNext()) {
				final Effect effect = effects.next();
				effect.SCRIPT.effectAnimationPaint(effect);
			}
			break;
		case FREEZE:
			while(bullets.hasNext()) {
				final Bullet bullet = bullets.next();
				bullet.SCRIPT.bulletPaint(bullet);
			}
			while(effects.hasNext()) {
				final Effect effect = effects.next();
				effect.SCRIPT.effectPaint(effect);
			}
			break;
		default:
			while(bullets.hasNext()) {
				final Bullet bullet = bullets.next();
				bullet.SCRIPT.bulletIdle(bullet);
			}
			while(effects.hasNext()) {
				final Effect effect = effects.next();
				effect.SCRIPT.effectIdle(effect);
			}
		}
	}
	public static final void defaultCharaIdle(Chara chara) {
		switch(stopEventKind) {
		case STOP:
			chara.idle(Chara.PASSIVE_CONS);
			break;
		case FREEZE:
			chara.idle(Chara.PAINT_FREEZED);
			break;
		default:
			chara.idle();
			chara.resetSingleOrder();
		}
	}
	//information-characters
	public static final Chara getChara(String name) {
		for(Chara chara : characters) {
			if(chara.getName() == name)
				return chara;
		}
		return null;
	}
	public static final Chara[] getCharacters() {
		return characters;
	}
	public static final Chara getCharacters(int charaID) {
		return characters[charaID];
	}
	public static final Chara[] getCharacters_team(int team,boolean white) {
		final Chara[] charaArray = new Chara[characters.length];
		int founded = 0;
		for(Chara chara : characters) {
			if(white == isSameTeam(team,chara.getTeam()))
				charaArray[founded++] = chara;
		}
		return Arrays.copyOf(charaArray, founded);
	}
	public static final Chara[] getCharacters_team(int team) {
		return getCharacters_team(team,true);
	}
	public static final Chara getNearstEnemy(int team,int x,int y) {
		final Chara[] characters = getCharacters_team(team,true);
		double nearstDistance = NONE;
		Chara nearstChara = null;
		for(Chara chara : characters) {
			final double distance = abs(chara.getDistance(x, y));
			if(nearstChara == null || distance < nearstDistance) {
				nearstDistance = distance;
				nearstChara = chara;
			}
		}
		return nearstChara;
		
	}
	public static final int getCharaAmount() {
		return characters.length;
	}
	public static final int getCharaTeam(int charaID) {
		return characters[charaID].getTeam();
	}
	public static final boolean charaIsVisibleFrom(int charaID,int x,int y) {
		return characters[charaID].isVisibleFrom(x, y);
	}
	public static final double getCharaX(int charaID) {
		return characters[charaID].getX();
	}
	public static final double getCharaY(int charaID) {
		return characters[charaID].getY();
	}
	//information-stage
	public static final StageEngine getEngine() {
		return engine;
	}
	public static final Stage getStage() {
		return stage;
	}
	public static final boolean hitLandscape(int x,int y,int w,int h) {
		return stage.hitLandscape(x, y, w, h);
	}
	public static final boolean inStage(int x,int y) {
		return stage.inStage(x, y);
	}
	public static final double flitCodX(double screenX) {
		return viewX - screenX;
	}
	public static final double flitCodY(double screenY) {
		return viewY - screenY;
	}
	//information-team
	public static final boolean isSameTeam(int team1,int team2) {
		return team1 == team2 && team1 != NONE || team1 == ALL || team2 == ALL;
	}
	//information-GUI
	public static final int getScreenW(){
		return screenW;
	}
	public static final int getScreenH(){
		return screenH;
	}
	//information-paint
	public static final boolean inScreen(int x,int y) {
		return abs(viewX - x) < screenW && abs(viewY - y) < screenH;
	}
	//information-resource
	public final Image getImageByID(int imageID) {
		return arrayImage[imageID];
	}

	//control
	//control-viewPoint
	public static void viewMove(int dx,int dy) {
		viewX -= dx;viewY -= dy;
		viewDstX -= dx;viewDstY -= dy;
	}
	public static void viewTo(int x,int y) {
		viewDstX = viewX = -x + screenW/2;
		viewDstY = viewY = -y + screenH/2;//mouseX - (int)viewX
	}
	public static void pureViewMove(int dx,int dy) {
		viewX -= dx;viewY -= dy;
	}
	public static void pureViewTo(int x,int y) {
		viewX = x;viewY = y;
	}
	public static void viewTargetTo(int x,int y) {
		viewDstX = -x + screenW/2;viewDstY =  -y + screenH/2;
	}
	public static void viewTargetMove(int x,int y) {
		viewDstX -= x;viewDstY -= y;
	}
	public static void viewApproach_speed(int speed) {
		if(abs(viewDstX - viewX) < speed)
			viewX = viewDstX;
		else
			viewX += viewX < viewDstX ? speed : -speed;
		if(abs(viewDstY - viewY) < speed)
			viewY = viewDstY;
		else
			viewY += viewY < viewDstY ? speed : -speed;
	}
	public static void viewApproach_rate(double rate) {
		final double DX = (double)(viewDstX - viewX)/rate,DY = (double)(viewDstY - viewY)/rate;
		if(-0.1 < DX && DX < 0.1)
			viewX = viewDstX;
		else
			viewX += DX;
		if(-0.1 < DY && DY < 0.1)
			viewY = viewDstY;
		else
			viewY += DY;
	}
	public static double getViewX() {
		return viewX;
	}
	public static double getViewY() {
		return viewY;
	}
	//control-bullet
	public static final boolean deleteBullet(Bullet bullet) {
		if(bullet.SCRIPT.deleteBullet(bullet))
			return bullets.remove(bullet);
		return false;
	}
	public static final boolean deleteEffect(Effect effect) {
		if(effect.SCRIPT.deleteEffect(effect))
			return effects.remove(effect);
		return false;
	}
	/*public final void rollCharaTurn(){
		if(++chara == characters.length)
			chara = 0;
		characters[chara].turnStarted();
	}*/
	public final void paintHPArc(int x,int y,int radius,int hp,int maxHP) {
		g2.setStroke(stroke3);
		if((double)hp/(double)maxHP > 0.75) //HPが少なくなるごとに色が変化
			g2.setColor(Color.CYAN); //水色
		else if((double)hp/(double)maxHP > 0.50)
			g2.setColor(Color.GREEN); //緑色
		else if((double)hp/(double)maxHP > 0.25)
			g2.setColor(Color.YELLOW); //黄色
		else if((double)hp/(double)maxHP > 0.10 || systemFrame % 4 < 2)
			g2.setColor(Color.RED); //赤色
		else
			g2.setColor(HPWarningColor);
		g2.drawString(String.valueOf(hp),x + (int)(radius*1.1) + (hp >= 10 ? 0 : 6),y + (int)(radius*1.1));
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2.drawArc(x - radius,y - radius,radius*2,radius*2,90,(int)((double)hp/(double)maxHP*360));
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
	}
	public final int receiveDamageInSquare(int team,int hp,int x,int y,int size){ //自処理被弾(弾消滅)
		for(Bullet bullet : bullets){
			if(team != bullet.team && bullet.atk > 0 && squreCollision((int)bullet.x,(int)bullet.y,bullet.SIZE,x,y,size)){ //衝突
				hp -= bullet.atk;
				bullet.SCRIPT.bulletHitObject(bullet);
				if(hp <= 0)
					break;
			}
		}
		return hp; //残ったHPを返す
	}
	public final int receiveBulletInCircle(int hp,int x,int y,int size,int team){ //自処理被弾(弾消滅)
		for(Bullet bullet : bullets){
			if(team != bullet.team && bullet.atk > 0 && circleCollision((int)bullet.x,(int)bullet.y,bullet.SIZE,x,y,size)){ //衝突
				hp -= bullet.atk;
				bullet.SCRIPT.bulletHitObject(bullet);
				if(hp <= 0)
					break;
			}
		}
		return hp; //残ったHPを返す
	}
	public final Bullet searchBulletInSquare_single(int x,int y,int size,int team){
		for(int i = 0;i < bullets.size();i++){
			final Bullet bullet = bullets.get(i);
			if(team != bullet.team && squreCollision((int)bullet.x,(int)bullet.y,bullet.SIZE,x,y,size)) //衝突
				return bullet; //return ID
		}
		return null; //Not found
	}
	public final Bullet[] searchBulletInSquare_multiple(int x,int y,int size,int team){
		Bullet[] foundIDs = new Bullet[bullets.size()];
		int foundAmount =  0;
		for(int i = 0;i < bullets.size();i++){
			final Bullet bullet = bullets.get(i);
			if(team != bullet.team && squreCollision((int)bullet.x,(int)bullet.y,bullet.SIZE,x,y,size)) //衝突
				foundIDs[foundAmount++] = bullet; //record ID
		}
		return Arrays.copyOf(foundIDs,foundAmount);
	}
	public final static boolean squreCollision(int x1,int y1,int size1,int x2,int y2,int size2) {
		final int halfSize = (size1 + size2)/2;
		return abs(x1 - x2) < halfSize && abs(y1 - y2) < halfSize;
	}
	public final static boolean rectangleCollision(int x1,int y1,int w1,int h1,int x2,int y2,int w2,int h2) {
		return abs(x1 - x2) < (w1 + w2)/2 && abs(y1 - y2) < (h1 + h2)/2;
	}
	public final static boolean circleCollision(int x1,int y1,int size1,int x2,int y2,int size2) {
		final double DX = x1 - x1,DY = y1 - y2,RANGE = size1 + size2;
		return DX*DX + DY*DY <= RANGE*RANGE;
	}
	
	//input
	private static int mouseX,mouseY;
	private boolean mouseLeftPress,mouseMiddlePress,mouseRightPress;
	private int mouseLeftPressedFrame,mouseMiddlePressedFrame,mouseRightPressedFrame;
	private int mousePointing = NONE; //マウスがポイントしているもの
	private static int mouse2DragSX,mouse2DragSY;
	
	public void mouseWheelMoved(MouseWheelEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mousePressed(MouseEvent e){
		switch(e.getButton()) {
		case MouseEvent.BUTTON1:
			mouseLeftPress = true;
			mouseLeftPressedFrame = gameFrame;
			break;
		case MouseEvent.BUTTON2:
			mouseMiddlePress = true;
			mouse2DragSX = mouseX - (int)viewX;
			mouse2DragSY = mouseY - (int)viewY;
			break;
		case MouseEvent.BUTTON3:
			mouseRightPress = true;
			mouseRightPressedFrame = gameFrame;
			break;
		}
	}
	public void mouseReleased(MouseEvent e){
		switch(e.getButton()) {
		case MouseEvent.BUTTON1:
			mouseLeftPress = false;
			break;
		case MouseEvent.BUTTON2:
			mouseMiddlePress = false;
			break;
		case MouseEvent.BUTTON3:
			mouseRightPress = false;
			break;
		}
	}
	public void mouseClicked(MouseEvent e){}
	public void mouseMoved(MouseEvent e){
		final int x = e.getX(),y = e.getY();
		mouseX = x;mouseY = y;
	}
	public void mouseDragged(MouseEvent e){
		final int x = e.getX(),y = e.getY();
		mouseX = x;mouseY = y;
		if(mouseMiddlePress) {
			final int newRealX = x - (int)viewX,newRealY = y - (int)viewY;
			viewX += newRealX - mouse2DragSX;
			viewY += newRealY - mouse2DragSY;
			mouse2DragSX = newRealX;
			mouse2DragSY = newRealY;
		}
	}
	public static final int getMouseX(){
		return mouseX - (int)viewX;
	}
	public static final int getMouseY(){
		return mouseY - (int)viewY;
	}
	public static final int getMouseScreenX(){
		return mouseX;
	}
	public static final int getMouseScreenY(){
		return mouseY;
	}
	public final int getMousePressedFrame_left(){
		return mouseLeftPressedFrame;
	}
	public final int getMousePressedFrame_middle(){
		return mouseMiddlePressedFrame;
	}
	public final int getMousePressedFrame_right(){
		return mouseRightPressedFrame;
	}
	public static final boolean isMouseInArea(int x,int y,int w,int h) {
		return abs(x - mouseX + viewX) < w/2 && abs(y - mouseY + viewY) < h/2;
	}
	public final boolean isMouseOnImage(int imgID,int x,int y) {
		final Image img = arrayImage[imgID];
		return isMouseInArea(x,y,img.getWidth(null),img.getHeight(null));
	}
	//僉乕忣曬
	private boolean key_1,key_2,key_3,key_4;
	private long key_1_time,key_2_time,key_3_time,key_4_time;
	private boolean key_W,key_A,key_S,key_D,key_enter;
	private final int[] keyInputFrame = new int[1024];
	public void keyPressed(KeyEvent e){
		final int KEY_CODE = e.getKeyCode();
		keyInputFrame[KEY_CODE] = gameFrame;
		switch(KEY_CODE){
		case VK_1:
			key_1 = true;
			key_1_time = getNowTime();
			break;
		case VK_2:
			key_2 = true;
			key_2_time = getNowTime();
			break;
		case VK_3:
			key_3 = true;
			key_3_time = getNowTime();
			break;
		case VK_4:
			key_4 = true;
			key_4_time = getNowTime();
			break;
		case VK_W:
		case VK_UP:
			key_W = true;
			break;
		case VK_A:
		case VK_LEFT:
			key_A = true;
			break;
		case VK_S:
		case VK_DOWN:
			key_S = true;
			break;
		case VK_D:
		case VK_RIGHT:
			key_D = true;
			break;
		case VK_ENTER:
			key_enter = true;
			break;
		case VK_F3:
			debugMode = !debugMode;
			break;
		case VK_F5:
			freezeScreen = !freezeScreen;
			break;
		}
	}
	public void keyReleased(KeyEvent e){
		switch(e.getKeyCode()){
		case VK_1:
			key_1 = false;
			break;
		case VK_2:
			key_2 = false;
			break;
		case VK_3:
			key_3 = false;
			break;
		case VK_4:
			key_4 = false;
			break;
		case VK_W:
		case VK_UP:
			key_W = false;
			break;
		case VK_A:
		case VK_LEFT:
			key_A = false;
			break;
		case VK_S:
		case VK_DOWN:
			key_S = false;
			break;
		case VK_D:
		case VK_RIGHT:
			key_D = false;
			break;
		case VK_ENTER:
			key_enter = false;
			break;
		}
	}
	public void keyTyped(KeyEvent e){}
	class MyWindowAdapter extends WindowAdapter{
		public void windowClosing(WindowEvent e){
			System.exit(0);
		}
		public void windowDeactivated(WindowEvent e){
			if(mainEvent == BATTLE)
				mainEvent = BATTLE_PAUSE;
		}
	}
	public int getKeyInputFrame(int keyCode) {
		return keyInputFrame[keyCode];
	}
	
	//generation
	public final static void createBullet(DynamInteractable source){ //弾生成
		bullets.add(new Bullet(source));
	}
	public final static void createBullet(DynamInteractable source,int amount) {
		for(int i = 0;i < amount;i++)
			bullets.add(new Bullet(source));
	}
	public final static void createBullet_RoundDesign(DynamInteractable source,int amount,double gunnerX,double gunnerY,double radius){
		final double ANGLE = 2*PI/amount;
		for(int i = 0;i < amount;i++){
			BulletInfo.x = gunnerX + radius*cos(ANGLE*i);
			BulletInfo.y = gunnerY + radius*sin(ANGLE*i);
			createBullet(source);
		}
	}
	public final static void createBullet_BurstDesign(DynamInteractable source,int amount,double gunnerX,double gunnerY,double radius,int speed){
		final double BASE_ANGLE = 2*PI/amount;
		for(int i = 0;i < amount;i++){
			final double ANGLE = BASE_ANGLE*i;
			final double COS_ANGLE = cos(ANGLE),SIN_ANGLE = sin(ANGLE);
			BulletInfo.x = gunnerX + radius*COS_ANGLE;
			BulletInfo.y = gunnerY + radius*SIN_ANGLE;
			BulletInfo.angle = ANGLE;
			BulletInfo.xSpeed = speed*COS_ANGLE;
			BulletInfo.ySpeed = speed*SIN_ANGLE;
			createBullet(source);
		}
	}
	public final static void createEffect(DynamInteractable source){ //弾生成
		effects.add(new Effect(source));
	}
	public final static void createEffect(DynamInteractable source,int amount) {
		for(int i = 0;i < amount;i++)
			effects.add(new Effect(source));
	}
	public final static void createEffect_RoundDesign(DynamInteractable source,int amount,double gunnerX,double gunnerY,double radius){
		final double ANGLE = 2*PI/amount;
		for(int i = 0;i < amount;i++){
			EffectInfo.x = gunnerX + radius*cos(ANGLE*i);
			EffectInfo.y = gunnerY + radius*sin(ANGLE*i);
			createEffect(source);
		}
	}
	public static final int getNowFrame() {
		return gameFrame;
	}
	public static final int getPassedFrame(int frame) {
		return frame == NONE ? NONE : gameFrame - frame;
	}
	public static final boolean isExpired_frame(int initialFrame,int limitFrame) {
		return initialFrame == NONE || (gameFrame - initialFrame) >= limitFrame;
	}
	public static final long getNowTime() {
		return System.currentTimeMillis();
	}
	public static final int getPassedTime(long time) {
		return time == NONE ? NONE : (int)(System.currentTimeMillis() - time);
	}
	public static final boolean isExpired_time(long initialFrame,long limitTime) {
		return initialFrame == NONE || (System.currentTimeMillis() - initialFrame) >= limitTime;
	}
	public static boolean isNoStopEvent() {
		return !freezeScreen && stopEventKind == NONE;
	}
	public static final void prepareBulletInfo() {
		BulletInfo.clear();
		BulletInfo.nowFrame = gameFrame;
	}
	public static final void prepareEffectInfo() {
		EffectInfo.clear();
		EffectInfo.nowFrame = gameFrame;
	}
	public final void addKeyListener(KeyListener e) {
		myFrame.addKeyListener(e);
	}
	
	//event
	public static final void addMessage(MessageSource source,String message) {
		stopEventKind = STOP;
		stopEventReason = MESSAGE;
		messageSource.add(source);
		messageStr.add(message);
		messageEvent.add(THH.NONE);
	}
	public static final void addMessage(MessageSource source,int event,String message) {
		stopEventKind = STOP;
		stopEventReason = MESSAGE;
		messageSource.add(source);
		messageStr.add(message);
		messageEvent.add(event);
	}
	public static final void addControlExpansion(ControlExpansion ctrlEX) {
		thh.addMouseMotionListener(ctrlEX);
		thh.addMouseListener(ctrlEX);
		thh.addMouseWheelListener(ctrlEX);
		thh.myFrame.addKeyListener(ctrlEX);
	}
	
	//premade stage test area
	final private void resetStage(){
		bullets.clear();
		effects.clear();
		messageSource.clear();
		messageStr.clear();
		messageEvent.clear();
		ErrorCounter.clear();
		gameFrame = 0;
		
		System.out.println("add characters to the game");
		characters = engine.charaSetup();
		stage = engine.stageSetup();
		engine.openStage();
	}
	
	//ResourceLoad
	/**
	* 夋憸傪撉傒崬傓婎杮儊僜僢僪偱偡丅assets/image僼僅儖僟偐傜巜掕僷僗傪扵偟傑偡丅僄儔乕偑婲偒偨嵺丄僄儔乕応強偺柤慜傪昞帵偝偣傞偙偲偑偱偒傑偡丅
	* 捛壛僐乕僪側偳偼偙偺儊僜僢僪傪巊偭偰捛壛夋憸傪撉傒崬傑偣傞偙偲偑偱偒傑偡丅
	* @param url 夋憸僷僗
	* @param errorSource 撉傒崬傒僄儔乕偺嵺丄昞帵偝偣傞僄儔乕応強偺柤慜丂(椺:loadImage("img.png","揋夋憸撉傒崬傒")
	* @return 夋憸Image抣
	* @since ~beta7.0
	*/
	public final int loadImage(String url,String errorSource){ //夋憸撉傒崬傒儊僜僢僪
		//old URL
		for(int id = 0;id < arrayImageURL.length;id++){
			if(url.equals(arrayImageURL[id])) //URL登録済み
				return id;
		}
		//new URL
		if(arrayImage_maxID + 1 == arrayImage.length) { //check & expand length
			arrayImage = Arrays.copyOf(arrayImage, arrayImage.length*2);
			arrayImageURL = Arrays.copyOf(arrayImageURL, arrayImageURL.length*2);
		}
		Image img = null;
		try{
			img = createImage((ImageProducer)getClass().getResource("/image/" + url).getContent());
			tracker.addImage(img,1);
		}catch(IOException | NullPointerException e){ //堎忢-撉傒崬傒幐攕
			if(errorSource != null && !errorSource.isEmpty())
				warningBox("画像\"" + url + "\"がロードできませんでした。この画像は描画されません。\n場所：" + errorSource,"読み込みエラー");
			else
				warningBox("画像\"" + url + "\"がロードできませんでした。この画像は描画されません。\n場所：指定なし","読み込みエラー");
			arrayImage[arrayImage_maxID] = createImage(1,1);
		}
		//save URL
		arrayImage_maxID++; //画像URL数を増やす
		if(arrayImage_maxID == arrayImage.length){ //手動配列延長
			arrayImage = Arrays.copyOf(arrayImage,arrayImage_maxID*2);
			arrayImageURL = Arrays.copyOf(arrayImageURL,arrayImage_maxID*2);
		}
		arrayImageURL[arrayImage_maxID] = url; //この画像URLを登録
		arrayImage[arrayImage_maxID] = img; //この画像を登録
		return arrayImage_maxID;
	}
	/**
	* 夋憸傪撉傒崬傓婎杮儊僜僢僪偱偡丅assets/image僼僅儖僟偐傜巜掕僷僗傪扵偟傑偡丅
	* 捛壛僐乕僪側偳偼偙偺儊僜僢僪傪巊偭偰捛壛夋憸傪撉傒崬傑偣傞偙偲偑偱偒傑偡丅
	* 僄儔乕尦偺巜掕傪徣棯偟偨僶乕僕儑儞偱偡丅
	* @param url 夋憸僷僗
	* @return 夋憸Image抣
	* @since ~beta7.0
	*/
	public final int loadImage(String url){ //夋憸撉傒崬傒儊僜僢僪
		return this.loadImage(url,null);
	}
	/**
	* 壒惡傪撉傒崬傓婎杮儊僜僢僪偱偡丅assets/sound僼僅儖僟偐傜巜掕僷僗傪扵偟傑偡丅
	* 捛壛僐乕僪側偳偼偙偺儊僜僢僪傪巊偭偰捛壛壒惡傪撉傒崬傑偣傞偙偲偑偱偒傑偡丅
	* 偙偺偲偒敪惗偟偨僄儔乕偼SoundClip傛傝曬崘偝傟傑偡丅
	* @param url 夋憸僷僗
	* @return 壒惡SoundClip抣
	* @since beta8.0
	*/
	public final int loadSound(String url){ //夋憸撉傒崬傒儊僜僢僪
		//old URL
		for(int id = 0;id < arraySoundURL.length;id++){
			if(url.equals(arraySoundURL[id])) //URL登録済み
				return id;
		}
		//save new URL
		if(arraySound_maxID + 1 == arraySound.length) { //check & expand length
			arraySound = Arrays.copyOf(arraySound, arraySound.length*2);
			arraySoundURL = Arrays.copyOf(arraySoundURL, arraySoundURL.length*2);
		}
		arraySound_maxID++; //URL数を増やす
		if(arraySound_maxID == arraySound.length){ //手動配列延長
			arraySound = Arrays.copyOf(arraySound,arraySound_maxID*2);
			arraySoundURL = Arrays.copyOf(arraySoundURL,arraySound_maxID*2);
		}
		arraySoundURL[arraySound_maxID] = url; //このURLを登録
		arraySound[arraySound_maxID] = new SoundClip("/sound/" + url);
		return arraySound_maxID;
	}
	
	public final Font createFont(String filename){
		try{
			return Font.createFont(Font.TRUETYPE_FONT,getClass().getResourceAsStream("/" + filename));
		}catch (IOException | FontFormatException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public final URL CHARA_DIC_URL = getClass().getResource("../chara");
	public final Chara[] loadAllChara(URL url) {
		final FilenameFilter classFilter = new FilenameFilter(){
			public boolean accept(File dir,String name){
				return name.endsWith(".class");
			}
		};
		File charaFolder = null;
		try{
			charaFolder = new File(url.toURI());
		}catch(URISyntaxException e){
		}catch(NullPointerException e) {
			System.out.println("ファイル構造に問題が発生しています。");
		}
		if(charaFolder == null){
			System.out.println("charaフォルダが見つかりません");
			return new Chara[0];
		}
		int classAmount = 0;
		final Chara[] charaClass = new Chara[64];
		for(String className : charaFolder.list(classFilter)){
			try{
				final Object obj = Class.forName("chara." + className.substring(0,className.length() - 6)).newInstance();
				if(obj instanceof Chara){
					final Chara cls = (Chara)obj;
					cls.loadImageData();
					cls.loadSoundData();
					charaClass[classAmount++] = cls;
				}
			}catch(InstantiationException e) {
				System.out.println("ignored abstract class: " + className);
			}catch(ClassNotFoundException | IllegalAccessException e){
				System.out.println(e);
			}
		}
		return Arrays.copyOf(charaClass,classAmount);
	}
	
	//Paint
	/**
	* 巜掕偟偨夋憸傪THH傊昤夋偡傞婎杮儊僜僢僪偱偡丅
	* @param img 昤夋夋憸
	* @param x 昤夋埵抲x
	* @param y 昤夋埵抲y
	* @since beta8.0
	*/
	public final void drawImageTHH(Image img,int x,int y){
		g2.drawImage(img,x - img.getWidth(null)/2,y - img.getHeight(null)/2,this);
	}
	public final void drawImageTHH(int imgID,int x,int y){
		if(0 <= imgID && imgID < arrayImage.length)
			this.drawImageTHH(arrayImage[imgID],x,y);
	}
	/**
	* 巜掕悺朄偱巜掕偟偨夋憸傪THH傊昤夋偡傞婎杮儊僜僢僪偱偡丅w,h傕愝掕偱偒傑偡丅
	* @param img 昤夋夋憸
	* @param x 昤夋埵抲x
	* @param y 昤夋埵抲y
	* @param w 墶暆
	* @param h 廲暆
	* @since beta8.0
	*/
	public final void drawImageTHH(Image img,int x,int y,int w,int h){
		g2.drawImage(img,x,y,w,h,this);
	}
	public final void drawImageTHH(int imgID,int x,int y,int w,int h){
		this.drawImageTHH(arrayImage[imgID],x,y,w,h);
	}
	/**
	* 巜掕嵗昗傪拞怱偲偟偰巜掕偟偨夋憸傪THH偵昤夋偡傞婎杮儊僜僢僪偱偡丅
	* @param img 昤夋夋憸
	* @param x 昤夋拞怱埵抲x
	* @param y 昤夋拞怱埵抲y
	* @since beta8.0
	*/
	public final void drawImageTHH_center(Image img,int x,int y,double angle){
		g2.rotate(angle,x,y);
		g2.drawImage(img,x - img.getWidth(null)/2,y - img.getHeight(null)/2,this);
		g2.rotate(-angle,x,y);
	}
	public final void drawImageTHH_center(int imgID,int x,int y,double angle) {
		this.drawImageTHH_center(arrayImage[imgID],x,y,angle);
	}
	public final void drawImageTHH_center(Image img,int x,int y){
		g2.drawImage(img,x - img.getWidth(null)/2,y - img.getHeight(null)/2,this);
	}
	public final void drawImageTHH_center(int imgID,int x,int y){
		this.drawImageTHH_center(arrayImage[imgID],x,y);
	}
	public static final Graphics2D getGraphics2D() {
		return thh.g2;
	}
	
	//Sound
	public final void playSound(int soundID) {
		arraySound[soundID].play();
	}
	public final void stopSound(int soundID) {
		arraySound[soundID].stop();
	}
	public final void loopSound(int soundID,int loops) {
		arraySound[soundID].loop(loops);
	}
	
	//special
	public static final boolean isRival(int team1,int team2) {
		return team1 == NONE || team1 != team2;
	}
	//math & string
	public static final double angleFormat(double radian){ //儔僕傾儞惍棟儊僜僢僪 -PI~+PI偵捈偡
		radian %= PI*2;
		if(radian > PI)
			radian -= PI*2;
		else if(radian <= -PI)
			radian += PI*2;
		return radian;
	}
	public static final int[] toIntArray(ArrayList<Integer> arrayList) {
		final Integer[] array = arrayList.toArray(new Integer[0]);
		final int[] result = new int[array.length];
		for(int i = 0;i < array.length;i++) {
			result[i] = array[i];
		}
		return result;
	}
	public static final double[] toDoubleArray(ArrayList<Double> arrayList) {
		final Double[] array = arrayList.toArray(new Double[0]);
		final double[] result = new double[array.length];
		for(int i = 0;i < array.length;i++) {
			result[i] = array[i];
		}
		return result;
	}
	public final static String trim2(String str){
		if(str == null || str.length() == 0)
			return "";
		for(int i = 0;;i++){
			if(i >= str.length())
				return "";
			final char c = str.charAt(i);
			if(c == ' ' || c == '　')
				continue;
			else{
				str = str.substring(i);
				break;
			}
		}
		for(int i = str.length() - 1;i >= 0;i--){
			final char c = str.charAt(i);
			if(c == ' ' || c == '　')
				continue;
			else
				return str.substring(0,i + 1);
		}
		return str;
	}
	/**
	* 文字列をtokenで分割して配列で返す、String.splitメソッドのブレスコ版です。
	* 分割された文字は、さらに前後の半角/全角空白を除去されます。
	* 指定された文字列がnullであったときは空配列が返され、例外は投げません。
	* @param str 分割される文字列
	* @param token 分割に使うトークン
	* @return 分割された文字配列
	* @since beta8.0
	*/
	public final static String[] split2(String str,String token){
		if(!isActualString(str))
			return new String[0];
		final String[] strs = str.split(token);
		for(int i = 0;i < strs.length;i++)
			strs[i] = trim2(strs[i]);
		return strs;
	}
	/**
	* String値が無効値ではないことを検証します。
	*/
	public final static boolean isActualString(String value){
		if(value != null && !value.isEmpty() && !value.equalsIgnoreCase("NONE"))
			return true;
		return false;
	}
	/**
	* String配列が無効値ではないことを検証します。
	*/
	public final static boolean isActualString(String[] value){
		if(value != null && value.length > 0 && !value[0].isEmpty() && !value[0].equalsIgnoreCase("NONE"))
			return true;
		return false;
	}
	/**
	* このint値は特別な意味が含まれない実数値ゾーンであるかを調べます。
	* BreakScopeでは一部の変数に特殊な意味を持たせた数値を代入し、違った挙動を示すような仕組みがあります。
	* (例：gimmickHPは定数MAXのとき破壊不能,定数NONEのとき衝突判定なし)
	* 実数値と混同しないため、このゾーンは限界値付近であることが多いようになっています。
	* @param value 調べる値
	* @return 実数値であるときtrue,そうでなければfalse
	* @since beta8.0
	*/
	public final static boolean isActualNumber(int value){
		switch(value){
		case NONE:
		case MAX:
		case MIN:
		case ConfigLoader.SELF:
		case ConfigLoader.REVERSE_SELF:
		case ConfigLoader.TARGET:
		case ConfigLoader.REVERSE_TARGET:
		case ConfigLoader.FOCUS:
			return false;
		}
		return true;
	}
	/**
	* このdouble値は特別な意味が含まれない実数値ゾーンであるかを調べます。
	* BreakScopeでは一部の変数に特殊な意味を持たせた数値を代入し、違った挙動を示すような仕組みがあります。
	* (例：gimmickHPが定数MAXのとき破壊不能,定数NONEのとき衝突判定なし)
	* 実数値と混同しないため、このゾーンは限界値付近であることが多いようになっています。
	* なお、NaNや[NEGATIVE/POSITIVE]_INFINITYでもfalseが返ってきます。
	* @param value 調べる値
	* @return 実数値とであるときtrue,そうでなければfalse
	* @since beta8.0
	*/
	public final static boolean isActualNumber(double value){
		if(	value == Double.NaN ||
			value == Double.NEGATIVE_INFINITY ||
			value == Double.POSITIVE_INFINITY ||
			!isActualNumber((int)value))
			return false;
		else
			return true;
	}
	/**
	* 度をラジアンへ変換しますが、実数値ではない値を保持します。
	* たとえば、コンフィグ内の角度項目で指定できる"TARGET"や"SELF"などの特殊値はこのメソッドを通しても値は変化しません。
	* 内部的には実際の掛け算で求めています。
	* @param ラジアンに変換する数値
	* @return 変換された値
	* @since beta9.0
	*/
	public final static double toRadians2(double degress){
		if(isActualNumber(degress))
			return degress*PI/180;
		else
			return degress;
	}
	
	public final static int random2(int value1,int value2) {
		if(value1 > value2)
			return new Random().nextInt(abs(value1 - value2)) + value2;
		else
			return new Random().nextInt(abs(value2 - value1)) + value1;
	}
	public final static double random2(double value1,double value2) {
		if(value1 > value2)
			return Math.random()*(value1 - value2) + value2;
		else
			return Math.random()*(value2 - value1) + value1;
	}
	//儊僢僙乕僕僂傿儞僪僂宯
	/**
	* 寈崘僂傿儞僪僂偺昞帵傪峴偄傑偡丅
	*丂傑偨丄偙偺僂傿儞僪僂傪昞帵偟偰偄偨帪娫傪曉偟傑偡丅
	* 偙偺娫偵僀儀儞僩偑僎乕儉偱偁偭偨応崌丄億乕僘夋柺偵堏峴偡傞偙偲偑偁傝傑偡丅
	* 撪晹揑偵偼JOptionPane.showMessageDialog儊僜僢僪傪屇傫偱偄傑偡丅
	* @param message 昞帵偡傞寈崘
	* @param title 僟僀傾儘僌偺僞僀僩儖暥帤楍
	* @return 僟僀傾儘僌偑暵偠傜傟傞傑偱偺宱夁帪娫
	* @since beta9.0
	*/
	public static final long warningBox(String message,String title){
		long openTime = System.currentTimeMillis();
		JOptionPane.showMessageDialog(null,message,title,JOptionPane.WARNING_MESSAGE);
		return System.currentTimeMillis() - openTime;
	}
}