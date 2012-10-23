package jodroid.d3obj;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * Superclass of all D3 Objects.
 * Each object can construct itself through a JSONObject and reflection.
 * @author JRD
 *
 */
public abstract class D3Obj {
	/**
	 * Parse recursively a JSONObject.
	 * @param jsonObject the json object to parse
	 */
	public void jsonBuild(JSONObject jsonObject) {
		Class<?> c=this.getClass();
		Field[] fields=c.getFields();  // Get all fields
		
		for (Field f : fields) {
			if ((f.getModifiers() & Modifier.TRANSIENT) != 0) continue;
			
			String t=f.getGenericType().toString();
			if(t.startsWith("class")) {
				t=t.substring(6);
			}
//			Log.i(this.getClass().getName(), t+" "+f.getName());
			
			// If we expect an array
			if (f.getType().isArray()) {
				try {
					// Getting the array
					JSONArray jsonArray = jsonObject.getJSONArray(f.getName());
					// Allocating the new array
					Class<?> arrayComponentType = f.getType().getComponentType();
					Object tab = Array.newInstance(arrayComponentType, jsonArray.length());
					// Setting the array to this new instance
					f.set(this, tab);
//					Log.i(this.getClass().getName(), tab.getClass().getSimpleName() + " ["+jsonArray.length()+"]");
					// Parsing each item of the array
					for (int i =  0; i < jsonArray.length(); i++) {
						// Getting one array item
						JSONObject jsonTmp = jsonArray.getJSONObject(i);
						// Allocating the new item
						Object val = arrayComponentType.newInstance();
						Array.set(tab, i, val);
						// Parsing this item
						((D3Obj)val).jsonBuild(jsonTmp);
					}
					
				} catch (IllegalArgumentException e) {
					Log.e(this.getClass().getName(), e.getClass().getName() + ": " + e.getMessage());
				} catch (IllegalAccessException e) {
					Log.e(this.getClass().getName(), e.getClass().getName() + ": " + e.getMessage());
				} catch (JSONException e) {
					Log.e(this.getClass().getName(), e.getClass().getName() + ": " + e.getMessage());
				} catch (InstantiationException e) {
					Log.e(this.getClass().getName(), e.getClass().getName() + ": " + e.getMessage());
				}
				
			} else { // if this is not an array
				try {
					// Getting the object depends on type (other types invoke an exception)
					if (f.getType().isPrimitive() || f.getType() == String.class) {
						// primitive types and Strings
						if (f.getName().startsWith("_"))
							f.set(this, jsonObject.get(f.getName().substring(1)));
						else
							f.set(this, jsonObject.get(f.getName()));
					} else {
						// all D3Obj types
						JSONObject obj;
						if (f.getName().startsWith("_"))
							obj = jsonObject.getJSONObject(f.getName().substring(1)); 
						else
							obj = jsonObject.getJSONObject(f.getName()); 
						Object val = f.getType().newInstance();
						f.set(this, val);
						((D3Obj)val).jsonBuild(obj); // exception if not a D3Obj
					}
				} catch (JSONException e) {
					Log.e(this.getClass().getName(), e.getClass().getName() + ": " + e.getMessage());
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					Log.e(this.getClass().getName(), e.getClass().getName() + ": " + e.getMessage());
				} catch (IllegalAccessException e) {
					Log.e(this.getClass().getName(), e.getClass().getName() + ": " + e.getMessage());
				} catch (InstantiationException e) {
					Log.e(this.getClass().getName(), e.getClass().getName() + ": " + e.getMessage());
				}
			}
		}
	}
	
	/**
	 * Generate a blank (fill with spaces) string.
	 * @param i length of the string
	 * @return the generated string
	 */
	private String blankStr(int i) {
		String str = new String();
		for (int j = 0; j < i; j++)
			str +=" ";
		return str;
	}
	
	/**
	 * Return a string representation of the object with a left margin of blank characters.
	 * @param marginleft size in character of the left margin
	 * @return this object string representation
	 */
	private String toFormattedString(int marginleft) {
		String str = new String();
		Class<?> c=this.getClass();
		Field[] fields=c.getFields();  // Get all fields
		
		str+=blankStr(marginleft)+"{\n";
		for (Field f : fields) {
			if ((f.getModifiers() & Modifier.TRANSIENT) != 0) continue;
			
			if (f.getType().isArray()) { // array field
				try {
					int length = Array.getLength(f.get(this));
					str += blankStr(marginleft)+"Array "+f.getName()+" of "+length+" "+f.getType().getComponentType().getSimpleName()+"=[\n";
					for (int i = 0; i < length; i++) {
						if (f.getType().getComponentType().getSuperclass() == D3Obj.class)
							str += ((D3Obj)Array.get(f.get(this), i)).toString(marginleft+2);
						else 
							str += Array.get(f.get(this), i).toString();
					}
					str += blankStr(marginleft)+"]\n";
				} catch (IllegalArgumentException e) {
					Log.e(this.getClass().getName(), e.getClass().getName() + ": " + e.getMessage());
				} catch (IllegalAccessException e) {
					Log.e(this.getClass().getName(), e.getClass().getName() + ": " + e.getMessage());
				}	
			} else { // non array field
				try {
					String strtmp;
					if (f.getType().getSuperclass() == D3Obj.class)
						strtmp = ((D3Obj)f.get(this)).toString(marginleft+2);
					else
						strtmp = f.get(this).toString();
					if (f.getName().startsWith("_"))
						str += blankStr(marginleft)+f.getName().substring(1)+"="+strtmp+"\n";
					else
						str += blankStr(marginleft)+f.getName()+"="+strtmp+"\n";
				} catch (IllegalArgumentException e) {
					Log.e(this.getClass().getName(), e.getClass().getName() + ": " + e.getMessage());
				} catch (IllegalAccessException e) {
					Log.e(this.getClass().getName(), e.getClass().getName() + ": " + e.getMessage());
				}
			}
		}
		str+=blankStr(marginleft)+"}\n";
		return str;
	}
	
	/**
	 * classic toString
	 */
	public String toString() {
		return toFormattedString(0);
	}
	
	/**
	 * Modification of the toString to insert a left margin.
	 * @param i size in character of the left margin
	 * @return this object string representation
	 */
	public String toString(int i) {
		return toFormattedString(i);
	}

}
