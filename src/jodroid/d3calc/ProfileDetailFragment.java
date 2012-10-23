package jodroid.d3calc;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import jodroid.d3calc.dummy.ProfileListContent;
import jodroid.d3obj.D3Profile;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;

import d3api.D3json;

public class ProfileDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";
    private D3Profile playerProfile = null;

    ProfileListContent.ProfileItem mItem;

    public ProfileDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mItem = ProfileListContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
            playerProfile = new D3Profile();
            Log.i(this.getClass().getName(), "btag:"+mItem.toString());
            getUrlProfile("http://www.ecole.ensicaen.fr/~reynaud/android/solo-2284.json");
//            getProfile(mItem.battlehost, mItem.battlename, mItem.battletag);
        }
    }
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile_detail, container, false);
        if (mItem != null) {
        	String str = mItem.toString()+" on "+mItem.battlehost;
            ((TextView) rootView.findViewById(R.id.profile_detail)).setText(str);
        }
        return rootView;
    }
    
    /**
     * Build objects from the JSON file and display them
     * @param obj a JSON object parsed from the file
     */
    private void buildAndDisplay(JSONObject obj) {
    	playerProfile.jsonBuild(obj);
    	Activity act = getActivity();
    	if (act == null) return;
    	if (getView() == null) return;
//    	if (act.getClass() == ProfileDetailActivity.class) return; // wrong the master activity is sometimes another activity
    	act.setTitle(playerProfile.toString());
    	String str = "\nMonster kills="+playerProfile.kills.monsters;
    	str += "\nElite kills="+playerProfile.kills.elites;
    	str += "\nHardcore kills="+playerProfile.kills.hardcoreMonsters;
		((TextView)getView().findViewById(R.id.profile_detail)).setText(str);
    }
    
    
    /**
	 * Get and parse a JSON player profile (from D3api) to provide a hierarchical representation of this file in the form of D3Obj.<br/>
	 * The HttpRequest is asynchronous.
	 * @param url where to find the JSON file
	 * @return the player profile's instance
	 */
	public void getUrlProfile(String url) {
		D3json.get(url, null, new JsonHttpResponseHandler() {
			public void onSuccess(JSONObject obj) {
				Log.i(D3Profile.class.getName(), "json get successful");
				buildAndDisplay(obj);
			}
			
			public void onFailure(Throwable e, JSONObject obj) {
				Log.e(D3Profile.class.getName(), "json failure: "+e.getMessage());
			}
		});
	}
	
	/**
	 * @see #getUrlProfile(String)
	 */
	public void getProfile(String battlehost, String battlename, String battletag) {
		String url = null;
		try {
			url = "http://"+battlehost+"/api/d3/profile/"+URLEncoder.encode(battlename, "UTF-8")+"-"+battletag+"/";
			getUrlProfile(url);
		} catch (UnsupportedEncodingException e) {
			Log.e(D3Profile.class.getName(), e.getClass().getName() + ": " + e.getMessage());
		}
	}
}
