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
	private static final int PROFILE_SESSION = 0, BOX_SESSION = 1; // TODO: add new sections
	private final ImageFrame defaultSlotPaint, buttonPaint;
	private static final String StartScreenPic = "thhimage/WhiteBall.png";
	private int centerW, centerH;

	public GamePageSwitcher() {
		super(3, STARTSCREEN);
		defaultSlotPaint = ImageFrame.create("thhimage/MillkyWay.png");
		buttonPaint = ImageFrame.create("thhimage/veg_leaf3.png"); // 按钮图片
		centerH = 384;
		centerW = 512;

		// Start Menu
		set(STARTSCREEN, new GUIParts() {

			{
				this.setBGImage(StartScreenPic);
				setName("STARTSCREEN");
				// setBGColor(Color.BLACK);

				this.appendLast(new GUIParts() {

					final GUIParts gameScrBtn = getSwitcherButton(GAMESCREEN).setBGPaint(buttonPaint)
							.setName("gameScrBtn").setBounds(centerW - 150, 200, 300, 100),
							settingsScrBtn = getSwitcherButton(SETTINGSCREEN).setBGPaint(buttonPaint)
									.setName("settingScrBtn").setBounds(centerW - 150, 350, 300, 100);
					// quitScrBtn = getSwitcherButton(SETTINGSCREEN).setBGPaint(buttonPaint)
					// .setName("settingScrBtn").setBounds(512, 400, 100, 200);
					// TODO: Add quit
					// button
					{
						// this.addNewLine(gameScrBtn, settingsScrBtn);
						this.setName("START_MENU_TOP_TAB");
						this.appendFirst(gameScrBtn);
						this.appendFirst(settingsScrBtn);

					}

				});

			}

			// @Override
			// public void idle() {
			// super.idle();
			// // StartScreen.dotPaint_rate(200, 200, 1.2);
			// }
		});
		// TODO: add new sections
		set(GAMESCREEN, new GUIPartsSwitcher(2, PROFILE_SESSION) {
			{
				setName("GAMESCREEN");
				// setBGColor(Color.BLUE);

			}
		});
		set(SETTINGSCREEN, new GUIParts() {
			{
				setName("SETTINGSCREEN");
				setBGColor(Color.red);
			}
		});

		GHQ.addGUIParts(this);
	}

}
