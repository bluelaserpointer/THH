package gui;

import java.util.ArrayList;

import core.GHQ;
import paint.RectPaint;

public class GUIGroup extends GUIParts{

	protected final ArrayList<GUIParts> parts = new ArrayList<GUIParts>();
	public GUIGroup(String group, RectPaint paintScript, int x, int y, int w, int h) {
		super(group, paintScript, x, y, w, h, true);
	}
	public GUIGroup(String group, RectPaint paintScript) {
		super(group, paintScript, true);
	}
	
	//main role
	@Override
	public void idle() {
		super.idle();
		for(int i = parts.size() - 1;i >= 0;i--)
			parts.get(i).defaultIdle();
	}
	
	//control
	/**
	 * Add a GUIParts to this GUIGroup.
	 * It's idle method and other event methods is called automatically by this GUIGroup.
	 * @param guiParts
	 * @return added GUIParts
	 */
	public <T extends GUIParts>T addParts(T guiParts) {
		parts.add(guiParts);
		return guiParts;
	}
	/**
	 * Add a GUIParts to this GUIGroup, and set it's click priority to the highest.
	 * It's idle method and other event methods is called automatically by this GUIGroup.
	 * @param guiParts
	 * @return added GUIParts
	 */
	public <T extends GUIParts>T addPartsToTop(T guiParts) {
		parts.add(0,guiParts);
		return guiParts;
	}
	/**
	 * Only enable those specified GUIParts whose group name is [groupName], and disable the others.
	 * @param groupName
	 */
	public void enableSpecifiedChild(String groupName) {
		boolean isFound = false;
		for(GUIParts ver : parts) {
			if(ver.GROUP.equals(groupName)) {
				ver.enable();
				isFound = true;
			}else
				ver.disable();
		}
		if(!isFound)
			System.out.println("[" + GROUP + "]GUIGroup could not found a child GUIParts whose group name is: " + groupName);
	}
	@Override
	public boolean isMouseEntered() {
		return GHQ.isMouseInArea_Screen(x, y, w, h);
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
		GHQ.guiPartsClickCheck(parts);
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
		final int DX = x - this.x, DY = y - this.y;
		addXY(DX, DY);
	}
	@Override
	public void addXY(int dx, int dy) {
		super.addXY(dx, dy);
		for(GUIParts ver : parts)
			ver.addXY(dx, dy);
	}
	//////////////////////
}
