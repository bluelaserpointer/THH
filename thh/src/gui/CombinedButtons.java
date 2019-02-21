package gui;

import java.awt.Color;

import core.GHQ;
import paint.ColorFraming;
import paint.PaintScript;

public class CombinedButtons extends GUIParts{
	private int selection;
	private int defaultSelection;
	private PaintScript emphasizer = new ColorFraming(Color.RED,GHQ.stroke3);
	public CombinedButtons(String group, int defaultValue, PaintScript paintScript,int x, int y, int w, int h) {
		super(group, paintScript, x, y, w, h);
		selection = this.defaultSelection = defaultValue;
	}
	public CombinedButtons(String group, int defaultValue) {
		super(group, null, 0, 0, 0, 0);
		selection = this.defaultSelection = defaultValue;
	}
	public CombinedButtons(String group, PaintScript paintScript,int x, int y, int w, int h) {
		super(group, paintScript, x, y, w, h);
		selection = this.defaultSelection = GHQ.NONE;
	}
	public CombinedButtons(String group, int defaultValue,int x, int y, int w, int h) {
		super(group, null, x, y, w, h);
		selection = this.defaultSelection = defaultValue;
	}
	public CombinedButtons(String group) {
		super(group, null, 0, 0, 0, 0);
		selection = this.defaultSelection = GHQ.NONE;
	}
	//init
	public void setDefaultValue(int value) {
		defaultSelection = value;
	}
	public void setEmphasizer(PaintScript paintScript) {
		emphasizer = paintScript;
	}
	//fix
	@Override
	public boolean absorbClickEvent() {
		return false;
	}
	//main role
	public void addButton(int buttonID, PaintScript paintScript,int x, int y, int w, int h) {
		GHQ.addGUIParts(new BasicButton(super.GROUP, paintScript, x, y, w, h) {
			@Override
			public void clicked() {
				selection = (selection == buttonID ? defaultSelection : buttonID);
			}
			@Override
			public void paint() {
				super.paint();
				if(selection == buttonID)
					emphasizer.paint(x, y, w, h);
			}
		});
	}
	//control
	public void setSelection(int id) {
		selection = id;
	}
	public void reset() {
		selection = defaultSelection;
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
