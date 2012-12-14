package d3api;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import jodroid.d3obj.D3Icon;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

/**
 * This class offers file caching services.<br/>
 * @author JRD
 */
public class D3Cache {
	public final static String D3BASEDIR = "data/D3Calc";
	public final static int TIMEOUT_CONNECT = 10000;
	public final static int TIMEOUT_READ = 30000;
	
	private static File getBaseDirectory() {
		File baseDirectory = new File(Environment.getExternalStorageDirectory(), D3BASEDIR);
		if (!baseDirectory.exists()) {
			Log.i(D3Cache.class.getSimpleName(), "Creating dir : "+baseDirectory.getPath());
			if (!baseDirectory.mkdirs()) return null;
		}
		return baseDirectory;
	}
	
	public static void createDir(File dir) {
		if (dir.exists()) return;
		Log.i(D3Cache.class.getSimpleName(), "Creating dir : "+dir.getPath());
		dir.mkdirs();
	}

	public static D3Icon getItemIcon(String url) {
		int index = url.indexOf("d3/");
		File fileimage = new File(getBaseDirectory(), url.substring(index+3));
		Bitmap bm = null;
		if (!fileimage.exists()) {
			createDir(fileimage.getParentFile());
			try {
				FileOutputStream fos = new FileOutputStream(fileimage);
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				
				URL aURL = new URL(url);
				URLConnection conn = aURL.openConnection();
				conn.setConnectTimeout(TIMEOUT_CONNECT);
				conn.setReadTimeout(TIMEOUT_READ);
				conn.connect();
				InputStream is = conn.getInputStream();
//				BufferedInputStream bis = new BufferedInputStream(is);
				
				byte [] buffer = new byte[1024];
				int bytesRead = 0;
				while ((bytesRead = is.read(buffer, 0, buffer.length)) >= 0) {
					bos.write(buffer, 0, bytesRead);
				}
				
//				bm = BitmapFactory.decodeStream(bis); // obtain an image directly from the url
//				bis.close();
				is.close();
				bos.flush();
				bos.close();
				fos.close();
			} catch (IOException e) {
				Log.e(D3Cache.class.getSimpleName(), "Error getting bitmap : "+e.getMessage());
			}
		}
		bm = BitmapFactory.decodeFile(fileimage.getPath());
		return new D3Icon(bm);
	}
}
