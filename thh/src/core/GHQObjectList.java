package core;

import java.util.Iterator;
import java.util.LinkedList;

public class GHQObjectList<T extends GHQObject> extends LinkedList<T>{
	private static final long serialVersionUID = 1774337313239922412L;
	
	public void traverseIdle() {
		for(GHQObject element : this)
			element.idle();
		//check delete claim
		final Iterator<? extends GHQObject> iterator = descendingIterator();
		while(iterator.hasNext()) {
			if(iterator.next().hasDeleteClaim())
				iterator.remove();
		}
	}
	public void traversePaint(boolean doAnimation) {
		for(GHQObject element : this)
			element.paint(doAnimation);
	}
	public final void defaultTraverse() {
		if(GHQ.isNoStopEvent())
			traverseIdle();
		else
			traversePaint(false);
	}
	public void forceRemove(T element) {
		remove(element);
	}
	public T forName(String name) {
		for(T element : this) {
			if(element.getName().equals(name))
				return element;
		}
		return null;
	}
	public T forMouseOver() {
		if(peek() instanceof HasBoundingBox) {
			for(T element : this) {
				if(((HasBoundingBox)element).isMouseOveredBoundingBox())
					return element;
			}
		}
		return null;
	}
}
