package unit;

import core.GHQ;
import paint.DotPaint;
import paint.HasRectPaint;
import paint.RectPaint;
import vegetation.DropItem;

public abstract class Item implements HasRectPaint{
	protected int amount;
	protected final int STACK_CAP;
	protected final RectPaint paintScript;
	public abstract String getName();
	
	public Item(int stackCap,RectPaint paintScript) {
		STACK_CAP = stackCap;
		this.paintScript = paintScript;
	}
	public Item(RectPaint paintScript) {
		STACK_CAP = GHQ.MAX;
		this.paintScript = paintScript;
	}
	//main role
	public DropItem drop(DotPaint paintScript, int x, int y) {
		return new DropItem(this,paintScript,x,y);
	}
	public DropItem drop(int x, int y) {
		if(paintScript instanceof DotPaint)
			return new DropItem(this,(DotPaint)paintScript,x,y);
		else {
			System.out.println("Item.drop(int x, int y) has called illegally because it's paintScript is not a instance of DotPaint");
			return new DropItem(this,DotPaint.BLANK_SCRIPT,x,y);
		}
	}
	//information
	public boolean isStackable(Item item) {
		return item.getName().equals(this.getName());
	}
	public final boolean isEmpty() {
		return amount == 0;
	}
	public boolean keepEvenEmpty() {
		return false;
	}
	public int getAmount() {
		return amount;
	}
	@Override
	public final RectPaint getPaintScript() {
		return paintScript;
	}
	//control
	public void clear() {
		this.amount = 0;
	}
	public int add(int amount) {
		final int leftSpace = STACK_CAP - (this.amount + amount);
		if(leftSpace <= 0)
			this.amount = STACK_CAP;
		else if(this.amount < 0)
			this.amount = 0;
		else
			this.amount += amount;
		return leftSpace;
	}
	/**
	 * 
	 * @param item
	 * @return true - not overflowed / false - overflowed
	 */
	public boolean stack(Item item) {
		final int leftSpace = STACK_CAP - (amount + item.amount);
		if(leftSpace <= 0) {
			this.amount = STACK_CAP;
			item.amount = -leftSpace;
			return false;
		}else {
			this.amount += amount;
			item.amount = 0;
			return true;
		}
	}
}
