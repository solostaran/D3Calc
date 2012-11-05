package jodroid.d3obj;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
@interface D3FieldAnnotation {
	String jsonName() default "";
	String method() default "";
}

/**
 * Superclass of all D3 Objects.
 * Each object can construct itself through a JSONObject and reflection.
 * @author JRD
 * @see <a href="http://blizzard.github.com/d3-api-docs/">Diablo 3 Web API</a>
 */
public abstract class D3Obj {

	protected static Context context;
	public static void setContext(Context c) {
		context = c;
	}
	public static Context getContext() {
		return context;
	}

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

			D3FieldAnnotation annot = f.getAnnotation(D3FieldAnnotation.class);
			String jsonName = f.getName();
			if (annot != null && !annot.jsonName().isEmpty()) jsonName = annot.jsonName();
			//			Log.i(this.getClass().getName(), t+" "+f.getName());

			try {
				// If we expect an array
				if (f.getType().isArray()) {

					// Getting the array
					JSONArray jsonArray = jsonObject.getJSONArray(jsonName);
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
				} else { // if this is not an array

					// Getting the object depends on type (other types invoke an exception)
					if (f.getType().isPrimitive() || f.getType() == String.class) {
						// primitive types and Strings
						//						if (f.getName().startsWith("_"))
						//							f.set(this, jsonObject.get(f.getName().substring(1)));
						//						else
						//							f.set(this, jsonObject.get(f.getName()));
						f.set(this, jsonObject.get(jsonName));
					} else { 
						// all D3Obj types
						JSONObject obj;
						//						if (f.getName().startsWith("_"))
						//							obj = jsonObject.getJSONObject(f.getName().substring(1)); 
						//						else
						//							obj = jsonObject.getJSONObject(f.getName());
						obj = jsonObject.getJSONObject(jsonName);
						Object val = f.getType().newInstance();
						f.set(this, val);
						((D3Obj)val).jsonBuild(obj); // exception if not a D3Obj
					}
				}
			} catch (IllegalArgumentException e) {
				Log.e(this.getClass().getName(), e.getClass().getName() + "["+f.getName()+"]: " + e.getMessage());
			} catch (IllegalAccessException e) {
				Log.e(this.getClass().getName(), e.getClass().getName() + "["+f.getName()+"]: " + e.getMessage());
			} catch (JSONException e) {
				Log.w(this.getClass().getName(), e.getClass().getName() + ": " + e.getMessage());
			} catch (InstantiationException e) {
				Log.e(this.getClass().getName(), e.getClass().getName() + "["+f.getName()+"]: " + e.getMessage());
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

			D3FieldAnnotation annot = f.getAnnotation(D3FieldAnnotation.class);
			String jsonName = f.getName();
			if (annot != null && !annot.jsonName().isEmpty()) jsonName = annot.jsonName();

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
					if (annot != null && !annot.method().isEmpty()) {
						Method m = c.getMethod(annot.method());
						strtmp = (String)(m.invoke(this));
					} else {
						if (f.getType().getSuperclass() == D3Obj.class)
							strtmp = ((D3Obj)f.get(this)).toString(marginleft+2);
						else
							strtmp = f.get(this).toString();
					}
					//					if (f.getName().startsWith("_"))
					//						str += blankStr(marginleft)+f.getName().substring(1)+"="+strtmp+"\n";
					//					else
					//						str += blankStr(marginleft)+f.getName()+"="+strtmp+"\n";
					str += blankStr(marginleft)+jsonName+"="+strtmp+"\n";
				} catch (IllegalArgumentException e) {
					Log.e(this.getClass().getName(), e.getClass().getName() + ": " + e.getMessage());
				} catch (IllegalAccessException e) {
					Log.e(this.getClass().getName(), e.getClass().getName() + ": " + e.getMessage());
				} catch (NoSuchMethodException e) {
					Log.e(this.getClass().getName(), e.getClass().getName() + ": " + e.getMessage());
				} catch (InvocationTargetException e) {
					Log.e(this.getClass().getName(), e.getClass().getName()
							+": field="+f.getName()
							+", method="+f.getAnnotation(D3FieldAnnotation.class).method()
							+", msg="+e.getMessage());
					e.printStackTrace();
				}
			}
		}
		str+=blankStr(marginleft)+"}\n";
		return str;
	}

	/**
	 * Reflection through fields of this instance to fill in TextViews.<br/>
	 * We are searching for TextView with android:tag = "&lt;simple name of this class&gt;.&lt;field name&gt;".<br/>
	 * @param view Android view source (gives us the context and the base view containing subviews for display)
	 */
	public void fieldsToView(View view) {
		if (view == null) return;
		Class<?> c=this.getClass();
		Field[] fields=c.getFields();
		TextView v = null;
		for (Field f : fields) {
			if ((f.getModifiers() & Modifier.TRANSIENT) != 0) continue;
			if (f.getType().isPrimitive() || f.getType().getSimpleName().equals("String")) {
				String tag = this.getClass().getSimpleName()+"."+f.getName();
				v = (TextView)view.findViewWithTag(tag);
				if (v != null) {
//					Log.i(this.getClass().getName(), "tag="+tag);
					try {
						D3FieldAnnotation annot = f.getAnnotation(D3FieldAnnotation.class);
						if (annot != null && !annot.method().isEmpty()) {
							Method m = c.getMethod(annot.method());
							v.setText((String)(m.invoke(this)));
						} else {
							//							String value = convertValueToString(view.getContext(), f.getName(), f.get(this));
							v.setText(""+f.get(this));
						}
					} catch (IllegalArgumentException e) {
						Log.e(this.getClass().getName(), e.getClass().getName() + ": " + e.getMessage());
					} catch (IllegalAccessException e) {
						Log.e(this.getClass().getName(), e.getClass().getName() + ": " + e.getMessage());
					} catch (InvocationTargetException e) {
						Log.e(this.getClass().getName(), e.getClass().getName() + ": " + e.getMessage());
					} catch (NoSuchMethodException e) {
						Log.e(this.getClass().getName(), e.getClass().getName() + ": " + e.getMessage());
					}
				}
			}
		}
	}

	/**
	 * Some values need conversion before being displayed.<br/>
	 * I do conversions depending on the field name.
	 * @param c We need the context to access to resource strings
	 * @param fieldName conversion based on field name
	 * @param value the value to convert
	 * @return this will be displayed
	 *
	private String convertValueToString(Context c, String fieldName, Object value) {
		if (fieldName.equals("gender")) {
			if (((Integer)value).intValue() == 0)
				return c.getText(R.string.gendermale).toString();
			else
				return c.getText(R.string.genderfemale).toString();
		}
		if (fieldName.equals("paragonLevel")) {
			if (((Integer)value).intValue() > 0)
				return c.getText(R.string.paragon)+" ("+value.toString()+")";
			else
				return new String();
		}
		if (fieldName.equals("hardcore")) {
			if (((Boolean)value).booleanValue())
				return c.getText(R.string.hardcore).toString();
			else
				return new String();
		}
		return value.toString();
	}*/

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
