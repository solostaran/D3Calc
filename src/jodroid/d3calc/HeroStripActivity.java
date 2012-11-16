package jodroid.d3calc;

import jodroid.d3calc.fragments.HeroDetailsFragment;
import jodroid.d3calc.fragments.HeroFragment;
import jodroid.d3calc.fragments.ItemListFragment;
import jodroid.d3calc.fragments.ProfileDetailFragment;
import jodroid.d3obj.D3Hero;
import jodroid.d3obj.D3Profile;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import d3api.D3Url;
import d3api.D3json;

public class HeroStripActivity extends FragmentActivity {

	public static final String ARG_HOST_VAL = "host_value";
	public static final String ARG_NAME_VAL = "name_value";
	public static final String ARG_TAG_VAL = "tag_value";
	public static final String ARG_HERO_VAL = "hero_value";

	private ProfileListContent.ProfileItem mItem;
	public static final String ARG_HERO_ID = "hero_id";
	private String mHeroId;
	private ProgressDialog mProgressDialog;
	public static D3Hero mHero;

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
	 * sections. We use a {@link android.support.v4.app.FragmentPagerAdapter} derivative, which will
	 * keep every loaded fragment in memory. If this becomes too memory intensive, it may be best
	 * to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	
	static HeroFragment mHeroDetailsFrag = new HeroDetailsFragment();
	static HeroFragment mHeroItemsFrag = new ItemListFragment();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hero_strip);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		if (savedInstanceState == null) {
			mItem = ProfileListContent.ITEM_MAP.get(getIntent().getStringExtra(ProfileDetailFragment.ARG_PROFILE_ID));
			mHeroId = getIntent().getStringExtra(ARG_HERO_ID);
		}

		// Create the adapter that will return a fragment for each of the three primary sections
		// of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mHeroId);

		this.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
		if (savedInstanceState == null) {
			mProgressDialog = ProgressDialog.show(this, "", "Loading hero ...");
			//			getUrlHero("http://www.ecole.ensicaen.fr/~reynaud/android/hero-4808413.json"); // dev example
			String url = D3Url.hero2Url(mItem, mHeroId);
			Log.i(this.getClass().getName(), ""+url);
			getUrlHero(url);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_hero_strip, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			//			NavUtils.navigateUpFromSameTask(this);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
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
						Toast.makeText(HeroStripActivity.this, obj.getString("reason"), Toast.LENGTH_LONG).show();
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
		getActionBar().setTitle(mHero.name);
		mHeroDetailsFrag.setHero(mHero);
		mHeroItemsFrag.setHero(mHero);
		mViewPager.setCurrentItem(0);
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
	 * sections of the app.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			Fragment fragment = null;
			if (i == 0) {
				fragment = mHeroDetailsFrag;
			} else {
				fragment = mHeroItemsFrag;
//				fragment = new DummySectionFragment();
//				Bundle args = new Bundle();
//				args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
//				fragment.setArguments(args);
			}
			return fragment;
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0: return getString(R.string.title_sectionDetails).toUpperCase();
			case 1: return getString(R.string.title_sectionItems).toUpperCase();
			//                case 2: return getString(R.string.title_sectionShoulders).toUpperCase();
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply displays dummy text.
	 */
	public static class DummySectionFragment extends HeroFragment {
		public DummySectionFragment() {
		}

		public static final String ARG_SECTION_NUMBER = "section_number";

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			TextView textView = new TextView(getActivity());
			textView.setGravity(Gravity.CENTER);
			Bundle args = getArguments();
			textView.setText(Integer.toString(args.getInt(ARG_SECTION_NUMBER)));
			return textView;
		}
	}
}
