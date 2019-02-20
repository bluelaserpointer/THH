package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import core.GHQ;
import input.KeyTypeListener;
import paint.PaintScript;

public class TitledLabel extends GUIParts{
	private final KeyTypeListener typeListener = new KeyTypeListener(w/5) {
		@Override
		public void typeEnd(String text) {
			typeEnded(text);
		}
	};
	public TitledLabel(String group, PaintScript paintScript, int x, int y, int w, int h) {
		super(group, paintScript, x, y, w, h);
		GHQ.addListenerEx(typeListener);
	}
	private String titleStr = "";
	public boolean activated;
	public void setTitle(String title) {
		titleStr = title;
	}
	@Override
	public void clicked() {
		activated = true;
		typeListener.enable();
	}
	@Override
	public void outsideClicked() {
		activated = false;
		typeListener.disable();
	}
	@Override
	public void paint() {
		final Graphics2D G2 = GHQ.getGraphics2D();
		G2.setColor(Color.BLACK);
		G2.setStroke(GHQ.stroke1);
		G2.setFont(GHQ.basicFont);
		G2.drawString(titleStr, x, y - 8);
		PAINT_SCRIPT.paint(x, y, w, h);
		G2.setColor(Color.BLACK);
		G2.setStroke(activated ? GHQ.stroke3 : GHQ.stroke1);
		G2.drawRect(x, y, w, h);
		G2.drawString(typeListener.getText(), x + 3, y + 20);
	}
	public void typeEnded(String text) {}
	public void setText(String text) {
		typeListener.setText(text);
	}
	public String getText() {
		return typeListener.getText();
	}
	public boolean textEquals(String str) {
		return typeListener.textEquals(str);
	}
	@Override
	public void disable() {
		super.disable();
		typeListener.disable();
	}
	public void clear() {
		typeListener.clear();
	}

}
