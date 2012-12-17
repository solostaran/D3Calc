package d3api;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.URL;
import java.net.URLConnection;

import jodroid.d3obj.D3Hero;
import jodroid.d3obj.D3Icon;
import jodroid.d3obj.D3Profile;
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
	
	/**
	 * File deserialization of a D3Profile.
	 * @param battlehost host part
	 * @param battletag name and tag part
	 * @return a new profile
	 */
	public static D3Profile readProfile(String battlehost, String battletag) {
		if (battlehost == null) return null;
		D3Profile ret = null;
		File filecache = new File(getBaseDirectory(), battlehost+"/"+battletag.replace('#', '-'));
		ObjectInputStream ois = null;
		try {
			FileInputStream fis = new FileInputStream(filecache);
			ois = new ObjectInputStream(fis);
			ret = (D3Profile)ois.readObject();
			ois.close();
			fis.close();
		} catch (FileNotFoundException e) {
			Log.w(D3Cache.class.getName(), e.getMessage());
		} catch (StreamCorruptedException e) {
			Log.e(D3Cache.class.getName(), e.getMessage());
		} catch (IOException e) {
			Log.e(D3Cache.class.getName(), e.getMessage());
		} catch (ClassNotFoundException e) {
			Log.e(D3Cache.class.getName(), e.getMessage());
		}
		return ret;
	}
	
	/**
	 * File serialization of a D3Profile.
	 * @param profile
	 */
	public static void writeProfile(D3Profile profile) {
		if (profile.battlehost == null) return;
		File filecache = new File(getBaseDirectory(), profile.battlehost+"/"+profile.battleTag.replace('#', '-'));
		if (!filecache.exists()) createDir(filecache.getParentFile());
		try {
			FileOutputStream fos = new FileOutputStream(filecache);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(profile);
			oos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			Log.e(D3Cache.class.getName(), e.getMessage());
		} catch (IOException e) {
			Log.e(D3Cache.class.getName(), e.getMessage());
		}
	}
	
	/**
	 * File deserialization of a D3Hero + Items.
	 * @param battlehost host part
	 * @param battletag name and tag part
	 * @return a new profile
	 */
	public static D3Hero readHero(String battlehost, String heroID) {
		if (battlehost == null) return null;
		if (heroID == null) return null;
		D3Hero ret = null;
		File filecache = new File(getBaseDirectory(), battlehost+"/"+heroID);
		ObjectInputStream ois = null;
		try {
			FileInputStream fis = new FileInputStream(filecache);
			ois = new ObjectInputStream(fis);
			ret = (D3Hero)ois.readObject();
			if (ret != null && ret.items != null) ret.items.buildItemArray();
			ois.close();
			fis.close();
		} catch (FileNotFoundException e) {
			Log.w(D3Cache.class.getName(), e.getMessage());
		} catch (StreamCorruptedException e) {
			Log.e(D3Cache.class.getName(), e.getMessage());
		} catch (IOException e) {
			Log.e(D3Cache.class.getName(), e.getMessage());
		} catch (ClassNotFoundException e) {
			Log.e(D3Cache.class.getName(), e.getMessage());
		}
		return ret;
	}
	
	/**
	 * File serialization of a D3Hero.
	 * @param profile
	 */
	public static void writeHero(D3Hero hero) {
		if (hero.battlehost == null) return;
		File filecache = new File(getBaseDirectory(), hero.battlehost+"/"+hero.id);
		if (!filecache.exists()) createDir(filecache.getParentFile());
		try {
			hero.items.itemArray2Items();
			FileOutputStream fos = new FileOutputStream(filecache);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(hero);
			oos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			Log.e(D3Cache.class.getName(), e.getMessage());
		} catch (IOException e) {
			Log.e(D3Cache.class.getName(), e.getMessage());
		}
	}
}
