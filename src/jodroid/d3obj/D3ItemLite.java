package jodroid.d3obj;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import d3api.D3Url;
import jodroid.d3calc.R;

public class D3ItemLite extends D3Obj {
	public String id;
	public String name;
	@D3FieldAnnotation(image=true, method="getIcon")
	public String icon;
	public String displayColor;
	public String tooltipParams;
	transient protected Bitmap itemIcon = null;
	transient public String itemSlot = null;
	
	public D3ItemLite () {}
	public D3ItemLite (D3ItemLite item) {
		id = item.id;
		name = item.name;
		icon = item.icon;
		displayColor = item.displayColor;
		tooltipParams = item.tooltipParams;
		itemIcon = item.itemIcon;
		itemSlot = item.itemSlot;
	}
	
	public String toString() {
		return id+" / "+name;
	}
	
	public int getColor(int defaultColor) {
		if (displayColor.equals("orange")) return context.getResources().getColor(R.color.legendary);
		if (displayColor.equals("green")) return context.getResources().getColor(R.color.set);
		if (displayColor.equals("yellow")) return context.getResources().getColor(R.color.rare);
		if (displayColor.equals("blue")) return context.getResources().getColor(R.color.magic);
		return defaultColor;
	}
	
	public Bitmap getIcon() {
		if (itemIcon == null) {
			itemIcon = getImageBitmap(D3Url.itemIconSmall2Url(this));
		}
		return itemIcon;
	}
	
	/**
	 * Getting an image with {@link android.graphics.BitmapFactory}
	 * @param url Where to get the image
	 * @return Bitmap representation of the image
	 */
	private Bitmap getImageBitmap(String url) {
		Bitmap bm = null;
		try {
			URL aURL = new URL(url);
			URLConnection conn = aURL.openConnection();
			conn.connect();
			InputStream is = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			bm = BitmapFactory.decodeStream(bis);
			bis.close();
			is.close();
		} catch (IOException e) {
			Log.e(this.getClass().getName(), "Error getting bitmap : "+e.getMessage());
		}
		return bm;
	}
}
