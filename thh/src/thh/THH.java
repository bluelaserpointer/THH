package thh;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import static java.awt.event.KeyEvent.*;
import javax.swing.*;

import bullet.Bullet;
import bullet.BulletInfo;
import bullet.BulletSource;
import effect.Effect;
import effect.EffectInfo;
import effect.EffectSource;
import engine.Ver_I;

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
final public class THH extends JPanel implements MouseListener,MouseMotionListener,MouseWheelListener,KeyListener,Runnable{
	private static final long serialVersionUID = 123412351L;

	final static String
		GAME_VERSION = "Ver beta1.0.0";
	final public static int
		NONE = -999999999, //���ʤ�
		MAX = Integer.MAX_VALUE, //���
		MIN = Integer.MIN_VALUE; //��С

	//�����ƥ��v�B
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
	
	//���٥���v�B
	public final int 
		OPENING = 1000,
		TITLE = 2000,
		LOAD = 3000,
		SAVE = 4000,
		OPTION = 5000,OPTION_sound = 5100,OPTION_grahics = 5200,
		ACTION_PART = 6000,
		BATTLE = 7000,BATTLE_PAUSE = 7001,
		EVENT_PART = 8000;
	private int mainEvent = OPENING; //���٥���}�����(���٥�ȷN�+���٥�ȥǩ`��)
	
	//stopEvent
	private static int stopEventKind = NONE;
	public static final int STOP = 0,FREEZE = 1;
	private static int stopEventReason;
	public static final int MESSAGE = 0,SPELL = 1;
	
	//������ɥ��v�B
	final int defaultScreenW = 1000,defaultScreenH = 600; //�ǥե���ȥ�����ɥ�����
	private static int screenW = 1000,screenH = 600;
	private static int viewX,viewY;
	
	private int page,page_max; //�ک`���C��
	
	//���������
	private int clearFrame; //���`���v�S�r�g
	private static int systemFrame; //�F�ڥե�`��
	private static int gameFrame;
	
	//����饯���`���
	private static Chara[] battleCharaClass = new Chara[0];
	
	//stage
	private static StageEngine engine = new Ver_I();
	private static Stage stage;
	
	//bullet data
	private static final ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	
	//effect data
	private static final ArrayList<Effect> effects = new ArrayList<Effect>();
	
