package core;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import static java.awt.event.KeyEvent.*;
import javax.swing.*;

import bullet.Bullet;
import effect.Effect;
import engine.Engine_THH1;
import stage.ControlExpansion;
import stage.Stage;
import stage.StageEngine;
import unit.Unit;

import java.io.*;
import java.net.*;
import java.text.DecimalFormat;
import java.util.*;
import static java.lang.Math.*;

/**
 * 
 * @author bluelaserpointer
 * @version alpha1.0
 *
 */
public final class GHQ extends JPanel implements MouseListener,MouseMotionListener,MouseWheelListener,KeyListener,Runnable{
	private static final long serialVersionUID = 123412351L;

	private static GHQ hq;
	
	static final String
		GAME_VERSION = "Ver beta1.0.0";
	public static final int
		NONE = -999999999,
		ALL = -NONE,
		MAX = Integer.MAX_VALUE,
		MIN = Integer.MIN_VALUE;
	public static final String
		NOT_NAMED = "<Not Named>";
	
	//File Pass
	public final URL CHARA_DIC_URL = getClass().getResource("../chara");
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
	
	//event
	public final int 
		OPENING = 1000,
		TITLE = 2000,
		LOAD = 3000,
		SAVE = 4000,
		OPTION = 5000,OPTION_sound = 5100,OPTION_grahics = 5200,
		ACTION_PART = 6000,
		BATTLE = 7000,BATTLE_PAUSE = 7001,
		EVENT_PART = 8000;
	private int mainEvent = OPENING;
	
	//stopEvent
	private static int stopEventKind = NONE;
	public static final int STOP = 0,NO_ANM_STOP = 1;
	private static boolean messageStop,spellStop;
	
	//screen
	private final int defaultScreenW = 1000,defaultScreenH = 600;
	private static int screenW = 1000,screenH = 600;
	private static double viewX,viewY,viewDstX,viewDstY;

	//page
	private int page,page_max;
	
	//frame
	private int clearFrame;
	private static int systemFrame;
	private static int gameFrame;
	
	//stage
	private static StageEngine engine;
	private static Stage stage;
	private static ControlExpansion ctrlEx;

	//character data
	private static Unit[] characters = new Unit[0];
	
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
	private static Image[] arrayImage = new Image[128];
	private static URL[] arrayImageURL = new URL[128];
	private static int arrayImage_maxID = -1;
	private static SoundClip[] arraySound = new SoundClip[128];
	private static String[] arraySoundURL = new String[128];
	private static int arraySound_maxID = -1;
	
	//Initialize methods/////////////
	public static StageEngine loadEngine(String fileURL) {
		StageEngine result = null;
		try{
			final Object obj = Class.forName(fileURL).newInstance(); //インスタンスを生成
			if(obj instanceof StageEngine)
				result =  (StageEngine)obj; //インスタンスを保存
		}catch(ClassNotFoundException | InstantiationException | IllegalAccessException e){
			System.out.println(e);
			result = new Engine_THH1();
		}
		return result;
	}
	
