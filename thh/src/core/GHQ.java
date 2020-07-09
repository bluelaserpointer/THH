package core;

import static java.awt.event.KeyEvent.*;
import static java.lang.Math.*;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import javax.swing.*;

import calculate.Consumables;
import exsampleGame.engine.Engine_THH1;

import java.io.*;
import java.net.*;
import java.text.DecimalFormat;
import java.util.*;

import gui.GUIParts;
import gui.MouseHook;
import input.key.KeyListenerEx;
import input.keyType.KeyTypeListener;
import input.mouse.MouseListenerEx;
import physics.HasPoint;
import physics.Point;
import physics.stage.GHQStage;
import preset.unit.Unit;

/**
 * The core class for engine "THH"
 * @author bluelaserpointer
 * @version alpha1.0
 */

public final class GHQ extends JPanel implements MouseListener,MouseMotionListener,MouseWheelListener,KeyListener, Runnable{
	private static final long serialVersionUID = 123412351L;

	/**
	 * A static instance of GHQ for enabling static methods accessing non-static objects.
	 * @since alpha1.0
	 */
	public static GHQ hq;
	
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
	public final URL UNIT_DIC_URL = getClass().getResource("../unit");
	final String
		ASSETS_URL = "assets",
		UNIT_FOLDERS_URL = "assets/chara",
		LOCAL_IMAGE_URL = "assets/image",
		LOCAL_SOUND_URL = "assets/sound",
		LOCAL_FONT_URL = "assets/font";
	
	//debug
	private static boolean freezeScreen;
	private boolean debugMode;
	private static int loadTime_total;
	public static String errorPoint = "NONE";
	
	//ui
	public static final GUIParts BASE_SCREEN_UI = new GUIParts().setName("BASE_SCREEN_UI");
	
	//inputEvent
	private static ArrayList<KeyListenerEx> keyListeners = new ArrayList<KeyListenerEx>();
	private static ArrayList<MouseListenerEx> mouseListeners = new ArrayList<MouseListenerEx>();
	private static ArrayList<KeyTypeListener> typeListeners = new ArrayList<KeyTypeListener>();
	
	//stopEvent
	private static int stopEventKind = NONE;
	public static final int STOP = 0;
	public static boolean messageStop, spellStop;
	
	//screen
	private static int screenW, screenH;
	private static double viewX, viewY, viewDstX, viewDstY;
	private static double stageZoomRate = 1.0;
	
	//frame
	private static int systemFrame;
	private static int gameFrame;
	
	//stage
	private static Game engine;

	//stage object data
	private static GHQStage stage;
	
	//Initialize methods/////////////
	public static Game loadEngine(String fileURL) {
		Game result = null;
		try{
			final Object obj = Class.forName(fileURL).newInstance(); //インスタンスを生成
			if(obj instanceof Game)
				result =  (Game)obj; //インスタンスを保存
		}catch(ClassNotFoundException | InstantiationException | IllegalAccessException e){
			System.out.println(e);
			result = new Engine_THH1();
		}
		return result;
	}
	
