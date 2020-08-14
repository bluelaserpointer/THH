package troubleCrasher.engine;

import java.awt.Color;

import core.GHQ;
import gui.AutoResizeMenu;
import gui.GUIParts;
import gui.GUIPartsSwitcher;
import paint.ImageFrame;
import paint.rect.RectPaint;

public class GamePageSwitcher extends GUIPartsSwitcher {
	private static final int STARTSCREEN = 0, GAMESCREEN = 1, SETTINGSCREEN = 2;
//	private final ImageFrame defaultSlotPaint, buttonPaint;
//	private final RectPaint bgPaint;
	
	public GamePageSwitcher() {
		super(3,STARTSCREEN);
//		defaultSlotPaint = ImageFrame.create("picture/banana.png");
//		buttonPaint = ImageFrame.create("picture/banana.png");
//		bgPaint = new RectPaint() {
//			private final ImageFrame
//				bg1 = ImageFrame.create("picture/banana.png"),
//				bg2 = ImageFrame.create("picture/banana.png");
//			@Override
//			public void rectPaint(int x, int y, int w, int h) {
//				bg1.rectPaint(x, y, w, h);
//				bg2.rectPaint(x, y, w, h);
//			}
//		};
//		this.setBGPaint(bgPaint);
//		this.appendLast(new AutoResizeMenu(0, 0, GHQ.screenW(), 70) {
//			final GUIParts 
//				gameScrBtn = getSwitcherButton(GAMESCREEN).setBGPaint(buttonPaint).setName("inventoryScrBtn"),
//				settingsScrBtn = getSwitcherButton(SETTINGSCREEN).setBGPaint(buttonPaint).setName("talentScrBtn");
//			{
//				this.addNewLine(gameScrBtn, settingsScrBtn);
//				this.setName("START_MENU_TOP_TAB");
//			}
//		});
		
		set(STARTSCREEN, new GUIParts() {
//			final ImageFrame StartScreen = ImageFrame.create("picture/banana.png");
			{
				setName("STARTSCREEN");
				setBGColor(Color.GREEN);
				
			}
		});
		set(GAMESCREEN,new GUIParts() {
			{
				setName("GAMESCREEN");
				setBGColor(Color.BLUE);
			}
		});
		set(SETTINGSCREEN, new GUIParts() {
			{
			setName("SETTINGSCREEN");
			setBGColor(Color.red);
			}
		});
	}

}
