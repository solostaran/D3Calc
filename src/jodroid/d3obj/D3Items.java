package jodroid.d3obj;

import java.lang.reflect.Field;
import java.util.ArrayList;

import jodroid.d3calc.R;

import android.util.Log;


public class D3Items extends D3Obj {
	
	// on modification, change these string arrays :
	// R.array.ItemsJsonNames and R.array.ItemsJsonFields
	public D3ItemLite head;
	public D3ItemLite shoulders;
	public D3ItemLite neck;
	public D3ItemLite torso;
	public D3ItemLite bracers;
	public D3ItemLite hands;
	public D3ItemLite waist;
	public D3ItemLite rightFinger;
	public D3ItemLite leftFinger;
	public D3ItemLite legs;
	public D3ItemLite feet;
	public D3ItemLite mainHand;
	public D3ItemLite offHand;
	
	transient public D3ItemLite [] itemArray = null;
	
	/**
	 * Given the name, parse through all field names, find the corresponding one and return its value.
	 * @param name the field name to compare to
	 * @return the item or null if not found
	 */
	public D3ItemLite getItemByName(String name) {
		Class<?> c=this.getClass();
		Field[] fields=c.getFields();
		for (Field f : fields) {
			if (f.getName().equals(name)) {
				try {
					return (D3ItemLite)(f.get(this));
				} catch (IllegalArgumentException e) {
					Log.w(this.getClass().getName(), e.getMessage());
				} catch (IllegalAccessException e) {
					Log.w(this.getClass().getName(), e.getMessage());
				}
			}
		}
		return null;
	}
	
	/**
	 * Try to find the item reference in the items and return the slot name of this item if found.
	 * @param item the item reference
	 * @return slot name of the item
	 */
	public String getItemSlot(D3ItemLite item) {
		Class<?> c=this.getClass();
		Field[] fields=c.getFields();
		for (Field f : fields) {
			try {
				if (f.get(this) == item) {
					return f.getName();
				}
			} catch (IllegalArgumentException e) {
				Log.e(this.getClass().getName(), e.getMessage());
			} catch (IllegalAccessException e) {
				Log.e(this.getClass().getName(), e.getMessage());
			}
		}
		return null;
	}
	
	private void addItem(ArrayList<D3ItemLite> list, D3ItemLite item, int resId) {
		list.add(item);
		item.itemSlot = context.getResources().getString(resId);
	}
	
	public D3ItemLite [] toItemArray() {
		ArrayList<D3ItemLite> list = new ArrayList<D3ItemLite>();
		if (head != null) addItem(list, head, R.string.slot_head);
		if (neck != null) addItem(list, neck, R.string.slot_neck);
		if (torso != null) addItem(list, torso, R.string.slot_torso);
		if (bracers != null) addItem(list, bracers, R.string.slot_bracers);
		if (hands != null) addItem(list, hands, R.string.slot_hands);
		if (waist != null) addItem(list, waist, R.string.slot_waist);
		if (rightFinger != null) addItem(list, rightFinger, R.string.slot_rightFinger);
		if (leftFinger != null) addItem(list, leftFinger, R.string.slot_leftFinger);
		if (legs != null) addItem(list, legs, R.string.slot_legs);
		if (feet != null) addItem(list, feet, R.string.slot_feet);
		if (mainHand != null) addItem(list, mainHand, R.string.slot_mainHand);
		if (offHand != null) addItem(list, offHand, R.string.slot_offHand);
		itemArray = list.toArray(new D3ItemLite[1]);
		return itemArray;
	}
}
