package gui;

import java.awt.Color;
import java.util.ArrayList;

import core.GHQ;
import paint.ColorFraming;
import paint.RectPaint;

public class CombinedButtons extends GUIParts{
	private int selection;
	private int defaultSelection;
	private RectPaint emphasizer = new ColorFraming(Color.RED,GHQ.stroke3);
	private final ArrayList<BasicButton> buttons = new ArrayList<BasicButton>();
	public CombinedButtons(String group, int defaultValue, RectPaint paintScript,int x, int y, int w, int h) {
		super(group, paintScript, x, y, w, h, false);
		selection = this.defaultSelection = defaultValue;
	}
	public CombinedButtons(String group, int defaultValue) {
		super(group, null, 0, 0, 0, 0, false);
		selection = this.defaultSelection = defaultValue;
	}
	public CombinedButtons(String group, RectPaint paintScript,int x, int y, int w, int h) {
		super(group, paintScript, x, y, w, h, false);
		selection = this.defaultSelection = GHQ.NONE;
	}
	public CombinedButtons(String group, int defaultValue,int x, int y, int w, int h) {
		super(group, null, x, y, w, h, false);
		selection = this.defaultSelection = defaultValue;
	}
	public CombinedButtons(String group) {
		super(group, null, 0, 0, 0, 0, false);
		selection = this.defaultSelection = GHQ.NONE;
	}
	//init
	public void setDefaultValue(int value) {
		defaultSelection = value;
	}
	public void setEmphasizer(RectPaint paintScript) {
		emphasizer = paintScript;
	}
	//main role
	public void addButton(int buttonID, RectPaint paintScript,int x, int y, int w, int h) {
		final BasicButton BUTTON = new BasicButton(super.GROUP + ">buttons", paintScript, x, y, w, h) {
			@Override
			public void clicked() {
				selection = (selection == buttonID ? defaultSelection : buttonID);
			}
			@Override
			public void idle() {
				super.idle();
				if(selection == buttonID)
					emphasizer.paint(x, y, w, h);
			}
		};
		GHQ.addGUIParts(BUTTON);
		buttons.add(BUTTON);
	}
	//control
	public void setSelection(int id) {
		selection = id;
	}
	public void reset() {
		selection = defaultSelection;
	}
	@Override
	public void enable() {
		super.enable();
		for(BasicButton button : buttons)
			button.enable();
	}
	@Override
	public void disable() {
		super.disable();
		for(BasicButton button : buttons)
			button.disable();
	}
	//information
	public int getSelection() {
		return selection;
	}
	public boolean isThisSelection(int selectionID) {
		return selection == selectionID;
	}
	public boolean isDefaultSelection() {
		return selection == defaultSelection;
	}
}
