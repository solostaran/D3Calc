package d3api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * This class offers different services :<br/>
 * - download of a JSON file<br/>
 * - starting points for parsing this file to D3 Objects.
 * @author JRD
 * @see jodroid.d3obj.D3Obj
 */
public class D3json {
	
	private static AsyncHttpClient httpClient = new AsyncHttpClient();
	
	/**
	 * Getting a JSON file asynchronously (method GET).<br/>
	 * Beware ! Android only.
	 * @param url
	 * @param params
	 * @param responseHandler
	 * @see <a href="http://loopj.com/android-async-http/doc/index.html">com.loopj.android.http</a>
	 * @see <a href="http://loopj.com/android-async-http/">Android Asynchronous Http Client</a>
	 */
	public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		httpClient.get(url, params, responseHandler);
	}

	/**
	 * Getting a JSON file asynchronously (method POST).<br/>
	 * Beware ! Android only.
	 * @param url
	 * @param params
	 * @param responseHandler
	 * @see <a href="http://loopj.com/android-async-http/doc/index.html">com.loopj.android.http</a>
	 * @see <a href="http://loopj.com/android-async-http/">Android Asynchronous Http Client</a>
	 */
	public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		httpClient.post(url, params, responseHandler);
	}
	
	/**
	 * Getting a JSON file.<br/>
	 * Synchronous so beware of a too long http request that would block the UI.
	 * @param url where to find the JSON file
	 * @return contains the whole JSON file in form of a String
	 */
	public static String jsonGet(String url) {
		Log.i(D3json.class.getName(), "Getting Json File = "+url);
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content, "iso-8859-1"), 8);
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
				content.close();
			} else {
				Log.e(D3json.class.getName(), "Failed to download file");
				return null;
			}
		} catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return builder.toString();
	}
}
