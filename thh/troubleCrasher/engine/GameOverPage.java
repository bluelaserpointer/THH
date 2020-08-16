package troubleCrasher.engine;

import core.GHQ;
import gui.GUIParts;
import paint.ImageFrame;

public class GameOverPage extends GUIParts {
	private final GUIParts toStartScreenBtn;
	private final GUIParts toLoadScreenBtn;
	private String text;
	public GameOverPage() {
		this.setBGImage("thhimage/Game_Over_BG.png");
		toStartScreenBtn = new GUIParts();
		//toStartScreenBtn = TCGame.gamePageSwitcher.getSwitcherButton(GamePageSwitcher.START_SCREEN);
		toLoadScreenBtn = new GUIParts();
		//toLoadScreenBtn = TCGame.gamePageSwitcher.getSwitcherButton(GamePageSwitcher.SETTINGSCREEN);
		toStartScreenBtn.setBGImage("thhimage/Main_Menu_LoadGame.png"); //TODO: change it
		toLoadScreenBtn.setBGImage("thhimage/Main_Menu_LoadGame.png");
		toStartScreenBtn.setBounds(400, 500, 200, 50);
		toLoadScreenBtn.setBounds(400, 585, 200, 50);
		this.addLast(toStartScreenBtn);
		this.addLast(toLoadScreenBtn);
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
		}
		GHQ.getG2D(GamePageSwitcher.COLOR_GOLD);
		GHQ.drawStringGHQ("警长在过大的压力与重伤之下倒下了", 340, 400, 20F);
	}
}
