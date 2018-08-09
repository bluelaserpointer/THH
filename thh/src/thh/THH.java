package thh;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import static java.awt.event.KeyEvent.*;
import javax.swing.*;

import bullet.Bullet2;
import bullet.BulletInfo;

import java.io.*;
import java.net.*;
import java.util.*;
import static java.lang.Math.*;

final public class THH extends JPanel implements MouseListener,MouseMotionListener,MouseWheelListener,KeyListener,Runnable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 123412351L;

	//フィールド
	//システム関連
	//File Pass
	final String
		ASSETS_URL = "assets",
		CHARA_FOLDERS_URL = "assets/chara",
		LOCAL_IMAGE_URL = "assets/image",
		LOCAL_SOUND_URL = "assets/sound",
		LOCAL_FONT_URL = "assets/font";
	
	//debug
	private boolean freezeScreen,debugMode;
	private long loadTime_total;

	//others
	final static String
		GAME_VERSION = "Ver beta1.0.0";
	final public static int
		NONE = -999999999, //値なし
		EXIST = 888888888, //
		MAX = Integer.MAX_VALUE, //最大
		MIN = Integer.MIN_VALUE; //最小
	
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
	
	//ウィンドウ関連
	final int defaultScreenW = 1000,defaultScreenH = 600; //デフォルトウィンドサイズ
	int screenW = 1000,screenH = 600,displayW,displayH;
	int viewX,viewY;
	
	int page,page_max; //ページ機能
	
	//タイム情報
	private int gameTime,clearFrame; //ゲーム関係時間
	private int nowFrame; //現在フレーム
	private long nowTime; //現在ミリ秒時間
	
	//stage Info
	private int nowStage; //現在ステージ
	private int stageW = 2000,stageH = 2000;
	
	//キャラクター情報
	private int chara = NONE; //峴摦拞偺恖
	private Chara[] charaClass,battleCharaClass = new Chara[0];
	private int charaTeam[];
	
	//bullet data
	public ArrayList<Bullet2> bullets = new ArrayList<Bullet2>();
	
	//effect data
	public final int[]
		effectChara = new int[1024],
		effectKind = new int[1024],
		effectAppearedFrame = new int[1024],
		effectX = new int[1024],
		effectY = new int[1024];
	private int effect_maxID = -1;
	private int effect_total;
	
	//ResourceSystem
	private Image[] arrayImage = new Image[128];
	private String[] arrayImageURL = new String[128];
	private int arrayImage_maxID = -1;
	private SoundClip[] arraySound = new SoundClip[128];
	private String[] arraySoundURL = new String[128];
	private int arraySound_maxID = -1;
	
	//Initialize methods/////////////
	
	public static void main(String args[]){
		new THH();
	}
	private final JFrame myFrame;
	private final MediaTracker tracker;
	public THH(){
		final long loadTime = System.currentTimeMillis();
		//僂傿儞僪僂僙僢僩傾僢僾
		myFrame = new JFrame("搶曽榋抏枊愢");
		myFrame.add(this,BorderLayout.CENTER); //僉儍儞僶僗傪愝抲
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
		//load chara classes
		loadChara:{
			final FilenameFilter classFilter = new FilenameFilter(){
					public boolean accept(File dir,String name){
						return name.endsWith(".class");
					}
				};
			int classNumber = 0;
			charaClass = new Chara[64];
			File charaFolder = null;
			try{
				charaFolder = new File(getClass().getResource("../chara").toURI());
			}catch(URISyntaxException e){
			}catch(NullPointerException e) {
				System.out.println("ファイル構造に問題が発生しています。");
			}
			if(charaFolder == null){
				System.out.println("charaフォルダが見つかりません");
				charaClass = new Chara[0];
				break loadChara;
			}
			for(String className : charaFolder.list(classFilter)){
				try{
					final Object obj = Class.forName("chara." + className.substring(0,className.length() - 6)).newInstance();
					if(obj instanceof Chara){
						final Chara cls = (Chara)obj;
						cls.giveInstance(this);
						cls.loadImageData();
						cls.loadSoundData();
						charaClass[classNumber++] = cls;
					}
				}catch(ClassNotFoundException | InstantiationException | IllegalAccessException e){
					System.out.println(e);
				}
			}
			charaClass = Arrays.copyOf(charaClass,classNumber);
			System.out.println("loaded chara class: " + charaClass.length);
			resetData();
		}
		try{
			tracker.waitForAll(); //夋憸儘乕僪
		}catch(InterruptedException | NullPointerException e){}
		
		System.out.println("loadTimeReslut: " + (System.currentTimeMillis() - loadTime));//儘乕僪強梫帪娫寢壥昞帵
		new Thread(this).start();
	}
	final void loadConfig(boolean isFirstReload){
		/*
		//コンフィグ本体読み込み
		if(!isFirstReload)
			System.out.println("\nStart Config Reload...");
		final long usedTime = System.currentTimeMillis();
		try(ObjectInputStream ois = new ObjectInputStream(getClass().getResourceAsStream(CFG_URL))){
			cfg = (Config)ois.readObject();
		}catch(IOException | ClassNotFoundException e){
			JOptionPane.showMessageDialog(null,CFG_URL + "の読み込みに失敗しました。\n全ステージに入れない可能性があります。\n「ConfigLoaderコンパイル.bat」の起動をお試しください。","config読み込みエラー",JOptionPane.ERROR_MESSAGE);
			cfg = new Config();
		}
		//所要時間表示
		if(firstLoad)
			System.out.println("Config&Mod: " + (System.currentTimeMillis() - usedTime));
		else{
			System.out.println("config reload complete!");
			System.out.println("TimeUsed: " + (System.currentTimeMillis() - usedTime) + "(ms)");
		}*/
	}
	//mainLoop///////////////
	public void run(){
		//titleBGM.loop();
		//try{
			while(true){
				nowTime = System.currentTimeMillis();
				try{
					Thread.sleep(25L);
				}catch(InterruptedException e){}
				if(freezeScreen)
					continue;
				if(mainEvent != BATTLE_PAUSE){
					nowFrame++;
					if(mainEvent == BATTLE){
					}
				}
				repaint();
				if(mainEvent == BATTLE)
					gameTime += (System.currentTimeMillis() - nowTime);
			}
		//}catch(Exception e){
			//JOptionPane.showMessageDialog(null, "怽偟栿偁傝傑偣傫偑丄僄儔乕偑敪惗偟傑偟偨丅\n僄儔乕僐乕僪丗" + e.toString(),"僄儔乕",JOptionPane.ERROR_MESSAGE);
		//}
	}
	
	private BufferedImage offImage = new BufferedImage(defaultScreenW,defaultScreenH,BufferedImage.TYPE_INT_ARGB_PRE); //僟僽儖僶僢僼傽僉儍儞僶僗
	private Graphics2D g2;
	private final Font basicFont = createFont("font/upcibi.ttf").deriveFont(Font.BOLD + Font.ITALIC,30.0f),commentFont = createFont("font/HGRGM.TTC").deriveFont(Font.PLAIN,15.0f);
	private final BasicStroke stroke1 = new BasicStroke(1f),stroke2 = new BasicStroke(2f),stroke3 = new BasicStroke(3f),stroke5 = new BasicStroke(5f),stroke10 = new BasicStroke(10f);
	private final Color HPWarningColor = new Color(255,120,120),debugTextColor = new Color(200,200,200,160);
	private final Rectangle2D screenRect = new Rectangle2D.Double(0,0,defaultScreenW,defaultScreenH);
	
	private int[] poliX = {0,0,300,400,500,600,700,700,},poliY = {650,450,450,350,350,450,450,650};
	private Polygon landPolygon = new Polygon(poliX,poliY,poliX.length);
	public void paintComponent(Graphics g){
		final long LOAD_TIME_PAINTCOMPONENT = System.currentTimeMillis();
		super.paintComponent(g);
		if(g2 == null){
			g2 = offImage.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
			g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_COLOR_RENDER_SPEED);
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			g2.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_SPEED);
		}
		g2.setColor(Color.WHITE);
		g2.fill(screenRect);
		final int TRANSLATE_X = viewX,TRANSLATE_Y = viewY;
		g2.translate(TRANSLATE_X, TRANSLATE_Y);
		g2.setColor(Color.GRAY);
		g2.fill(landPolygon);
		////////////////////////////////////////////////////////////////////////
		//charaAction
		{
			for(int i = 0;i < battleCharaClass.length;i++)
				battleCharaClass[i].idle(chara == i);
		}
		////////////////////////////////////////////////////////////////////////
		//bulletAction
		{
			for(Bullet2 bullet : bullets.toArray(new Bullet2[0])) {
				final int gunnerID = bullet.SOURCE;
				if(0 <= gunnerID && gunnerID < battleCharaClass.length)
					battleCharaClass[gunnerID].bulletIdle(bullet,chara == gunnerID);
			}
		}
		///////////////////////////////////////////////////////////////
		//effectAction
		{
			for(int i = 0;i <= effect_maxID;i++) {
				if(effectKind[i] != NONE) {
					final int charaID = effectChara[i];
					battleCharaClass[charaID].effectIdle(i,effectKind[i],chara == charaID);
				}
			}
		}
		///////////////////////////////////////////////////////////////
		//GUIAction
		{
			//mouseInfo
			g2.setColor(Color.GRAY);
			g2.setStroke(stroke5);
			g2.setFont(basicFont);
			//bulletInfo
			if(debugMode){
				g2.setColor(debugTextColor);
				for(int i = 100;i < defaultScreenW;i += 100)
					g2.drawLine(i,0,i,defaultScreenH);
				for(int i = 100;i < defaultScreenH;i += 100)
					g2.drawLine(0,i,defaultScreenW,i);
				g2.setColor(debugTextColor);
				g2.drawString(mouseX + "," + mouseY,mouseX + 20,mouseY + 20);
				g2.drawString("Chara:" + battleCharaClass.length + " EF:" + 0 + " B:" + bullets.size(),30,100);
				g2.drawString("LoadTime(ms):" + loadTime_total,30,120);
				//g2.drawString("EM:" + loadTime_enemy + " ET:" + loadTime_entity + " G:" + loadTime_gimmick + " EF:" + loadTime_effect + " B:" + loadTime_bullet + " I:" + loadTime_item + " W: " + loadTime_weapon + " Other: " + loadTime_other,30,140);
				g2.drawString("GameTime(ms):" + gameTime,30,160);
				g2.setColor(debugTextColor);
				g2.drawString("(" + (mouseX - viewX) + "," + (mouseY - viewY) + ")",mouseX + 20,mouseY + 40);
			}
			//keyScroll
			final int SCROLL_SPEED = 8;
			if(key_W) {
				viewY += SCROLL_SPEED;
				middleDragGapY += SCROLL_SPEED;
			}else if(key_S) {
				viewY -= SCROLL_SPEED;
				middleDragGapY -= SCROLL_SPEED;
			}
			if(key_A) {
				viewX += SCROLL_SPEED;
				middleDragGapX += SCROLL_SPEED;
			}else if(key_D) {
				viewX -= SCROLL_SPEED;
				middleDragGapX -= SCROLL_SPEED;
			}
		}
		g2.translate(-TRANSLATE_X, -TRANSLATE_Y);
		//UI昤夋
		g2.setColor(Color.GRAY);
		g2.fillRect(0,0,defaultScreenW,40);
		g.drawImage(offImage,0,0,screenW,screenH,this);
		loadTime_total = System.currentTimeMillis() - LOAD_TIME_PAINTCOMPONENT;
	}
	
	public final int[] callBulletEngage(Bullet2 bullet) {
		int[] result = new int[battleCharaClass.length];
		int searched = 0;
		for(int i = 0;i < battleCharaClass.length;i++) {
			if(battleCharaClass[i].bulletEngage(bullet))
				result[searched++] = i;
		}
		return Arrays.copyOf(result, searched);
	}
	public final Chara getCharaClass(int charaID) {
		return battleCharaClass[charaID];
	}
	public final int getCharaTeam(int charaID) {
		return charaTeam[charaID];
	}
	public final Image getImageByID(int imageID) {
		return arrayImage[imageID];
	}

	//tool
	public final boolean deleteBullet(Bullet2 bullet) {
		if(battleCharaClass[bullet.SOURCE].deleteBullet(bullet))
			return bullets.remove(bullet);
		return false;
	}
	public final void deleteEffect(int id) {
		if(battleCharaClass[effectChara[id]].deleteEffect(effectKind[id],id)) {
			effectKind[id] = NONE;
			effect_total--;
			if(id == effect_maxID) {
				do {
					effect_maxID--;
				}while(effect_maxID > -1 && effectKind[effect_maxID] == NONE);
			}
		}
	}
	public final void fastFillArray(int[] array,int[] targetIDs,int value){
		for(int targetID : targetIDs){
			if(0 <= targetID && targetID < array.length)
				array[targetID] = value;
			else if(targetID != NONE)
				System.out.println("<Warning> fastFillArray detected a illegal targetID (" + targetID + ")");
		}
	}
	public final void fastFillArray(int[] array,int[] targetIDs,int[] values){
		if(targetIDs.length != values.length){
			System.out.println("<SystemError> fastFillArray called illegally: length of targetIDs(" + targetIDs.length + ") and length of values(" + values.length + ") are different");
			return;
		}
		for(int i = 0;i < targetIDs.length;i++){
			if(targetIDs[i] < array.length)
				array[targetIDs[i]] = values[i];
		}
	}
	public final void rollCharaTurn(){
		if(++chara == battleCharaClass.length)
			chara = 0;
		battleCharaClass[chara].turnStarted();
	}
	public final void paintHPArc(int x,int y,int hp,int maxHP) {
		g2.setStroke(stroke5);
		if((double)hp/(double)maxHP > 0.75) //HPが少なくなるごとに色が変化
			g2.setColor(Color.CYAN); //水色
		else if((double)hp/(double)maxHP > 0.50)
			g2.setColor(Color.GREEN); //緑色
		else if((double)hp/(double)maxHP > 0.25)
			g2.setColor(Color.YELLOW); //黄色
		else if((double)hp/(double)maxHP > 0.10 || nowFrame % 4 < 2)
			g2.setColor(Color.RED); //赤色
		else
			g2.setColor(HPWarningColor);
		g2.drawString(String.valueOf(hp),x + (hp >= 10 ? 61 : 67),y + 60);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2.drawArc(x - 65,y - 65,130,130,90,(int)((double)hp/(double)maxHP*360));
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
	}
	public final int receiveDamageInSquare(int team,int hp,int x,int y,int size){ //自処理被弾(弾消滅)
		for(int i = 0;i < bullets.size() && hp > 0;i++){
			final Bullet2 bullet = bullets.get(i);
			if(team != bullet.team && bullet.atk > 0 && squreCollision((int)bullet.x,(int)bullet.y,bullet.SIZE,x,y,size)){ //衝突
				hp -= bullet.atk;
				battleCharaClass[bullet.SOURCE].bulletHitObject(bullet);
			}
		}
		return hp; //残ったHPを返す
	}
	public final int receiveBulletInCircle(int hp,int x,int y,int size,int team){ //自処理被弾(弾消滅)
		//Editting
		return 0;
	}
	public final Bullet2 searchBulletInSquare_single(int x,int y,int size,int team){
		for(Bullet2 bullet : bullets.toArray(new Bullet2[0])){
			if(team != bullet.team && squreCollision((int)bullet.x,(int)bullet.y,bullet.SIZE,x,y,size)) //衝突
				return bullet; //return ID
		}
		return null; //Not found
	}
	public final Bullet2[] searchBulletInSquare_multiple(int x,int y,int size,int team){
		Bullet2[] foundIDs = new Bullet2[bullets.size()];
		int foundAmount =  0;
		for(Bullet2 bullet : bullets.toArray(new Bullet2[0])){
			if(team != bullet.team && squreCollision((int)bullet.x,(int)bullet.y,bullet.SIZE,x,y,size)) //衝突
				foundIDs[foundAmount++] = bullet; //record ID
		}
		return Arrays.copyOf(foundIDs,foundAmount);
	}
	public final boolean squreCollision(int x1,int y1,int size1,int x2,int y2,int size2) {
		final int halfSize = (size1 + size2)/2;
		return abs(x1 - x2) < halfSize && abs(y1 - y2) < halfSize;
	}
	public final boolean hitLandscape(int x,int y,int w,int h){
		return landPolygon.intersects(x - w/2,y + h/2,w,h);
	}
	public final boolean inStage(int x,int y) {
		return 0 <= x && x < stageW && 0 <= y && y < stageH;
	}
	
	//input
	private int mouseX,mouseY;
	private boolean mouseLeftPress,mouseMiddlePress,mouseRightPress;
	private int mouseLeftCount,mouseMiddleCount,mouseRightCount;
	private int mouseLeftPressedFrame,mouseMiddlePressedFrame,mouseRightPressedFrame;
	private int mouseWheelMovedFrame;
	private int mouseWheelMoveAmount;
	private int mousePointing = NONE; //マウスがポイントしているもの
	private int middleDragGapX,middleDragGapY;
	
	public void mouseWheelMoved(MouseWheelEvent e){
		mouseWheelMovedFrame = nowFrame;
		mouseWheelMoveAmount = e.getWheelRotation();
	}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mousePressed(MouseEvent e){
		switch(e.getButton()) {
		case MouseEvent.BUTTON1:
			mouseLeftPress = true;
			mouseLeftPressedFrame = nowFrame;
			mouseLeftCount++;
			break;
		case MouseEvent.BUTTON2:
			mouseMiddlePress = true;
			mouseMiddleCount++;
			middleDragGapX = viewX - mouseX;
			middleDragGapY = viewY - mouseY;
			break;
		case MouseEvent.BUTTON3:
			mouseRightPress = true;
			mouseRightPressedFrame = nowFrame;
			mouseRightCount++;
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
			viewX = x + middleDragGapX;
			viewY = y + middleDragGapY;
		}
	}
	public final int getMouseX(){
		return mouseX - viewX;
	}
	public final int getMouseY(){
		return mouseY - viewY;
	}
	public final boolean getMouseLeftPress(){
		return mouseLeftPress;
	}
	public final boolean getMouseMiddlePress(){
		return mouseMiddlePress;
	}
	public final boolean getMouseRightPress(){
		return mouseRightPress;
	}
	public final int getMouseLeftCount(){
		return mouseLeftCount;
	}
	public final int getMouseMiddleCount(){
		return mouseMiddleCount;
	}
	public final int getMouseRightCount(){
		return mouseRightCount;
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
	public final int getMouseWheelMovedFrame() {
		return mouseWheelMovedFrame;
	}
	public final int getMouseWheelMoveAmount() {
		return mouseWheelMoveAmount;
	}
	public final boolean isMouseInArea(int x,int y,int w,int h) {
		return abs(x - mouseX + viewX) < w/2 && abs(y - mouseY + viewY) < h/2;
	}
	public final boolean isMouseOnImage(int imgID,int x,int y) {
		final Image img = arrayImage[imgID];
		return this.isMouseInArea(x,y,img.getWidth(null),img.getHeight(null));
	}
	//僉乕忣曬
	private boolean key_W,key_A,key_S,key_D;
	private final int[] keyInputFrame = new int[1024];
	public void keyPressed(KeyEvent e){
		final int KEY_CODE = e.getKeyCode();
		keyInputFrame[KEY_CODE] = nowFrame;
		switch(KEY_CODE){
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
	public final void createBullet(){ //弾生成
		bullets.add(new Bullet2());
	}
	public final void createBullet(int amount) {
		for(int i = 0,id = bullets.size();i < amount;i++,id++)
			bullets.add(new Bullet2());
	}
	public final void createBullet_RoundDesign(int amount,double gunnerX,double gunnerY,double radius){
		final double ANGLE = 2*PI/amount;
		for(int i = 0;i < amount;i++){
			BulletInfo.x = gunnerX + radius*cos(ANGLE*i);
			BulletInfo.y = gunnerY + radius*sin(ANGLE*i);
			this.createBullet();
		}
	}
	public final int createEffectID_cut(){ //弾ID生成
		for(int i = 0;i < effectKind.length;i++){
			if(effectKind[i] == NONE){
				effectKind[i] = EXIST;
				if(i > effect_maxID)
					effect_maxID = i;
				effect_total++;
				effectAppearedFrame[i] = nowFrame;
				return i;
			}
		}
		return NONE;
	}
	public final int createEffectID_safe(){ //弾ID生成
		for(int i = 0;i < effectKind.length;i++){
			if(effectKind[i] == NONE){
				effectKind[i] = EXIST;
				if(i > effect_maxID)
					effect_maxID = i;
				effect_total++;
				effectAppearedFrame[i] = nowFrame;
				return i;
			}
		}
		return effectKind.length - 1;
	}
	public final int[] createEffectID_cut(int amount){ //弾ID生成
		int[] ids = new int[amount];
		int j = 0;
		for(int i = 0;j < amount;i++){
			if(effectKind[i] == NONE){
				effectKind[i] = EXIST;
				effectAppearedFrame[i] = nowFrame;
				if(i > effect_maxID)
					effect_maxID = i;
				effect_total++;
				ids[j++] = i;
			}
			if(++i >= effectKind.length) {
				ids = Arrays.copyOf(ids, j);
				break;
			}
		}
		return ids;
	}
	public final int[] createEffectID_safe(int amount){ //弾ID生成
		final int[] ids = new int[amount];
		int j = 0;
		for(int i = 0;j < amount;i++){
			if(effectKind[i] == NONE){
				effectKind[i] = EXIST;
				effectAppearedFrame[i] = nowFrame;
				if(i > effect_maxID)
					effect_maxID = i;
				effect_total++;
				ids[j++] = i;
			}
			if(++i >= effectKind.length) {
				while(++j < amount)
					ids[j] = effectKind.length - 1;
				break;
			}
		}
		return ids;
	}
	public final int getNowFrame() {
		return nowFrame;
	}
	public final int getPassedFrame(int frame) {
		return nowFrame - frame;
	}
	public final boolean isExpired(int appearFrame,int limitFrame) {
		return (nowFrame - appearFrame) >= limitFrame;
	}
	public final void prepareBulletInfo() {
		BulletInfo.clear();
		BulletInfo.nowFrame = nowFrame;
	}
	
	//premade stage test area
	final private void resetData(){
		bullets.clear();
		Arrays.fill(effectKind, NONE);
		System.out.println("add characters to the game");
		if(charaClass.length > 0){
			battleCharaClass = new Chara[]{charaClass[0],charaClass[1]};
			battleCharaClass[0].battleStarted(0);
			battleCharaClass[1].battleStarted(1);
			battleCharaClass[0].spawn(0,0,50,200);
			battleCharaClass[1].spawn(1,1,400,200);
			charaTeam = new int[]{0,1};
			chara = 0;
		}else
			battleCharaClass = new Chara[0];
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
	public final Graphics2D getGraphics2D() {
		return g2;
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
	
	//悢抣宯
	public final double angleFormat(double radian){ //儔僕傾儞惍棟儊僜僢僪 -PI~+PI偵捈偡
		radian %= PI*2;
		if(radian > PI)
			radian -= PI*2;
		else if(radian <= -PI)
			radian += PI*2;
		return radian;
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
	public final long warningBox(String message,String title){
		long openTime = System.currentTimeMillis();
		JOptionPane.showMessageDialog(null,message,title,JOptionPane.WARNING_MESSAGE);
		return System.currentTimeMillis() - openTime;
	}
}