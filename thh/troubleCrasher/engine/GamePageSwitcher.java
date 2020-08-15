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
	private static final int PROFILE_SESSION = 0, BOX_SESSION = 1, SETTING_SESSION = 2, SAVE_SESSION = 3;
	private static final int WORK_SCENE = 0, HOME_SCENE = 1, BAR_SCENE = 2; // DIFFERENT SCENES
	private static final int NO_ONE = 0, NPC_1 = 1, NPC_2 = 2, NPC_3 = 3, NPC_4 = 4; // DIFFERENT NPCS
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

		// GAME SCREEN
		set(GAMESCREEN, new GUIParts() {
			{
				setName("GAMESCREEN");
				// 游戏功能栏及其展开画面
				this.appendLast(new GUIPartsSwitcher(4, PROFILE_SESSION) {
					{
						setName("ACTIVITY_SESSION");

						this.appendLast(new GUIParts() {

							final GUIParts profileScrBtn = getSwitcherButton(PROFILE_SESSION).setBGPaint(buttonPaint)
									.setName("profileScrBtn").setBounds(5, 20, 60, 60),
									boxScrBtn = getSwitcherButton(BOX_SESSION).setBGPaint(buttonPaint)
											.setName("boxScrBtn").setBounds(5, 90, 60, 60),
									settingScrBtn = getSwitcherButton(SETTING_SESSION).setBGPaint(buttonPaint)
											.setName("settingScrBtn").setBounds(5, 160, 60, 60),
									saveScrBtn = getSwitcherButton(SAVE_SESSION).setBGPaint(buttonPaint)
											.setName("saveScrBtn").setBounds(5, 230, 60, 60);
							// quitScrBtn = getSwitcherButton(SETTINGSCREEN).setBGPaint(buttonPaint)
							// .setName("settingScrBtn").setBounds(512, 400, 100, 200);
							// TODO: Add quit
							// button
							{
								// this.addNewLine(gameScrBtn, settingsScrBtn);
								this.setName("START_MENU_TOP_TAB");
								this.appendFirst(profileScrBtn);
								this.appendFirst(boxScrBtn);
								this.appendFirst(settingScrBtn);
								this.appendFirst(saveScrBtn);

							}

						});

						set(PROFILE_SESSION, new GUIParts() {
							{
								setName("PROFILE_SESSION");
								setBGColor(Color.red);
								setBounds(70, 0, 360, 768);
							}
						});

						set(BOX_SESSION, new GUIParts() {
							{
								setName("BOX_SESSION");
								setBGColor(Color.yellow);
								setBounds(70, 0, 360, 768);
							}
						});
						set(SETTING_SESSION, new GUIParts() {
							{
								setName("SETTING_SESSION");
								setBGColor(Color.MAGENTA);
								setBounds(70, 0, 360, 768);
							}
						});
						set(SAVE_SESSION, new GUIParts() {
							{
								setName("SAVE_SESSION");
								setBGColor(Color.pink);
								setBounds(70, 0, 360, 768);
							}
						});

						// setBGColor(Color.BLUE);
					}
				});

				// 场景画面
				this.appendLast(new GUIParts() {
					{
						this.appendLast(new GUIPartsSwitcher(4, WORK_SCENE) {
							{
								setName("SCENE_SESSION");

								this.appendLast(new GUIParts() {

									final GUIParts workScrBtn = getSwitcherButton(WORK_SCENE).setBGPaint(buttonPaint)
											.setName("workScrBtn").setBounds(430, 20, 60, 60),
											barScrBtn = getSwitcherButton(BAR_SCENE).setBGPaint(buttonPaint)
													.setName("barScrBtn").setBounds(430, 90, 60, 60),
											homeScrBtn = getSwitcherButton(HOME_SCENE).setBGPaint(buttonPaint)
													.setName("homeScrBtn").setBounds(430, 160, 60, 60);

									// quitScrBtn = getSwitcherButton(SETTINGSCREEN).setBGPaint(buttonPaint)
									// .setName("settingScrBtn").setBounds(512, 400, 100, 200);
									// button
									{
										// this.addNewLine(gameScrBtn, settingsScrBtn);
										this.setName("SCENE_MENU_SWTCHER_TAB");
										this.appendFirst(workScrBtn);
										this.appendFirst(barScrBtn);
										this.appendFirst(homeScrBtn);

									}

								});

								set(WORK_SCENE, new GUIParts() {
									{
										setName("WORK_SCENE");
										setBGColor(Color.blue);
										setBounds(430, 0, 594, 520);
									}
								});

								// 设定
								// 存档
								// 箱子

								set(BAR_SCENE, new GUIParts() {
									{
										setName("BAR_SCENE");
										setBGColor(Color.orange);
										setBounds(430, 0, 594, 520);
									}
								});

								set(HOME_SCENE, new GUIParts() {
									{
										setName("HOME_SCENE");
										setBGColor(Color.GRAY);
										setBounds(430, 0, 594, 520);
									}
								});
								// setBGColor(Color.BLUE);
							}
						});

						// NPC
						this.appendFirst(new GUIPartsSwitcher(5, NPC_1) {
							{
								setName("SCENE_SESSION");

								set(NO_ONE, new GUIParts() {
									{
										// NO MPC IN SCENE
									}
								});

								set(NPC_1, new GUIParts() {
									{
										setName("NPC_1");
										setBGColor(Color.orange);
										setBounds(627, 220, 200, 300);
									}
								});

								set(NPC_2, new GUIParts() {
									{
										setName("NPC_2");
										setBGColor(Color.GRAY);
										setBounds(627, 220, 200, 300);
									}
								});
								set(NPC_3, new GUIParts() {
									{
										setName("NPC_3");
										setBGColor(Color.pink);
										setBounds(627, 220, 200, 300);
									}
								});
								set(NPC_4, new GUIParts() {
									{
										setName("NPC_4");
										setBGColor(Color.cyan);
										setBounds(627, 220, 200, 300);
									}
								});

								// setBGColor(Color.BLUE);
							}
						});

					}
				});

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
