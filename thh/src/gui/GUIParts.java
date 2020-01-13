package gui;

import java.awt.Color;
import java.util.Iterator;
import java.util.LinkedList;

import core.GHQ;
import core.GHQObject;
import paint.ColorFilling;
import paint.ImageFrame;
import paint.rect.RectPaint;
import physics.Point;
import physics.hitShape.HitShape;
import physics.hitShape.RectShape;

/**
 * A primely class for managing GUI.
 * @author bluelaserpointer
 * @since alpha1.0
 */
public class GUIParts extends GHQObject implements DragIO{ //TODO real point
	protected boolean isEnabled;
	protected boolean isClicking;
	private boolean absorbsClickEvent;
	protected RectPaint backGroundPaint;
	protected LinkedList<GUIParts> childList = null;
	public GUIParts() {
		backGroundPaint = RectPaint.BLANK_SCRIPT;
		physics().setPoint(new Point.IntPoint());
		physics().setHitShape(new RectShape(this, GHQ.screenW(), GHQ.screenH()));
		absorbsClickEvent = true;
	}
	@Override
	public GUIParts setName(String name) {
		super.setName(name);
		return this;
	}
	//tool-getClildren
	public LinkedList<GUIParts> getChildren(){
		if(childList == null)
			childList = new LinkedList<GUIParts>();
		return childList;
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
	public RectShape rectShape() {
		return ((RectShape)hitShape());
	}
	public GUIParts setSize(int w, int h) {
		rectShape().setSize(w, h);
		return this;
	}
	public GUIParts setSize(HitShape hitShape) {
		rectShape().setSize(hitShape);
		return this;
	}
	public GUIParts setBounds(int x, int y, int w, int h) {
		point().setXY(x, y);
		setSize(w, h);
		return this;
	}
	public GUIParts setBounds(GUIParts sample) {
		point().setAll(sample.point());
		setSize(sample.hitShape());
		return this;
	}
	public GUIParts setBounds_center(int centerX, int centerY, int w, int h) {
		point().setXY(centerX - w/2, centerY - h/2);
		setSize(w, h);
		return this;
	}
	public GUIParts setBounds_center(Point centerPoint, int w, int h) {
		return setBounds_center(centerPoint.intX(), centerPoint.intY(), w, h);
	}
	//init-children
	public GUIParts appendFirst(GUIParts childParts) {
		if(childParts == this)
			System.out.println("! -- GUIParts.addFirst: cannot add itself to childParts");
		else {
			getChildren().addFirst(childParts);
			childParts.enable();
		}
		return this;
	}
	public <T extends GUIParts>T addFirst(T childParts) {
		if(childParts == this)
			System.out.println("! -- GUIParts.addFirst: cannot add itself to childParts");
		else {
			getChildren().addFirst(childParts);
			childParts.enable();
		}
		return childParts;
	}
	public GUIParts appendLast(GUIParts childParts) {
		if(childParts == this)
			System.out.println("! -- GUIParts.addFirst: cannot add itself to childParts");
		else {
			getChildren().addLast(childParts);
			childParts.enable();
		}
		return this;
	}
	public <T extends GUIParts>T addLast(T childParts) {
		if(childParts == this)
			System.out.println("! -- GUIParts.addFirst: cannot add itself to childParts");
		else {
			getChildren().addLast(childParts);
			childParts.enable();
		}
		return childParts;
	}
	public <T extends GUIParts>T remove(T childParts) {
		getChildren().remove(childParts);
		return childParts;
	}
	//control-coordinate
	public void addXY(int dx, int dy) {
		point().addXY(dx, dy);
		if(childList != null) {
			for(GUIParts ver : childList)
				ver.addXY(dx, dy);
		}
	}
	//main role
	@Override
	public void idle() {
		super.idle();
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
	@Override
	public void paint() {
		backGroundPaint.rectPaint(point().intX(), point().intY(), width(), height());
	}
	//control-uiEvent
	public void enable() {
		isEnabled = true;
	}
	public void disable() {
		isClicking = false;
		isEnabled = false;
	}
	public boolean isMouseEntered() {
		return GHQ.isMouseInArea_Screen(point().intX(), point().intY(), width(), height());
	}
	public void clicked() {
		GHQ.setLastClickedUI(this);
		isClicking = true;
	}
	public void outsideClicked() {
		isClicking = false;
		if(childList != null) {
			for(GUIParts parts : childList) {
				if(parts.isEnabled())
					parts.outsideClicked();
			}
		}
	}
	public void released() {
		isClicking = false;
	}
	public void outsideReleased() {
		isClicking = false;
		if(childList != null) {
			for(GUIParts parts : childList) {
				if(parts.isEnabled())
					parts.outsideReleased();
			}
		}
	}
	public void mouseOver() {
	}
	public void mouseOut() {
		if(childList != null) {
			for(GUIParts parts : childList) {
				if(parts.isEnabled())
					parts.mouseOut();
			}
		}
	}
	@Override
	public GUIParts uiAtCursur() {
		if(childList != null) {
			for(GUIParts parts : childList) {
				if(parts.isEnabled() && parts.isMouseEntered()) {
					return parts.uiAtCursur();
				}
			}
		}
		return this;
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
					System.out.println(parts.name() + " is outside clicked.");
					parts.outsideClicked();
				}else {
					parts.clicked();
					System.out.println(parts.name() + " is clicked.");
					clicked = true;
					clickAbsorbed = parts.absorbsClickEvent();
				}
			}
		}
		return clicked;
	}
}