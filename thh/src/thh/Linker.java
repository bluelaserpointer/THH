package thh;

import java.util.ArrayList;
import java.util.HashMap;

public class Linker {
	private HashMap<Integer,IntList> map = new HashMap<Integer,IntList>();
	
	public void put(int key,int[] values) {
		if(values == null)
			return;
		if(map.containsKey(key))
			map.get(key).append(values);
		else
			map.put(key, new IntList(values));
	}
	public void put(int key,int value) {
		if(value == THH.NONE)
			return;
		if(map.containsKey(key))
			map.get(key).append(value);
		else
			map.put(key, new IntList(new int[] {value}));
	}
	public void remove(int key) {
		map.remove(key);
	}
	public void remove_IntList(int key,Integer value) {
		try {
			map.get(key).remove(value);
		}catch(NullPointerException e) {}
	}
	public int[] get(int key) {
		try {
			return map.get(key).toArray();
		}catch(NullPointerException e) {
			return new int[0];
		}
	}
}

class IntList{
	private ArrayList<Integer> list = new ArrayList<Integer>();
	
	public IntList(int[] value) {
		for(int ver : value)
			list.add(ver);
	}
	
	public void append(int value) {
		list.add(value);
	}
	public void append(int[] values) {
		for(int ver : values)
			list.add(ver);
	}
	public boolean remove(Integer value) {
		return list.remove(value);
	}
	public int[] toArray() {
		Integer[] array = list.toArray(new Integer[0]);
		int[] array2 = new int[array.length];
		for(int i = 0;i < array.length;i++)
			array2[i] = array[i];
		return array2;
	}
}
