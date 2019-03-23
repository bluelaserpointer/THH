package core;

import static java.awt.event.KeyEvent.*;
import static java.lang.Math.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.text.DecimalFormat;
import java.util.*;

import bullet.Bullet;
import effect.Effect;
import engine.Engine_THH1;
import gui.GUIParts;
import gui.MessageSource;
import input.KeyListenerEx;
import input.KeyTypeListener;
import input.MouseListenerEx;
import item.Item;
import physicis.Dynam;
import physicis.DynamInteractable;
import structure.Structure;
import stage.StageEngine;
import unit.Unit;
import vegetation.DropItem;
import vegetation.Vegetation;

/**
 * The core class for engine "THH"
 * @author bluelaserpointer
 * @version alpha1.0
 */

public final class GHQ extends JPanel implements MouseListener,MouseMotionListener,MouseWheelListener,KeyListener,Runnable{
	private static final long serialVersionUID = 123412351L;

	/**
	 * A static instance of GHQ for enabling static methods accessing non-static objects.
	 * @since alpha1.0
	 */
	private static GHQ hq;
	
	public static final String
		GHQ_VERSION = "Ver alpha1.0.0";
	/**
	 * A major constant which describe various meaning related to "nothing".
	 * @since alpha1.0
	 */
	public static final int NONE = -999999999;
	public static final int  
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
	
	//inputEvent
	private static ArrayList<KeyListenerEx> keyListeners = new ArrayList<KeyListenerEx>();
	private static ArrayList<MouseListenerEx> mouseListeners = new ArrayList<MouseListenerEx>();
	private static ArrayList<KeyTypeListener> typeListeners = new ArrayList<KeyTypeListener>();
	
	//stopEvent
	private static int stopEventKind = NONE;
	public static final int STOP = 0,NO_ANM_STOP = 1;
	public static boolean messageStop,spellStop;
	
	//screen
	private final int defaultScreenW = 1000,defaultScreenH = 600;
	private static int screenW = 1000,screenH = 600;
	private static double viewX,viewY,viewDstX,viewDstY;
	
	//frame
	private static int systemFrame;
	private static int gameFrame;
	
	//stage
	private static StageEngine engine;

	//character data
	private static final ArrayListEx<Unit> characters = new ArrayListEx<Unit>();
	
	//bullet data
	private static final ArrayListEx<Bullet> bullets = new ArrayListEx<Bullet>();
	
	//effect data
	private static final ArrayListEx<Effect> effects = new ArrayListEx<Effect>();
	
	//structure data
	private static final ArrayListEx<Structure> structures = new ArrayListEx<Structure>();
	
	//vegetation data
	private static final ArrayList<Vegetation> vegetations = new ArrayList<Vegetation>();
	
