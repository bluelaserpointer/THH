package gui;

import paint.RectPaint;

public class AutoResizeMenu extends GUIGroup{
	private final int DEFAULT_LINE_H;
	private final int MARGIN;
	private int lineAmount;
	public AutoResizeMenu(String group, RectPaint paintScript, int x, int y, int w, int defaultLineH) {
		super(group, paintScript, x, y, w, 1);
		DEFAULT_LINE_H = defaultLineH;
		MARGIN = 0;
	}
	public AutoResizeMenu(String group, RectPaint paintScript, int x, int y, int w, int defaultLineH, int partsMargin) {
		super(group, paintScript, x, y, w, 1);
		DEFAULT_LINE_H = defaultLineH;
		MARGIN = partsMargin;
	}
	
	@Override
	public void idle() {
		super.idle();
	}
	public void addPartsLine(GUIParts...guiParts) {
		if(MARGIN == 0) {
			final int AMOUNT = guiParts.length;
			final int PARTS_W = super.w / AMOUNT;
			for(int i = 0;i < guiParts.length;i++)
				super.addParts(guiParts[i]).setBounds(super.x + PARTS_W*i, super.y + DEFAULT_LINE_H*lineAmount, PARTS_W, DEFAULT_LINE_H);
		}else {
			final int AMOUNT = guiParts.length;
			final int PARTS_W = super.w / AMOUNT;
			for(int i = 0;i < guiParts.length;i++)
				super.addParts(guiParts[i]).setBounds(super.x + PARTS_W*i + MARGIN, super.y + DEFAULT_LINE_H*lineAmount + MARGIN, PARTS_W - MARGIN*2, DEFAULT_LINE_H - MARGIN*2);
		}
		lineAmount++;
		super.h += DEFAULT_LINE_H;
	}
	public void addEmptyLine() {
		lineAmount++;
		super.h += DEFAULT_LINE_H;
	}
}
