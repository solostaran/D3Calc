package jodroid.d3obj;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class D3Icon extends D3Obj {
	public Bitmap icon;
	
	public D3Icon(String url) {
		icon = getImageBitmap(url);
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