	private static boolean loadComplete;
	public final MediaTracker tracker;
	public GHQ(Game engine, int screenW, int screenH){
		targetImageObserver = this;
		GHQ.engine = engine;
		offImage = new BufferedImage(GHQ.screenW = screenW, GHQ.screenH = screenH, BufferedImage.TYPE_INT_RGB);
		screenRect = new Rectangle2D.Double(0, 0, screenW, screenH);
		hq = this;
		//window setup
		final JFrame myFrame = new JFrame(engine.getTitleName());
		myFrame.add(this,BorderLayout.CENTER);
		myFrame.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
			@Override
			public void windowActivated(WindowEvent e){
				freezeScreen = false;
			}
			@Override
			public void windowDeactivated(WindowEvent e){
				freezeScreen = true;
			}
		});
		addMouseMotionListener(this);
		addMouseListener(this);
		addMouseWheelListener(this);
		myFrame.addKeyListener(this);
		myFrame.setBackground(Color.BLACK);
		myFrame.setBounds(250, 20, screenW + 6, screenH + 28);
		myFrame.setResizable(false);
		myFrame.setVisible(true);
		myFrame.setFocusTraversalKeysEnabled(false);
		//load assets
		tracker = new MediaTracker(this);
		//setup
		resetStage();
		//font
		engine.loadResource();
		basicFont = createFont("font/upcibi.ttf").deriveFont(25.0f);
		commentFont = createFont("font/HGRGM.TTC").deriveFont(Font.PLAIN, 15.0f);
		new Thread(this).start();
	}

	//mainLoop///////////////
	private static long mili1000Stamp;
	private static int framesCount;
	private static double nowFPS;
	public final void run(){
		//titleBGM.loop();
		//try{
			mili1000Stamp = GHQ.nowTime();
			while(true){
				//fps
				final int PASSED_TIME = GHQ.passedTime(mili1000Stamp);
				++framesCount;
				if(PASSED_TIME >= 1000) {
					nowFPS = (double)framesCount / (double)PASSED_TIME * 1000.0;
					mili1000Stamp = GHQ.nowTime();
					framesCount = 0;
				}
				//sleep
				try{
					Thread.sleep(25L);
				}catch(InterruptedException e){}
				//main process
				if(!freezeScreen) {
					repaint();
				}
			}
		//}catch(Exception e){
			//JOptionPane.showMessageDialog(null, "申し訳ありませんが、エラーが発生しました。\nエラーコード：" + e.toString(),"エラー",JOptionPane.ERROR_MESSAGE);
		//}
	}
	
	private final BufferedImage offImage; //ダブルバッファキャンバス
	private static Graphics2D g2;
	public static Font initialFont, basicFont, commentFont;
	public static final BasicStroke stroke1 = new BasicStroke(1f), stroke3 = new BasicStroke(3f), stroke5 = new BasicStroke(5f), stroke7 = new BasicStroke(7f);
	private static final Color HPWarningColor = new Color(255,120,120), debugTextColor = new Color(200, 200, 200);
	public static final DecimalFormat DF0_0 = new DecimalFormat("0.0"), DF0_00 = new DecimalFormat("0.00"), DF00_00 = new DecimalFormat("00.00");
	private final Rectangle2D screenRect;
	
	public void paintComponent(Graphics g) {
		final long LOAD_TIME_PAINT_COMPONENT = System.currentTimeMillis();
		super.paintComponent(g);
		if(g2 == null) {
			g2 = offImage.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
			g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
			initialFont = g2.getFont();
			loadComplete = true;
			for(LoadRequester ver : loadRequesters)
				ver.loadResource();
			try{
				tracker.waitForAll();
			}catch(InterruptedException | NullPointerException e){}
			return;
		}
		g2.setColor(Color.WHITE);
		g2.fill(screenRect);
		final int TRANSLATE_X = (int)viewX,TRANSLATE_Y = (int)viewY;
		g2.translate(TRANSLATE_X, TRANSLATE_Y);
		g2.setFont(initialFont);
		////////////////////////////////////////////////////////////////////////
		//gameIdle
		final int tx = (int)GHQ.getScreenCenterStageX(), ty = (int)GHQ.getScreenCenterStageY();
		final double zoomRate = stageZoomRate;
		if(zoomRate != 1.0) {
			g2.translate(tx, ty);
			GHQ.scale(zoomRate, zoomRate);
			g2.translate(-tx, -ty);
			engine.idle(g2, stopEventKind);
			g2.translate(tx, ty);
			final double _zoomRate = 1.0/zoomRate;
			GHQ.scale(_zoomRate, _zoomRate);
			g2.translate(-tx, -ty);
		} else
			engine.idle(g2, stopEventKind);
		////////////////////////////////////////////////////////////////////////
		//GUIIdle
		g2.translate(-TRANSLATE_X, -TRANSLATE_Y);
		//gui parts////////////////////
		BASE_SCREEN_UI.idle();
		//debug ////////////////////////
		if(debugMode){
			//grid
			g2.setColor(debugTextColor);
			g2.setFont(basicFont);
			g2.setStroke(stroke1);
			for(int i = 100;i < screenW;i += 100)
				g2.drawLine(i, 0, i, screenH);
			for(int i = 100;i < screenH;i += 100)
				g2.drawLine(0, i, screenW, i);
			GHQ.translateForGUI(false);
			//Origin
			g2.setColor(debugTextColor);
			g2.setStroke(stroke1);
			g2.drawOval(-35, -35, 70, 70);
			g2.drawOval(-25, -25, 50, 50);
			g2.drawOval(-15, -15, 30, 30);
			//stageEdge
			g2.setStroke(stroke3);
			final int STAGE_W = stage.width(), STAGE_H = stage.height();
			{ //LXRX lines
				final int LX = 0, RX = STAGE_W;
				for(int i = 0;i < 50;i++) {
					final int Y = STAGE_H*i/50;
					g2.drawLine(LX - 20, Y - 20, LX + 20, Y + 20);
					g2.drawLine(RX - 20, Y - 20, RX + 20, Y + 20);
				}
			}
			{ //LYRY lines
				final int LY = 0, RY = STAGE_H;
				for(int i = 0;i < 50;i++) {
					final int X = STAGE_W*i/50;
					g2.drawLine(X - 20, LY - 20, X + 20, LY + 20);
					g2.drawLine(X - 20, RY - 20, X + 20, RY + 20);
				}
			}
			GHQ.translateForGUI(true);
			//entityInfo
			g2.drawString(stage.entityAmountInfo(), 30, 100);
			g2.drawString("LoadTime(ms):" + loadTime_total, 30, 120);
			g2.drawString("FPS:" + DF00_00.format(nowFPS), 30, 140);
			g2.drawString("GameTime(ms):" + gameFrame, 30, 160);
			//guiInfo
			g2.drawString("PointingGUI: " + mouseHoveredUI().name(), 30, 200);
			//mouseInfo
			g2.setColor(debugTextColor);
			g2.setStroke(stroke5);
			final int MOUSE_X = GHQ.mouseX, MOUSE_Y = GHQ.mouseY;
			g2.drawString((int)MOUSE_X + "," + (int)MOUSE_Y, MOUSE_X + 20, MOUSE_Y + 20);
			g2.setStroke(stroke1);
			g2.drawLine(MOUSE_X - 15, MOUSE_Y, MOUSE_X + 15, MOUSE_Y);
			g2.drawLine(MOUSE_X, MOUSE_Y - 15, MOUSE_X, MOUSE_Y + 15);
			g2.setStroke(stroke5);
			g2.drawString("(" + (MOUSE_X - (int)viewX) + "," + (MOUSE_Y - (int)viewY) + ")", MOUSE_X + 20, MOUSE_Y + 40);
			//unitInfo
			stage.unitDebugPaint(g2);
			//ruler
			if(mouseDebugMode) {
				g2.setColor(Color.RED);
				g2.setStroke(stroke1);
				g2.drawString((int)mouseDebugX1 + "," + (int)mouseDebugY1, mouseDebugX1 + 20, mouseDebugY1 + 20);
				if(mouseDebugX2 == GHQ.NONE) {
					g2.drawLine(mouseDebugX1, mouseDebugY1, mouseX, mouseY);
				}else {
					g2.drawLine(mouseDebugX1, mouseDebugY1, mouseDebugX2, mouseDebugY2);
					g2.drawString((int)mouseDebugX2 + "," + (int)mouseDebugY2, mouseDebugX2 + 20, mouseDebugY2 + 20);
					final int DX = mouseDebugX2 - mouseDebugX1, DY = mouseDebugY2 - mouseDebugY1;
					g2.drawString(DX + "," + DY, mouseDebugX1 + DX/2 + 20, mouseDebugY1 + DY/2 + 20);
				}
			}
		}
		g.drawImage(offImage, 0, 0, screenW, screenH, this);
		//increase frame count
		systemFrame++;
		if(stopEventKind == NONE)
			gameFrame++;
		//calculate total paintComponent time
		loadTime_total = (int)(System.currentTimeMillis() - LOAD_TIME_PAINT_COMPONENT);
	}
	
	//information-stage
	public static final GHQStage stage() {
		return stage;
	}
	public static final Game getEngine() {
		return engine;
	}
	//information-GUI
	public static final int screenW(){
		return screenW;
	}
	public static final int screenH(){
		return screenH;
	}
	//information-paint
	public static final boolean inScreen(int x, int y) {
		return abs(viewX - x) < screenW && abs(viewY - y) < screenH;
	}

	//control
	//control-stage
	public static GHQStage setStage(GHQStage stage) {
		return GHQ.stage = stage;
	}
	public static void setStageZoomRate(double value) {
		stageZoomRate = value;
	}
	public static void showCursor(boolean show) {
		if(show) {
			hq.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		} else {
			hq.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(hq.createImage(new MemoryImageSource(0, 0, new int[0], 0, 0)), new java.awt.Point(0, 0), null));
		}
	}
	//control-viewPoint
	public static void viewMove(double dx, double dy) {
		viewX -= dx;viewY -= dy;
		viewDstX -= dx;viewDstY -= dy;
	}
	public static void viewTo(double x, double y) {
		viewDstX = viewX = -x + screenW/2;
		viewDstY = viewY = -y + screenH/2;
	}
	public static void viewTo(Point point) {
		viewTo(point.intX(), point.intY());
	}
	public static void viewTo(HasPoint target) {
		viewTo(target.point());
	}
	public static void pureViewMove(double dx, double dy) {
		viewX -= dx;viewY -= dy;
	}
	public static void pureViewTo(double x, double y) {
		viewX = x;viewY = y;
	}
	public static void viewTargetTo(double x, double y) {
		viewDstX = -x + screenW/2;viewDstY = -y + screenH/2;
	}
	public static void viewTargetMove(double x, double y) {
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
	public static int getScreenCenterStageX() {
		return toStageCoordinate_x(GHQ.screenW/2);
	}
	public static int getScreenCenterStageY() {
		return toStageCoordinate_y(GHQ.screenH/2);
	}
	public static int getScreenLeftX_stageCod() {
		return toStageCoordinate_x(0);
	}
	public static int getScreenTopY_stageCod() {
		return toStageCoordinate_y(0);
	}
	public static int getScreenW_stageCod() {
		return (int)(screenW/stageZoomRate);
	}
	public static int getScreenH_stageCod() {
		return (int)(screenH/stageZoomRate);
	}
	//control-add

	//control-delete
	public static final void paintHPArc(int x, int y, int radius, int hp, int maxHP) {
		g2.setStroke(stroke3);
		if((double)hp/(double)maxHP > 0.75)
			g2.setColor(Color.CYAN);
		else if((double)hp/(double)maxHP > 0.50)
			g2.setColor(Color.GREEN);
		else if((double)hp/(double)maxHP > 0.25)
			g2.setColor(Color.YELLOW);
		else if((double)hp/(double)maxHP > 0.10 || gameFrame % 4 < 2)
			g2.setColor(Color.RED);
		else
			g2.setColor(HPWarningColor);
		g2.drawString(String.valueOf(hp), x + (int)(radius*1.1) + (hp >= 10 ? 0 : 6), y + (int)(radius*1.1));
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.drawArc(x - radius, y - radius, radius*2, radius*2, 90, (int)((double)hp/(double)maxHP*360));
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
	}
	public static final void paintHPArc(int x, int y, int radius, Consumables hp) {
		paintHPArc(x, y, radius, hp.intValue(), hp.max().intValue());
	}
	public static final void paintHPArc(Point point, int radius, int hp, int maxHP) {
		paintHPArc(point.intX(), point.intY(), radius, hp, maxHP);
	}
	public static final void paintHPArc(Point point, int radius, Consumables hp) {
		paintHPArc(point.intX(), point.intY(), radius, hp);
	}
	
	//input
	private static int mouseX,mouseY;
	public static int mousePressButton = GHQ.NONE;
	private static Stack<GUIParts> mouseHoveredUIs = new Stack<GUIParts>();
	private static GUIParts lastClickedUI = BASE_SCREEN_UI;
	private static boolean mouseDebugMode;
	private static int mouseDebugX1, mouseDebugY1;
	private static int mouseDebugX2, mouseDebugY2;
	public static MouseHook mouseHook = new MouseHook();
	public void mouseWheelMoved(MouseWheelEvent e) {
		engine.mouseWheelMoved(e);
	}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mousePressed(MouseEvent e){
		//debug of ruler
		if(e.getButton() == MouseEvent.BUTTON2) { 
			mouseDebugMode = true;
			mouseDebugX2 = GHQ.NONE;
			mouseDebugX1 = mouseX;
			mouseDebugY1 = mouseY;
		}
		//MouseListenerExs event
		switch(e.getButton()) {
		case MouseEvent.BUTTON1:
			mousePressButton = 1;
			for(MouseListenerEx mle : mouseListeners)
				mle.pressButton1Event();
			break;
		case MouseEvent.BUTTON2:
			mousePressButton = 2;
			for(MouseListenerEx mle : mouseListeners)
				mle.pressButton2Event();
			break;
		case MouseEvent.BUTTON3:
			mousePressButton = 3;
			for(MouseListenerEx mle : mouseListeners)
				mle.pressButton3Event();
			break;
		}
		engine.mousePressed(e);
	}
	public void mouseReleased(MouseEvent e){
		if(e.getButton() == MouseEvent.BUTTON2) {
			mouseDebugX2 = mouseX;
			mouseDebugY2 = mouseY;
			System.out.println("RULER: " + (mouseDebugX2 - mouseDebugX1) + ", " + (mouseDebugY2 - mouseDebugY1));
		}
		mousePressButton = NONE;
		for(MouseListenerEx mle : mouseListeners) {
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
		engine.mouseReleased(e);
	}
	@Override
	public void mouseClicked(MouseEvent e){}
	@Override
	public void mouseMoved(MouseEvent e){
		mouseX = e.getX();mouseY = e.getY();
		final Stack<GUIParts> newHoveredUIs = BASE_SCREEN_UI.uiAtCursur();
		final Iterator<GUIParts> newIterator = newHoveredUIs.iterator();
		while(!mouseHoveredUIs.isEmpty()) {
			if(!newIterator.hasNext() || mouseHoveredUIs.peek() != newIterator.next()) {
				while(!mouseHoveredUIs.isEmpty()) {
					mouseHoveredUIs.pop().mouseOut();
				}
			}
		}
		while(newIterator.hasNext())
			newIterator.next().mouseOver();
		mouseHoveredUIs = newHoveredUIs;
		engine.mouseMoved(e);
	}
	public void mouseDragged(MouseEvent e){
		mouseX = e.getX();mouseY = e.getY();
		engine.mouseDragged(e);
	}
	public static void doMouseClickUIEvent(MouseEvent e) {
		final ListIterator<GUIParts> iterator = mouseHoveredUIs.listIterator(mouseHoveredUIs.size());
		while(iterator.hasPrevious()) {
			final GUIParts parts = iterator.previous();
			if(parts.clicked(e))
				break;
		}
	}
	public static void doMouseWheelMovedUIEvent(MouseWheelEvent e) {
		final Iterator<GUIParts> iterator = mouseHoveredUIs.iterator();
		while(iterator.hasNext()) {
			if(iterator.next().mouseWheelMoved(e))
				break;
		}
	}
	public static final int mouseX() {
		return toStageCoordinate_x(mouseX);
	}
	public static final int mouseY() {
		return toStageCoordinate_y(mouseY);
	}
	public static final int toStageCoordinate_x(int x) {
		//return (int)(x/stageZoomRate + screenW/2*(1 - 1/stageZoomRate)) - (int)viewX;
		return (int)((x - screenW/2)/stageZoomRate) + screenW/2 - (int)viewX;
	}
	public static final int toStageCoordinate_y(int y) {
		//return (int)(y/stageZoomRate + screenH/2*(1 - 1/stageZoomRate)) - (int)viewY;
		return (int)((y - screenH/2)/stageZoomRate) + screenH/2 - (int)viewY;
	}
	public static final int toScreenCoordinate_x(int x) { 
		return (int)(x*stageZoomRate - screenW/2*(stageZoomRate - 1)) + (int)viewX;
	}
	public static final int toScreenCoordinate_y(int y) {
		return (int)(y*stageZoomRate - screenH/2*(stageZoomRate - 1)) + (int)viewY;
	}
	public static final boolean intersectsScreen_screenCod(int x, int y, int w, int h) {
		return Math.abs(x - screenW/2) < (screenW + w)/2 && Math.abs(y - screenH/2) < (screenH + h)/2;
	}
	public static final boolean intersectsScreen_stageCod(int x, int y, int w, int h) {
		return intersectsScreen_screenCod(toScreenCoordinate_x(x), toScreenCoordinate_y(y), (int)(w/stageZoomRate), (int)(h/stageZoomRate));
	}
	public static final Point mousePoint() {
		return new Point.IntPoint(mouseX(), mouseY());
	}
	public static final int mouseScreenX(){
		return mouseX;
	}
	public static final int mouseScreenY(){
		return mouseY;
	}
	public static final boolean isMouseInArea_Stage(int x, int y, int w, int h) { //TODO: need be fixed
		return abs(x - mouseX + viewX) < w/2 && abs(y - mouseY + viewY) < h/2;
	}
	public static final GUIParts mouseHoveredUI() {
		return mouseHoveredUIs.isEmpty() ? GUIParts.NULL_GUIPARTS : mouseHoveredUIs.peek();
	}
	public static final Stack<GUIParts> mouseHoveredUIs() {
		return mouseHoveredUIs;
	}
	public static final boolean isMouseHoveredAnyUI() {
		return mouseHoveredUI() != BASE_SCREEN_UI;
	}
	public static final GUIParts lastClickedUI() {
		return lastClickedUI;
	}
	public static final boolean isLastClickedUI(GUIParts parts) {
		return lastClickedUI == parts;
	}
	public static final void setLastClickedUI(GUIParts parts) {
		if(lastClickedUI != parts) {
			lastClickedUI.outsideClicked();
			lastClickedUI = parts;
		}
	}
	/**
	 * Check if the mouse coordinate is in this rectangle area.
	 * @param luX
	 * @param luY
	 * @param w
	 * @param h
	 * @return true - in / false - out
	 */
	public static final boolean isMouseInArea_Screen(int luX, int luY, int w, int h) {
		return luX < mouseX && mouseX < luX + w && luY < mouseY && mouseY < luY + h;
	}
	public final boolean isMouseOnImage(Image img, int x, int y) {
		return isMouseInArea_Stage(x, y, img.getWidth(null), img.getHeight(null));
	}
	//キー情報
	public static boolean key_1,key_2,key_3,key_4;
	public static long key_1_time,key_2_time,key_3_time,key_4_time;
	public static boolean key_W,key_A,key_S,key_D,key_enter;
	public static boolean key_shift;
	public void keyPressed(KeyEvent e){
		final int KEY_CODE = e.getKeyCode();
		for(KeyListenerEx kle : keyListeners)
			kle.pressEvent(KEY_CODE);
		switch(KEY_CODE){
		case VK_1:
			key_1 = true;
			key_1_time = nowTime();
			break;
		case VK_2:
			key_2 = true;
			key_2_time = nowTime();
			break;
		case VK_3:
			key_3 = true;
			key_3_time = nowTime();
			break;
		case VK_4:
			key_4 = true;
			key_4_time = nowTime();
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
			if(!debugMode)
				mouseDebugMode = false;
			break;
		case VK_F5:
			freezeScreen = !freezeScreen;
			break;
		}
		engine.keyPressed(e);
	}
	public void keyReleased(KeyEvent e){
		final int KEY_CODE = e.getKeyCode();
		for(KeyListenerEx kle : keyListeners)
			kle.releaseEvent(KEY_CODE);
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
		engine.keyReleased(e);
	}
	public void keyTyped(KeyEvent e){
		for(KeyTypeListener ktl : typeListeners) {
			if(ktl.isEnabled())
				ktl.typed(e.getKeyChar());
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
	/**
	 * Clear freeze screen and other stopEvents.
	 * @since alpha1.0
	 */
	public static final void clearStopEvent() {
		stopEventKind = NONE;
	}
	
	//generation
	/**
	 * Add a {@link GUIParts}.(Enable it automatically.)
	 * @param guiParts
	 * @return added GUIParts
	 */
	public static final <T extends GUIParts>T addGUIParts(T guiParts) {
		if(guiParts == null) {
			System.out.println("GHQ.addGUIParts recieved a null guiParts.");
			return null;
		}
		GHQ.BASE_SCREEN_UI.addLast(guiParts);
		return guiParts;
	}
	public static final GHQObject getMouseOveredGHQObject() {
		stage().forMouseOver_stage();
		return null;
	}
	/**
	 * Add a {@link MouseListenerEx}.(Doesn't enable it automatically.)
	 * @param mle
	 * @return added GUIParts
	 */
	public static final void addListenerEx(MouseListenerEx mle) {
		mouseListeners.add(mle);
	}
	/**
	 * Add a {@link KeyListenerEx}.(Doesn't enable it automatically.)
	 * @param kle
	 * @return added GUIParts
	 */
	public static final void addListenerEx(KeyListenerEx kle) {
		keyListeners.add(kle);
	}
	/**
	 * Add a {@link KeyTypeListener}.(Doesn't enable it automatically.)
	 * @param kte
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
	public static final int nowFrame() {
		return gameFrame;
	}
	/**
	 * Returns passed frame that independents from game pause.
	 * @return now gameFrame
	 * @since 1.0
	 */
	public static final int systemFrame() {
		return systemFrame;
	}
	/**
	 * Returns frames passed from the indicated frame.
	 * @param frame
	 * @return passed frames
	 * @since 1.0
	 */
	public static final int passedFrame(int frame) {
		return frame == NONE ? MAX : gameFrame - frame;
	}
	/**
	 * Check if passed frames from the indicated frame is much than the limit.
	 * @return if expired
	 * @since 1.0
	 */
	public static final boolean isExpired_frame(int initialFrame, int limitFrame) {
		return initialFrame == NONE || (gameFrame - initialFrame) >= limitFrame;
	}
	/**
	 * Returns systemTime. {@link System#currentTimeMillis()}
	 * @return now systemTime
	 * @since 1.0
	 */
	public static final long nowTime() {
		return System.currentTimeMillis();
	}
	/**
	 * Returns time passed from the indicated time. {@link System#currentTimeMillis()}
	 * @param time
	 * @return passed time
	 * @since 1.0
	 */
	public static final int passedTime(long time) {
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
	public static final boolean isExpired_dynamicSeconds(int frame, double seconds) {
		return frame == NONE || passedFrame(frame) > nowFPS*seconds;
	}
	public static final boolean checkSpan_dynamicSeconds(double seconds) {
		final int SPAN = (int)(nowFPS * seconds);
		return SPAN == 0 ? true : gameFrame % SPAN == 0;
	}
	public static final double getFPS() {
		return nowFPS;
	}
	public static final double getSPF() {
		return 1.0/nowFPS;
	}
	public static final double mulSPF(double value) {
		return getSPF()*value;
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
	
	//stage test area
	final private void resetStage(){
		if(stage != null)
			stage.clear();
		ErrorCounter.clear();
		gameFrame = 0;
		System.gc();
		//System.out.println("stage reset done");
		stage = engine.loadStage();
	}
	
	//ResourceLoad
	private static final LinkedList<LoadRequester> loadRequesters = new LinkedList<LoadRequester>();
	public static final void addLoadRequester(LoadRequester loadRequire) {
		if(loadComplete)
			loadRequire.loadResource();
		else
			loadRequesters.add(loadRequire);
	}

	/**
	* Load the font file.
	* @param filename
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
	
	public final ArrayList<Unit> loadAllUnit(URL url) {
		final FilenameFilter classFilter = new FilenameFilter(){
			public boolean accept(File dir, String name){
				return name.endsWith(".class");
			}
		};
		File unitFolder = null;
		try{
			unitFolder = new File(url.toURI());
		}catch(URISyntaxException | NullPointerException e){
			System.out.println("Unable to load Unit class.");
		}
		if(unitFolder == null){
			System.out.println("Pass does not exist.");
			return new ArrayList<Unit>();
		}
		final ArrayList<Unit> unitClass = new ArrayList<Unit>();
		for(String unitName : unitFolder.list(classFilter)){
			try{
				final Object obj = Class.forName("unit." + unitName.substring(0,unitName.length() - 6)).newInstance();
				if(obj instanceof Unit){
					final Unit cls = (Unit)obj;
					cls.loadImageData();
					unitClass.add(cls);
				}
			}catch(InstantiationException e) {
				System.out.println("ignored abstract class: " + unitName);
			}catch(ClassNotFoundException | IllegalAccessException e){
				System.out.println(e);
			}
		}
		return unitClass;
	}
	public static final void setScreenSize(int w, int h) {
		screenW = w;
		screenH = h;
	}
	
	//PaintTool
	/**
	 * Change paint coordinate for GUI/stageObjects.
	 * Note that if you don't reset changed paint coordinate, it will collapse coordinates of following paints.(like Graphics2D.rotate method)
	 * @param forGUI true - change for GUI / false - change for objects in its stage;
	 * @since alpha1.0
	 */
	public static final void translateForGUI(boolean forGUI) {
		final int tx = (int)GHQ.getScreenCenterStageX(), ty = (int)GHQ.getScreenCenterStageY();
		final double zoomRate = stageZoomRate;
		if(forGUI) {
			if(zoomRate != 1.0) {
				g2.translate(tx, ty);
				final double _zoomRate = 1/zoomRate;
				GHQ.scale(_zoomRate, _zoomRate);
				g2.translate(-tx, -ty);
			}
			g2.translate(-viewX, -viewY);
		}else {
			g2.translate(viewX, viewY);
			if(zoomRate != 1.0) {
				g2.translate(tx, ty);
				GHQ.scale(zoomRate, zoomRate);
				g2.translate(-tx, -ty);
			}
		}
	}
	/**
	 * Call {@link Graphics2D#setClip(int, int, int, int)} directly.
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @since alpha1.0
	 */
	public static final void setClip(int x, int y, int width, int height) {
		g2.setClip(x, y, width, height);
	}
	public static final void scale(double sx, double sy) {
		g2.scale(sx, sy);
	}
	public static final void scale(double s) {
		g2.scale(s, s);
	}
	public static final void rotate(double angle, int x, int y) {
		g2.rotate(angle, x, y);
	}
	public static final void rotate(double angle, Point point) {
		rotate(angle, point.intX(), point.intY());
	}
	/**
	 * Reset AlphaComposite value.
	 * @since alpha1.0
	 */
	public static final void setImageAlpha() {
		g2.setComposite(AlphaComposite.SrcOver);
	}
	/**
	 * Set AlphaComposite's transparent value.
	 * @float alpha transparent degree
	 * @since alpha1.0
	 */
	public static final void setImageAlpha(float alpha) {
		if(alpha < 0)
			alpha = 0;
		else if(alpha > 1F)
			alpha = 1F;
		g2.setComposite(AlphaComposite.SrcOver.derive(alpha));
	}
	public static final float getFadingAlpha(int initialFrame, int limitFrame) {
		final float f = (float)(1.0 - (double)GHQ.passedFrame(initialFrame)/limitFrame);
		return f < 0 ? 0 : (f > 1 ? 1 : f);
	}
	//Paint
	static boolean doXFlip, doYFlip;
	static ImageObserver targetImageObserver;
	/**
	* Draw an image.
	* @param img 
	* @param x 
	* @param y 
	* @param w 
	* @param h 
	* @since alpha1.0
	*/
	public static final void drawImageGHQ(Image img, int x, int y, int w, int h){
		if(doXFlip || doYFlip) {
			final double xs = doXFlip ? -1 : 1;
			final double ys = doYFlip ? -1 : 1;
			g2.translate(x + w/2, y + h/2);
			g2.scale(xs, ys);
			g2.drawImage(img, -w/2, -h/2, w, h, targetImageObserver);
			g2.scale(xs, ys);
			g2.translate(-x - w/2, -y - h/2);
		}else
			g2.drawImage(img, x, y, w, h, targetImageObserver);
	}
	public static final void drawImageGHQ(Image img, int x, int y, int w, int h, double angle) {
		if(angle != 0.0) {
			final double OX = x + w/2, OY = y + h/2;
			g2.rotate(angle, OX, OY);
			drawImageGHQ(img, x, y, w, h);
			g2.rotate(-angle, OX, OY);
		}else
			drawImageGHQ(img, x, y, w, h);
	}
	/**
	* Draw an image.
	* @param img 
	* @param x 
	* @param y 
	* @param angle 
	* @since alpha1.0
	*/
	public static final void drawImageGHQ_center(Image img, int x, int y, double angle){
		if(img == null)
			return;
		if(angle != 0.0) {
			g2.rotate(angle, x, y);
			drawImageGHQ(img, x - img.getWidth(null)/2, y - img.getHeight(null)/2, img.getWidth(null), img.getHeight(null));
			g2.rotate(-angle, x, y);
		}else
			drawImageGHQ(img, x - img.getWidth(null)/2, y - img.getHeight(null)/2, img.getWidth(null), img.getHeight(null));
	}
	/**
	* Draw an image.
	* @param img 
	* @param x 
	* @param y 
	* @since alpha1.0
	*/
	public static final void drawImageGHQ_center(Image img, int x, int y){
		//g2.scale(-1.0, 1.0);
		drawImageGHQ(img, x - img.getWidth(null)/2, y - img.getHeight(null)/2, img.getWidth(null), img.getHeight(null));
		//g2.scale(-1.0, 1.0);
	}
	public static final void drawStringGHQ(String string, int x, int y, Font tmpFont) {
		final Font FONT = g2.getFont();
		g2.setFont(tmpFont);
		g2.drawString(string, x, y);
		g2.setFont(FONT);
	}
	public static final void drawStringGHQ(String string, int x, int y, float fontSize) {
		drawStringGHQ(string, x, y, g2.getFont().deriveFont(fontSize));
	}
	public static final void drawStringGHQ(String string, int x, int y, int fontStyle, float fontSize) {
		drawStringGHQ(string, x, y, g2.getFont().deriveFont(fontStyle, fontSize));
	}
	public static final void drawStringGHQ(String string, int x, int y) {
		g2.drawString(string, x, y);
	}
	public static final void drawStringGHQ(String string, int x, int y, int lineH, int lineWordAmount) {
		int nowIndex = 0;
		while(!string.isEmpty()) {
			if(nowIndex + lineWordAmount < string.length())
				g2.drawString(string.substring(nowIndex, nowIndex + lineWordAmount), x, y);
			else {
				g2.drawString(string.substring(nowIndex), x, y);
				break;
			}
			nowIndex += lineWordAmount;
			y += lineH;
		}
	}
	public static final void setFlip(boolean doXFlip, boolean doYFlip) {
		GHQ.doXFlip = doXFlip ^ GHQ.doXFlip;
		GHQ.doYFlip = doYFlip ^ GHQ.doYFlip;
	}
	public static final void setTargetImageObserver(ImageObserver imageObserver) {
		targetImageObserver = imageObserver;
	}
	public static final void setTargetImageObserver() {
		targetImageObserver = hq;
	}
	/**
	 * Return Graphics2D instance.
	 * @return
	 */
	public static final Graphics2D getG2D() {
		return g2;
	}
	/**
	 * Return Graphics2D instance, and set color.
	 * @return
	 */
	public static final Graphics2D getG2D(Color color) {
		g2.setColor(color);
		return g2;
	}
	/**
	 * Return Graphics2D instance, and set color and stroke.
	 * @return
	 */
	public static final Graphics2D getG2D(Color color, Stroke stroke) {
		g2.setColor(color);
		g2.setStroke(stroke);
		return g2;
	}
	public static final Graphics2D getG2D(Color color, float strokeSize) {
		g2.setColor(color);
		g2.setStroke(new BasicStroke(strokeSize));
		return g2;
	}
	
	//math & string
	public static final int toStageX(int screenX) {
		return screenX - (int)viewX;
	}
	public static final int toStageY(int screenY) {
		return screenY - (int)viewY;
	}
	public static final double angleFormat(double radian){ //ラジアン整理メソッド -PI~+PIに直す
		radian %= PI*2;
		if(radian > PI)
			radian -= PI*2;
		else if(radian <= -PI)
			radian += PI*2;
		return radian;
	}
	public static final Random random = new Random();
	public static final int random2(int value1, int value2) {
		if(value1 == value2)
			return value1;
		else if(value1 > value2)
			return new Random().nextInt(abs(value1 - value2) + 1) + value2;
		else
			return new Random().nextInt(abs(value2 - value1) + 1) + value1;
	}
	public static final double random2(double value1, double value2) {
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
	public static final int arrangeIn(int value, int min, int max) {
		if(value < min)
			return min;
		if(value > max)
			return max;
		return value;
	}
	//message window
	/**
	* Show a warning message window.
	* @param message
	* @param title
	* @return The time passed while this window is shown.
	* @since alpha1.0
	*/
	public static final long warningBox(String message, String title){
		long openTime = System.currentTimeMillis();
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
		return System.currentTimeMillis() - openTime;
	}
}