package troubleCrasher.engine;

import java.awt.Color;
import java.util.List;

// import org.w3c.dom.events.MouseEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;

import core.GHQ;
import gui.AnimatedGHQTextArea;
import gui.GUIParts;
import gui.GUIPartsSwitcher;
import paint.ImageFrame;
import troubleCrasher.jigsaw.Jigsaw;
import troubleCrasher.jigsaw.JigsawEnum;
import troubleCrasher.person.PersonEnum;
import troubleCrasher.person.SceneEnum;

public class GamePageSwitcher extends GUIPartsSwitcher {
	private static final int STARTSCREEN = 0, GAMESCREEN = 1, SETTINGSCREEN = 2;
	private static final int PROFILE_SESSION = 0, BOX_SESSION = 1, SETTING_SESSION = 2, SAVE_SESSION = 3;
	public static final Color COLOR_BROWN = new Color(35, 12, 2), COLOR_GOLD = new Color(220, 207, 152);

	private GUIParts nextButton, NPC_PART, SCENE_PART;
	private AnimatedGHQTextArea Dialogue, Speaker;

	public GamePageSwitcher() {
		super(3, STARTSCREEN);
		Dialogue = (AnimatedGHQTextArea) new AnimatedGHQTextArea().setTextSpeed(1).setBounds(477, 580, 500, 100)
				.setBGColor(Color.getHSBColor(45, 21, 91)).disable();
		Speaker = (AnimatedGHQTextArea) new AnimatedGHQTextArea().setBounds(477, 540, 500, 30)
				.setBGImage("thhimage/Name_Tag.png").disable();

		nextButton = new GUIParts() {

			ImageFrame nextBar = ImageFrame.create("thhimage/Next_Bar.png");

			public boolean clicked(MouseEvent event) {
				if (!this.isEnabled)
					return super.clicked(event);
				// TODO: select option EDWARD
				System.out.println("Button clicked");
				TCGame.scriptManager.parseLine(TCGame.scriptManager.readLine(TCGame.scriptManager.buffReader));
				return super.clicked(event);
			}

			{
				setBounds(900, 698, 100, 40);
			}

			@Override
			public void paint() {
				super.paint();
				nextBar.rectPaint(left(), top(), width(), height());
				GHQ.getG2D(Color.WHITE);
				GHQ.drawStringGHQ("NEXT>" , left() + 10, top() + 30, 25F);
			}

		};
		NPC_PART = new GUIParts().setName("NPC_IMAGE").setBounds(627, 220, 200, 300)
				.setBGImage(PersonEnum.CAPTAIN.personImage);
		SCENE_PART = new GUIParts().setName("SCENE_PART").setBounds(430, 0, 594, 520);
		final ImageFrame arrowIF = ImageFrame.create("thhimage/Main_Menu_Arrow.png");

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
					{
						this.setName("START_MENU_TABS");
						this.appendFirst(newgameScrBtn);
						this.appendFirst(settingsScrBtn);
						this.appendFirst(loadgameScrBtn);
					}
					@Override
					public void paint() {
						super.paint();
						if(newgameScrBtn.isScreenMouseOvered()) {
							arrowIF.dotPaint(newgameScrBtn.left() - 100, newgameScrBtn.cy());
						}
						if(loadgameScrBtn.isScreenMouseOvered()) {
							arrowIF.dotPaint(loadgameScrBtn.left() - 100, loadgameScrBtn.cy());
						}
						if(settingsScrBtn.isScreenMouseOvered()) {
							arrowIF.dotPaint(settingsScrBtn.left() - 100, settingsScrBtn.cy());
						}
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
				this.setBGColor(Color.GRAY);
				// 游戏功能栏及其展开画面
				this.appendLast(new GUIPartsSwitcher(4, PROFILE_SESSION) {
					{
						setName("LEFT_MENU_AND_CONTENT");
						this.setBounds(0, 0, 70, 768);
						this.setBGImage("thhimage/UtilityBar.png");

						this.appendLast(
								getSwitcherButton_selectChangeImage(PROFILE_SESSION, "thhimage/UtilityBar_Profile.png", "thhimage/UtilityBar_Profile_Chosen.png")
										.setName("profileScrBtn").setBounds(10, 10, 50, 50));
						this.appendLast(getSwitcherButton_selectChangeImage(BOX_SESSION, "thhimage/UtilityBar_Resource.png", "thhimage/UtilityBar_Resource_Chosen.png")
								.setName("boxScrBtn").setBounds(10, 80, 50, 50));
						this.appendLast(
								getSwitcherButton_selectChangeImage(SETTING_SESSION, "thhimage/UtilityBar_Settings.png", "thhimage/UtilityBar_Settings_Chosen.png")
										.setName("settingScrBtn").setBounds(10, 160, 50, 50));
						this.appendLast(getSwitcherButton_selectChangeImage(SAVE_SESSION, "thhimage/UtilityBar_Save.png", "thhimage/UtilityBar_Save_Chosen.png")
								.setName("saveScrBtn").setBounds(10, 230, 50, 50));

						set(PROFILE_SESSION, new GUIPartsSwitcher(5, 0) {
							{
								setName("PROFILE_SESSION");
								this.setBounds(70, 0, 70, 768);
								setBGImage("thhimage/Character_Bar.png");
								// 一个人物一个框
								for (int i = 0; i < PersonEnum.values().length - 3; i++) {
									final GUIParts NPC_Button = getSwitcherButton(i)
											.setBGImage(PersonEnum.values()[i].personIcon).setName("NPC_1ScrBtn")
											.setBounds(75, 20 + i * 70, 60, 60);
									this.appendLast(NPC_Button);
									final GUIParts NPC_Profile = new GUIParts().setName("NPC_PROFILE")
											.setBGColor(COLOR_BROWN).setBounds(140, 0, 290, 768);
									set(i, NPC_Profile);
								}

							}
						});

						set(BOX_SESSION, new GUIParts() {
							{
								setName("BOX_SESSION");
								setBounds(70, 0, 360, 768);
								setBGColor(COLOR_BROWN);
								this.addLast(TCGame.jigsawViewer).setBGColor(COLOR_BROWN).setBounds(100, 600,
										JigsawEnum.JIGSAW_GRID_SIZE * 6, JigsawEnum.JIGSAW_GRID_SIZE * 3);
								this.addLast(new GUIParts() {
									{
										this.setBGColor(Color.BLACK);
										this.setBounds(100, 400, 300, 200);
									}

									@Override
									public boolean clicked(MouseEvent e) {
										final boolean consumed = super.clicked(e);
										Jigsaw hookingJigsaw = TCGame.jigsawViewer.hookingJigsaw();
										Jigsaw disposedJigsaw = TCGame.jigsawViewer.disposedJigsaw();
										if (hookingJigsaw != null) { // 拿进
											TCGame.jigsawViewer.disposeHookJigsaw();
											TCGame.resource.delHp(1);
										} else if (disposedJigsaw != null) { // 拿走
										}
										return consumed;
									}

									@Override
									public void paint() {
										super.paint();
										GHQ.getG2D(Color.WHITE);
										GHQ.drawStringGHQ("承受", cx() - 30, cy() + 10, 30F);
										Jigsaw disposedJigsaw = TCGame.jigsawViewer.disposedJigsaw();
										if (disposedJigsaw != null) {
											disposedJigsaw.paint(left(), top());
										}
									}
								});
								this.appendLast(new GUIParts() {
									{
										this.setBGColor(COLOR_GOLD);
										this.setBounds(100, 280, 100, 100);
									}
								});
								this.appendLast(new GUIParts() {
									
									final GUIParts inspectScrBtn = getSwitcherButton(GAMESCREEN)
											.setBGImage("thhimage/InspectButton.png").setName("inspectScrBtn")
											.setBounds(280, 290, 50, 30),
											placeScrBtn = getSwitcherButton(SETTINGSCREEN)
													.setBGImage("thhimage/PlaceButton.png").setName("placeScrBtn")
													.setBounds(280, 340, 50, 30);
									{
										this.appendFirst(inspectScrBtn);
										this.appendFirst(placeScrBtn);
									}
									@Override
									public void paint() {
										super.paint();
										if(inspectScrBtn.isScreenMouseOvered()) {
											arrowIF.rectPaint(inspectScrBtn.left() - 60, inspectScrBtn.cy()-10, 40,20);
										}
										if(placeScrBtn.isScreenMouseOvered()) {
											arrowIF.rectPaint(placeScrBtn.left() - 60, placeScrBtn.cy()-10, 40,20);
										}
									}
								});
							}
							ImageFrame heartIF = ImageFrame.create("thhimage/Heart.png");
							ImageFrame staminaIF = ImageFrame.create("thhimage/Stamina.png");
							ImageFrame heartGaugeIF = ImageFrame.create("thhimage/Life_Gauge.png");
							ImageFrame staminaGaugeIF = ImageFrame.create("thhimage/Stamina_Gauge.png");

							@Override
							public void paint() {
								super.paint();
								GHQ.getG2D(COLOR_GOLD).setFont(GHQ.initialFont.deriveFont(25F));
								GHQ.getG2D().drawString("金钱$      " + TCGame.resource.getMoney(), 100, 50);
								GHQ.getG2D().drawString("行动点", 100, 100);
								GHQ.getG2D().drawString("生命值", 100, 155);
								for (int i = 0; i < 4; ++i) {
									staminaGaugeIF.dotPaint(220 + i * 55, 93);
									if (i < TCGame.resource.getStamina())
										staminaIF.dotPaint(220 + i * 55, 93);
								}
								for (int i = 0; i < 3; ++i) {
									heartGaugeIF.dotPaint(220 + i * 55, 145);
									if (i < TCGame.resource.getHp())
										heartIF.dotPaint(220 + i * 55, 145);
								}
								GHQ.getG2D().setFont(GHQ.initialFont);
							}
						});
						set(SETTING_SESSION, new GUIParts() {
							{
								setName("SETTING_SESSION");
								this.setBGImage("thhimage/Settings_Bar.png");
								setBounds(70, 0, 360, 768);
								this.appendLast(new GUIParts() {
									final GUIParts displayScrBtn = getSwitcherButton(GAMESCREEN)
											.setBGImage("thhimage/Settings_Bar_DisplayButton.png").setName("displayScrBtn")
											.setBounds(200, 140, 100, 30),
											musicScrBtn = getSwitcherButton(SETTINGSCREEN)
													.setBGImage("thhimage/Settings_Bar_MusicButton.png").setName("musicScrBtn")
													.setBounds(200, 200, 100, 30),
													exitScrBtn = getSwitcherButton(SETTINGSCREEN)
															.setBGImage("thhimage/Settings_Bar_ExitButton.png").setName("exitScrBtn")
															.setBounds(200, 260, 100, 30);
									{
										this.setName("SAVE_LOAD_TABS");
										this.appendFirst(displayScrBtn);
										this.appendFirst(musicScrBtn);
										this.appendFirst(exitScrBtn);
									}
									@Override
									public void paint() {
										super.paint();
										if(displayScrBtn.isScreenMouseOvered()) {
											arrowIF.rectPaint(displayScrBtn.left() - 60, displayScrBtn.cy()-10, 40,20);
										}
										if(musicScrBtn.isScreenMouseOvered()) {
											arrowIF.rectPaint(musicScrBtn.left() - 60, musicScrBtn.cy()-10, 40,20);
										}
										if(exitScrBtn.isScreenMouseOvered()) {
											arrowIF.rectPaint(exitScrBtn.left() - 60, exitScrBtn.cy()-10, 40,20);
										}
									}
								});
							}
						});
						set(SAVE_SESSION, new GUIParts() {
							{
								setName("SAVE_SESSION");
								setBGImage("thhimage/SaveBar.png");
								setBounds(70, 0, 360, 768);
								this.appendLast(new GUIParts() {
									final GUIParts savegameScrBtn = getSwitcherButton(GAMESCREEN)
											.setBGImage("thhimage/SaveBar_SaveButton.png").setName("newgameScrBtn")
											.setBounds(210, 160, 80, 30),
											loadgameScrBtn = getSwitcherButton(SETTINGSCREEN)
													.setBGImage("thhimage/SaveBar_LoadButton.png").setName("loadgameScrBtn")
													.setBounds(210, 220, 80, 30);
									{
										this.setName("SAVE_LOAD_TABS");
										this.appendFirst(savegameScrBtn);
										this.appendFirst(loadgameScrBtn);
									}
									@Override
									public void paint() {
										super.paint();
										if(savegameScrBtn.isScreenMouseOvered()) {
											arrowIF.rectPaint(savegameScrBtn.left() - 60, savegameScrBtn.cy()-10, 40,20);
										}
										if(loadgameScrBtn.isScreenMouseOvered()) {
											arrowIF.rectPaint(loadgameScrBtn.left() - 60, loadgameScrBtn.cy()-10, 40,20);
										}
									}
								});
							}
						});
					}
				});

				// 场景画面
				this.appendLast(SCENE_PART);
				// NPC
				this.appendFirst(NPC_PART);

				this.appendLast(Speaker.setText("警长").enable());

				this.appendLast(Dialogue.setText("点击开始游戏").enable());

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
				setBGColor(COLOR_BROWN);
			}
		});

		//init options button
		for (int i = 0; i < appendedGUIOption.length; i++) {
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
			guiOption.setName(String.valueOf(i));
			appendedGUIOption[i] = guiOption;
		}

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

	private final AnimatedGHQTextArea[] appendedGUIOption = new AnimatedGHQTextArea[4];
	public void generateOptions(List<String> options, List<Boolean> optionStatus) {
		for (GUIParts parts : appendedGUIOption)
			parts.disable();
		if (options == null) {
			return;
		}
		nextButton.disable();
		for (int i = 0; i < options.size(); i++) {
			final AnimatedGHQTextArea guiOption = appendedGUIOption[i];
			guiOption.enable();
			guiOption.setText("       " + options.get(i));
			guiOption.setTextColor(Color.white);
			switch (i) {
				case 0:
					guiOption.setBGImage("thhimage/Option1.png");
					guiOption.setBounds(477, 690, 200, 30);
					break;
				case 1:
					guiOption.setBGImage("thhimage/Option2.png");
					guiOption.setBounds(690, 690, 200, 30);
					break;
				case 2:
					guiOption.setBGImage("thhimage/Option3.png");
					guiOption.setBounds(477, 720, 200, 30);
					break;
				case 3:
					guiOption.setBGImage("thhimage/Option4.png");
					guiOption.setBounds(690, 720, 200, 30);
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
