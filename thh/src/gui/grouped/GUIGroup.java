package gui.grouped;

import java.util.Iterator;
import java.util.LinkedList;

import core.GHQ;
import gui.GUIParts;
import paint.rect.RectPaint;

public class GUIGroup extends GUIParts{

	protected final LinkedList<GUIParts> parts = new LinkedList<GUIParts>();
	public GUIGroup(String name, RectPaint bgPaint, int x, int y, int w, int h) {
		super(name, bgPaint, x, y, w, h, true);
	}
	public GUIGroup(String group, RectPaint bgPaint) {
		super(group, bgPaint, true);
	}
	
	//main role
	@Override
	public void idle() {
		super.idle();
		//paint
		final Iterator<GUIParts> ite = parts.descendingIterator();
		while(ite.hasNext())
			ite.next().idleIfEnabled();
	}
	
	//control
	/**
	 * Add a GUIParts to this GUIGroup.
	 * It's idle method and other event methods is called automatically by this GUIGroup.
	 * @param guiParts
	 * @return added GUIParts
	 */
	public <T extends GUIParts>T addLast(T guiParts) {
		parts.addLast(guiParts);
		return guiParts;
	}
	public GUIGroup appendLast(GUIParts guiParts) {
		parts.addLast(guiParts);
		return this;
	}
	/**
	 * Add a GUIParts to this GUIGroup, and set it's click priority to the highest.
	 * It's idle method and other event methods is called automatically by this GUIGroup.
	 * @param guiParts
	 * @return added GUIParts
	 */
	public <T extends GUIParts>T addFirst(T guiParts) {
		parts.addFirst(guiParts);
		return guiParts;
	}
	public GUIGroup appendFirst(GUIParts guiParts) {
		parts.addFirst(guiParts);
		return this;
	}
	/**
	 * Only enable those specified GUIParts whose group name is [groupName], and disable the others.
	 * @param groupName
	 */
	public void enableSpecifiedChild(String groupName) {
		boolean isFound = false;
		for(GUIParts ver : parts) {
			if(ver.NAME.equals(groupName)) {
				ver.enable();
				isFound = true;
			}else
				ver.disable();
		}
		if(!isFound)
			System.out.println("[" + NAME + "]GUIGroup could not found a child GUIParts whose group name is: " + groupName);
	}
	public boolean contains(GUIParts parts) {
		return this.parts.contains(parts);
	}
	//////////////////////
	//For child parts
	@Override
	public void enable() {
		super.enable();
		for(GUIParts ver : parts) {
			ver.enable();
		}
	}
	@Override
	public void disable() {
		super.disable();
		for(GUIParts ver : parts)
			ver.disable();
	}
	@Override
	public void clicked() {
		super.clicked();
		if(!GHQ.guiPartsClickCheck(parts))
			marginClicked();
	}
	protected void marginClicked() {
		
	}
	@Override
	public void outsideClicked() {
		super.outsideClicked();
		for(GUIParts ver : parts)
			ver.outsideClicked();
	}
	@Override
	public void released() {
		super.released();
		GHQ.guiPartsReleaseCheck(parts);
	}
	@Override
	public void outsideReleased() {
		super.outsideReleased();
		for(GUIParts ver : parts)
			ver.outsideReleased();
	}
	@Override
	public void mouseOvered() {
		GHQ.guiPartsMouseOverCheck(parts);
	}
	@Override
	public void outsideMouseOvered() {
		for(GUIParts ver : parts)
			ver.outsideMouseOvered();
	}
	@Override
	public void setXY(int x, int y) {
		addXY(x - this.x, y - this.y);
	}
	@Override
	public void addXY(int dx, int dy) {
		super.addXY(dx, dy);
		for(GUIParts ver : parts)
			ver.addXY(dx, dy);
	}
	//////////////////////
}