	//event data
	private static final ArrayDeque<String> messageStr = new ArrayDeque<String>();
	private static final ArrayDeque<Integer> messageSource = new ArrayDeque<Integer>();
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
		//�E�B���h�E�Z�b�g�A�b�v
		myFrame = new JFrame("�����Z�e����");
		myFrame.add(this,BorderLayout.CENTER); //�L�����o�X��ݒu
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
			tracker.waitForAll(); //�摜���[�h
		}catch(InterruptedException | NullPointerException e){}
		
		System.out.println("loadTimeReslut: " + (System.currentTimeMillis() - loadTime));//���[�h���v���Ԍ��ʕ\��
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
			//JOptionPane.showMessageDialog(null, "�\���󂠂�܂��񂪁A�G���[���������܂����B\n�G���[�R�[�h�F" + e.toString(),"�G���[",JOptionPane.ERROR_MESSAGE);
		//}
	}
	
	private boolean loadComplete;
	private final BufferedImage offImage = new BufferedImage(defaultScreenW,defaultScreenH,BufferedImage.TYPE_INT_ARGB_PRE); //�_�u���o�b�t�@�L�����o�X
	private Graphics2D g2;
	private final Font basicFont = createFont("font/upcibi.ttf").deriveFont(Font.BOLD + Font.ITALIC,30.0f),commentFont = createFont("font/HGRGM.TTC").deriveFont(Font.PLAIN,15.0f);
	private static final BasicStroke stroke1 = new BasicStroke(1f),stroke5 = new BasicStroke(5f);
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
		final int TRANSLATE_X = viewX,TRANSLATE_Y = viewY;
		g2.translate(TRANSLATE_X, TRANSLATE_Y);
		////////////////////////////////////////////////////////////////////////
		//stageAction
		engine.idle(g2,stopEventKind);
		////////////////////////////////////////////////////////////////////////
		//charaAction
		switch(stopEventKind) {
		case STOP:
			for(Chara chara : battleCharaClass)
				chara.animationPaint();
			break;
		case FREEZE:
			for(Chara chara : battleCharaClass)
				chara.freezePaint();
			break;
		default:
			for(Chara chara : battleCharaClass) {
				chara.idle(true);
				chara.resetSingleOrder();
			}
		}
		////////////////////////////////////////////////////////////////////////
		//bulletAction&effectAction
		switch(stopEventKind) {
		case STOP:
			for(int i = 0;i < bullets.size();i++) {
				final Bullet bullet = bullets.get(i);
				bullet.SOURCE.bulletAnimationPaint(bullet);
			}
			for(int i = 0;i < effects.size();i++) {
				final Effect effect = effects.get(i);
				effect.SOURCE.effectAnimationPaint(effect);
			}
			break;
		case FREEZE:
			for(int i = 0;i < bullets.size();i++) {
				final Bullet bullet = bullets.get(i);
				bullet.SOURCE.bulletPaint(bullet);
			}
			for(int i = 0;i < effects.size();i++) {
				final Effect effect = effects.get(i);
				effect.SOURCE.effectPaint(effect);
			}
			break;
		default:
			for(int i = 0;i < bullets.size();i++) {
				final Bullet bullet = bullets.get(i);
				bullet.SOURCE.bulletIdle(bullet,true);
			}
			for(int i = 0;i < effects.size();i++) {
				final Effect effect = effects.get(i);
				effect.SOURCE.effectIdle(effect,true);
			}
		}
		///////////////////////////////////////////////////////////////
		//GUIAction
		{
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
			//message
			if(stopEventKind != NONE && stopEventReason == MESSAGE) {
				if(messageStr.size() > 0) {
					if(messageIterator > messageStr.getFirst().length() - 1){
						if(key_enter) { //next message order
							messageStr.remove();
							final int SOURCE = messageSource.remove();
							final int EVENT = messageEvent.remove();
							if(EVENT != NONE && 0 <= SOURCE && SOURCE < battleCharaClass.length)
								battleCharaClass[SOURCE].eventNotice(EVENT);
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
		}
		g2.translate(-TRANSLATE_X, -TRANSLATE_Y);
		//Fixed GUI
		{
			if(debugMode){
				g2.setColor(debugTextColor);
				g2.setFont(basicFont);
				g2.setStroke(stroke1);
				for(int i = 100;i < defaultScreenW;i += 100)
					g2.drawLine(i,0,i,defaultScreenH);
				for(int i = 100;i < defaultScreenH;i += 100)
					g2.drawLine(0,i,defaultScreenW,i);
				g2.setStroke(stroke5);
				g2.drawString(mouseX + "," + mouseY,mouseX + 20,mouseY + 20);
				//entityInfo
				g2.drawString("Chara:" + battleCharaClass.length + " EF:" + effects.size() + " B:" + bullets.size(),30,100);
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
				for(Chara chara : battleCharaClass) {
					final int X = (int)chara.getX(),Y = (int)chara.getY();
					g2.setStroke(stroke1);
					g2.drawRect(X + viewX - 50, Y + viewY - 50, 100,100);
					g2.drawLine(X + viewX + 50, Y + viewY - 50, X + viewX + 60, Y + viewY - 60);
					g2.setStroke(stroke5);
					g2.drawString(chara.getName(), X + viewX + 62, Y + viewY - 68);
				}
			}
		}
		//UI�`��
		g2.setColor(Color.GRAY);
		g2.fillRect(0,0,defaultScreenW,40);
		g.drawImage(offImage,0,0,screenW,screenH,this);
		loadTime_total = System.currentTimeMillis() - LOAD_TIME_PAINTCOMPONENT;
	}
	
	public static final int[] callBulletEngage(Bullet bullet) {
		int[] result = new int[battleCharaClass.length];
		int searched = 0;
		for(int i = 0;i < battleCharaClass.length;i++) {
			if(battleCharaClass[i].bulletEngage(bullet))
				result[searched++] = i;
		}
		return Arrays.copyOf(result, searched);
	}
	//information-chara
	public static final Chara[] getCharaClass() {
		return battleCharaClass;
	}
	public static final Chara getCharaClass(int charaID) {
		return battleCharaClass[charaID];
	}
	public static final int getCharaTeam(int charaID) {
		return battleCharaClass[charaID].getTeam();
	}
	//information-stage
	public static final Stage getStage() {
		return stage;
	}
	public static final boolean hitLandscape(int x,int y,int w,int h) {
		return stage.hitLandscape(x, y, w, h);
	}
	public static final boolean inStage(int x,int y) {
		return stage.inStage(x, y);
	}
	//information-paint
	public static final boolean inScreen(int x,int y) {
		return abs(viewX - x) < screenW && abs(viewY - y) < screenH;
	}
	//information-resource
	public final Image getImageByID(int imageID) {
		return arrayImage[imageID];
	}

	//tool
	public static final boolean deleteBullet(Bullet bullet) {
		if(bullet.SOURCE.deleteBullet(bullet))
			return bullets.remove(bullet);
		return false;
	}
	public static final boolean deleteEffect(Effect effect) {
		if(effect.SOURCE.deleteEffect(effect))
			return effects.remove(effect);
		return false;
	}
	/*public final void rollCharaTurn(){
		if(++chara == battleCharaClass.length)
			chara = 0;
		battleCharaClass[chara].turnStarted();
	}*/
	public final void paintHPArc(int x,int y,int hp,int maxHP) {
		g2.setStroke(stroke5);
		if((double)hp/(double)maxHP > 0.75) //HP���٤ʤ��ʤ뤴�Ȥ�ɫ���仯
			g2.setColor(Color.CYAN); //ˮɫ
		else if((double)hp/(double)maxHP > 0.50)
			g2.setColor(Color.GREEN); //�vɫ
		else if((double)hp/(double)maxHP > 0.25)
			g2.setColor(Color.YELLOW); //��ɫ
		else if((double)hp/(double)maxHP > 0.10 || systemFrame % 4 < 2)
			g2.setColor(Color.RED); //��ɫ
		else
			g2.setColor(HPWarningColor);
		g2.drawString(String.valueOf(hp),x + (hp >= 10 ? 61 : 67),y + 60);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2.drawArc(x - 65,y - 65,130,130,90,(int)((double)hp/(double)maxHP*360));
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
	}
	public final int receiveDamageInSquare(int team,int hp,int x,int y,int size){ //�ԄI����(������)
		for(Bullet bullet : bullets){
			if(team != bullet.team && bullet.atk > 0 && squreCollision((int)bullet.x,(int)bullet.y,bullet.SIZE,x,y,size)){ //�nͻ
				hp -= bullet.atk;
				bullet.SOURCE.bulletHitObject(bullet);
				if(hp <= 0)
					break;
			}
		}
		return hp; //�Фä�HP�򷵤�
	}
	public final int receiveBulletInCircle(int hp,int x,int y,int size,int team){ //�ԄI����(������)
		for(Bullet bullet : bullets){
			if(team != bullet.team && bullet.atk > 0 && circleCollision((int)bullet.x,(int)bullet.y,bullet.SIZE,x,y,size)){ //�nͻ
				hp -= bullet.atk;
				bullet.SOURCE.bulletHitObject(bullet);
				if(hp <= 0)
					break;
			}
		}
		return hp; //�Фä�HP�򷵤�
	}
	public final Bullet searchBulletInSquare_single(int x,int y,int size,int team){
		for(Bullet bullet : bullets.toArray(new Bullet[0])){
			if(team != bullet.team && squreCollision((int)bullet.x,(int)bullet.y,bullet.SIZE,x,y,size)) //�nͻ
				return bullet; //return ID
		}
		return null; //Not found
	}
	public final Bullet[] searchBulletInSquare_multiple(int x,int y,int size,int team){
		Bullet[] foundIDs = new Bullet[bullets.size()];
		int foundAmount =  0;
		for(Bullet bullet : bullets.toArray(new Bullet[0])){
			if(team != bullet.team && squreCollision((int)bullet.x,(int)bullet.y,bullet.SIZE,x,y,size)) //�nͻ
				foundIDs[foundAmount++] = bullet; //record ID
		}
		return Arrays.copyOf(foundIDs,foundAmount);
	}
	public final static boolean squreCollision(int x1,int y1,int size1,int x2,int y2,int size2) {
		final int halfSize = (size1 + size2)/2;
		return abs(x1 - x2) < halfSize && abs(y1 - y2) < halfSize;
	}
	public final static boolean circleCollision(int x1,int y1,int size1,int x2,int y2,int size2) {
		final double DX = x1 - x1,DY = y1 - y2,RANGE = size1 + size2;
		return DX*DX + DY*DY <= RANGE*RANGE;
	}
	
	//input
	private static int mouseX,mouseY;
	private boolean mouseLeftPress,mouseMiddlePress,mouseRightPress;
	private int mouseLeftPressedFrame,mouseMiddlePressedFrame,mouseRightPressedFrame;
	private int mousePointing = NONE; //�ޥ������ݥ���Ȥ��Ƥ�����
	private int middleDragGapX,middleDragGapY;
	
	public void mouseWheelMoved(MouseWheelEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mousePressed(MouseEvent e){
		switch(e.getButton()) {
		case MouseEvent.BUTTON1:
			mouseLeftPress = true;
			mouseLeftPressedFrame = gameFrame;
			if(key_1 || key_2 || key_3 || key_4) {
				if(key_1 && battleCharaClass.length >= 1)
					battleCharaClass[0].attackOrder = true;
				if(key_2 && battleCharaClass.length >= 2)
					battleCharaClass[1].attackOrder = true;
				if(key_3 && battleCharaClass.length >= 3)
					battleCharaClass[2].attackOrder = true;
				if(key_4 && battleCharaClass.length >= 4)
					battleCharaClass[3].attackOrder = true;
			}else
				for(Chara chara : battleCharaClass)
					chara.attackOrder = true;
			break;
		case MouseEvent.BUTTON2:
			mouseMiddlePress = true;
			middleDragGapX = viewX - mouseX;
			middleDragGapY = viewY - mouseY;
			break;
		case MouseEvent.BUTTON3:
			mouseRightPress = true;
			mouseRightPressedFrame = gameFrame;
			if(isNoStopEvent()) {
				if(key_1 || key_2 || key_3 || key_4) {
					if(key_1 && battleCharaClass.length >= 1)
						battleCharaClass[0].dodgeOrder = true;
					if(key_2 && battleCharaClass.length >= 2)
						battleCharaClass[1].dodgeOrder = true;
					if(key_3 && battleCharaClass.length >= 3)
						battleCharaClass[2].dodgeOrder = true;
					if(key_4 && battleCharaClass.length >= 4)
						battleCharaClass[3].dodgeOrder = true;
				}else
					for(Chara chara : battleCharaClass)
						chara.dodgeOrder = true;
			}
			break;
		}
	}
	public void mouseReleased(MouseEvent e){
		switch(e.getButton()) {
		case MouseEvent.BUTTON1:
			mouseLeftPress = false;
			for(Chara chara : battleCharaClass)
				chara.attackOrder = false;
			break;
		case MouseEvent.BUTTON2:
			mouseMiddlePress = false;
			break;
		case MouseEvent.BUTTON3:
			mouseRightPress = false;
			for(Chara chara : battleCharaClass)
				chara.moveOrder = false;
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
	public static final int getMouseX(){
		return mouseX - viewX;
	}
	public static final int getMouseY(){
		return mouseY - viewY;
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
	//�L�[���
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
			if(isNoStopEvent() && getPassedTime(key_1_time) < 200 && battleCharaClass.length >= 1)
				battleCharaClass[0].spellOrder = true;
			key_1_time = getNowTime();
			break;
		case VK_2:
			key_2 = true;
			if(isNoStopEvent() && getPassedTime(key_2_time) < 200 && battleCharaClass.length >= 2)
				battleCharaClass[1].spellOrder = true;
			key_2_time = getNowTime();
			break;
		case VK_3:
			key_3 = true;
			if(isNoStopEvent() && getPassedTime(key_3_time) < 200 && battleCharaClass.length >= 3)
				battleCharaClass[2].spellOrder = true;
			key_3_time = getNowTime();
			break;
		case VK_4:
			key_4 = true;
			if(isNoStopEvent() && getPassedTime(key_4_time) < 200 && battleCharaClass.length >= 4)
				battleCharaClass[3].spellOrder = true;
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
	public final static void createBullet(BulletSource source){ //������
		bullets.add(new Bullet(source));
	}
	public final static void createBullet(BulletSource source,int amount) {
		for(int i = 0;i < amount;i++)
			bullets.add(new Bullet(source));
	}
	public final static void createBullet_RoundDesign(BulletSource source,int amount,double gunnerX,double gunnerY,double radius){
		final double ANGLE = 2*PI/amount;
		for(int i = 0;i < amount;i++){
			BulletInfo.x = gunnerX + radius*cos(ANGLE*i);
			BulletInfo.y = gunnerY + radius*sin(ANGLE*i);
			createBullet(source);
		}
	}
	public final static void createEffect(EffectSource source){ //������
		effects.add(new Effect(source));
	}
	public final static void createEffect(EffectSource source,int amount) {
		for(int i = 0;i < amount;i++)
			effects.add(new Effect(source));
	}
	public final static void createEffect_RoundDesign(EffectSource source,int amount,double gunnerX,double gunnerY,double radius){
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
		return gameFrame - frame;
	}
	public static final boolean isExpired_frame(int appearFrame,int limitFrame) {
		return (gameFrame - appearFrame) >= limitFrame;
	}
	public static final long getNowTime() {
		return System.currentTimeMillis();
	}
	public static final int getPassedTime(long time) {
		return (int)(System.currentTimeMillis() - time);
	}
	public static final boolean isExpired_time(long time,int limitTime) {
		return (System.currentTimeMillis() - time) >= limitTime;
	}
	public static boolean isNoStopEvent() {
		return !freezeScreen && stopEventKind == NONE;
	}
	public static final void prepareBulletInfo(int charaID) {
		BulletInfo.clear();
		BulletInfo.nowFrame = gameFrame;
		BulletInfo.source = charaID;
	}
	public static final void prepareEffectInfo(int charaID) {
		EffectInfo.clear();
		EffectInfo.nowFrame = gameFrame;
		EffectInfo.source = charaID;
	}
	public final void addKeyListener(KeyListener e) {
		myFrame.addKeyListener(e);
	}
	
	//event
	public static final void addMessage(int source,String message) {
		stopEventKind = STOP;
		stopEventReason = MESSAGE;
		messageSource.add(source);
		messageStr.add(message);
		messageEvent.add(THH.NONE);
	}
	public static final void addMessage(int source,int event,String message) {
		stopEventKind = STOP;
		stopEventReason = MESSAGE;
		messageSource.add(source);
		messageStr.add(message);
		messageEvent.add(event);
	}
	
	//premade stage test area
	final private void resetStage(){
		bullets.clear();
		effects.clear();
		messageSource.clear();
		messageStr.clear();
		messageEvent.clear();
		gameFrame = 0;
		
		System.out.println("add characters to the game");
		battleCharaClass = engine.charaSetup();
		stage = engine.stageSetup();
	}
	
	//ResourceLoad
	/**
	* �摜��ǂݍ��ފ�{���\�b�h�ł��Bassets/image�t�H���_����w��p�X��T���܂��B�G���[���N�����ہA�G���[�ꏊ�̖��O��\�������邱�Ƃ��ł��܂��B
	* �ǉ��R�[�h�Ȃǂ͂��̃��\�b�h���g���Ēǉ��摜��ǂݍ��܂��邱�Ƃ��ł��܂��B
	* @param url �摜�p�X
	* @param errorSource �ǂݍ��݃G���[�̍ہA�\��������G���[�ꏊ�̖��O�@(��:loadImage("img.png","�G�摜�ǂݍ���")
	* @return �摜Image�l
	* @since ~beta7.0
	*/
	public final int loadImage(String url,String errorSource){ //�摜�ǂݍ��݃��\�b�h
		//old URL
		for(int id = 0;id < arrayImageURL.length;id++){
			if(url.equals(arrayImageURL[id])) //URL���h�g��
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
		}catch(IOException | NullPointerException e){ //�ُ�-�ǂݍ��ݎ��s
			if(errorSource != null && !errorSource.isEmpty())
				warningBox("����\"" + url + "\"����`�ɤǤ��ޤ���Ǥ��������λ�����軭����ޤ���\n������" + errorSource,"�i���z�ߥ���`");
			else
				warningBox("����\"" + url + "\"����`�ɤǤ��ޤ���Ǥ��������λ�����軭����ޤ���\n������ָ���ʤ�","�i���z�ߥ���`");
			arrayImage[arrayImage_maxID] = createImage(1,1);
		}
		//save URL
		arrayImage_maxID++; //����URL���򉈤䤹
		if(arrayImage_maxID == arrayImage.length){ //�ք��������L
			arrayImage = Arrays.copyOf(arrayImage,arrayImage_maxID*2);
			arrayImageURL = Arrays.copyOf(arrayImageURL,arrayImage_maxID*2);
		}
		arrayImageURL[arrayImage_maxID] = url; //���λ���URL����h
		arrayImage[arrayImage_maxID] = img; //���λ������h
		return arrayImage_maxID;
	}
	/**
	* �摜��ǂݍ��ފ�{���\�b�h�ł��Bassets/image�t�H���_����w��p�X��T���܂��B
	* �ǉ��R�[�h�Ȃǂ͂��̃��\�b�h���g���Ēǉ��摜��ǂݍ��܂��邱�Ƃ��ł��܂��B
	* �G���[���̎w����ȗ������o�[�W�����ł��B
	* @param url �摜�p�X
	* @return �摜Image�l
	* @since ~beta7.0
	*/
	public final int loadImage(String url){ //�摜�ǂݍ��݃��\�b�h
		return this.loadImage(url,null);
	}
	/**
	* ������ǂݍ��ފ�{���\�b�h�ł��Bassets/sound�t�H���_����w��p�X��T���܂��B
	* �ǉ��R�[�h�Ȃǂ͂��̃��\�b�h���g���Ēǉ�������ǂݍ��܂��邱�Ƃ��ł��܂��B
	* ���̂Ƃ����������G���[��SoundClip���񍐂���܂��B
	* @param url �摜�p�X
	* @return ����SoundClip�l
	* @since beta8.0
	*/
	public final int loadSound(String url){ //�摜�ǂݍ��݃��\�b�h
		//old URL
		for(int id = 0;id < arraySoundURL.length;id++){
			if(url.equals(arraySoundURL[id])) //URL���h�g��
				return id;
		}
		//save new URL
		if(arraySound_maxID + 1 == arraySound.length) { //check & expand length
			arraySound = Arrays.copyOf(arraySound, arraySound.length*2);
			arraySoundURL = Arrays.copyOf(arraySoundURL, arraySoundURL.length*2);
		}
		arraySound_maxID++; //URL���򉈤䤹
		if(arraySound_maxID == arraySound.length){ //�ք��������L
			arraySound = Arrays.copyOf(arraySound,arraySound_maxID*2);
			arraySoundURL = Arrays.copyOf(arraySoundURL,arraySound_maxID*2);
		}
		arraySoundURL[arraySound_maxID] = url; //����URL����h
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
			System.out.println("�ե����똋��ˆ��}���k�����Ƥ��ޤ���");
		}
		if(charaFolder == null){
			System.out.println("chara�ե������Ҋ�Ĥ���ޤ���");
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
	* �w�肵���摜��THH�֕`�悷���{���\�b�h�ł��B
	* @param img �`��摜
	* @param x �`��ʒux
	* @param y �`��ʒuy
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
	* �w�萡�@�Ŏw�肵���摜��THH�֕`�悷���{���\�b�h�ł��Bw,h���ݒ�ł��܂��B
	* @param img �`��摜
	* @param x �`��ʒux
	* @param y �`��ʒuy
	* @param w ����
	* @param h �c��
	* @since beta8.0
	*/
	public final void drawImageTHH(Image img,int x,int y,int w,int h){
		g2.drawImage(img,x,y,w,h,this);
	}
	public final void drawImageTHH(int imgID,int x,int y,int w,int h){
		this.drawImageTHH(arrayImage[imgID],x,y,w,h);
	}
	/**
	* �w����W�𒆐S�Ƃ��Ďw�肵���摜��THH�ɕ`�悷���{���\�b�h�ł��B
	* @param img �`��摜
	* @param x �`�撆�S�ʒux
	* @param y �`�撆�S�ʒuy
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
	
	//���l�n
	public static final double angleFormat(double radian){ //���W�A���������\�b�h -PI~+PI�ɒ���
		radian %= PI*2;
		if(radian > PI)
			radian -= PI*2;
		else if(radian <= -PI)
			radian += PI*2;
		return radian;
	}
	
	//���b�Z�[�W�E�B���h�E�n
	/**
	* �x���E�B���h�E�̕\�����s���܂��B
	*�@�܂��A���̃E�B���h�E��\�����Ă������Ԃ�Ԃ��܂��B
	* ���̊ԂɃC�x���g���Q�[���ł������ꍇ�A�|�[�Y��ʂɈڍs���邱�Ƃ�����܂��B
	* �����I�ɂ�JOptionPane.showMessageDialog���\�b�h���Ă�ł��܂��B
	* @param message �\������x��
	* @param title �_�C�A���O�̃^�C�g��������
	* @return �_�C�A���O��������܂ł̌o�ߎ���
	* @since beta9.0
	*/
	public static final long warningBox(String message,String title){
		long openTime = System.currentTimeMillis();
		JOptionPane.showMessageDialog(null,message,title,JOptionPane.WARNING_MESSAGE);
		return System.currentTimeMillis() - openTime;
	}
}