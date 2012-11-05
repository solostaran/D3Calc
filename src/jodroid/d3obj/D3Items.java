package jodroid.d3obj;

import java.lang.reflect.Field;

import android.util.Log;


public class D3Items extends D3Obj {
	
	// on modification, change this string array : R.array.ItemsJsonNames
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
}
