package jodroid.d3calc.fragments;

import jodroid.d3calc.HeroStripActivity;
import jodroid.d3calc.PreferenceSettings;
import jodroid.d3calc.ProfileListContent;
import jodroid.d3calc.R;
import jodroid.d3calc.adapters.D3ObjArrayAdapter;
import jodroid.d3obj.D3Profile;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import d3api.D3Cache;
import d3api.D3JsonListener;
import d3api.D3Url;
import d3api.D3json;

public class ProfileDetailFragment extends Fragment implements OnItemClickListener, D3JsonListener<D3Profile> {

    public static final String ARG_PROFILE_ID = "profile_id";
    public static final String ARG_FORCE_LOAD = "forceload";
    private D3Profile playerProfile = null;
    private boolean forceload = false;

    ProfileListContent.ProfileItem mItem;
    
    private ProgressDialog progressDialog;

    public ProfileDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forceload = getArguments().getBoolean(ARG_FORCE_LOAD);
        forceload = PreferenceSettings.loadondemand(getActivity(), forceload);
        if (getArguments().containsKey(ARG_PROFILE_ID)) {
            mItem = ProfileListContent.ITEM_MAP.get(getArguments().getString(ARG_PROFILE_ID));
            progressDialog = ProgressDialog.show(getActivity(), "", "Loading profile ...");
            progressDialog.setCancelable(true);
//            getUrlProfile("http://www.ecole.ensicaen.fr/~reynaud/android/solo-2284.json"); // dev example
            String url = D3Url.playerProfile2Url(mItem);
            getUrlProfile(url);
        }
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile_detail, container, false);
        if (mItem != null) {
        	String str = mItem.toString()+" on "+mItem.battlehost;
        	getActivity().setTitle(str);
        }
		if (playerProfile != null) {
			buildAndDisplay(rootView, null);
		}
        return rootView;
    }
    
    /**
	 * Get and parse a JSON player profile (from D3api) to provide a hierarchical representation of this file in the form of D3Obj.<br/>
	 * The HttpRequest is asynchronous.
	 * @param url where to find the JSON file
	 * @return the player profile's instance
	 */
	public void getUrlProfile(final String url) {
		// CACHE
		if (!forceload) {
			playerProfile = D3Cache.readProfile(mItem.battlehost, mItem.battlename+"-"+mItem.battletag);
			if (playerProfile != null && !PreferenceSettings.loadold(getActivity(), forceload, playerProfile.lastUpdated)) {
				Log.i(this.getClass().getSimpleName(), "Cached Profile : "+playerProfile);
				if (progressDialog != null) progressDialog.dismiss();
				return;
			}
		}
		Log.i(this.getClass().getSimpleName(), url);
		D3json.get(url, null, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject obj) {
				// bad file test
				try {
					String code = obj.getString("code");
					if (code != null) {
						Log.w(getActivity().getClass().getName(), "code="+code);
						Toast.makeText(getActivity(), obj.getString("reason"), Toast.LENGTH_LONG).show();
						return;
					}
				}
				catch (JSONException e) {}
	            playerProfile = new D3Profile(mItem.battlehost);
				buildAndDisplay(getView(), obj);
				if (progressDialog != null) progressDialog.dismiss();
			}
			
			@Override
			public void onFailure(Throwable e, JSONObject obj) {
				if (progressDialog != null) progressDialog.dismiss();
				Log.e(this.getClass().getSimpleName(), "json failure: "+e.getMessage());
				Toast.makeText(getActivity(), url+"\n"+getString(R.string.error_loading_profile), Toast.LENGTH_SHORT).show();
			}
		});
	}
    
    /**
     * Build objects from the JSON file and display them
     * @param obj a JSON object parsed from the file
     */
    private void buildAndDisplay(View parentview, JSONObject obj) {
    	Activity act = getActivity();
    	if (act == null) return;
    	if (parentview == null) return;
    	
    	TextView tv = (TextView)parentview.findViewById(R.id.textProfileLastUpdated);
    	
    	// CACHE
    	if (obj != null) {
    		playerProfile.jsonBuild(obj);
    		tv.setText(getString(R.string.last_updated)+" : "+playerProfile.getLastUpdated());
    		playerProfile.lastUpdated = PreferenceSettings.saveloaddate(getActivity(), playerProfile.lastUpdated);
    		D3Cache.writeProfile(playerProfile);
    	} else {
    		tv.setText(getString(R.string.last_updated)+" : "+playerProfile.getLastUpdated());
    	}
    	
    	act.setTitle(playerProfile.toString());
    	playerProfile.kills.fieldsToView(getView());
    	
    	ListView lv = (ListView)parentview.findViewById(R.id.listHeroesLite);
    	D3ObjArrayAdapter adapter = new D3ObjArrayAdapter(getActivity(), R.layout.hero_list_item, playerProfile.heroes);
    	lv.setAdapter(adapter);
//    	lv.setAdapter(new ArrayAdapter<D3HeroLite>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, playerProfile.heroes));
    	adapter.notifyDataSetChanged();
    	lv.setOnItemClickListener(this);
    }

	@Override
	public void onItemClick(AdapterView<?> adapterView, View itemView, int position, long id) {
//		Intent heroIntent = new Intent(getActivity(), HeroDropdownActivity.class);
		Intent heroIntent = new Intent(getActivity(), HeroStripActivity.class);
		heroIntent.putExtra(ARG_PROFILE_ID, mItem.id);
//		Log.i(this.getClass().getSimpleName(), "id="+playerProfile.heroes[position].id);
		heroIntent.putExtra(HeroStripActivity.ARG_HERO_ID, Long.toString(playerProfile.heroes[position].id));
		startActivity(heroIntent);
	}

	@Override
	public void displayD3Obj(D3Profile obj) {
	}
}
