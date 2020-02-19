package item;

import calculate.Filter;
import calculate.verifier.Verifier;
import calculate.verifier.WhiteList;
import core.GHQ;
import core.GHQObject;
import paint.dot.DotPaint;
import physics.Point;
import physics.hitShape.ImageRectShape;
import storage.Storage;
import unit.BodyParts;
import unit.BodyPartsType;
import unit.Unit;
import vegetation.DropItem;

/**
 * A primal class for managing item data.
 * Note that Item class and {@link DropItem} class is different.(One commonly put in {@link storage.Storage} but the another one can put in stage as {@link vegetation.Vegetation} object.)
 * @author bluelaserpointer
 * @since alpha1.0
 */
public class ItemData extends GHQObject implements Usable {
	public static final ItemData BLANK_ITEM = new ItemData(DotPaint.BLANK_SCRIPT);
	
	//base info
	protected Unit owner;
	protected int amount;
	protected int stackCap = GHQ.MAX;
	protected DotPaint paintScript;
	private boolean isEquipped;
	//equipment info
	private Verifier<BodyPartsType> equippableBodyPartsTypeVerifier;

	@Override
	public String name() {
		return "<ItemData>" + getClass().getName();
	}
	//init
	public ItemData(DotPaint dotScript) {
		super(new Point.IntPoint());
		this.paintScript = dotScript == null ? DotPaint.BLANK_SCRIPT : dotScript;
		physics().setHitShape(new ImageRectShape(this, paintScript));
		//physics().setHitShape(new RectShape(this, 100, 100));
	}
	public ItemData setDotPaint(DotPaint dotPaint) {
		paintScript = dotPaint;
		return this;
	}
	public ItemData setEquippableBodyPartsType(BodyPartsType...whiteList) {
		equippableBodyPartsTypeVerifier = new WhiteList<BodyPartsType>(whiteList);
		return this;
	}
	//main role
	@Override
	public void idle() {
		if(!hasOwner()) //dropped in stage
			paint();
	}
	@Override
	public void paint() {
		super.paint();
		paintScript.dotPaint(point());
	}
	//control
	public void setStackCap(int amount) {
		stackCap = amount;
	}
	public void setOwner(Unit unit) {
		owner = unit;
	}
	@Override
	public void use() {}
	//information
	public boolean isStackable(ItemData item) {
		return item.name().equals(this.name());
	}
	public final boolean isEmpty() {
		return amount == 0;
	}
	public boolean keepEvenEmpty() {
		return false;
	}
	public final int getAmount() {
		return amount;
	}
	public final int getLeftSpace() {
		return stackCap != GHQ.MAX ? stackCap - amount : GHQ.MAX;
	}
	@Override
	public final DotPaint getDotPaint() {
		return paintScript;
	}
	//control
	public void clear() {
		this.amount = 0;
	}
	/**
	 * Drop this item form a unit.
	 * Clear its owner information and appear to the current stage.
	 * @param x
	 * @param y
	 * @return dropped item
	 */
	public ItemData drop(int x, int y) {
		if(owner != null) {
			owner.removedItem(this);
			setOwner(null);
		}
		cancelDelete();
		point().setXY(x, y);
		return GHQ.stage().addItem(this);
	}
	public ItemData pickup(Unit newOwner) {
		claimDelete();
		setOwner(newOwner);
		return this;
	}
	public int add(int amount) {
		final int LFET_SPACE = getLeftSpace();
		if(LFET_SPACE > amount) {
			this.amount += amount;
			return amount;
		}else {
			this.amount = stackCap;
			return LFET_SPACE;
		}
	}
	public int consume(int amount) {
		if(this.amount > amount) {
			this.amount -= amount;
			return amount;
		}else {
			final int CONSUMED = this.amount;
			this.amount = 0;
			return CONSUMED;
		}
	}
	/**
	 * 
	 * @param item
	 * @return
	 */
	public int stack(ItemData item) {
		final int ADDED = add(item.amount);
		item.consume(ADDED);
		return ADDED;
	}
	/**
	 * 
	 * @param item
	 * @return
	 */
	public int setOff(ItemData item) {
		final int CONSUMED = consume(item.amount);
		item.consume(CONSUMED);
		return CONSUMED;
	}
	//public tool
	public static boolean addInInventory(Storage<ItemData> itemStorage, ItemData targetItem) {
		for(int i = itemStorage.traverseFirst();i != -1;i = itemStorage.traverseNext(i)) {
			final ItemData ITEM = itemStorage.get(i);
			if(ITEM.isStackable(targetItem)) {
				ITEM.stack(targetItem);
				if(targetItem.isEmpty()) {
					if(targetItem.keepEvenEmpty()){
						return itemStorage.add(targetItem);
					}else
						return true;
				}
			}
		}
		//not found same kind (stackable) item.
		return itemStorage.add(targetItem);
	}
	public static int removeInInventory(Storage<ItemData> itemStorage, ItemData targetItem) {
		int removed = 0;
		for(int i = itemStorage.traverseFirst();i != -1;i = itemStorage.traverseNext(i)) {
			final ItemData ITEM = itemStorage.get(i);
			if(ITEM.isStackable(targetItem)) {
				removed += ITEM.setOff(targetItem);
				if(targetItem.isEmpty())
					return removed;
			}
		}
		for(int i = itemStorage.traverseFirst();i != -1;i = itemStorage.traverseNext(i)) {
			final ItemData ITEM = itemStorage.get(i);
			if(ITEM.isEmpty() && !ITEM.keepEvenEmpty())
				itemStorage.remove(ITEM);
		}
		//not found enough amount of the item.
		return removed;
	}
	public static int removeInInventory(Storage<ItemData> itemStorage, Filter<ItemData> filter, int amount) {
		int removed = 0;
		for(int i = itemStorage.traverseFirst();i != -1;i = itemStorage.traverseNext(i)) {
			final ItemData ITEM = itemStorage.get(i);
			if(filter.judge(ITEM)) {
				removed += ITEM.consume(amount);
				if(ITEM.isEmpty())
					return removed;
			}
		}
		for(int i = itemStorage.traverseFirst();i != -1;i = itemStorage.traverseNext(i)) {
			final ItemData ITEM = itemStorage.get(i);
			if(ITEM.isEmpty() && !ITEM.keepEvenEmpty())
				itemStorage.remove(ITEM);
		}
		//not found enough amount of the item.
		return removed;
	}
	//tell
	public void equipped() {
		isEquipped = true;
	}
	public void dequipped() {
		isEquipped = false;
	}
	//information
	public Unit owner() {
		return owner;
	}
	public boolean hasOwner() {
		return owner != null;
	}
	public static boolean isValid(ItemData item) {
		return item != null && item != ItemData.BLANK_ITEM;
	}
	public boolean canEquipTo(BodyParts bodyParts) {
		for(BodyPartsType type : bodyParts.types()) {
			if(equippableBodyPartsTypeVerifier.verify(type))
				return true;
		}
		return false;
	}
	public Verifier<BodyPartsType> equippableBodyPartsTypeVerifier() {
		return equippableBodyPartsTypeVerifier;
	}
	public boolean isEquipped() {
		return isEquipped;
	}
}
