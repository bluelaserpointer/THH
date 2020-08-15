package troubleCrasher.engine;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

// import org.w3c.dom.events.MouseEvent;
import java.awt.event.MouseEvent;

import core.GHQ;
import gui.AnimatedGHQTextArea;
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
	private GUIParts nextButton, DIALOGUE_SECTION;
	private AnimatedGHQTextArea Dialogue, Speaker;
	private static final String StartScreenPic = "thhimage/WhiteBall.png";
	private int centerW, centerH;

	public GamePageSwitcher() {
		super(3, STARTSCREEN);
		defaultSlotPaint = ImageFrame.create("thhimage/MillkyWay.png");
		buttonPaint = ImageFrame.create("thhimage/veg_leaf3.png"); // 按钮图片
		Dialogue = (AnimatedGHQTextArea) new AnimatedGHQTextArea().setTextSpeed(3).setBounds(477, 580, 500, 100)
				.setBGColor(Color.green).disable();
		Speaker = (AnimatedGHQTextArea) new AnimatedGHQTextArea().setBounds(477, 540, 500, 30).setBGColor(Color.yellow)
				.disable();
		nextButton = new GUIParts().setBGColor(Color.pink).setBounds(900, 698, 100, 50);

		DIALOGUE_SECTION = new GUIParts().setBGColor(Color.green).setBounds(430, 520, 1024, 768).appendLast(Speaker)
				.appendLast(Dialogue.setText(
						"1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111")
						.enable())
				.appendLast(nextButton);

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
								this.setName("GAME_MENU_TOP_TAB");
								this.setBGColor(Color.RED);
								this.setBounds(0, 0, 70, 768);
								this.appendFirst(profileScrBtn);
								this.appendFirst(boxScrBtn);
								this.appendFirst(settingScrBtn);
								this.appendFirst(saveScrBtn);
								this.setBounds(0, 0, 70, 768);

							}

						});

						set(PROFILE_SESSION, new GUIPartsSwitcher(5, NPC_1) {
							{
								setName("PROFILE_SESSION");
								this.appendLast(new GUIParts() {
									final GUIParts NPC_1ScrBtn = getSwitcherButton(NPC_1).setBGPaint(buttonPaint)
											.setName("NPC_1ScrBtn").setBounds(75, 20, 60, 60),
											NPC_2ScrBtn = getSwitcherButton(NPC_2).setBGPaint(buttonPaint)
													.setName("NPC_2ScrBtn").setBounds(75, 90, 60, 60),
											NPC_3ScrBtn = getSwitcherButton(NPC_3).setBGPaint(buttonPaint)
													.setName("NPC_3ScrBtn").setBounds(75, 160, 60, 60),
											NPC_4ScrBtn = getSwitcherButton(NPC_4).setBGPaint(buttonPaint)
													.setName("NPC_4ScrBtn").setBounds(75, 230, 60, 60);
									// setBGColor(Color.red);
									// setBounds(70, 0, 360, 768);
									{
										// this.addNewLine(gameScrBtn, settingsScrBtn);
										this.setName("PROFILE_MENU_TOP_TAB");
										this.appendLast(NPC_1ScrBtn);
										this.appendLast(NPC_2ScrBtn);
										this.appendLast(NPC_3ScrBtn);
										this.appendLast(NPC_4ScrBtn);
										this.setBounds(70, 0, 70, 768);
									}
								});
								set(NPC_1, new GUIParts() {
									{
										setName("NPC_1_PROFILE");
										setBGColor(Color.lightGray);
										setBounds(140, 0, 290, 768);
									}
								});
								set(NPC_2, new GUIParts() {
									{
										setName("NPC_2_PROFILE");
										setBGColor(Color.orange);
										setBounds(140, 0, 290, 768);
									}
								});
								set(NPC_3, new GUIParts() {
									{
										setName("NPC_3_PROFILE");
										setBGColor(Color.pink);
										setBounds(140, 0, 290, 768);
									}
								});
								set(NPC_4, new GUIParts() {
									{
										setName("NPC_4_PROFILE");
										setBGColor(Color.green);
										setBounds(140, 0, 290, 768);
									}
								});
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
								// setBGColor(Color.black);
								setBounds(430, 0, 594, 520);
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
								setBounds(627, 220, 200, 300);

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

						this.appendLast(Speaker.setText("警长").enable());

						this.appendLast(Dialogue.setText(
								"1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111")
								.enable());

						this.appendLast(nextButton);
						// if (nextButton.clicked(e)) {

						// }
						// this.appendLast(DIALOGUE_SECTION);
						// System.out.println(this.childList);

					}
				});
			}
		});
		String[] options = { "1", "2", "3", "4" };
		generateOptions(options);
		set(SETTINGSCREEN, new GUIParts() {
			{
				setName("SETTINGSCREEN");
				setBGColor(Color.red);
			}
		});

		// 1.

		GHQ.addGUIParts(this);
	}

	public void setSpeaker(String speaker) {
		this.Speaker.setText(speaker);
	}

	public void setDialogue(String text) {
		this.Speaker.setText(text);
	}

	public void generateOptions(String[] options) {
		// List<AnimatedGHQTextArea> guiOptions = new ArrayList<AnimatedGHQTextArea>();
		for (int i = 0; i < options.length; i++) {
			AnimatedGHQTextArea guiOption = new AnimatedGHQTextArea() {
				@Override
				public boolean clicked(MouseEvent event) {
					// TODO: selectoption EDWARD
					System.out.println(Integer.valueOf(this.name()));
					return super.clicked(event);
				}
			};
			guiOption.setBGColor(Color.red);
			guiOption.setText(options[i]);
			guiOption.setName(String.valueOf(i));
			switch (i) {
				case 0:
					guiOption.setBounds(477, 690, 200, 20);
					break;
				case 1:
					guiOption.setBounds(690, 690, 200, 20);
					break;
				case 2:
					guiOption.setBounds(477, 720, 200, 20);
					break;
				case 3:
					guiOption.setBounds(690, 720, 200, 20);
					break;
				default:
					break;
			}
			// System.out.println(this);
			this.get(GAMESCREEN).appendFirst(guiOption);
			// this.childList.get(0).getChildren().get(GAMESCREEN).appendLast(guiOption);
		}
	}

}
