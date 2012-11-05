package jodroid.d3calc;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import jodroid.d3obj.D3Hero;
import jodroid.d3obj.D3ItemLite;
import jodroid.d3obj.D3Profile;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import d3api.D3json;

public class HeroDropdownActivity extends FragmentActivity implements ActionBar.OnNavigationListener {

    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
    
	public static final String ARG_HERO_VAL = "hero_value";
    
    private ProfileListContent.ProfileItem mItem;
    private String mHeroName;
    private ProgressDialog mProgressDialog;
    public static D3Hero mHero;
    
    private ArrayList<Fragment> mListFrag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero_dropdown);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        mItem = ProfileListContent.ITEM_MAP.get(getIntent().getStringExtra(ProfileDetailFragment.ARG_ITEM_ID));
        mHeroName = getIntent().getStringExtra(ARG_HERO_VAL);
             
        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mHeroName);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        // Set up the dropdown list navigation in the action bar.
        actionBar.setListNavigationCallbacks(
                // Specify a SpinnerAdapter to populate the dropdown list.
                new ArrayAdapter<String>(
                        actionBar.getThemedContext(),
                        android.R.layout.activity_list_item,
                        android.R.id.text1,
//                        new String[]{
//                                getString(R.string.title_sectionDetails),
//                                getString(R.string.title_sectionHead),
//                                getString(R.string.title_sectionShoulders),
//                        }),
                        getResources().getStringArray(R.array.HeroTabs)),
                this);
        
        mListFrag = new ArrayList<Fragment>();
        for (int i = 0; i < getResources().getStringArray(R.array.HeroTabs).length; i++) {
        	Fragment frag = new HeroDetailsSectionFragment();
        	Bundle args = new Bundle();
            args.putInt(HeroDetailsSectionFragment.ARG_SECTION_NUMBER, i + 1);
            frag.setArguments(args);
            mListFrag.add(frag);
        }
        
        Log.i(this.getClass().getName(), "hero:"+mHeroName);
        mProgressDialog = ProgressDialog.show(this, "", "Loading hero ...");
        getUrlHero("http://www.ecole.ensicaen.fr/~reynaud/android/hero-4808413.json"); // dev example
//        getHero(mItem.battlehost, mItem.battlename, mItem.battletag, mHeroName);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
            getActionBar().setSelectedNavigationItem(
                    savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_SELECTED_NAVIGATION_ITEM,
                getActionBar().getSelectedNavigationIndex());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_hero_dropdown, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(int position, long id) {
        // When the given tab is selected, show the tab contents in the container
//        Fragment fragment = new HeroDetailsSectionFragment();
//        Bundle args = new Bundle();
//        args.putInt(HeroDetailsSectionFragment.ARG_SECTION_NUMBER, position + 1);
//        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mListFrag.get(position))
                .commit();
        return true;
    }
    
    /**
	 * Get and parse a JSON D3 hero file (from D3api) to provide a hierarchical representation of this file in the form of D3Obj.<br/>
	 * The HttpRequest is asynchronous.
	 * @param url where to find the JSON file
	 */
	public void getUrlHero(String url) {
		D3json.get(url, null, new JsonHttpResponseHandler() {
			public void onSuccess(JSONObject obj) {
				try {
					String code = obj.getString("code");
					if (code != null) {
						Log.w(HeroDropdownActivity.class.getName(), "code="+code);
						Toast.makeText(HeroDropdownActivity.this, obj.getString("reason"), Toast.LENGTH_LONG).show();
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
	public void getHero(String battlehost, String battlename, String battletag, String heroName) {
		String url = null;
		try {
			url = "http://"+battlehost+"/api/d3/profile/"+URLEncoder.encode(battlename, "UTF-8")+"-"+battletag+"/hero/"+URLEncoder.encode(heroName, "UTF-8");
			getUrlHero(url);
		} catch (UnsupportedEncodingException e) {
			Log.e(D3Profile.class.getName(), e.getClass().getName() + ": " + e.getMessage());
		}
	}
	
	/**
     * Build objects from the JSON file and display them
     * @param obj a JSON object parsed from the file
     */
    private void buildAndDisplay(JSONObject obj) {
    	mHero = new D3Hero();
    	mHero.jsonBuild(obj);
//    	try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//    	getActionBar().setSelectedNavigationItem(0);
    	if (mProgressDialog != null) mProgressDialog.dismiss();
    }

    /**
     * A dummy fragment representing a section of the app, but that simply displays dummy text.
     */
    public static class HeroDetailsSectionFragment extends Fragment {
        public HeroDetailsSectionFragment() {
        }

        public static final String ARG_SECTION_NUMBER = "section_number";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
        	
        	Bundle args = getArguments();
            int index = args.getInt(ARG_SECTION_NUMBER);
        	View retView = null;
        	
            if (mHero != null) {
            	if (index == 1) {
            		retView = inflater.inflate(R.layout.fragment_hero_details, container, false);
            		mHero.stats.fieldsToView(retView);
            	} else {
            		TextView textView = new TextView(getActivity());
                    textView.setGravity(Gravity.CENTER);
                    String str = Integer.toString(index)+"\n";
            		String [] sectionNames = getResources().getStringArray(R.array.ItemsJsonNames);
            		D3ItemLite temp = mHero.items.getItemByName(sectionNames[index-1]);
            		str += temp.toString();
            		textView.setText(str);
            		retView = textView;
            	}
            }
           
            return retView;
        }
    }
}
