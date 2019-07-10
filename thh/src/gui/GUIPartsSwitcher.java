package gui;

import java.util.Stack;

import paint.rect.RectPaint;

public class GUIPartsSwitcher extends GUIParts{

	private int defaultIndex, nowIndex;
	private final GUIParts[] parts;
	private final Stack<Integer> history = new Stack<Integer>();
	public GUIPartsSwitcher(String NAME, int partsAmount, int defaultIndex, RectPaint bgPaint) {
		super(NAME, bgPaint, true);
		this.parts = new GUIParts[partsAmount];
		nowIndex = this.defaultIndex = defaultIndex;
	}
	
	//idle
	@Override
	public void idle() {
		parts[nowIndex].idle();
	}
	
	//control
	public GUIParts get(int index) {
		return parts[index];
	}
	public <T extends GUIParts>T set(int index, T parts) {
		this.parts[index] = parts;
		return parts;
	}
	public void switchTo(int index) {
		if(nowIndex == index)
			return;
		parts[nowIndex].disable();
		parts[nowIndex = index].enable();
	}
	public void switchToDefault() {
		switchTo(defaultIndex);
	}
	public void prev() {
		switchTo(nowIndex > 0 ? nowIndex - 1 : parts.length - 1);
	}
	public void next() {
		switchTo(nowIndex < parts.length - 1 ? nowIndex + 1 : 0);
	}
	public void prev(int page) {
		switchTo((nowIndex - page) % parts.length);
	}
	public void next(int page) {
		switchTo((nowIndex + page) % parts.length);
	}
	public void pushHistory() {
		history.push(nowIndex);
	}
	public void clearHistory() {
		history.clear();
	}
	public boolean returns() {
		if(history.isEmpty())
			return false;
		switchTo(history.pop());
		return true;
	}
	public boolean returnsFirst() {
		if(history.isEmpty())
			return false;
		switchTo(history.lastElement());
		history.clear();
		return true;
	}
	@Override
	public void enable() {
		super.enable();
		parts[nowIndex].enable();
	}
	@Override
	public void disable() {
		super.disable();
		parts[nowIndex].disable();
	}
	
	//information
	public int nowIndex() {
		return nowIndex;
	}
	
	//////////////////////
	//For child parts
	@Override
	public void clicked() {
		super.clicked();
		if(parts[nowIndex].isMouseEntered())
			parts[nowIndex].clicked();
	}
	@Override
	public void outsideClicked() {
		super.outsideClicked();
		parts[nowIndex].outsideClicked();
	}
	@Override
	public void released() {
		super.released();
		if(parts[nowIndex].isMouseEntered())
			parts[nowIndex].released();
	}
	@Override
	public void outsideReleased() {
		super.outsideReleased();
		parts[nowIndex].outsideReleased();
	}
	@Override
	public void mouseOvered() {
		super.mouseOvered();
		if(parts[nowIndex].isMouseEntered())
			parts[nowIndex].mouseOvered();
	}
	@Override
	public void outsideMouseOvered() {
		super.outsideMouseOvered();
		parts[nowIndex].outsideMouseOvered();
	}
}
