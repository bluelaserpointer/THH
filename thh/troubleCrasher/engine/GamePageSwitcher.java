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
	private static final int WORK_SCENE_DAY = 0, WORK_SCENE_NIGHT = 1, BAR_SCENE = 2; // DIFFERENT SCENES
	private static final int NO_ONE = -1, CAPTAIN = 0, FARMER = 1, DOCTOR = 2, ANNOUNCER = 3; // DIFFERENT NPCS
	// private final ImageFrame buttonPaint;
	private String[] SceneBG = { "thhimage/OfficeDay.png", "thhimage/OfficeNight.png", "thhimage/Bar.png" };
	private String[] NPCImg = { "thhimage/Captain.png", "thhimage/Farmer.png", "thhimage/Doctor.png",
			"thhimage/Announcer.png" };

	private GUIParts nextButton, DIALOGUE_SECTION, NPC_PART, SCENE_PART;
	private AnimatedGHQTextArea Dialogue, Speaker;
	private int centerW, centerH;

	public GamePageSwitcher() {
		super(3, STARTSCREEN);
		// buttonPaint = ImageFrame.create("thhimage/veg_leaf3.png"); // 按钮图片
		Dialogue = (AnimatedGHQTextArea) new AnimatedGHQTextArea().setTextSpeed(3).setBounds(477, 580, 500, 100)
				.setBGColor(Color.green).disable();
		Speaker = (AnimatedGHQTextArea) new AnimatedGHQTextArea().setBounds(477, 540, 500, 30).setBGColor(Color.yellow)
				.disable();
		nextButton = new GUIParts().setBGColor(Color.pink).setBounds(900, 698, 100, 50);
		System.out.println(SceneBG[WORK_SCENE_DAY]);
		NPC_PART = new GUIParts().setName("NPC_IMAGE").setBounds(627, 220, 200, 300).setBGImage(NPCImg[CAPTAIN]);
		SCENE_PART = new GUIParts().setName("SCENE_PART").setBounds(430, 0, 594, 520)
				.setBGImage(SceneBG[WORK_SCENE_DAY]);

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
				this.setBGImage(SceneBG[WORK_SCENE_DAY]);
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
				this.appendLast(SCENE_PART);
				// NPC
				this.appendFirst(NPC_PART);

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

	public void setNPCImage(String img) {
		this.NPC_PART.setBGImage(img);
	}

	public void setSceneImage(String img) {
		this.SCENE_PART.setBGImage(img);
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
