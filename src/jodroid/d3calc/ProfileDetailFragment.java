package jodroid.d3calc;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.loopj.android.http.JsonHttpResponseHandler;

import d3api.D3json;

public class ProfileDetailFragment extends Fragment implements OnItemClickListener {

    public static final String ARG_ITEM_ID = "item_id";
    private D3Profile playerProfile = null;

    ProfileListContent.ProfileItem mItem;
    
    private ProgressDialog progressDialog;

    public ProfileDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mItem = ProfileListContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
            playerProfile = new D3Profile();
            Log.i(this.getClass().getName(), "btag:"+mItem.toString());
            progressDialog = ProgressDialog.show(getActivity(), "", "Loading profile ...");
            getUrlProfile("http://www.ecole.ensicaen.fr/~reynaud/android/solo-2284.json"); // dev example
//            getProfile(mItem.battlehost, mItem.battlename, mItem.battletag);
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
//    	if (act.getClass() == ProfileDetailActivity.class) return; // wrong : the master activity is sometimes another activity
    	
    	act.setTitle(playerProfile.toString());
    	
    	playerProfile.kills.fieldsToView(getView());
    	
    	ListView lv = (ListView)getView().findViewById(R.id.listHeroesLite);
    	D3ObjArrayAdapter adapter = new D3ObjArrayAdapter(getActivity(), R.layout.hero_list_item, playerProfile.heroes);
    	lv.setAdapter(adapter);
//    	lv.setAdapter(new ArrayAdapter<D3HeroLite>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, playerProfile.heroes));
    	adapter.notifyDataSetChanged();
    	
    	lv.setOnItemClickListener(this);
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
				if (progressDialog != null) progressDialog.dismiss();
				try {
					String code = obj.getString("code");
					if (code != null) {
						Log.w(getActivity().getClass().getName(), "code="+code);
						Toast.makeText(getActivity(), obj.getString("reason"), Toast.LENGTH_LONG).show();
						return;
					}	
				} catch (JSONException e) {}
				buildAndDisplay(obj);
			}
			
			public void onFailure(Throwable e, JSONObject obj) {
				Log.e(D3Profile.class.getName(), "json failure: "+e.getMessage());
			}
		});
	}
	
	/**
	 * Get a profile by constructing the correct D3 api URL.
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

	@Override
	public void onItemClick(AdapterView<?> adapterView, View itemView, int position, long id) {
//		Toast.makeText(getActivity(), "Hero choice = "+playerProfile.heroes[position].name, Toast.LENGTH_SHORT).show();
//		Intent heroIntent = new Intent(getActivity(), HeroStripActivity.class);
//		heroIntent.putExtra(ARG_ITEM_ID, mItem.id);
//		heroIntent.putExtra(HeroStripActivity.ARG_HOST_VAL, mItem.battlehost);
//		heroIntent.putExtra(HeroStripActivity.ARG_NAME_VAL, mItem.battlename);
//		heroIntent.putExtra(HeroStripActivity.ARG_TAG_VAL, mItem.battletag);
//		heroIntent.putExtra(HeroStripActivity.ARG_HERO_VAL, playerProfile.heroes[position].name);
//		startActivity(heroIntent);
		
//		Intent heroIntent = new Intent(getActivity(), HeroTabActivity.class);
//		startActivity(heroIntent);
		
		Intent heroIntent = new Intent(getActivity(), HeroDropdownActivity.class);
		heroIntent.putExtra(ARG_ITEM_ID, mItem.id);
		heroIntent.putExtra(HeroStripActivity.ARG_HERO_VAL, playerProfile.heroes[position].name);
		startActivity(heroIntent);
	}
}
