package d3api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import jodroid.d3calc.ProfileListContent;
import jodroid.d3obj.D3ItemLite;
import jodroid.d3obj.D3Profile;
import android.util.Log;

public class D3Url {
	
	public static boolean log_state = true;

	public static String playerProfile2Url(String battlehost, String battlename, String battletag) {
		try {
			return "http://"+battlehost+"/api/d3/profile/"+URLEncoder.encode(battlename, "UTF-8")+"-"+battletag+"/";
		} catch (UnsupportedEncodingException e) {
			Log.e(D3Profile.class.getName(), e.getClass().getName() + ": " + e.getMessage());
		}
		return null;
	}
	
	public static String playerProfile2Url(ProfileListContent.ProfileItem p) {
		return playerProfile2Url(p.battlehost, p.battlename, p.battletag);
	}
	
	public static String hero2Url(String battlehost, String battlename, String battletag, String heroid) {
		try {
			return playerProfile2Url(battlehost, battlename, battletag)+"hero/"+URLEncoder.encode(heroid, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			Log.e(D3Profile.class.getName(), e.getClass().getSimpleName() + ": " + e.getMessage());
		}
		return null;
	}
	
	public static String hero2Url(ProfileListContent.ProfileItem p, String heroid) {
		return hero2Url(p.battlehost, p.battlename, p.battletag, heroid);
	}
	
	public static String item2Url(String tooltipParams) {
		return "http://eu.battle.net/api/d3/data/"+tooltipParams;
	}
	
	public static String item2Url(D3ItemLite item) {
		return item2Url(item.tooltipParams);
	}
	
	public static String itemIconSmall2Url(String iconName) {
		return "http://us.media.blizzard.com/d3/icons/items/small/" + iconName + ".png";
	}
	
	public static String itemIconLarge2Url(String iconName) {
		return "http://us.media.blizzard.com/d3/icons/items/large/" + iconName + ".png";
	}
	
	public static String skillIconSmall2Url(String iconName) {
		return "http://us.media.blizzard.com/d3/icons/skills/21/"+iconName+".png";
	}
	
	public static String skillIconLarge2Url(String iconName) {
		return "http://us.media.blizzard.com/d3/icons/skills/64/"+iconName+".png";
	}
}