	//GUI data
	private static final ArrayList<GUIParts> guiParts = new ArrayList<GUIParts>();
	
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
		hq = this;
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
		myFrame.setFocusTraversalKeysEnabled(false);
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
		//font
		basicFont = createFont("font/upcibi.ttf").deriveFont(25.0f);
		commentFont = createFont("font/HGRGM.TTC").deriveFont(Font.PLAIN,15.0f);
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
	public static Font basicFont,commentFont;
	public static final BasicStroke stroke1 = new BasicStroke(1f),stroke3 = new BasicStroke(3f),stroke5 = new BasicStroke(5f);
	private static final Color HPWarningColor = new Color(255,120,120),debugTextColor = new Color(200,200,200,160);
	public static final DecimalFormat DF00_00 = new DecimalFormat("00.00");
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
		//stageIdle
		engine.idle(g2,stopEventKind);
		////////////////////////////////////////////////////////////////////////
		//GUIIdle
		g2.translate(-TRANSLATE_X_double, -TRANSLATE_Y_double);
		{
			//gui parts////////////////////
			for(GUIParts parts : guiParts)
				parts.defaultIdle();
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
				final int STAGE_W = engine.getStageW(),STAGE_H = engine.getStageH();
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
				g2.drawString("Chara:" + characters.size() + " EF:" + effects.size() + " B:" + bullets.size(),30,100);
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
		return engine.callBulletEngage(getCharacters_standpoint(bullet,false),bullet);
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
		removeDeadUnits();
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
		removeDeadUnits();
	}
	public static final int removeDeadUnits() {
		int deleted = 0;
		characters.setIterator(0);
		while(characters.hasNext()) {
			final Unit UNIT = characters.next();
			if(!UNIT.isAlive()) {
				characters.removeCurrent();
				deleted++;
			}
		}
		return deleted;
	}
	//idle-entity
	public static final void defaultEntityIdle() {
		bullets.setIterator(0);
		effects.setIterator(0);
		switch(stopEventKind) {
		case STOP:
			while(bullets.hasNext()) {
				final Bullet bullet = bullets.next();
				bullet.SCRIPT.paint(bullet);
			}
			while(effects.hasNext()) {
				final Effect effect = effects.next();
				effect.SCRIPT.paint(effect);
			}
			break;
		case NO_ANM_STOP:
			while(bullets.hasNext()) {
				final Bullet bullet = bullets.next();
				bullet.SCRIPT.noAnmPaint(bullet);
			}
			while(effects.hasNext()) {
				final Effect effect = effects.next();
				effect.SCRIPT.noAnmPaint(effect);
			}
			break;
		default:
			while(bullets.hasNext()) {
				final Bullet bullet = bullets.next();
				bullet.SCRIPT.idle(bullet);
			}
			while(effects.hasNext()) {
				final Effect effect = effects.next();
				effect.SCRIPT.idle(effect);
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
	//information-get-mouseOver
	public static final Unit getMouseOverChara() {
		for(Unit unit : characters) {
			if(unit.isMouseOveredBoundingBox())
				return unit;
		}
		return null;
	}
	public static final Structure getMouseOverStructure() {
		for(Structure structure : structures) {
			if(structure.isMouseOveredBoundingBox())
				return structure;
		}
		return null;
	}
	public static final Vegetation getMouseOverVegetation() {
		for(Vegetation vegetation : vegetations) {
			if(vegetation.isMouseOveredBoundingBox())
				return vegetation;
		}
		return null;
	}
	//information-characters
	public static final Unit getChara(String name) {
		for(Unit chara : characters) {
			if(chara.getName() == name)
				return chara;
		}
		return null;
	}
	public static final ArrayListEx<Unit> getCharacterList() {
		return characters;
	}
	public static final Unit getCharacter(int index) {
		return characters.get(index);
	}
	public static final Unit[] getCharacters_standpoint(HasStandpoint source,boolean white) {
		final Unit[] charaArray = new Unit[characters.size()];
		int founded = 0;
		for(Unit chara : characters) {
			if(white == source.isFriendly(chara))
				charaArray[founded++] = chara;
		}
		return Arrays.copyOf(charaArray, founded);
	}
	public static final Unit[] getCharacters_standPoint(HasStandpoint source) {
		return getCharacters_standpoint(source,true);
	}
	public static final Unit[] getCharacters() {
		final Unit[] unitArray = new Unit[characters.size()];
		for(int i = 0;i < unitArray.length;i++)
			unitArray[i] = characters.get(i);
		return unitArray;
	}
	public static final Unit getNearstEnemy(HasStandpoint source,int x,int y) {
		double nearstDistance = NONE;
		Unit nearstChara = null;
		for(Unit enemy : getCharacters_standpoint(source,false)) {
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
		return characters.size();
	}
	public static final ArrayList<Unit> getVisibleEnemies(Unit unit) {
		final ArrayList<Unit> visibleEnemies = new ArrayList<Unit>();
		for(Unit enemy : getCharacters_standpoint(unit,false)) {
			if(!enemy.isAlive())
				continue;
			if(enemy.dynam.isVisible(unit))
				visibleEnemies.add(enemy);
		}
		return visibleEnemies;
	}
	public static final Unit getNearstVisibleEnemy(Unit chara) {
		final Dynam DYNAM = chara.dynam;
		Unit neastVisibleEnemy = null;
		double enemyDistance = MAX;
		for(Unit enemy : getCharacters_standpoint(chara,false)) {
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
		for(Unit enemy : getCharacters_standpoint(bullet,false)) {
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
	//information-vegetation
	public static ArrayList<Vegetation> getVegetationList(){
		return vegetations;
	}
	public static final Vegetation[] getVegetations() {
		final Vegetation[] vegetationArray = new Vegetation[vegetations.size()];
		for(int i = 0;i < vegetationArray.length;i++)
			vegetationArray[i] = vegetations.get(i);
		return vegetationArray;
	}
	public static final DropItem getCoveredDropItem(DynamInteractable di, int distance) {
		for(Vegetation vegetation : vegetations) {
			if(vegetation instanceof DropItem && ((DropItem)vegetation).isCovered(di, distance)) {
				return (DropItem)vegetation;
			}
		}
		return null;
	}
	public static final Item getCoveredDropItem_pickup(DynamInteractable di, int distance) {
		for(Vegetation vegetation : vegetations) {
			if(vegetation instanceof DropItem && ((DropItem)vegetation).isCovered(di, distance)) {
				return ((DropItem)vegetation).pickup();
			}
		}
		return null;
	}
	//information-stage
	public static final StageEngine getEngine() {
		return engine;
	}
	public static ArrayListEx<Structure> getStructureList(){
		return structures;
	}
	public static final Structure[] getStructures() {
		final Structure[] structureArray = new Structure[structures.size()];
		for(int i = 0;i < structureArray.length;i++)
			structureArray[i] = structures.get(i);
		return structureArray;
	}

	public static Structure[] getStructures(HasStandpoint source,boolean white){
		Structure result[] = new Structure[structures.size()];
		int searched = 0;
		for(Structure structure : structures) {
			if(white == structure.isFriendly(source))
				result[searched++] = structure;
		}
		return Arrays.copyOf(result, searched);
	}
	public static final boolean checkLoS(Line2D line) {
		for(Structure structure : structures) {
			if(structure.intersectsLine(line))
				return false;
		}
		return true;
	}
	public static final boolean hitStructure_Rect(int x,int y,int w,int h){
		for(Structure structure : structures) {
			if(structure.contains(x, y, w, h))
				return true;
		}
		return false;
	}
	public static final boolean hitLandscape_Rect(HasStandpoint standpoint,int x,int y,int w,int h){
		for(Structure structure : structures) {
			if(structure.getStandpoint() == null)
				System.out.println("bug.");
			if(!standpoint.isFriendly(structure) && structure.contains(x, y, w, h))
				return true;
		}
		return false;
	}
	public static final boolean inStage(int x,int y) {
		return engine.inStage(x,y);
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
	public static final URL getImgageURLByID(int imageID) {
		return arrayImageURL[imageID];
	}
	public static final Image getImageByID(int imageID) {
		return arrayImage[imageID];
	}
	public static final int getImageWByID(int imageID) {
		return arrayImage[imageID].getWidth(null);
	}
	public static final int getImageHByID(int imageID) {
		return arrayImage[imageID].getHeight(null);
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
	//control-add

	//control-delete
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
	public static final boolean deleteUnit(Unit unit) {
		return characters.remove(unit);
	}
	public static final boolean deleteStructure(Structure structure) {
		return structures.remove(structure);
	}
	public static final void deleteVegetation(Vegetation vegetation) {
		vegetations.remove(vegetation);
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
	public final int receiveDamageInSquare(HasStandpoint source,int hp,int x,int y,int size){
		for(Bullet bullet : bullets){
			if(!source.isFriendly(bullet) && bullet.atk > 0 && bullet.dynam.squreCollision(x,y,(bullet.SIZE + size)/2)){
				hp -= bullet.atk;
				bullet.SCRIPT.bulletHitObject(bullet);
				if(hp <= 0)
					break;
			}
		}
		return hp;
	}
	public final int receiveBulletInCircle(HasStandpoint source, int hp,int x,int y,int size){
		for(Bullet bullet : bullets){
			if(!source.isFriendly(bullet) && bullet.atk > 0 && circleCollision((int)bullet.dynam.getX(),(int)bullet.dynam.getY(),bullet.SIZE,x,y,size)){ //ﾐnﾍｻ
				hp -= bullet.atk;
				bullet.SCRIPT.bulletHitObject(bullet);
				if(hp <= 0)
					break;
			}
		}
		return hp;
	}
	public final Bullet searchBulletInSquare_single(HasStandpoint source,int x,int y,int size){
		for(int i = 0;i < bullets.size();i++){
			final Bullet bullet = bullets.get(i);
			if(!source.isFriendly(bullet) && bullet.dynam.squreCollision(x,y,(bullet.SIZE + size)/2))
				return bullet; //return ID
		}
		return null; //Not found
	}
	public final Bullet[] searchBulletInSquare_multiple(HasStandpoint source,int x,int y,int size){
		Bullet[] foundIDs = new Bullet[bullets.size()];
		int foundAmount =  0;
		for(int i = 0;i < bullets.size();i++){
			final Bullet bullet = bullets.get(i);
			if(!source.isFriendly(bullet) && bullet.dynam.squreCollision(x,y,(bullet.SIZE + size)/2))
				foundIDs[foundAmount++] = bullet; //record ID
		}
		return Arrays.copyOf(foundIDs,foundAmount);
	}
	//control-GUI
	public static final void enableGUIs(String group) {
		for(GUIParts parts : guiParts) {
			if(parts.GROUP == group)
				parts.enable();
		}
	}
	public static final void disableGUIs(String group) {
		for(GUIParts parts : guiParts) {
			if(parts.GROUP == group)
				parts.disable();
		}
	}
	public static final void flitGUIs(String group) {
		for(GUIParts parts : guiParts) {
			if(parts.GROUP == group)
				parts.flit();
		}
	}
	public static final void EnableCertainGUIs(String group) {
		for(GUIParts parts : guiParts) {
			if(parts.GROUP == group)
				parts.enable();
			else
				parts.disable();
		}
	}
	//information-collision
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
	
	public void mouseWheelMoved(MouseWheelEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mousePressed(MouseEvent e){
		//MouseListenerExs event
		for(MouseListenerEx mle : mouseListeners) {
			if(!mle.isEnabled())
				continue;
			switch(e.getButton()) {
			case MouseEvent.BUTTON1:
				mle.pressButton1Event();
				break;
			case MouseEvent.BUTTON2:
				mle.pressButton2Event();
				break;
			case MouseEvent.BUTTON3:
				mle.pressButton3Event();
				break;
			}
		}
		//GUIParts click event
		boolean alreadyClicked = false;
		for(GUIParts parts : guiParts) {
			if(parts.isEnabled()) {
				if(alreadyClicked || !parts.isMouseEntered()) {
					parts.outsideClicked();
				}else {
					parts.clicked();
					alreadyClicked = parts.absorbsClickEvent();
				}
			}
		}
	}
	public void mouseReleased(MouseEvent e){
		for(MouseListenerEx mle : mouseListeners) {
			if(!mle.isEnabled())
				continue;
			switch(e.getButton()) {
			case MouseEvent.BUTTON1:
				mle.pullButton1Event();
				break;
			case MouseEvent.BUTTON2:
				mle.pullButton2Event();
				break;
			case MouseEvent.BUTTON3:
				mle.pullButton3Event();
				break;
			}
		}
	}
	public void mouseClicked(MouseEvent e){
	}
	public void mouseMoved(MouseEvent e){
		final int x = e.getX(),y = e.getY();
		mouseX = x;mouseY = y;
		boolean alreadyOvered = false;
		for(GUIParts parts : guiParts) {
			if(parts.isEnabled()) {
				if(alreadyOvered || !parts.isMouseEntered())
					parts.outsideMouseOvered();
				else {
					parts.mouseOvered();
					alreadyOvered = parts.absorbsClickEvent();
				}
			}
		}
	}
	public void mouseDragged(MouseEvent e){
		final int x = e.getX(),y = e.getY();
		mouseX = x;mouseY = y;
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
	public static final boolean isMouseInArea_Stage(int x,int y,int w,int h) {
		return abs(x - mouseX + viewX) < w/2 && abs(y - mouseY + viewY) < h/2;
	}
	public static final boolean isMouseInArea_Screen(int x,int y,int w,int h) {
		return abs(x - mouseX) < w/2 && abs(y - mouseY) < h/2;
	}
	public final boolean isMouseOnImage(int imgID,int x,int y) {
		final Image img = arrayImage[imgID];
		return isMouseInArea_Stage(x,y,img.getWidth(null),img.getHeight(null));
	}
	//キー情報
	public static boolean key_1,key_2,key_3,key_4;
	public static long key_1_time,key_2_time,key_3_time,key_4_time;
	public static boolean key_W,key_A,key_S,key_D,key_enter;
	public static boolean key_shift;
	public void keyPressed(KeyEvent e){
		final int KEY_CODE = e.getKeyCode();
		for(KeyListenerEx kle : keyListeners) {
			if(kle.isEnabled())
				kle.pressEvent(KEY_CODE);
		}
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
		case VK_SHIFT:
			key_shift = true;
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
		final int KEY_CODE = e.getKeyCode();
		for(KeyListenerEx kle : keyListeners) {
			if(kle.isEnabled())
				kle.releaseEvent(KEY_CODE);
		}
		switch(KEY_CODE){
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
		case VK_SHIFT:
			key_shift = false;
			break;
		case VK_ENTER:
			key_enter = false;
			break;
		}
	}
	public void keyTyped(KeyEvent e){
		for(KeyTypeListener ktl : typeListeners) {
			if(ktl.isEnabled())
				ktl.typed(e.getKeyChar());
		}
	}
	private final class MyWindowAdapter extends WindowAdapter{
		public void windowClosing(WindowEvent e){
			System.exit(0);
		}
		public void windowDeactivated(WindowEvent e){
			//if(mainEvent == BATTLE)
				//mainEvent = BATTLE_PAUSE;
		}
	}
	
	//control
	/**
	 * Freeze screen manually.
	 * @since alpha1.0
	 */
	public static final void freezeScreen() {
		freezeScreen = true;
	}
	public static final void stopScreen() {
		stopEventKind = STOP;
	}
	public static final void stopScreen_noAnm() {
		stopEventKind = NO_ANM_STOP;
	}
	/**
	 * Clear freeze screen and other stopEvents.
	 * @since alpha1.0
	 */
	public static final void clearStopEvent() {
		stopEventKind = NONE;
	}
	
	//generation
	/**
	 * Create a {@link Bullet} and add it to current stage.
	 * Parameters of the bullet refers to {@link BulletInfo} settings.
	 * @param source A shooter of this bullet(Unit, Bullet, etc.)
	 * @return created Bullet
	 * @since alpha1.0
	 */
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
	/**
	 * Create a {@link Effect} and add it to current stage.
	 * Parameters of the effect refers to {@link EffectInfo} settings.
	 * @param source A shooter of this effect(Unit, Bullet, etc.)
	 * @return created Effect
	 * @since alpha1.0
	 */
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
	/**
	 * Add an {@link Unit} to current stage.
	 * @param unit
	 * @return added unit
	 */
	public static final <T extends Unit>T addUnit(T unit) {
		characters.add(unit);
		return unit;
	}
	public static final <T extends Unit>T replaceUnit(Unit targetUnit,T newUnit){
		newUnit.dynam.setAllBySample(targetUnit.dynam);
		deleteUnit(targetUnit);
		addUnit(newUnit);
		return newUnit;
	}
	/**
	 * Add a {@link Structure} to current stage.
	 * @param structure
	 * @return added structure
	 */
	public static final <T extends Structure>T addStructure(T structure) {
		structures.add(structure);
		return structure;
	}
	/**
	 * Add a {@link Vegetation} to current stage.
	 * @param vegetation
	 * @return added vegetation
	 */
	public static final <T extends Vegetation>T addVegetation(T vegetation){
		vegetations.add(vegetation);
		return vegetation;
	}
	/**
	 * Add a {@link GUIParts}.(Doesn't enable it automatically.)
	 * @param GUIParts
	 * @return added GUIParts
	 */
	public static final <T extends GUIParts>T addGUIParts(T guiParts) {
		if(guiParts == null) {
			System.out.println("GHQ.addGUIParts recieved a null guiParts.");
			return null;
		}
		GHQ.guiParts.add(guiParts);
		return guiParts;
	}
	/**
	 * Add a {@link MouseListenerEx}.(Doesn't enable it automatically.)
	 * @param GUIParts
	 * @return added GUIParts
	 */
	public static final void addListenerEx(MouseListenerEx mle) {
		mouseListeners.add(mle);
	}
	/**
	 * Add a {@link KeyListenerEx}.(Doesn't enable it automatically.)
	 * @param GUIParts
	 * @return added GUIParts
	 */
	public static final void addListenerEx(KeyListenerEx kle) {
		keyListeners.add(kle);
	}
	/**
	 * Add a {@link KeyTypeListener}.(Doesn't enable it automatically.)
	 * @param GUIParts
	 * @return added GUIParts
	 */
	public static final void addListenerEx(KeyTypeListener kte) {
		typeListeners.add(kte);
	}
	//information-frame&time
	/**
	 * Returns gameFrame.
	 * @return now gameFrame
	 * @since 1.0
	 */
	public static final int getNowFrame() {
		return gameFrame;
	}
	/**
	 * Returns frames passed from the indicated frame.
	 * @param frame
	 * @return passed frames
	 * @since 1.0
	 */
	public static final int getPassedFrame(int frame) {
		return frame == NONE ? MAX : gameFrame - frame;
	}
	/**
	 * Check if passed frames from the indicated frame is much than the limit.
	 * @return if expired
	 * @since 1.0
	 */
	public static final boolean isExpired_frame(int initialFrame,int limitFrame) {
		return initialFrame == NONE || (gameFrame - initialFrame) >= limitFrame;
	}
	/**
	 * Returns systemTime. {@link System#currentTimeMillis()}
	 * @return now systemTime
	 * @since 1.0
	 */
	public static final long getNowTime() {
		return System.currentTimeMillis();
	}
	/**
	 * Returns time passed from the indicated time. {@link System#currentTimeMillis()}
	 * @param time
	 * @return passed time
	 * @since 1.0
	 */
	public static final int getPassedTime(long time) {
		return time == NONE ? MAX : (int)(System.currentTimeMillis() - time);
	}
	/**
	 * Check if passed time from the indicated time is much than the limit.
	 * @param initialFrame
	 * @param limitTime
	 * @return if expired
	 * @since 1.0
	 */
	public static final boolean isExpired_time(long initialFrame,long limitTime) {
		return initialFrame == NONE || (System.currentTimeMillis() - initialFrame) >= limitTime;
	}
	/**
	 * Check if there is a stopEvent.
	 * @return boolean
	 * @since 1.0
	 */
	public static boolean isNoStopEvent() {
		return !freezeScreen && stopEventKind == NONE;
	}
	/**
	 * Check if there is a "freezeScreen" stop Event.
	 * @return boolean
	 * @since 1.0
	 */
	public static boolean isFreezeScreen() {
		return freezeScreen;
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
	
	//stage test area
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
		engine.charaSetup();
		engine.stageSetup();
		engine.openStage();
	}
	
	//ResourceLoad
	/**
	* Load the image file.
	* @param url
	* @return ImageID (id of Image array arrayImage)
	* @since alpha1.0
	*/
	public static final int loadImage(URL url){ //画像読み込みメソッド
		if(url == null) {
			System.out.println("received null image url at: " + null);
			return 0;
		}
		//old URL
		for(int id = 0;id < arrayImageURL.length;id++){
			if(url.equals(arrayImageURL[id]))
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
			if(url.toString() != null && !url.toString().isEmpty())
				warningBox("Image " + url + " is not found and could not be loaded.Error code: " + url.toString(),"ImageLoadingError");
			else
				warningBox("Image " + url + " is not found and could not be loaded.","ImageLoadingError");
			arrayImage[arrayImage_maxID] = hq.createImage(1,1);
		}
		//save URL
		arrayImage_maxID++;
		if(arrayImage_maxID == arrayImage.length){
			arrayImage = Arrays.copyOf(arrayImage,arrayImage_maxID*2);
			arrayImageURL = Arrays.copyOf(arrayImageURL,arrayImage_maxID*2);
		}
		arrayImageURL[arrayImage_maxID] = url;
		arrayImage[arrayImage_maxID] = img;
		//System.out.println(arrayImage_maxID + ">>:" + arrayImageURL[arrayImage_maxID].toString());
		return arrayImage_maxID;
	}
	/**
	* Load the image file.
	* @param url 
	* @return ImageID (id of Image array arrayImage)
	* @since alpha1.0
	*/
	public static final int loadImage(String urlStr){
		final URL url = hq.getClass().getResource("/" + urlStr);
		if(url == null)
			System.out.println("not founded image file:" + urlStr);
		return loadImage(url);
	}
	private static final FileFilter PICTURE_FINDER = new FileFilter(){
		public boolean accept(File dir){
			return dir.getName().endsWith(".png") || dir.getName().endsWith(".jpeg");
		}
	};
	public static final void loadImageFolder(File folder) {
		final File[] fileList = folder.listFiles(PICTURE_FINDER);
		for(File imageFile : fileList) {
			try {
				loadImage(imageFile.toURI().toURL());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	* Load the sound file.
	* @param url
	* @return SoundID (id of SoundClip array arraySound)
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
	* Load the font file.
	* @param url
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
	
	public final Unit[] loadAllUnit(URL url) {
		final FilenameFilter classFilter = new FilenameFilter(){
			public boolean accept(File dir,String name){
				return name.endsWith(".class");
			}
		};
		File charaFolder = null;
		try{
			charaFolder = new File(url.toURI());
		}catch(URISyntaxException | NullPointerException e){
			System.out.println("Unable to load Unit class.");
		}
		if(charaFolder == null){
			System.out.println("Pass does not exist.");
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
	/**
	 * Change paint coordinate for GUI/stageObjects.
	 * Note that if you don't reset changed paint coordinate, it will collapse coordinates of following paints.(like Graphics2D.rotate method)
	 * @param forGUI true - change for GUI / false - change for objects in its stage;
	 * @since alpha1.0
	 */
	public static final void translateForGUI(boolean forGUI) {
		if(forGUI)
			hq.g2.translate(-viewX,-viewY);
		else
			hq.g2.translate(viewX,viewY);
	}
	/**
	 * Call {@link Graphics2D#setClip(int, int, int, int)} directly.
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @since alpha1.0
	 */
	public static final void setClip(int x,int y,int width,int height) {
		hq.g2.setClip(x, y, width, height);
	}
	/**
	 * Reset AlphaComposite value.
	 * @since alpha1.0
	 */
	public static final void setImageAlpha() {
		hq.g2.setComposite(AlphaComposite.SrcOver);
	}
	/**
	 * Set AlphaComposite's transparent value.
	 * @float alpha transparent degree
	 * @since alpha1.0
	 */
	public static final void setImageAlpha(float alpha) {
		hq.g2.setComposite(AlphaComposite.SrcOver.derive(alpha));
	}
	//Paint
	/**
	* Draw an image.
	* @param img 
	* @param x 
	* @param y 
	* @param w 
	* @param h 
	* @since alpha1.0
	*/
	public static final void drawImageGHQ(Image img,int x,int y,int w,int h){
		hq.g2.drawImage(img,x,y,w,h,hq);
	}
	/**
	* Draw an image by imageID.
	* @param imgID 
	* @param x 
	* @param y 
	* @param w 
	* @param h 
	* @since alpha1.0
	*/
	public static final void drawImageGHQ(int imgID,int x,int y,int w,int h){
		drawImageGHQ(arrayImage[imgID],x,y,w,h);
	}
	public static final void drawImageGHQ(int imgID,int x,int y,int w,int h,double angle) {
		if(angle != 0.0) {
			final double OX = x + w/2,OY = y + h/2;
			hq.g2.rotate(angle, OX, OY);
			hq.g2.drawImage(arrayImage[imgID],x,y,w,h,hq);
			hq.g2.rotate(-angle, OX, OY);
		}else
			hq.g2.drawImage(arrayImage[imgID],x,y,w,h,hq);
	}
	/**
	* Draw an image.
	* @param img 
	* @param x 
	* @param y 
	* @param angle 
	* @since alpha1.0
	*/
	public static final void drawImageGHQ_center(Image img,int x,int y,double angle){
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
	* Draw an image by imageID.
	* @param imgID 
	* @param x 
	* @param y 
	* @param angle 
	* @since alpha1.0
	*/
	public static final void drawImageGHQ_center(int imgID,int x,int y,double angle) {
		drawImageGHQ_center(arrayImage[imgID],x,y,angle);
	}
	/**
	* Draw an image.
	* @param img 
	* @param x 
	* @param y 
	* @since alpha1.0
	*/
	public static final void drawImageGHQ_center(Image img,int x,int y){
		hq.g2.drawImage(img,x - img.getWidth(null)/2,y - img.getHeight(null)/2,hq);
	}
	/**
	* Draw an image by imageID.
	* @param imgID 
	* @param x 
	* @param y 
	* @since alpha1.0
	*/
	public static final void drawImageGHQ_center(int imgID,int x,int y){
		drawImageGHQ_center(arrayImage[imgID],x,y);
	}
	/**
	* Draw an image by imageID.
	* @param imgID 
	* @param x 
	* @param y 
	* @param w 
	* @param h 
	* @since alpha1.0
	*/
	public static final void drawImageGHQ_center(int imgID,int x,int y,int w,int h){
		hq.g2.drawImage(arrayImage[imgID],x - w/2,y - h/2,w,h,hq);
	}
	/**
	 * Draw an image by imageID.
	 * @param imgID
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @param angle
	* @since alpha1.0
	 */
	public static final void drawImageGHQ_center(int imgID,int x,int y,int w,int h,double angle){
		if(angle != 0.0) {
			hq.g2.rotate(angle, x, y);
			hq.g2.drawImage(arrayImage[imgID],x - w/2,y - h/2,w,h,hq);
			hq.g2.rotate(-angle, x, y);
		}else
			hq.g2.drawImage(arrayImage[imgID],x - w/2,y - h/2,w,h,hq);
	}
	/**
	 * Return Graphics2D instance.
	 * @return
	 */
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
	
	//math & string
	public static final double angleFormat(double radian){ //ラジアン整理メソッド -PI~+PIに直す
		radian %= PI*2;
		if(radian > PI)
			radian -= PI*2;
		else if(radian <= -PI)
			radian += PI*2;
		return radian;
	}
	public static final int[] deboxingIntArray(ArrayList<Integer> arrayList) {
		final int SIZE = arrayList.size();
		final int[] result = new int[SIZE];
		for(int i = 0;i < SIZE;i++)
			result[i] = arrayList.get(i);
		return result;
	}
	public static final double[] deboxingDoubleArray(ArrayList<Double> arrayList) {
		final int SIZE = arrayList.size();
		final double[] result = new double[SIZE];
		for(int i = 0;i < SIZE;i++)
			result[i] = arrayList.get(i);
		return result;
	}
	public static String buildStringArray(Object...objects) {
		final StringBuilder SB = new StringBuilder();
		for(int i = 0;;) {
			SB.append(objects[i]);
			if(++i < objects.length)
				SB.append(',');
			else
				break;
		}
		return SB.toString();
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
	* Original version of String.split that prevent throwing NullPointerException.
	* @param str
	* @param token 
	* @return string array
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
	* Check if the string is null or equals "NONE"
	*/
	public static final boolean isActualString(String value){
		if(value != null && !value.isEmpty() && !value.equalsIgnoreCase("NONE"))
			return true;
		return false;
	}
	/**
	* Check if the string array is null or empty or have only element "NONE"
	*/
	public static final boolean isActualString(String[] value){
		if(value != null && value.length > 0 && !value[0].isEmpty() && !value[0].equalsIgnoreCase("NONE"))
			return true;
		return false;
	}
	/**
	* Check if the int value has a special meaning, including NONE.
	* @param value
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
	* Check if the double value has a special meaning, including NONE.
	* @param value
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
	* Execute {@link Math#toRadians(double)} only if the value doesen't have special meaning.
	* @param degress
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
	* Show a warning message window.
	* @param message
	* @param title
	* @return The time passed while this window is shown.
	* @since alpha1.0
	*/
	public static final long warningBox(String message,String title){
		long openTime = System.currentTimeMillis();
		JOptionPane.showMessageDialog(null,message,title,JOptionPane.WARNING_MESSAGE);
		return System.currentTimeMillis() - openTime;
	}

	public static final void saveData(SaveData save,File file) {
		try{ //データの書き込み
			if(!file.exists())
				file.createNewFile();
			final ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(new SaveHolder(save));
			oos.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static final SaveData loadData(File file) {
		if(!file.exists()) {
			System.out.println("File doesn't exist.");
			return null;
		}
		SaveHolder saveHolder = null;
		try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))){
			saveHolder = (SaveHolder)ois.readObject();
		}catch(Exception e){
			e.printStackTrace();
		}
		if(saveHolder == null) {
			System.out.println("Load Error.");
			return null;
		}
		return saveHolder.getData();
	}
}