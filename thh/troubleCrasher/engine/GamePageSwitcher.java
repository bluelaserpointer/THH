package troubleCrasher.engine;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

// import org.w3c.dom.events.MouseEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;

import core.GHQ;
import gui.AnimatedGHQTextArea;
import gui.AutoResizeMenu;
import gui.GUIParts;
import gui.GUIPartsSwitcher;
import paint.ImageFrame;
import paint.rect.RectPaint;
import troubleCrasher.jigsaw.JigsawEnum;
import troubleCrasher.person.PersonEnum;
import troubleCrasher.person.SceneEnum;

public class GamePageSwitcher extends GUIPartsSwitcher {
	private static final int STARTSCREEN = 0, GAMESCREEN = 1, SETTINGSCREEN = 2;
	private static final int PROFILE_SESSION = 0, BOX_SESSION = 1, SETTING_SESSION = 2, SAVE_SESSION = 3;
	private static final int NO_ONE = -1, CAPTAIN = 0, FARMER = 1, DOCTOR = 2, ANNOUNCER = 3; // DIFFERENT NPCS
	// private final ImageFrame buttonPaint;

	private GUIParts nextButton, NPC_PART, SCENE_PART;
	private AnimatedGHQTextArea Dialogue, Speaker;

	public GamePageSwitcher() {
		super(3, STARTSCREEN);
		// buttonPaint = ImageFrame.create("thhimage/veg_leaf3.png"); // 按钮图片
		Dialogue = (AnimatedGHQTextArea) new AnimatedGHQTextArea().setTextSpeed(1).setBounds(477, 580, 500, 100)
				.setBGColor(Color.green).disable();
		Speaker = (AnimatedGHQTextArea) new AnimatedGHQTextArea().setBounds(477, 540, 500, 30).setBGColor(Color.yellow)
				.disable();
		nextButton = new GUIParts() {

			public boolean clicked(MouseEvent event) {
				if (!this.isEnabled)
					return super.clicked(event);
				// TODO: select option EDWARD
				System.out.println("Button clicked");
				try {
					TCGame.scriptManager.parseLine(TCGame.scriptManager.buffReader.readLine());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return super.clicked(event);
			}

		}.setBGColor(Color.pink).setBounds(900, 698, 100, 50);
		NPC_PART = new GUIParts().setName("NPC_IMAGE").setBounds(627, 220, 200, 300)
				.setBGImage(PersonEnum.CAPTAIN.personImage);
		SCENE_PART = new GUIParts().setName("SCENE_PART").setBounds(430, 0, 594, 520)
				.setBGImage(SceneEnum.WORK_DAY.sceneImage);

		// DIALOGUE_SECTION = new GUIParts().setBGColor(Color.green).setBounds(430, 520,
		// 1024, 768).appendLast(Speaker)
		// .appendLast(Dialogue.setText(
		// "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111")
		// .enable())
		// .appendLast(nextButton);

		// Start Menu
		set(STARTSCREEN, new GUIParts() {
			{
				this.setBGImage("thhimage/Main_Menu_BG.png");
				setName("STARTSCREEN");

				this.appendLast(new GUIParts() {

					final GUIParts newgameScrBtn = getSwitcherButton(GAMESCREEN)
							.setBGImage("thhimage/Main_Menu_NewGame.png").setName("newgameScrBtn")
							.setBounds(730, 320, 230, 50),
							loadgameScrBtn = getSwitcherButton(SETTINGSCREEN)
									.setBGImage("thhimage/Main_Menu_LoadGame.png").setName("loadgameScrBtn")
									.setBounds(720, 410, 250, 50),
							settingsScrBtn = getSwitcherButton(SETTINGSCREEN)
									.setBGImage("thhimage/Main_Menu_Setting.png").setName("settingScrBtn")
									.setBounds(745, 500, 200, 50);

					// quitScrBtn =
					// getSwitcherButton(SETTINGSCREEN).setBGImage("thhimage/veg_leaf2.png")
					// .setName("settingScrBtn").setBounds(512, 400, 100, 200);
					// TODO: Add quit
					// button
					{
						this.setName("START_MENU_TABS");
						this.appendFirst(newgameScrBtn);
						this.appendFirst(settingsScrBtn);
						this.appendFirst(loadgameScrBtn);
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
						setName("LEFT_MENU_AND_CONTENT");
						this.setBounds(0, 0, 430, 768);
						this.setBGImage("thhimage/UtilityBar.png");

						this.appendLast(getSwitcherButton(PROFILE_SESSION)
									.setBGImage("thhimage/UtilityBar_Profile_Chosen.png").setName("profileScrBtn")
									.setBounds(10, 10, 50, 50));
						this.appendLast(getSwitcherButton(BOX_SESSION)
											.setBGImage("thhimage/UtilityBar_Resource.png").setName("boxScrBtn")
											.setBounds(10, 80, 50, 50));
						this.appendLast(getSwitcherButton(SETTING_SESSION)
											.setBGImage("thhimage/UtilityBar_Settings.png").setName("settingScrBtn")
											.setBounds(10, 160, 50, 50));
						this.appendLast(getSwitcherButton(SAVE_SESSION)
											.setBGImage("thhimage/UtilityBar_Save.png").setName("saveScrBtn")
											.setBounds(10, 230, 50, 50));

						set(PROFILE_SESSION, new GUIPartsSwitcher(5, 0) {
							{
								setName("PROFILE_SESSION");
								this.setBounds(70, 0, 70, 768);
								// 一个人物一个框
								for (int i = 0; i < PersonEnum.values().length - 2; i++) {
									final GUIParts NPC_Button = getSwitcherButton(i)
											.setBGImage("thhimage/veg_leaf2.png").setName("NPC_1ScrBtn")
											.setBounds(75, 20 + i * 70, 60, 60);
									this.appendLast(NPC_Button);
									final GUIParts NPC_Profile = new GUIParts().setName("NPC_PROFILE")
											.setBGImage(PersonEnum.values()[i].personIcon).setBounds(140, 0, 290, 768);
									set(i, NPC_Profile);
									// set(i, new GUIParts() {
									// {
									// setName("NPC_PROFILE");
									// setBGImage(PersonEnum.values()[i].personIcon);
									// setBounds(140, 0, 290, 768);
									// }
									// });
								}
								// this.appendLast(new GUIParts() {
								// final GUIParts NPC_1ScrBtn =
								// getSwitcherButton(NPC_1).setBGImage("thhimage/veg_leaf2.png")
								// .setName("NPC_1ScrBtn").setBounds(75, 20, 60, 60),
								// NPC_2ScrBtn =
								// getSwitcherButton(NPC_2).setBGImage("thhimage/veg_leaf2.png")
								// .setName("NPC_2ScrBtn").setBounds(75, 90, 60, 60),
								// NPC_3ScrBtn =
								// getSwitcherButton(NPC_3).setBGImage("thhimage/veg_leaf2.png")
								// .setName("NPC_3ScrBtn").setBounds(75, 160, 60, 60),
								// NPC_4ScrBtn =
								// getSwitcherButton(NPC_4).setBGImage("thhimage/veg_leaf2.png")
								// .setName("NPC_4ScrBtn").setBounds(75, 230, 60, 60);
								// // setBGColor(Color.red);
								// // setBounds(70, 0, 360, 768);
								// {
								// // this.addNewLine(gameScrBtn, settingsScrBtn);
								// this.setName("PROFILE_MENU_TOP_TAB");
								// this.appendLast(NPC_1ScrBtn);
								// this.appendLast(NPC_2ScrBtn);
								// this.appendLast(NPC_3ScrBtn);
								// this.appendLast(NPC_4ScrBtn);
								// this.setBounds(70, 0, 70, 768);
								// }
								// });
								// set(NPC_1, new GUIParts() {
								// {
								// setName("NPC_1_PROFILE");
								// setBGColor(Color.lightGray);
								// setBounds(140, 0, 290, 768);
								// }
								// });
								// set(NPC_2, new GUIParts() {
								// {
								// setName("NPC_2_PROFILE");
								// setBGColor(Color.orange);
								// setBounds(140, 0, 290, 768);
								// }
								// });
								// set(NPC_3, new GUIParts() {
								// {
								// setName("NPC_3_PROFILE");
								// setBGColor(Color.pink);
								// setBounds(140, 0, 290, 768);
								// }
								// });
								// set(NPC_4, new GUIParts() {
								// {
								// setName("NPC_4_PROFILE");
								// setBGColor(Color.green);
								// setBounds(140, 0, 290, 768);
								// }
								// });
							}
						});

						set(BOX_SESSION, new GUIParts() {
							{
								setName("BOX_SESSION");
								setBounds(70, 0, 360, 768);
								this.addLast(TCGame.jigsawViewer).setBGColor(Color.LIGHT_GRAY).setBounds(100, 600, JigsawEnum.JIGSAW_GRID_SIZE*6, JigsawEnum.JIGSAW_GRID_SIZE*3);
							}
							ImageFrame heartIF = ImageFrame.create("thhimage/Heart.png");
							ImageFrame staminaIF = ImageFrame.create("thhimage/Stamina.png");
							ImageFrame heartGaugeIF = ImageFrame.create("thhimage/Life_Gauge.png");
							ImageFrame staminaGaugeIF = ImageFrame.create("thhimage/Stamina_Gauge.png");
							@Override
							public void paint() {
								super.paint();
								GHQ.getG2D(Color.BLACK).setFont(GHQ.initialFont.deriveFont(25F));
								GHQ.getG2D().drawString("金钱$      " + TCGame.resource.getMoney(), 100, 50);
								GHQ.getG2D().drawString("行动点", 100, 100);
								GHQ.getG2D().drawString("生命值", 100, 155);
								for(int i = 0; i < 4; ++i) {
									staminaGaugeIF.dotPaint(220 + i*55, 93);
									if(i >= TCGame.resource.getStamina())
										staminaIF.dotPaint(220 + i*55, 93);
								}
								for(int i = 0; i < 3; ++i) {
									heartGaugeIF.dotPaint(220 + i*55, 145);
									if(i >= TCGame.resource.getHp())
										heartIF.dotPaint(220 + i*55, 145);
								}
								GHQ.getG2D().setFont(GHQ.initialFont);
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
								// setBGColor(Color.pink);
								setBGImage("thhimage/SaveBar.png");
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

	// 改变说话人物图片以及对话，讲话的人
	public void setDialogue(String text, PersonEnum Speaker) {
		System.out.println(Speaker.name);
		this.Dialogue.setText(text);
		this.Speaker.setText(Speaker.name);
		this.NPC_PART.setBGImage(Speaker.personImage);
	}

	public void setNPCImage(String img) {
		this.NPC_PART.setBGImage(img);
	}

	// 调换场景图片
	public void setSceneImage(SceneEnum Scene) {
		this.SCENE_PART.setBGImage(Scene.sceneImage);
	}

	private final LinkedList<GUIParts> appendedGUIOption = new LinkedList<>();

	public void generateOptions(List<String> options) {
		// List<AnimatedGHQTextArea> guiOptions = new ArrayList<AnimatedGHQTextArea>();

		for (GUIParts partsToDelete : appendedGUIOption) {
			this.get(GAMESCREEN).remove(partsToDelete);
		}
		appendedGUIOption.clear();

		if (options == null) {
			return;
		}
		nextButton.disable();
		for (int i = 0; i < options.size(); i++) {
			AnimatedGHQTextArea guiOption = new AnimatedGHQTextArea() {
				@Override
				public boolean clicked(MouseEvent event) {
					// TODO: selectoption EDWARD
					// System.out.println(Integer.valueOf(this.name()));
					TCGame.scriptManager.chooseOption(Integer.valueOf(this.name()) + 1);
					nextButton.enable();
					generateOptions(null);
					return super.clicked(event);
				}
			};

			appendedGUIOption.add(guiOption);

			guiOption.setBGColor(Color.red);
			guiOption.setText(options.get(i));
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
