package gui;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

import core.GHQ;
import core.GHQObject;
import paint.ColorFilling;
import paint.ImageFrame;
import paint.rect.RectPaint;
import physics.Point;
import physics.hitShape.HitShape;
import physics.hitShape.RectShape;

/**
 * A primely class for managing UI.
 * @author bluelaserpointer
 * @since alpha1.0
 */
public class GUIParts extends GHQObject implements DragIO {
	public static final GUIParts NULL_GUIPARTS = new GUIParts();
	protected boolean isEnabled;
	protected boolean isClicking;
	protected RectPaint backGroundPaint;
	protected LinkedList<GUIParts> childList = null;
	public GUIParts() {
		backGroundPaint = RectPaint.BLANK_SCRIPT;
		physics().setPoint(new Point.IntPoint());
		physics().setHitShape(new RectShape(this, GHQ.screenW(), GHQ.screenH()));
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
		backGroundPaint = ImageFrame.create(this, imageURL);
		return this;
	}
	public GUIParts setBGBlank() {
		backGroundPaint = RectPaint.BLANK_SCRIPT;
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
	public void removeAll() {
		getChildren().clear();
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
	public GUIParts enable() {
		isEnabled = true;
		return this;
	}
	public GUIParts disable() {
		isClicking = false;
		isEnabled = false;
		return this;
	}
	public boolean isMouseEntered() {
		return GHQ.isMouseInArea_Screen(point().intX(), point().intY(), width(), height());
	}
	/**
	 * 
	 * @param e
	 * @return If consume this signal.
	 */
	public boolean clicked(MouseEvent e) {
		GHQ.setLastClickedUI(this);
		isClicking = true;
		return true;
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
	public void released(MouseEvent e) {
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
	public boolean mouseWheelMoved(MouseWheelEvent e) {
		return false;
	}
	public final Stack<GUIParts> uiAtCursur() {
		Stack<GUIParts> stack = new Stack<GUIParts>();
		stack = uiAtCursur(stack);
		return stack;
	}
	public Stack<GUIParts> uiAtCursur(Stack<GUIParts> stack) {
		stack.push(this);
		if(childList != null) {
			for(GUIParts parts : childList) {
				if(parts.isEnabled() && parts.isMouseEntered()) {
					//System.out.println("give to child: " + parts.name());
					parts.uiAtCursur(stack);
					break;
				}
			}
		}
		return stack;
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
	
	//tool
	public boolean clicked_debugMode(MouseEvent e) {
		boolean clickAbsorbed = false, clicked = false;
		for(GUIParts parts : childList) {
			if(parts.isEnabled()) {
				if(clickAbsorbed || !parts.isMouseEntered()) {
					System.out.println(parts.name() + " is outside clicked.");
					parts.outsideClicked();
				}else {
					parts.clicked(e);
					System.out.println(parts.name() + " is clicked.");
					clicked = true;
				}
			}
		}
		return clicked;
	}
}