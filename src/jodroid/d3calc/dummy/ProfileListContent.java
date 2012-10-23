package jodroid.d3calc.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jodroid.d3calc.D3CalcDBoperations;
import jodroid.d3calc.ProfileListAdapter;
import jodroid.d3calc.ProfileListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

public class ProfileListContent {

	/**
	 * inline class, definition of a player profile item.<br/>
	 * Sufficient data to access a d3api player profile.
	 * @author JRD
	 *
	 */
    public static class ProfileItem {

        public String id;
        public String battlehost;
        public String battlename;
        public String battletag;

        public ProfileItem(String id, String battlehost, String battlename, String battletag) {
            this.id = id;
            this.battlehost = battlehost;
            this.battlename = battlename;
            this.battletag = battletag;
        }

        @Override
        public String toString() {
            return battlename+"#"+battletag;
        }
    }

    public static List<ProfileItem> ITEMS = new ArrayList<ProfileItem>();
    public static Map<String, ProfileItem> ITEM_MAP = new HashMap<String, ProfileItem>();
    private static Cursor ITEM_CURSOR;
    private static D3CalcDBoperations db = null;
    public static ProfileListAdapter adapter = null;
    
    /**
     * The DB needs a Context to be opened.<br>
     * @param c
     * @see ProfileListActivity#onCreate(Bundle)
     */
    public static void setContext(Context c) {
    	if (db == null) db = new D3CalcDBoperations(c);
    	if (db.isOpen() == false) {
    		db.open();
    		getProfiles();
    	}
    }

    static {
//        addItem(new ProfileItem("1", "eu.battle.net", "solo", "2284"));
//        addItem(new ProfileItem("2", "eu.battle.net", "tok", "2360"));
    }

    private static void addItem(ProfileItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }
    
    public static void removeItem(ProfileItem item) {
    	if (adapter != null) adapter.remove(item);
    	ITEMS.remove(item); // the adapter.remove should remove the item from ITEMS ?
    	ITEM_MAP.remove(item);
    	db.removeProfile(item);
    }
    
    public static void insertProfile(ProfileItem profile) {
    	db.insertProfile(profile);
    	addItem(profile);
    }
    
    private static void getProfiles() {
    	ITEM_CURSOR = db.getProfiles();
    	if (ITEM_CURSOR.moveToFirst()) {
    		do {
    			ProfileItem item = new ProfileItem(ITEM_CURSOR.getString(0), ITEM_CURSOR.getString(1),
    					ITEM_CURSOR.getString(2), ITEM_CURSOR.getString(3));
    			addItem(item);
    		} while (ITEM_CURSOR.moveToNext());
    	}
    }
}
