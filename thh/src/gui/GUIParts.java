package gui;

import java.awt.Color;
import java.util.Iterator;
import java.util.LinkedList;

import core.GHQ;
import paint.ColorFilling;
import paint.ImageFrame;
import paint.rect.RectPaint;
import physics.Point;

/**
 * A primal class for managing GUI.
 * @author bluelaserpointer
 * @since alpha1.0
 */
public class GUIParts {
	public String NAME;
	protected boolean isEnabled;
	protected boolean isClicking;
	private boolean absorbsClickEvent;
	public int x, y, w, h;
	protected RectPaint backGroundPaint;
	protected LinkedList<GUIParts> childList = null;
	public GUIParts() {
		NAME = getClass().getName() + "(default name)";
		backGroundPaint = RectPaint.BLANK_SCRIPT;
		x = y = 0;
		w = GHQ.screenW();
		h = GHQ.screenH();
		absorbsClickEvent = true;
	}
	//tool-getClildren
	public LinkedList<GUIParts> getChildren(){
		if(childList == null)
			childList = new LinkedList<GUIParts>();
		return childList;
	}
	//init-Name
	public GUIParts setName(String name) {
		NAME = name;
		return this;
	}
	//init-background paint
	public GUIParts setBGPaint(RectPaint paintScript) {
		backGroundPaint = paintScript;
		return this;
	}
	public GUIParts setBGColor(Color color) {
		backGroundPaint = new ColorFilling(color);
		return this;
	}
	public GUIParts setBGImage(String imageURL) {
		backGroundPaint = ImageFrame.create(imageURL);
		return this;
	}
	public GUIParts setBGBlank() {
		backGroundPaint = RectPaint.BLANK_SCRIPT;
		return this;
	}
	//init-ifAbsorbsClickEvent
	public GUIParts setAbsorbsClickEvent(boolean b) {
		absorbsClickEvent = b;
		return this;
	}
	//init-coordinate
	public void setXY(int x, int y) {
		if(childList == null) {
			this.x = x;
			this.y = y;
		}else {
			addXY(x - this.x, y - this.y);
		}
	}
	public void setXY_center(int centerX, int centerY) {
		setXY(centerX - w/2, centerY - h/2);
	}
	public GUIParts setBounds(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		return this;
	}
	public GUIParts setBounds(GUIParts sample) {
		x = sample.x;
		y = sample.y;
		w = sample.w;
		h = sample.h;
		return this;
	}
	public GUIParts setBounds_center(int centerX, int centerY, int w, int h) {
		this.x = centerX - w/2;
		this.y = centerY - h/2;
		this.w = w;
		this.h = h;
		return this;
	}
	public GUIParts setBounds_center(Point centerPoint, int w, int h) {
		return setBounds_center(centerPoint.intX(), centerPoint.intY(), w, h);
	}
	//init-children
	public GUIParts appendFirst(GUIParts childParts) {
		if(childParts == this)
			System.out.println("! -- GUIParts.addFirst: cannot add itself to childParts");
		else
			getChildren().addFirst(childParts);
		return this;
	}
	public <T extends GUIParts>T addFirst(T childParts) {
		if(childParts == this)
			System.out.println("! -- GUIParts.addFirst: cannot add itself to childParts");
		else
			getChildren().addFirst(childParts);
		return childParts;
	}
	public GUIParts appendLast(GUIParts childParts) {
		if(childParts == this)
			System.out.println("! -- GUIParts.addFirst: cannot add itself to childParts");
		else
			getChildren().addLast(childParts);
		return this;
	}
	public <T extends GUIParts>T addLast(T childParts) {
		if(childParts == this)
			System.out.println("! -- GUIParts.addFirst: cannot add itself to childParts");
		else
			getChildren().addLast(childParts);
		return childParts;
	}
	public <T extends GUIParts>T remove(T childParts) {
		getChildren().remove(childParts);
		return childParts;
	}
	//control-event
	public GUIParts callEvent(UIEvent event) {
		switch(event) {
		case CLICKED:
			this.clicked();
			break;
		case RELEASED:
			this.released();
			break;
		case ENABLE:
			this.enable();
			break;
		case DISABLE:
			this.disable();
			break;
		case OUTISDE_CLICKED:
			this.outsideClicked();
			break;
		case OUTSIDE_RELEASED:
			this.outsideReleased();
			break;
		case MOUSE_OVERED:
			this.mouseOvered();
			break;
		case OUTSIDE_MOUSE_OVERED:
			this.outsideMouseOvered();
			break;
		default:
			System.out.println("! -- GUIParts.callEvent: unknown UIEvent");
			break;
		}
		return this;
	}
	//control-coordinate
	public void addXY(int dx, int dy) {
		this.x += dx;
		this.y += dy;
		if(childList != null) {
			for(GUIParts ver : childList)
				ver.addXY(dx, dy);
		}
	}
	//main role
	protected void idle() {
		paint();
		//child idle
		if(childList != null) {
			final Iterator<GUIParts> ite = childList.descendingIterator();
			while(ite.hasNext())
				ite.next().idleIfEnabled();
		}
	}
	public final void idleIfEnabled() {
		if(isEnabled)
			idle();
	}
	public void paint() {
		backGroundPaint.rectPaint(x, y, w, h);
	}
	//control-uiEvent
	public void enable() {
		isEnabled = true;
		if(childList != null) {
			for(GUIParts ver : childList)
				ver.enable();
		}
	}
	public void disable() {
		isClicking = false;
		isEnabled = false;
		if(childList != null) {
			for(GUIParts ver : childList)
				ver.disable();
		}
	}
	public boolean isMouseEntered() {
		return GHQ.isMouseInArea_Screen(x, y, w, h);
	}
	public void clicked() {
		isClicking = true;
		if(!GHQ.guiPartsClickCheck(childList))
			marginClicked();
	}
	protected void marginClicked() {
		
	}
	public void outsideClicked() {
		isClicking = false;
		if(childList != null) {
			for(GUIParts ver : childList)
				ver.outsideClicked();
		}
	}
	public void released() {
		isClicking = false;
		GHQ.guiPartsReleaseCheck(childList);
	}
	public void outsideReleased() {
		isClicking = false;
		if(childList != null) {
			for(GUIParts ver : childList)
				ver.outsideReleased();
		}
	}
	public void mouseOvered() {
		GHQ.guiPartsMouseOverCheck(childList);
	}
	public void outsideMouseOvered() {
		if(childList != null) {
			for(GUIParts ver : childList)
				ver.outsideMouseOvered();
		}
	}
	public void flit() {
		if(isEnabled)
			disable();
		else
			enable();
	}
	//information
	public final boolean isEnabled() {
		return isEnabled;
	}
	public final boolean isClicking() {
		return isClicking;
	}
	public boolean absorbsClickEvent() {
		return absorbsClickEvent;
	}
	
	//tool
	public boolean clicked_debugMode() {
		boolean clickAbsorbed = false, clicked = false;
		for(GUIParts parts : childList) {
			if(parts.isEnabled()) {
				if(clickAbsorbed || !parts.isMouseEntered()) {
					System.out.println(parts.NAME + " is outside clicked.");
					parts.outsideClicked();
				}else {
					parts.clicked();
					System.out.println(parts.NAME + " is clicked.");
					clicked = true;
					clickAbsorbed = parts.absorbsClickEvent();
				}
			}
		}
		return clicked;
	}
}