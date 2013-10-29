package jodroid.d3obj;

import java.io.Serializable;
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

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Annotation made for JSON parsing.
 * @see D3Obj
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
@interface D3FieldAnnotation {
	/**
	 * jsonName in the file in case it different from the field name.
	 */
	String jsonName() default ""; 
	/**
	 * if we need to provide a different result than the field value.<br/>
	 * The parsing will call this method to provide a formatted string value.
	 */
	String method() default "";
	/**
	 * in case of a percent value.<br>
	 * If true, this will be displayed in percentage.
	 */
	boolean percent() default false;
	/**
	 * in case it is an image it will change the {@link D3Obj#fieldsToView(View)} to fill in an ImageView instead of a TextView.
	 */
	boolean image() default false;
	/**
	 * false to avoid JSONException messages for this field.<br/>
	 * By default, everything is in debug mode.
	 */
	boolean debug() default true; 
	/**
	 * true if this field is not to be found in the JSON.<br/>
	 * This has the same effect as <b>transient</b> in term of parsing (ie. no parsing) but it differs in term of serialization.
	 */
	boolean notInJson() default false; // 
}

/**
 * Superclass of all D3 Objects.
 * Each object can construct itself through a JSONObject and reflection.
 * @author JRD
 * @see <a href="http://blizzard.github.com/d3-api-docs/">Diablo 3 Web API</a>
 */
public abstract class D3Obj implements Serializable {

	private static final long serialVersionUID = 20121214L;
	
	transient protected static Context context;
	public static void setContext(Context c) {
		context = c;
	}
	public static Context getContext() {
		return context;
	}
	
	transient protected boolean debug = true; 
	
	static{
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
		.permitAll()
		.build();

		StrictMode.setThreadPolicy(policy);
	}
	
	public void jsonBuild(JSONObject jsonObject) {
		jsonBuild(jsonObject, true);
	}

	/**
	 * Parse recursively a JSONObject.
	 * @param jsonObject the json object to parse
	 * @param debug LogCat debug display or not
	 */
	public void jsonBuild(JSONObject jsonObject, boolean debug) {
		Class<?> c=this.getClass();
		Field[] fields=c.getFields();  // Get all fields

		for (Field f : fields) {
			if ((f.getModifiers() & Modifier.TRANSIENT) != 0) continue;

			String t=f.getGenericType().toString();
			if(t.startsWith("class")) {
				t=t.substring(6);
			}

			// General annotation 
			D3FieldAnnotation annot = f.getAnnotation(D3FieldAnnotation.class);
			String jsonName = f.getName();
			if (annot != null) {
				if (annot.notInJson()) continue;
				if (!annot.jsonName().isEmpty()) jsonName = annot.jsonName();
				if (!annot.debug()) debug = false;
			}

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
					// Parsing each item of the array
					for (int i =  0; i < jsonArray.length(); i++) {
						if (f.getType().getComponentType().isPrimitive() || f.getType().getComponentType() == String.class) {
							Array.set(tab, i, jsonArray.get(i));
						} else {
							JSONObject jsonTmp = jsonArray.getJSONObject(i);
							Object val = arrayComponentType.newInstance();
							Array.set(tab, i, val);
							((D3Obj)val).jsonBuild(jsonTmp, debug);
						}
					}
				} else { // if this is not an array

					// Getting the object depends on type (other types invoke an exception)
					if (f.getType().isPrimitive() || f.getType() == String.class) {
						f.set(this, jsonObject.get(jsonName));
					} else { 
						// all D3Obj types
						JSONObject obj;
						obj = jsonObject.getJSONObject(jsonName);
						Object val = f.getType().newInstance();
						f.set(this, val);
						((D3Obj)val).jsonBuild(obj, debug); // exception if not a D3Obj
					}
				}
			} catch (IllegalArgumentException e) {
				Log.e(this.getClass().getName(), e.getClass().getName() + "["+f.getName()+"]: " + e.getMessage());
			} catch (IllegalAccessException e) {
				Log.e(this.getClass().getName(), e.getClass().getName() + "["+f.getName()+"]: " + e.getMessage());
			} catch (JSONException e) {
				if (debug)
					Log.w(this.getClass().getName(), e.getClass().getSimpleName() + ": " + e.getMessage() + " in "+this.getClass().getSimpleName());
			} catch (InstantiationException e) {
				Log.e(this.getClass().getName(), e.getClass().getName() + "["+f.getName()+"]: " + e.getMessage());
			}
			
			if (annot != null && !annot.debug()) debug = true;
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

	@SuppressLint("DefaultLocale")
	private String fieldToString(Field f)
			throws NoSuchMethodException,
			IllegalArgumentException,
			IllegalAccessException,
			InvocationTargetException  {
		Class<?> c=this.getClass();
		if (f.getType().isPrimitive() || f.getType().getSimpleName().equals("String")) {
			D3FieldAnnotation annot = f.getAnnotation(D3FieldAnnotation.class);
			if (annot != null && !annot.method().isEmpty()) {
				Method m = c.getMethod(annot.method());
				return (String)(m.invoke(this));
			} else {
				if (annot != null && annot.percent())
					return String.format("%.2f", ((Double)f.get(this)).doubleValue()*100)+"%";
				if (f.getType() == double.class)
					return String.format("%.2f", ((Double)f.get(this)).doubleValue());
				return f.get(this).toString();
			}
		}
		return null;
	}

	public String getFieldByName(String name) {
		Class<?> c=this.getClass();
		Field f;
		try {
			f = c.getField(name);
			return fieldToString(f);
		} catch (NoSuchFieldException e) {
			Log.w(this.getClass().getName(), e.getClass().getName() + ": " + e.getMessage());
		} catch (IllegalArgumentException e) {
			Log.e(this.getClass().getName(), e.getClass().getName() + ": " + e.getMessage());
		} catch (IllegalAccessException e) {
			Log.e(this.getClass().getName(), e.getClass().getName() + ": " + e.getMessage());
		} catch (NoSuchMethodException e) {
			Log.e(this.getClass().getName(), e.getClass().getName() + ": " + e.getMessage());
		} catch (InvocationTargetException e) {
			Log.e(this.getClass().getName(), e.getClass().getName() + ": " + e.getMessage());
		}
		return null;
	}

	/**
	 * Reflection through fields of this instance to fill in TextViews.<br/>
	 * We are searching for a view with android:tag = "&lt;simple name of this class&gt;.&lt;field name&gt;".<br/>
	 * For now, there is 2 possibles views : ImageView and TextView
	 * @param view Android view source (gives us the context and the base view containing subviews for display)
	 */
	public void fieldsToView(View view) {
		if (view == null) return;
		Class<?> c=this.getClass();
		Field[] fields=c.getFields();
		View v = null;
		for (Field f : fields) {
			try {
				if ((f.getModifiers() & Modifier.TRANSIENT) != 0) continue;
				if (f.getType().isPrimitive() || f.getType().getSimpleName().equals("String")) {
					String tag = this.getClass().getSimpleName()+"."+f.getName();
					v = view.findViewWithTag(tag);
//					Log.i(this.getClass().getName(), "tag="+tag);
					if (v != null) {
						D3FieldAnnotation annot = f.getAnnotation(D3FieldAnnotation.class);	
						if (annot != null && annot.image() == true) {
							Method m = c.getMethod(annot.method());
							Bitmap b = (Bitmap)(m.invoke(this));
							((ImageView)v).setImageBitmap(b);
						} else {
							((TextView)v).setText(fieldToString(f));
						}
					}
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

	/**
	 * classic toString
	 */
	@Override
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
