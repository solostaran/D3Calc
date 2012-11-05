package jodroid.d3obj;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import d3api.D3json;

/**
 * Represents the complete D3 Hero that the D3api provides in a JSON file.<br/>
 * Resource JSON file can be found with :<br/>
 * <ul>
 * <li>battletag-name ::= &lt;regional battletag allowed characters&gt;</li>
 * <li>battletag-code ::= &lt;integer&gt;</li>
 * <li>hero-id ::= &lt;integer&gt;</li>
 * <li>url ::= &lt;host&gt; "/api/d3/profile/" &lt;battletag-name&gt; "-" &lt;battletag-code&gt; "/hero/" &lt;hero-id&gt;</li>
 * </ul>
 * The "hero-id" can be found in the player profile JSON file.
 * @author JRD
 * @see D3HeroLite
 * @see D3Profile
 * @see <a href="http://blizzard.github.com/d3-api-docs/">Diablo 3 Web API</a>
 */
public class D3Hero extends D3HeroLite {
	public D3Items items;
	public D3Stats stats;
	public D3Kills kills;
	// TODO : much more datas (at least passive and active skills and runes)
	
	public String toString() {
		return name;
	}
	
	/**
	 * Parse the JSON file to provide a hierarchical representation of this file in the form of D3Obj.<br/>
	 * @param url where to find the JSON file
	 * @return the player profile's instance
	 */
	public static D3Hero getStaticUrlHero(String url) {
		String jsonHero = D3json.jsonGet(url);
		if (jsonHero == null) return null;
		JSONObject o;
		D3Hero hero = null;
		try {
			o = new JSONObject(jsonHero);
			hero = new D3Hero();
			hero.jsonBuild(o);
		} catch (JSONException e) {
			Log.e(D3json.class.getName(), e.getClass().getName() + ": " + e.getMessage());
		}
		return hero;
	}
	
	/**
	 * @see #getStaticUrlHero(String)
	 */
	public static D3Hero getStaticHero(String battlehost, String battlename, String battletag, String heroName) {
		String url = null;
		try {
			url = "http://"+battlehost+"/api/d3/profile/"+URLEncoder.encode(battlename, "UTF-8")+"-"+battletag+"/hero/"+URLEncoder.encode(heroName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			Log.e(D3Profile.class.getName(), e.getClass().getName() + ": " + e.getMessage());
		}
		return getStaticUrlHero(url);
	}
}
