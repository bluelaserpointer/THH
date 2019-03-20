package gui;

import java.util.ArrayList;

import core.GHQ;
import paint.RectPaint;

public class GUIGroup extends GUIParts{

	private final ArrayList<GUIParts> parts = new ArrayList<GUIParts>();
	public GUIGroup(String group, RectPaint paintScript, int x, int y, int w, int h) {
		super(group, paintScript, x, y, w, h, false);
	}
	
	//control
	/**
	 * Add a GUIParts to this GUIGroup and automatically add it to GHQ.
	 * @param guiParts
	 * @return added GUIParts
	 */
	public <T extends GUIParts>T addParts(T guiParts) {
		this.parts.add(guiParts);
		GHQ.addGUIParts(guiParts);
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
	public void enable() {
		super.enable();
		for(GUIParts ver : parts)
			ver.enable();
	}
	@Override
	public void disable() {
		super.disable();
		for(GUIParts ver : parts)
			ver.disable();
	}
}
