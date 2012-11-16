package jodroid.d3calc;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import jodroid.d3calc.fragments.HeroDetailsFragment;
import jodroid.d3calc.fragments.HeroFragment;
import jodroid.d3calc.fragments.ProfileDetailFragment;
import jodroid.d3obj.D3Hero;
import jodroid.d3obj.D3ItemLite;
import jodroid.d3obj.D3Profile;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import d3api.D3Url;
import d3api.D3json;

public class HeroDropdownActivity extends FragmentActivity implements ActionBar.OnNavigationListener {

	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	public static final String ARG_SECTION_NUMBER = "section_number";

	public static final String ARG_HERO_ID = "hero_id";

	private ProfileListContent.ProfileItem mItem;
	private String mHeroId;
	private ProgressDialog mProgressDialog;
	public static D3Hero mHero;

	private ArrayList<HeroFragment> mListFrag;
	private Fragment mCurrentFrag = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hero_dropdown);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		mItem = ProfileListContent.ITEM_MAP.get(getIntent().getStringExtra(ProfileDetailFragment.ARG_PROFILE_ID));
		mHeroId = getIntent().getStringExtra(ARG_HERO_ID);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mHeroId);
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

		mListFrag = new ArrayList<HeroFragment>();
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		for (int i = 0; i < getResources().getStringArray(R.array.HeroTabs).length; i++) {
			HeroFragment frag;
			if (i == 0) frag = new HeroDetailsFragment();
			else frag = new ItemLiteFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, i + 1);
			frag.setArguments(args);
			mListFrag.add(frag);
			ft.add(R.id.container, frag);
			ft.detach(frag);
		}
		ft.commit();

		if (savedInstanceState == null) {
			mProgressDialog = ProgressDialog.show(this, "", "Loading hero ...");
//			getUrlHero("http://www.ecole.ensicaen.fr/~reynaud/android/hero-4808413.json"); // dev example
			String url = D3Url.hero2Url(mItem, mHeroId);
			Log.i(this.getClass().getName(), ""+url);
			getUrlHero(url);
		}
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


	/**
	 * On item selection, detach the current fragment and attach the new one.<br/>
	 * {@link #mCurrentFrag} contains the fragment to be detached,<br/>
	 * the position argument gives us the new fragment to be attached.
	 * @see #onCreate(Bundle)
	 */
	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		// When the given tab is selected, show the tab contents in the container
		//        Fragment fragment = new HeroDetailsFragment();
		//        Bundle args = new Bundle();
		//        args.putInt(HeroDetailsFragment.ARG_SECTION_NUMBER, position + 1);
		//        fragment.setArguments(args);
		Log.i(this.getClass().getName(), "item select, pos="+position+", id="+id);
		if (mHero != null) {
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			Fragment nextFrag = mListFrag.get(position);
			if (mCurrentFrag != null) ft.detach(mCurrentFrag);
			ft.attach(nextFrag);
			ft.commit();
			mCurrentFrag = nextFrag;
		}
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
				if (mProgressDialog != null) mProgressDialog.dismiss();
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
				if (mProgressDialog != null) mProgressDialog.dismiss();
				Log.e(D3Profile.class.getName(), "json failure: "+e.getMessage());
			}
		});
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
		if (mProgressDialog != null) mProgressDialog.dismiss();
		getActionBar().setTitle(mHero.name);
		for (HeroFragment f : mListFrag) f.setHero(mHero);
		onNavigationItemSelected(0,0);
	}
	
	public static class ItemListFragment extends Fragment {
		public ItemListFragment() {}
		
		private Bitmap getImageBitmap(String url) {
			Bitmap bm = null;
			try {
				URL aURL = new URL(url);
				URLConnection conn = aURL.openConnection();
				conn.connect();
				InputStream is = conn.getInputStream();
				BufferedInputStream bis = new BufferedInputStream(is);
				bm = BitmapFactory.decodeStream(bis);
				bis.close();
				is.close();
			} catch (IOException e) {
				Log.e(this.getClass().getName(), "Error getting bitmap : "+e.getMessage());
			}
			return bm;
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

			Bundle args = getArguments();
			int index = args.getInt(ARG_SECTION_NUMBER);
			View retView = null;
			if (mHero != null) {
				String [] sectionNames = getResources().getStringArray(R.array.ItemsJsonNames);
				D3ItemLite temp = mHero.items.getItemByName(sectionNames[index-1]);
				if (temp != null) {
					LinearLayout vLayout = new LinearLayout(getActivity());
					LinearLayout hLayout = new LinearLayout(getActivity());

					vLayout.setOrientation(LinearLayout.VERTICAL);

					TextView textView = new TextView(getActivity());
					String str = new String();

					str = temp.name;
					textView.setText(str);
					textView.setTextColor(temp.getColor(getResources().getColor(R.color.black)));

					ImageView iv = new ImageView(getActivity());
					str = D3Url.itemIconSmall2Url(temp);
					iv.setImageBitmap(getImageBitmap(str));
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
					lp.setMargins(0, 0, 15, 0);
					iv.setLayoutParams(lp);
//					iv.setBackgroundColor(temp.getColor(getResources().getColor(R.color.white)));

					hLayout.addView(iv);
					hLayout.addView(textView);

					vLayout.addView(hLayout);

					textView = new TextView(getActivity());
					textView.setGravity(Gravity.CENTER);
					textView.setText("------------");
					vLayout.addView(textView);

					retView = vLayout;
				}
			}
			return retView;
		}
	}

	/**
	 * @see #onCreate(Bundle)
	 * @see HeroDropdownActivity#onNavigationItemSelected(int, long)
	 */
	public static class ItemLiteFragment extends HeroFragment {  

		public ItemLiteFragment() {
		}

		private Bitmap getImageBitmap(String url) {
			Bitmap bm = null;
			try {
				URL aURL = new URL(url);
				URLConnection conn = aURL.openConnection();
				conn.connect();
				InputStream is = conn.getInputStream();
				BufferedInputStream bis = new BufferedInputStream(is);
				bm = BitmapFactory.decodeStream(bis);
				bis.close();
				is.close();
			} catch (IOException e) {
				Log.e(this.getClass().getName(), "Error getting bitmap : "+e.getMessage());
			}
			return bm;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

			Bundle args = getArguments();
			int index = args.getInt(ARG_SECTION_NUMBER);
			View retView = null;
			if (mHero != null) {
				String [] sectionNames = getResources().getStringArray(R.array.ItemsJsonNames);
				D3ItemLite temp = mHero.items.getItemByName(sectionNames[index-1]);
				if (temp != null) {
					LinearLayout vLayout = new LinearLayout(getActivity());
					LinearLayout hLayout = new LinearLayout(getActivity());

					vLayout.setOrientation(LinearLayout.VERTICAL);

					TextView textView = new TextView(getActivity());
					String str = new String();

					str = temp.name;
					textView.setText(str);
					textView.setTextColor(temp.getColor(getResources().getColor(R.color.black)));

					ImageView iv = new ImageView(getActivity());
					str = D3Url.itemIconSmall2Url(temp);
					iv.setImageBitmap(getImageBitmap(str));
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
					lp.setMargins(0, 0, 15, 0);
					iv.setLayoutParams(lp);
//					iv.setBackgroundColor(temp.getColor(getResources().getColor(R.color.white)));

					hLayout.addView(iv);
					hLayout.addView(textView);

					vLayout.addView(hLayout);

					textView = new TextView(getActivity());
					textView.setGravity(Gravity.CENTER);
					textView.setText("------------");
					vLayout.addView(textView);

					retView = vLayout;
				}
			}
			return retView;
		}
	}
}
