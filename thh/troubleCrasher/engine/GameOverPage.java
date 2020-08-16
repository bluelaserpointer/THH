package troubleCrasher.engine;

import core.GHQ;
import gui.GUIParts;
import paint.ImageFrame;

public class GameOverPage extends GUIParts {
	private final GUIParts toStartScreenBtn;
	private final GUIParts toLoadScreenBtn;
	private final GUIParts endGameScreenBtn;
	private String text;
	public GameOverPage(GamePageSwitcher gamePageSwitcher) {
		this.setBGImage("thhimage/Game_Over_BG.png");
//		toStartScreenBtn = new GUIParts();
		toStartScreenBtn = gamePageSwitcher.getSwitcherButton(GamePageSwitcher.STARTSCREEN);
//		toLoadScreenBtn = new GUIParts();
		toLoadScreenBtn = gamePageSwitcher.getSwitcherButton(GamePageSwitcher.SETTINGSCREEN);
//		endGameScreenBtn = new GUIParts();
		endGameScreenBtn = new GUIParts();
		
		toStartScreenBtn.setBGImage("thhimage/BackToMainMenu.png"); //TODO: change it
		toLoadScreenBtn.setBGImage("thhimage/SaveBar_LoadButton.png");
		endGameScreenBtn.setBGImage("thhimage/QuitGame.png");
		toStartScreenBtn.setBounds(420, 490, 160, 40);
		toLoadScreenBtn.setBounds(460, 550, 80, 40);
		endGameScreenBtn.setBounds(420, 610, 160, 40);
		this.addLast(toStartScreenBtn);
		this.addLast(toLoadScreenBtn);
		this.addLast(endGameScreenBtn);
	}
	private final ImageFrame arrowIF = ImageFrame.create("thhimage/Main_Menu_Arrow.png");
	@Override
	public void paint() {
		super.paint();
		if(toStartScreenBtn.isScreenMouseOvered()) {
			arrowIF.dotPaint(toStartScreenBtn.left() - 100, toStartScreenBtn.cy());
		}
		if(toLoadScreenBtn.isScreenMouseOvered()) {
			arrowIF.dotPaint(toLoadScreenBtn.left() - 100, toLoadScreenBtn.cy());
		}if(endGameScreenBtn.isScreenMouseOvered()) {
			arrowIF.dotPaint(endGameScreenBtn.left() - 100, endGameScreenBtn.cy());
		}
		GHQ.getG2D(GamePageSwitcher.COLOR_GOLD);
		GHQ.drawStringGHQ("警长在过大的压力与重伤之下倒下了", 340, 400, 20F);
	}
}
