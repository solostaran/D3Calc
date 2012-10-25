package jodroid.d3obj;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import d3api.D3json;

/**
 * Represents a Diablo 3 player profile that the D3api provides in the form of a JSON file.<br/>
 * The JSON Resource file can be found with :
 * <ul>
 * <li>battletag-name ::= &lt;regional battletag allowed characters&gt;</li>
 * <li>battletag-code ::= &lt;integer&gt;</li>
 * <li>url ::= &lt;host&gt; "/api/d3/profile/" &lt;battletag-name&gt; "-" &lt;battletag-code&gt; "/"</li>
 * </ul>
 * @author JRD
 * @see D3HeroLite
 * @see <a href="http://blizzard.github.com/d3-api-docs/">Diablo 3 Web API</a>
 */
public class D3Profile extends D3Obj {
	public D3HeroLite [] heroes;
	public String battleTag;
	public D3Kills kills;
	public D3Artisan [] artisans;
	
	public String toString() {
		return battleTag;
	}
	
	/**
	 * Parse the JSON file to provide a hierarchical representation of this file in the form of D3Obj.<br/>
	 * @param url where to find the JSON file
	 * @return the player profile's instance
	 */
	public static D3Profile getStaticUrlProfile(String url) {
		String jsonProfile = D3json.jsonGet(url);
		if (jsonProfile == null) return null;
		JSONObject o;
		D3Profile prof = null;
		try {
			o = new JSONObject(jsonProfile);
			prof = new D3Profile();
			prof.jsonBuild(o);
		} catch (JSONException e) {
			Log.e(D3json.class.getName(), e.getClass().getName() + ": " + e.getMessage());
		}
		return prof;
		
	}
	
	/**
	 * @see #getStaticUrlProfile(String)
	 */
	public static D3Profile getStaticProfile(String battlehost, String battlename, String battletag) {
		String url = null;
		try {
			url = "http://"+battlehost+"/api/d3/profile/"+URLEncoder.encode(battlename, "UTF-8")+"-"+battletag+"/";
		} catch (UnsupportedEncodingException e) {
			Log.e(D3Profile.class.getName(), e.getClass().getName() + ": " + e.getMessage());
		}
		return getStaticUrlProfile(url);
	}
}
