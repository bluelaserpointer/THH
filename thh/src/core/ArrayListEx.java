package core;

import java.util.ArrayList;

public class ArrayListEx<E> extends ArrayList<E>{
	private static final long serialVersionUID = -1813423514881483180L;
	
	private int nowIterator = -1;
	public E next() {
		return this.get(++nowIterator);
	}
	public boolean hasNext() {
		return nowIterator + 1 < this.size();
	}
	public void setIterator(int pos) {
		nowIterator = pos;
	}
	@Override
	public E remove(int arg0) {
		if(arg0 <= nowIterator)
			nowIterator--;
		return super.remove(arg0);
	}
	@Override
	public boolean remove(Object arg0) {
		final int INDEX = super.indexOf(arg0);
		if(INDEX != -1) {
			if(INDEX <= nowIterator)
				nowIterator--;
			super.remove(arg0);
			return true;
		}
		return false;
	}
}
