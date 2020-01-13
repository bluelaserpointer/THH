package gui;

import java.util.Stack;

public class GUIPartsSwitcher extends GUIParts{

	private int defaultIndex, nowIndex;
	private final GUIParts[] parts;
	private final Stack<Integer> history = new Stack<Integer>();
	public GUIPartsSwitcher(int partsAmount, int defaultIndex) {
		this.parts = new GUIParts[partsAmount];
		nowIndex = this.defaultIndex = defaultIndex;
	}
	
	//idle
	@Override
	public void idle() {
		super.idle();
		parts[nowIndex].idle();
	}
	
	//control
	public GUIParts get(int index) {
		return parts[index];
	}
	public <T extends GUIParts>T set(int index, T parts) {
		this.parts[index] = parts;
		super.addLast(parts);
		if(index != nowIndex)
			parts.disable();
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
	public GUIParts getSwitcherButton(int index) {
		return new GUIParts() {
			@Override
			public void clicked() {
				switchTo(index);
			}
		}.setName(name() + "->SwitcherOf" + index);
	}
	public void prev() {
		switchTo(nowIndex > 0 ? nowIndex - 1 : parts.length - 1);
	}
	public void next() {
		switchTo(nowIndex < parts.length - 1 ? nowIndex + 1 : 0);
	}
	public GUIParts getPrevButton() {
		return new GUIParts() {
			@Override
			public void clicked() {
				prev();
			}
		}.setName(name() + "->PrevButton");
	}
	public GUIParts getNextButton() {
		return new GUIParts() {
			@Override
			public void clicked() {
				next();
			}
		}.setName(name() + "->NextButton");
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
}