	private boolean loadComplete;
	private final JFrame myFrame;
	private final MediaTracker tracker;
	public GHQ(StageEngine engine){
		GHQ.engine = engine;
		ctrlEx = engine.getCtrl_ex();
		hq = this;
		StageEngine.thh = hq;
		final long loadTime = System.currentTimeMillis();
		//window setup
		myFrame = new JFrame(engine.getTitleName());
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
		//image & sound length fit
		arrayImage = Arrays.copyOf(arrayImage, arrayImage_maxID + 1);
		arraySound = Arrays.copyOf(arraySound, arrayImage_maxID + 1);
		try{
			tracker.waitForAll();
		}catch(InterruptedException | NullPointerException e){}
		
		System.out.println("loadTimeReslut: " + (System.currentTimeMillis() - loadTime));
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
				if(!freezeScreen) {
					repaint();
					systemFrame++;
					if(stopEventKind == NONE)
						gameFrame++;
				}
			}
		//}catch(Exception e){
			//JOptionPane.showMessageDialog(null, "申し訳ありませんが、エラーが発生しました。\nエラーコード：" + e.toString(),"エラー",JOptionPane.ERROR_MESSAGE);
		//}
	}
	
	private final BufferedImage offImage = new BufferedImage(defaultScreenW,defaultScreenH,BufferedImage.TYPE_INT_ARGB_PRE); //ダブルバッファキャンバス
	private Graphics2D g2;
	private final Font basicFont = createFont("font/upcibi.ttf").deriveFont(Font.BOLD + Font.ITALIC,30.0f),commentFont = createFont("font/HGRGM.TTC").deriveFont(Font.PLAIN,15.0f);
	public static final BasicStroke stroke1 = new BasicStroke(1f),stroke3 = new BasicStroke(3f),stroke5 = new BasicStroke(5f);
	private static final Color HPWarningColor = new Color(255,120,120),debugTextColor = new Color(200,200,200,160);
	private final DecimalFormat DF00_00 = new DecimalFormat("00.00");
	private final Rectangle2D screenRect = new Rectangle2D.Double(0,0,defaultScreenW,defaultScreenH);
	
	public void paintComponent(Graphics g){
		final long LOAD_TIME_PAINTCOMPONENT = System.currentTimeMillis();
		final int MOUSE_X = GHQ.mouseX,MOUSE_Y = GHQ.mouseY;
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
		final double TRANSLATE_X_double = viewX,TRANSLATE_Y_double = viewY;
		final int TRANSLATE_X = (int)TRANSLATE_X_double,TRANSLATE_Y = (int)TRANSLATE_Y_double;
		g2.translate(TRANSLATE_X_double, TRANSLATE_Y_double);
		////////////////////////////////////////////////////////////////////////
		//stageAction
		engine.idle(g2,stopEventKind);
		////////////////////////////////////////////////////////////////////////
		//GUIAction
		g2.translate(-TRANSLATE_X_double, -TRANSLATE_Y_double);
		{
			//message system///////////////
			if(messageStop) {
				if(messageStr.size() > 0) {
					if(messageIterator > messageStr.getFirst().length() - 1){
						if(key_enter) { //next message order
							messageStr.remove();
							final int EVENT = messageEvent.remove();
							if(EVENT != NONE)
								messageSource.remove().eventNotice(EVENT);
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
					stopEventKind = NONE;
					messageStop = false;
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
				//stageEdge
				g2.setStroke(stroke3);
				final int STAGE_W = stage.getStageW(),STAGE_H = stage.getStageH();
				{ //LXRX lines
					final int LX = TRANSLATE_X,RX = TRANSLATE_X + STAGE_W;
					for(int i = 0;i < 50;i++) {
						final int Y = TRANSLATE_Y + STAGE_H*i/50;
						g2.drawLine(LX - 20, Y - 20,LX + 20, Y + 20);
						g2.drawLine(RX - 20, Y - 20,RX + 20, Y + 20);
					}
				}
				{ //LYRY lines
					final int LY = TRANSLATE_Y,RY = TRANSLATE_Y + STAGE_H;
					for(int i = 0;i < 50;i++) {
						final int X = TRANSLATE_X + STAGE_W*i/50;
						g2.drawLine(X - 20, LY - 20, X + 20, LY + 20);
						g2.drawLine(X - 20, RY - 20, X + 20, RY + 20);
					}
				}
				//entityInfo
				g2.drawString("Chara:" + characters.length + " EF:" + effects.size() + " B:" + bullets.size(),30,100);
				g2.drawString("LoadTime(ms):" + loadTime_total,30,120);
				//g2.drawString("EM:" + loadTime_enemy + " ET:" + loadTime_entity + " G:" + loadTime_gimmick + " EF:" + loadTime_effect + " B:" + loadTime_bullet + " I:" + loadTime_item + " W: " + loadTime_weapon + " Other: " + loadTime_other,30,140);
				g2.drawString("GameTime(ms):" + gameFrame,30,160);
				//mouseInfo
				g2.setColor(debugTextColor);
				g2.setStroke(stroke5);
				g2.drawString((int)MOUSE_X + "," + (int)MOUSE_Y,MOUSE_X + 20,MOUSE_Y + 20);
				g2.setStroke(stroke1);
				g2.drawLine(MOUSE_X - 15, MOUSE_Y, MOUSE_X + 15, MOUSE_Y);
				g2.drawLine(MOUSE_X, MOUSE_Y - 15, MOUSE_X, MOUSE_Y + 15);
				g2.setStroke(stroke5);
				g2.drawString("(" + (MOUSE_X - (int)viewX) + "," + (MOUSE_Y - (int)viewY) + ")",MOUSE_X + 20,MOUSE_Y + 40);
				//charaInfo
				for(Unit chara : characters) {
					final int RECT_X = (int)chara.getDynam().getX() + (int)viewX,RECT_Y = (int)chara.getDynam().getY() + (int)viewY;
					g2.setStroke(stroke1);
					g2.drawRect(RECT_X - 50, RECT_Y - 50, 100,100);
					g2.drawLine(RECT_X + 50, RECT_Y - 50, RECT_X + 60, RECT_Y - 60);
					g2.setStroke(stroke5);
					g2.drawString(chara.getName(), RECT_X + 62, RECT_Y - 68);
				}
			}
		}
		g.drawImage(offImage,0,0,screenW,screenH,this);
		loadTime_total = System.currentTimeMillis() - LOAD_TIME_PAINTCOMPONENT;
	}
	
	public static final Unit[] callBulletEngage(Bullet bullet) {
		return engine.callBulletEngage(getCharacters_team(bullet.team,false),bullet);
	}
	public static final Unit[] callBulletEngage(Unit[] characters,Bullet bullet) {
		return engine.callBulletEngage(characters,bullet);
	}
	//idle-character
	public static final void defaultCharaIdle(Unit[] characters) {
		switch(stopEventKind) {
		case STOP:
			for(Unit chara : characters)
				chara.idle(Unit.PASSIVE_CONS);
			break;
		case NO_ANM_STOP:
			for(Unit chara : characters)
				chara.idle(Unit.PAINT_FREEZED);
			break;
		default:
			for(Unit chara : characters) {
				chara.defaultIdle();
				chara.resetSingleOrder();
			}
		}
	}
	public static final void defaultCharaIdle(ArrayList<Unit> characters) {
		switch(stopEventKind) {
		case STOP:
			for(Unit chara : characters)
				chara.idle(Unit.PASSIVE_CONS);
			break;
		case NO_ANM_STOP:
			for(Unit chara : characters)
				chara.idle(Unit.PAINT_FREEZED);
			break;
		default:
			for(Unit chara : characters) {
				chara.defaultIdle();
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
				bullet.SCRIPT.bulletPaint(bullet);
			}
			while(effects.hasNext()) {
				final Effect effect = effects.next();
				effect.SCRIPT.effectPaint(effect);
			}
			break;
		case NO_ANM_STOP:
			while(bullets.hasNext()) {
				final Bullet bullet = bullets.next();
				bullet.SCRIPT.bulletNoAnmPaint(bullet);
			}
			while(effects.hasNext()) {
				final Effect effect = effects.next();
				effect.SCRIPT.effectNoAnmPaint(effect);
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
	public static final void defaultCharaIdle(Unit chara) {
		switch(stopEventKind) {
		case STOP:
			chara.idle(Unit.PASSIVE_CONS);
			break;
		case NO_ANM_STOP:
			chara.idle(Unit.PAINT_FREEZED);
			break;
		default:
			chara.defaultIdle();
			chara.resetSingleOrder();
		}
	}
	//information-characters
	public static final Unit getChara(String name) {
		for(Unit chara : characters) {
			if(chara.getName() == name)
				return chara;
		}
		return null;
	}
	public static final Unit[] getCharacters() {
		return characters;
	}
	public static final Unit getCharacters(int charaID) {
		return characters[charaID];
	}
	public static final Unit[] getCharacters_team(int team,boolean white) {
		final Unit[] charaArray = new Unit[characters.length];
		int founded = 0;
		for(Unit chara : characters) {
			if(white == isSameTeam(team,chara.getTeam()))
				charaArray[founded++] = chara;
		}
		return Arrays.copyOf(charaArray, founded);
	}
	public static final Unit[] getCharacters_team(int team) {
		return getCharacters_team(team,true);
	}
	public static final Unit getNearstEnemy(int team,int x,int y) {
		double nearstDistance = NONE;
		Unit nearstChara = null;
		for(Unit enemy : getCharacters_team(team,true)) {
			if(!enemy.isAlive())
				continue;
			final double distance = abs(enemy.getDynam().getDistance(x, y));
			if(nearstChara == null || distance < nearstDistance) {
				nearstDistance = distance;
				nearstChara = enemy;
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
	public static final Unit[] getVisibleEnemies(Unit chara) {
		final ArrayList<Unit> visibleEnemies = new ArrayList<Unit>();
		for(Unit enemy : getCharacters_team(chara.getTeam(),false)) {
			if(!enemy.isAlive())
				continue;
			if(enemy.dynam.isVisible(chara))
				visibleEnemies.add(enemy);
		}
		return visibleEnemies.toArray(new Unit[0]);
	}
	public static final Unit getNearstVisibleEnemy(Unit chara) {
		final Dynam DYNAM = chara.dynam;
		Unit neastVisibleEnemy = null;
		double enemyDistance = MAX;
		for(Unit enemy : getCharacters_team(chara.getTeam(),false)) {
			if(!enemy.isAlive())
				continue;
			if(enemyDistance == MAX) {
				if(DYNAM.isVisible(enemy)) {
					neastVisibleEnemy = enemy;
					enemyDistance = DYNAM.getDistance(enemy);
				}
			}else {
				final double DISTANCE = DYNAM.getDistance(enemy);
				if(enemyDistance > DISTANCE && DYNAM.isVisible(enemy)) {
					neastVisibleEnemy = enemy;
					enemyDistance = DISTANCE;
				}
			}
		}
		return neastVisibleEnemy;
	}
	public static final Unit getNearstVisibleEnemy(Bullet bullet) {
		final Dynam DYNAM = bullet.dynam;
		Unit neastVisibleEnemy = null;
		double enemyDistance = MAX;
		for(Unit enemy : getCharacters_team(bullet.team,false)) {
			if(!enemy.isAlive())
				continue;
			if(enemyDistance == MAX) {
				if(DYNAM.isVisible(enemy)) {
					neastVisibleEnemy = enemy;
					enemyDistance = DYNAM.getDistance(enemy);
				}
			}else {
				final double DISTANCE = DYNAM.getDistance(enemy);
				if(enemyDistance > DISTANCE && DYNAM.isVisible(enemy)) {
					neastVisibleEnemy = enemy;
					enemyDistance = DISTANCE;
				}
			}
		}
		return neastVisibleEnemy;
	}
	public static final double getCharaX(int charaID) {
		return characters[charaID].getDynam().getX();
	}
	public static final double getCharaY(int charaID) {
		return characters[charaID].getDynam().getY();
	}
	//information-stage
	public static final StageEngine getEngine() {
		return engine;
	}
	public static final Stage getStage() {
		return stage;
	}
	public static final boolean hitLandscape(int x,int y,int size) {
		if(size <= 0)
			return false;
		if(size <= 2)
			return stage.hitLandscape(x, y, 1, 1);
		final int HALF_SIZE = size/2;
		return stage.hitLandscape(x, y, HALF_SIZE, HALF_SIZE);
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
	public static final Image getImageByID(int imageID) {
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
		viewDstY = viewY = -y + screenH/2;
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
	public static final void paintHPArc(int x,int y,int radius,int hp,int maxHP) {
		final Graphics2D G2 = hq.g2;
		G2.setStroke(stroke3);
		if((double)hp/(double)maxHP > 0.75)
			G2.setColor(Color.CYAN);
		else if((double)hp/(double)maxHP > 0.50)
			G2.setColor(Color.GREEN);
		else if((double)hp/(double)maxHP > 0.25)
			G2.setColor(Color.YELLOW);
		else if((double)hp/(double)maxHP > 0.10 || gameFrame % 4 < 2)
			G2.setColor(Color.RED);
		else
			G2.setColor(HPWarningColor);
		G2.drawString(String.valueOf(hp),x + (int)(radius*1.1) + (hp >= 10 ? 0 : 6),y + (int)(radius*1.1));
		G2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		G2.drawArc(x - radius,y - radius,radius*2,radius*2,90,(int)((double)hp/(double)maxHP*360));
		G2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
	}
	public final int receiveDamageInSquare(int team,int hp,int x,int y,int size){
		for(Bullet bullet : bullets){
			if(team != bullet.team && bullet.atk > 0 && bullet.dynam.squreCollision(x,y,(bullet.SIZE + size)/2)){
				hp -= bullet.atk;
				bullet.SCRIPT.bulletHitObject(bullet);
				if(hp <= 0)
					break;
			}
		}
		return hp;
	}
	public final int receiveBulletInCircle(int hp,int x,int y,int size,int team){
		for(Bullet bullet : bullets){
			if(team != bullet.team && bullet.atk > 0 && circleCollision((int)bullet.dynam.getX(),(int)bullet.dynam.getY(),bullet.SIZE,x,y,size)){ //ﾐnﾍｻ
				hp -= bullet.atk;
				bullet.SCRIPT.bulletHitObject(bullet);
				if(hp <= 0)
					break;
			}
		}
		return hp; //ｲﾐ､ﾃ､ｿHP､ｵ､ｹ
	}
	public final Bullet searchBulletInSquare_single(int x,int y,int size,int team){
		for(int i = 0;i < bullets.size();i++){
			final Bullet bullet = bullets.get(i);
			if(team != bullet.team && bullet.dynam.squreCollision(x,y,(bullet.SIZE + size)/2))
				return bullet; //return ID
		}
		return null; //Not found
	}
	public final Bullet[] searchBulletInSquare_multiple(int x,int y,int size,int team){
		Bullet[] foundIDs = new Bullet[bullets.size()];
		int foundAmount =  0;
		for(int i = 0;i < bullets.size();i++){
			final Bullet bullet = bullets.get(i);
			if(team != bullet.team && bullet.dynam.squreCollision(x,y,(bullet.SIZE + size)/2))
				foundIDs[foundAmount++] = bullet; //record ID
		}
		return Arrays.copyOf(foundIDs,foundAmount);
	}
	public static final boolean squreCollisionII(int x1,int y1,int size1,int x2,int y2,int size2) {
		final int halfSize = (size1 + size2)/2;
		return abs(x1 - x2) < halfSize && abs(y1 - y2) < halfSize;
	}
	public static final boolean rectangleCollision(int x1,int y1,int w1,int h1,int x2,int y2,int w2,int h2) {
		return abs(x1 - x2) < (w1 + w2)/2 && abs(y1 - y2) < (h1 + h2)/2;
	}
	public static final boolean circleCollision(int x1,int y1,int size1,int x2,int y2,int size2) {
		final double DX = x1 - x1,DY = y1 - y2,RANGE = size1 + size2;
		return DX*DX + DY*DY <= RANGE*RANGE;
	}
	
	//input
	private static int mouseX,mouseY;
	private int mousePointing = NONE;
	
	public void mouseWheelMoved(MouseWheelEvent e){
		ctrlEx.mouseWheelMoved(e);
		}
	public void mouseEntered(MouseEvent e){
		ctrlEx.mouseEntered(e);
		}
	public void mouseExited(MouseEvent e){
		ctrlEx.mouseExited(e);
		}
	public void mousePressed(MouseEvent e){
		ctrlEx.mousePressed(e);
	}
	public void mouseReleased(MouseEvent e){
		ctrlEx.mouseReleased(e);
	}
	public void mouseClicked(MouseEvent e){
		ctrlEx.mouseClicked(e);
		}
	public void mouseMoved(MouseEvent e){
		final int x = e.getX(),y = e.getY();
		mouseX = x;mouseY = y;
		ctrlEx.mouseMoved(e);
	}
	public void mouseDragged(MouseEvent e){
		final int x = e.getX(),y = e.getY();
		mouseX = x;mouseY = y;
		ctrlEx.mouseDragged(e);
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
	public static final boolean isMouseInArea(int x,int y,int w,int h) {
		return abs(x - mouseX + viewX) < w/2 && abs(y - mouseY + viewY) < h/2;
	}
	public final boolean isMouseOnImage(int imgID,int x,int y) {
		final Image img = arrayImage[imgID];
		return isMouseInArea(x,y,img.getWidth(null),img.getHeight(null));
	}
	//キー情報
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
		ctrlEx.keyPressed(e);
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
		ctrlEx.keyReleased(e);
	}
	public void keyTyped(KeyEvent e){
		ctrlEx.keyTyped(e);
	}
	private final class MyWindowAdapter extends WindowAdapter{
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
	
	//control
	public static final void freezeScreen() {
		freezeScreen = true;
	}
	public static final void stopScreen() {
		stopEventKind = STOP;
	}
	public static final void stopScreen_noAnm() {
		stopEventKind = NO_ANM_STOP;
	}
	public static final void clearStopEvent() {
		stopEventKind = NONE;
	}
	
	//generation
	public static final Bullet createBullet(DynamInteractable source){
		final Bullet bullet = new Bullet(source);
		bullets.add(bullet);
		return bullet;
	}
	public static final Bullet createBullet(Bullet sampleBullet){
		final Bullet bullet = new Bullet(sampleBullet);
		bullets.add(bullet);
		return bullet;
	}
	public static final Effect createEffect(DynamInteractable source){
		final Effect effect = new Effect(source);
		effects.add(effect);
		return effect;
	}
	public static final Effect createEffect(Effect sampleEffect){
		final Effect effect = new Effect(sampleEffect);
		effects.add(effect);
		return effect;
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
	public static boolean isFreezeScreen() {
		return freezeScreen;
	}
	public final void addKeyListener(KeyListener e) {
		myFrame.addKeyListener(e);
	}
	
	//event
	public static final void addMessage(MessageSource source,String message) {
		stopEventKind = STOP;
		messageStop = true;
		messageSource.add(source);
		messageStr.add(message);
		messageEvent.add(GHQ.NONE);
	}
	public static final void addMessage(MessageSource source,int event,String message) {
		stopEventKind = STOP;
		messageStop = true;
		messageSource.add(source);
		messageStr.add(message);
		messageEvent.add(event);
	}
	
	//premade stage test area
	final private void resetStage(){
		bullets.clear();
		effects.clear();
		Bullet.nowMaxUniqueID = Effect.nowMaxUniqueID = -1;
		messageSource.clear();
		messageStr.clear();
		messageEvent.clear();
		ErrorCounter.clear();
		gameFrame = 0;
		System.gc();
		System.out.println("add characters to the game");
		engine.loadResource();
		characters = engine.charaSetup();
		stage = engine.stageSetup();
		engine.openStage();
	}
	
	//ResourceLoad
	/**
	* 
	* @param url 
	* @param errorSource 
	* @return ImageID
	* @since alpha1.0
	*/
	public static final int loadImage(URL url,String errorSource){ //画像読み込みメソッド
		//old URL
		for(int id = 0;id < arrayImageURL.length;id++){
			if(url.equals(arrayImageURL[id])) //URLｵﾇ乕徃､ﾟ
				return id;
		}
		//new URL
		if(arrayImage_maxID + 1 == arrayImage.length) { //check & expand length
			arrayImage = Arrays.copyOf(arrayImage, arrayImage.length*2);
			arrayImageURL = Arrays.copyOf(arrayImageURL, arrayImageURL.length*2);
		}
		Image img = null;
		try{
			img = hq.createImage((ImageProducer)url.getContent());
			hq.tracker.addImage(img,1);
		}catch(IOException | NullPointerException e){ //異常-読み込み失敗
			if(errorSource != null && !errorSource.isEmpty())
				warningBox("Image " + url + " is not found and could not be loaded.Error code: " + errorSource,"ImageLoadingError");
			else
				warningBox("Image " + url + " is not found and could not be loaded.","ImageLoadingError");
			arrayImage[arrayImage_maxID] = hq.createImage(1,1);
		}
		//save URL
		arrayImage_maxID++; //ｻｭﾏRLﾊ�､愛荀ｹ
		if(arrayImage_maxID == arrayImage.length){ //ﾊﾖ�ﾓﾅ菽ﾐﾑﾓ餃
			arrayImage = Arrays.copyOf(arrayImage,arrayImage_maxID*2);
			arrayImageURL = Arrays.copyOf(arrayImageURL,arrayImage_maxID*2);
		}
		arrayImageURL[arrayImage_maxID] = url; //､ｳ､ﾎｻｭﾏRL､ﾇ乕
		arrayImage[arrayImage_maxID] = img; //､ｳ､ﾎｻｭﾏﾇ乕
		return arrayImage_maxID;
	}
	public static final int loadImage(URL url) {
		return loadImage(url,null);
	}
	public static final int loadImage(String urlStr,String errorSource) {
		return loadImage(hq.getClass().getResource("/image/" + urlStr),errorSource);
	}
	/**
	* 
	* @param url 
	* @return ImageID
	* @since alpha1.0
	*/
	public static final int loadImage(String urlStr){ //画像読み込みメソッド
		return loadImage(urlStr,null);
	}
	/**
	* ･ｵ･ｦ･ﾉ･ﾕ･｡･､･�､i､ﾟﾞz､爭皈ｽ･ﾃ･ﾉ､ﾇ､ｹ
	* @param url ･ｵ･ｦ･ﾉ･ﾕ･｡･､･�ﾃ�
	* @return ｽyｺﾏｻｯSoundClipID
	* @since alpha1.0
	*/
	public final int loadSound(String url){ //画像読み込みメソッド
		//old URL
		for(int id = 0;id < arraySoundURL.length;id++){
			if(url.equals(arraySoundURL[id])) //URLｵﾇ乕徃､ﾟ
				return id;
		}
		//save new URL
		if(arraySound_maxID + 1 == arraySound.length) { //check & expand length
			arraySound = Arrays.copyOf(arraySound, arraySound.length*2);
			arraySoundURL = Arrays.copyOf(arraySoundURL, arraySoundURL.length*2);
		}
		arraySound_maxID++; //URLﾊ�､愛荀ｹ
		if(arraySound_maxID == arraySound.length){ //ﾊﾖ�ﾓﾅ菽ﾐﾑﾓ餃
			arraySound = Arrays.copyOf(arraySound,arraySound_maxID*2);
			arraySoundURL = Arrays.copyOf(arraySoundURL,arraySound_maxID*2);
		}
		arraySoundURL[arraySound_maxID] = url; //､ｳ､ﾎURL､ﾇ乕
		arraySound[arraySound_maxID] = new SoundClip("/sound/" + url);
		return arraySound_maxID;
	}

	/**
	* ･ﾕ･ｩ･ﾈ､i､ﾟﾞz､爭皈ｽ･ﾃ･ﾉ､ﾇ､ｹ
	* @param url ･ﾕ･ｩ･ﾈ･ﾕ･｡･､･�ﾃ�
	* @return Font
	* @since alpha1.0
	*/
	public final Font createFont(String filename){
		try{
			return Font.createFont(Font.TRUETYPE_FONT,getClass().getResourceAsStream("/" + filename));
		}catch (IOException | FontFormatException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public final Unit[] loadAllChara(URL url) {
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
			System.out.println("･ﾕ･｡･､･��欽�､ﾋ�矢}､ｬｰkﾉ妤ｷ､ﾆ､､､ﾞ､ｹ｡｣");
		}
		if(charaFolder == null){
			System.out.println("chara･ﾕ･ｩ･�･ﾀ､ｬﾒ侃ﾄ､ｫ､熙ﾞ､ｻ､�");
			return new Unit[0];
		}
		int classAmount = 0;
		final Unit[] charaClass = new Unit[64];
		for(String className : charaFolder.list(classFilter)){
			try{
				final Object obj = Class.forName("chara." + className.substring(0,className.length() - 6)).newInstance();
				if(obj instanceof Unit){
					final Unit cls = (Unit)obj;
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
	
	//PaintTool
	public static final void translateForGUI(boolean forGUI) {
		if(forGUI)
			hq.g2.translate(-viewX,-viewY);
		else
			hq.g2.translate(viewX,viewY);
	}
	//Paint
	/**
	* x,y､ｬｻｭﾏﾎﾗﾏｽﾇ､ﾈ､ﾊ､�､隍ｦ､ﾋﾃ霆ｭ､ｷ､ﾞ､ｹ｡｣
	* @param img ｻｭﾏ�
	* @param x ﾖﾐﾐﾄxﾗﾋ
	* @param y ﾖﾐﾐﾄyﾗﾋ
	* @param w ｺ盥�
	* @param h ﾁ｢ｷ�
	* @since alpha1.0
	*/
	public static final void drawImageTHH(Image img,int x,int y,int w,int h){
		hq.g2.drawImage(img,x - w/2,y - h/2,w,h,hq);
	}
	/**
	* x,y､ｬｻｭﾏﾎﾗﾏｽﾇ､ﾈ､ﾊ､�､隍ｦ､ﾋﾃ霆ｭ､ｷ､ﾞ､ｹ｡｣
	* @param imgID ｻｭﾏD
	* @param x ﾖﾐﾐﾄxﾗﾋ
	* @param y ﾖﾐﾐﾄyﾗﾋ
	* @param w ｺ盥�
	* @param h ﾁ｢ｷ�
	* @since alpha1.0
	*/
	public static final void drawImageTHH(int imgID,int x,int y,int w,int h){
		drawImageTHH(arrayImage[imgID],x - w/2,y - h/2,w,h);
	}
	/**
	* x,y､ｬｻｭﾏﾎﾖﾐﾐﾄ､ﾈ､ﾊ､�､隍ｦ､ﾋﾃ霆ｭ､ｷ｡｢､ｫ､ﾄﾖｸｶｨｽﾇｶﾈ､ﾇｻﾘﾜ椄ｵ､ｻ､ﾞ､ｹ｡｣
	* @param img ｻｭﾏ�
	* @param x ﾖﾐﾐﾄxﾗﾋ
	* @param y ﾖﾐﾐﾄyﾗﾋ
	* @param angle ｻﾘﾜ椰ﾇｶﾈ
	* @since alpha1.0
	*/
	public static final void drawImageTHH_center(Image img,int x,int y,double angle){
		if(img == null)
			return;
		final Graphics2D G2 = hq.g2;
		if(angle != 0.0) {
			G2.rotate(angle,x,y);
			G2.drawImage(img,x - img.getWidth(null)/2,y - img.getHeight(null)/2,hq);
			G2.rotate(-angle,x,y);
		}else
			G2.drawImage(img,x - img.getWidth(null)/2,y - img.getHeight(null)/2,hq);
	}
	/**
	* x,y､ｬｻｭﾏﾎﾖﾐﾐﾄ､ﾈ､ﾊ､�､隍ｦ､ﾋﾃ霆ｭ､ｷ｡｢､ｫ､ﾄﾖｸｶｨｽﾇｶﾈ､ﾇｻﾘﾜ椄ｵ､ｻ､ﾞ､ｹ｡｣
	* @param imgID ｻｭﾏD
	* @param x ﾖﾐﾐﾄxﾗﾋ
	* @param y ﾖﾐﾐﾄyﾗﾋ
	* @param angle ｻﾘﾜ椰ﾇｶﾈ
	* @since alpha1.0
	*/
	public static final void drawImageTHH_center(int imgID,int x,int y,double angle) {
		drawImageTHH_center(arrayImage[imgID],x,y,angle);
	}
	/**
	* x,y､ｬｻｭﾏﾎﾖﾐﾐﾄ､ﾈ､ﾊ､�､隍ｦ､ﾋﾃ霆ｭ､ｷ､ﾞ､ｹ｡｣
	* @param img ｻｭﾏ�
	* @param x ﾖﾐﾐﾄxﾗﾋ
	* @param y ﾖﾐﾐﾄyﾗﾋ
	* @since alpha1.0
	*/
	public static final void drawImageTHH_center(Image img,int x,int y){
		hq.g2.drawImage(img,x - img.getWidth(null)/2,y - img.getHeight(null)/2,hq);
	}
	/**
	* x,y､ｬｻｭﾏﾎﾖﾐﾐﾄ､ﾈ､ﾊ､�､隍ｦ､ﾋﾃ霆ｭ､ｷ､ﾞ､ｹ｡｣
	* @param imgID ｻｭﾏD
	* @param x ﾖﾐﾐﾄxﾗﾋ
	* @param y ﾖﾐﾐﾄyﾗﾋ
	* @since alpha1.0
	*/
	public static final void drawImageTHH_center(int imgID,int x,int y){
		drawImageTHH_center(arrayImage[imgID],x,y);
	}
	public static final void setImageAlpha() {
		hq.g2.setComposite(AlphaComposite.SrcOver);
	}
	public static final void setImageAlpha(float alpha) {
		hq.g2.setComposite(AlphaComposite.SrcOver.derive(alpha));
	}
	public static final Graphics2D getGraphics2D() {
		return hq.g2;
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
	public static final double angleFormat(double radian){ //ラジアン整理メソッド -PI~+PIに直す
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
	public static final String trim2(String str){
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
	* ﾎﾄﾗﾖﾁﾐ､oken､ﾇｷﾖｸ釥ｷ､ﾆﾅ菽ﾐ､ﾇｷｵ､ｹ｡｢String.split･皈ｽ･ﾃ･ﾉ､ﾎTHHｰ讀ﾇ､ｹ｡｣
	* ｷﾖｸ釥ｵ､�､ｿﾎﾄﾗﾖ､ﾏ｡｢､ｵ､鬢ﾋﾇｰ矣､ﾎｰ�ｽﾇ/ﾈｫｽﾇｿﾕｰﾗ､�ﾈ･､ｵ､�､ﾞ､ｹ｡｣
	* ﾖｸｶｨ､ｵ､�､ｿﾎﾄﾗﾖﾁﾐ､ｬnull､ﾇ､｢､ﾃ､ｿ､ﾈ､ｭ､ﾏｿﾕﾅ菽ﾐ､ｬｷｵ､ｵ､�｡｢ﾀ�ﾍ筅ﾏﾍｶ､ｲ､ﾞ､ｻ､｣
	* @param str ｷﾖｸ釥ｵ､�､�ﾎﾄﾗﾖﾁﾐ
	* @param token ｷﾖｸ釥ﾋﾊｹ､ｦ･ﾈｩ`･ｯ･�
	* @return ｷﾖｸ釥ｵ､�､ｿﾎﾄﾗﾖﾅ菽ﾐ
	* @since alpha1.0
	*/
	public static final String[] split2(String str,String token){
		if(!isActualString(str))
			return new String[0];
		final String[] strs = str.split(token);
		for(int i = 0;i < strs.length;i++)
			strs[i] = trim2(strs[i]);
		return strs;
	}
	/**
	* Stringｎ､ｬ殪�ｿｎ､ﾇ､ﾏ､ﾊ､､､ｳ､ﾈ､ﾊﾔ^､ｷ､ﾞ､ｹ｡｣
	*/
	public static final boolean isActualString(String value){
		if(value != null && !value.isEmpty() && !value.equalsIgnoreCase("NONE"))
			return true;
		return false;
	}
	/**
	* Stringﾅ菽ﾐ､ｬ殪�ｿｎ､ﾇ､ﾏ､ﾊ､､､ｳ､ﾈ､ﾊﾔ^､ｷ､ﾞ､ｹ｡｣
	*/
	public static final boolean isActualString(String[] value){
		if(value != null && value.length > 0 && !value[0].isEmpty() && !value[0].equalsIgnoreCase("NONE"))
			return true;
		return false;
	}
	/**
	* ､ｳ､ﾎintｎ､ﾏﾌﾘ�e､ﾊﾒ簧ｶ､ｬｺｬ､ﾞ､�､ﾊ､､携ﾊ�ｎ･ｾｩ`･ﾇ､｢､�､ｫ､{､ﾙ､ﾞ､ｹ｡｣
	* THH､ﾇ､ﾏﾒｻｲｿ､ﾎ我ﾊ�､ﾋﾌﾘﾊ筅ﾊﾒ簧ｶ､ﾖ､ｿ､ｻ､ｿﾊ�ｎ､抦�､ｷ｡｢ﾟ`､ﾃ､ｿ彫�ﾓ､ｾ､ｹ､隍ｦ､ﾊﾊﾋｽM､ﾟ､ｬ､｢､熙ﾞ､ｹ｡｣
	* (ﾀ�｣ｺgimmickHP､ﾏｶｨﾊ�MAX､ﾎ､ﾈ､ｭﾆﾆ牡ｲｻﾄﾜ,ｶｨﾊ�NONE､ﾎ､ﾈ､ｭﾐnﾍｻﾅﾐｶｨ､ﾊ､ｷ)
	* 携ﾊ�ｎ､ﾈｻ�ﾍｬ､ｷ､ﾊ､､､ｿ､癸｢､ｳ､ﾎ･ｾｩ`･ﾏﾏﾞｽ轤失ｶｽ�､ﾇ､｢､�､ｳ､ﾈ､ｬｶ爨､､隍ｦ､ﾋ､ﾊ､ﾃ､ﾆ､､､ﾞ､ｹ｡｣
	* @param value ﾕ{､ﾙ､�ｎ
	* @return 携ﾊ�ｎ､ﾇ､｢､�､ﾈ､ｭtrue,､ｽ､ｦ､ﾇ､ﾊ､ｱ､�､ﾐfalse
	* @since alpha1.0
	*/
	public static final boolean isActualNumber(int value){
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
	* ､ｳ､ﾎdoubleｎ､ﾏﾌﾘ�e､ﾊﾒ簧ｶ､ｬｺｬ､ﾞ､�､ﾊ､､携ﾊ�ｎ･ｾｩ`･ﾇ､｢､�､ｫ､{､ﾙ､ﾞ､ｹ｡｣
	* THH､ﾇ､ﾏﾒｻｲｿ､ﾎ我ﾊ�､ﾋﾌﾘﾊ筅ﾊﾒ簧ｶ､ﾖ､ｿ､ｻ､ｿﾊ�ｎ､抦�､ｷ｡｢ﾟ`､ﾃ､ｿ彫�ﾓ､ｾ､ｹ､隍ｦ､ﾊﾊﾋｽM､ﾟ､ｬ､｢､熙ﾞ､ｹ｡｣
	* (ﾀ�｣ｺgimmickHP､ｬｶｨﾊ�MAX､ﾎ､ﾈ､ｭﾆﾆ牡ｲｻﾄﾜ,ｶｨﾊ�NONE､ﾎ､ﾈ､ｭﾐnﾍｻﾅﾐｶｨ､ﾊ､ｷ)
	* 携ﾊ�ｎ､ﾈｻ�ﾍｬ､ｷ､ﾊ､､､ｿ､癸｢､ｳ､ﾎ･ｾｩ`･ﾏﾏﾞｽ轤失ｶｽ�､ﾇ､｢､�､ｳ､ﾈ､ｬｶ爨､､隍ｦ､ﾋ､ﾊ､ﾃ､ﾆ､､､ﾞ､ｹ｡｣
	* ､ﾊ､ｪ｡｢NaN､膽NEGATIVE/POSITIVE]_INFINITY､ﾇ､稠alse､ｬｷｵ､ﾃ､ﾆ､ｭ､ﾞ､ｹ｡｣
	* @param value ﾕ{､ﾙ､�ｎ
	* @return 携ﾊ�ｎ､ﾈ､ﾇ､｢､�､ﾈ､ｭtrue,､ｽ､ｦ､ﾇ､ﾊ､ｱ､�､ﾐfalse
	* @since alpha1.0
	*/
	public static final boolean isActualNumber(double value){
		if(	value == Double.NaN ||
			value == Double.NEGATIVE_INFINITY ||
			value == Double.POSITIVE_INFINITY ||
			!isActualNumber((int)value))
			return false;
		else
			return true;
	}
	/**
	* ｶﾈ､鬣ｸ･｢･ﾘ我轍､ｷ､ﾞ､ｹ､ｬ｡｢携ﾊ�ｎ､ﾇ､ﾏ､ﾊ､､ｎ､｣ｳﾖ､ｷ､ﾞ､ｹ｡｣
	* ､ｿ､ﾈ､ｨ､ﾐ｡｢"NONE"､�"Double.NEGATIVE_INFINITY"､ﾊ､ﾉ､ﾎﾌﾘﾊ竄痔ﾏ､ｳ､ﾎ･皈ｽ･ﾃ･ﾉ､ｨ､ｷ､ﾆ､竄痔ﾏ我ｻｯ､ｷ､ﾞ､ｻ､｣
	* @param ･鬣ｸ･｢･ﾋ我轍､ｹ､�ﾊ�ｎ
	* @return 我轍､ｵ､�､ｿｎ
	* @since alpha1.0
	*/
	public static final double toRadians2(double degress){
		if(isActualNumber(degress))
			return degress*PI/180;
		else
			return degress;
	}
	
	public static final int random2(int value1,int value2) {
		if(value1 == value2)
			return value1;
		else if(value1 > value2)
			return new Random().nextInt(abs(value1 - value2)) + value2;
		else
			return new Random().nextInt(abs(value2 - value1)) + value1;
	}
	public static final int random2(int value) {
		if(value == 0)
			return 0;
		if(value < 0)
			value *= -1;
		return new Random().nextInt(value*2) - value;
	}
	public static final double random2(double value1,double value2) {
		if(value1 == value2)
			return value1;
		else if(value1 > value2)
			return Math.random()*(value1 - value2) + value2;
		else
			return Math.random()*(value2 - value1) + value1;
	}
	public static final double random2(double value) {
		if(value == 0.0)
			return 0.0;
		else
			return Math.random()*value*2 - value;
	}
	//message window
	/**
	* ｾｯｸ讌ｦ･｣･ﾉ･ｦ､桄ｾ､ｷ､ﾞ､ｹ｡｣
	* ､ｳ､ﾎ馮､ﾎ･ﾕ･�ｩ`･猝�､ﾎ･ｫ･ｦ･ﾈ､ｬｷｵ､ｵ､�､ﾞ､ｹ｡｣
	* @param message ･皈ﾃ･ｻｩ`･ｸ
	* @param title ･皈ﾃ･ｻｩ`･ｸ･ｿ･､･ﾈ･�
	* @return ﾍ｣ﾖｹ､ｷ､ｿ瓶馮
	* @since alpha1.0
	*/
	public static final long warningBox(String message,String title){
		long openTime = System.currentTimeMillis();
		JOptionPane.showMessageDialog(null,message,title,JOptionPane.WARNING_MESSAGE);
		return System.currentTimeMillis() - openTime;
	}
}