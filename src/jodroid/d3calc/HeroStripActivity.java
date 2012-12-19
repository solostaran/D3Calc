package jodroid.d3calc;

import jodroid.d3calc.fragments.HeroDetailsFragment;
import jodroid.d3calc.fragments.HeroFragment;
import jodroid.d3calc.fragments.ItemListFragment;
import jodroid.d3calc.fragments.ProfileDetailFragment;
import jodroid.d3obj.D3Hero;
import jodroid.d3obj.D3Item;
import jodroid.d3obj.D3ItemLite;
import jodroid.d3obj.D3Profile;

import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import d3api.D3Cache;
import d3api.D3Url;
import d3api.D3json;

public class HeroStripActivity extends FragmentActivity {

	private static final boolean DEBUG = false;
	
    public static final String ARG_HOST_VAL = "host_value";
	public static final String ARG_NAME_VAL = "name_value";
	public static final String ARG_TAG_VAL = "tag_value";
	public static final String ARG_HERO_VAL = "hero_value";

	private static ProfileListContent.ProfileItem mItem;
	public static final String ARG_HERO_ID = "hero_id";
	private String mHeroId;
	private ProgressDialog mProgressDialog;
	private int mProgressValue;
	private boolean forceload = false;
	public static D3Hero mHero = null;
	private JSONObject [] mJsonItems;
	private ProgressBar mLoadBar;
	private TextView mLoadMessage;

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
	
	static HeroFragment mHeroDetailsFrag = new HeroDetailsFragment();;
	static ItemListFragment mHeroItemsFrag = new ItemListFragment();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hero_strip);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		mLoadBar = (ProgressBar)findViewById(R.id.loadbar);
		mLoadMessage = (TextView)findViewById(R.id.loadmessage);

		if (savedInstanceState == null) {
			mItem = ProfileListContent.ITEM_MAP.get(getIntent().getStringExtra(ProfileDetailFragment.ARG_PROFILE_ID));
			mHeroId = getIntent().getStringExtra(ARG_HERO_ID);
			forceload = getIntent().getBooleanExtra(ProfileDetailFragment.ARG_FORCE_LOAD, false);
			mHero = null;
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

		if (savedInstanceState == null) {
			mProgressDialog = ProgressDialog.show(this, "", getString(R.string.hero_load_message));
			mProgressDialog.setCancelable(true);
//			getUrlHero("http://www.ecole.ensicaen.fr/~reynaud/android/hero-4808413.json"); // dev example
			String url = D3Url.hero2Url(mItem, mHeroId);
			getUrlHero(url);
		} else {
//			Log.i(this.getClass().getName(), "Recreate activity with : "+mHero.name);
		}
		this.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_right);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (mHero != null) {
			getActionBar().setTitle(mHero.name);
			reduceView(mLoadBar);
			reduceView(mLoadMessage);
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
//			finish(); // doesn't work if reload button is used
			Intent profileIntent = new Intent(this, ProfileDetailActivity.class);
			profileIntent.putExtra(ProfileDetailFragment.ARG_PROFILE_ID, mItem.id);
			NavUtils.navigateUpTo(this, profileIntent);
			return true;
		case R.id.menu_reload_hero:
			Log.i(this.getClass().getSimpleName(), "Reload ... "+mHero);
			Intent heroIntent = new Intent(this, HeroStripActivity.class);
			heroIntent.putExtra(ProfileDetailFragment.ARG_PROFILE_ID, mItem.id);
			heroIntent.putExtra(HeroStripActivity.ARG_HERO_ID, mHeroId);
			heroIntent.putExtra(ProfileDetailFragment.ARG_FORCE_LOAD, true);
			startActivity(heroIntent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Get and parse a JSON D3 hero file (from D3api) to provide a hierarchical representation of this file in the form of D3Obj.<br/>
	 * The HttpRequest is asynchronous.
	 * @param url where to find the JSON file
	 */
	public void getUrlHero(final String url) {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		if (sharedPref.getBoolean(PreferenceSettings.PREF_ALWAYS_LOAD, false)) forceload = true;
		// CACHE
		if (!forceload) {
			mHero = D3Cache.readHero(mItem.battlehost, mHeroId);
			if (mHero != null) {
				Log.i(this.getClass().getSimpleName(), "Cached Hero : "+mHero);
				if (mProgressDialog != null) mProgressDialog.dismiss();
				// Undisplay the loading message and progress bar
				reduceView(mLoadBar);
				reduceView(mLoadMessage);
				mHeroDetailsFrag.setHero(mHero);
				mHeroItemsFrag.setHero(mHero);
				getActionBar().setTitle(mHero.name);
				return;
			}
		}
		Log.i(this.getClass().getSimpleName(), url);
		D3json.get(url, null, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject obj) {
				try {
					String code = obj.getString("code");
					if (code != null) {
						Log.w(HeroStripActivity.this.getClass().getName(), "code="+code);
						Toast.makeText(HeroStripActivity.this, obj.getString("reason"), Toast.LENGTH_LONG).show();
						return;
					}	
				} catch (JSONException e) {}
				buildHero(obj);
			}

			@Override
			public void onFailure(Throwable e, JSONObject obj) {
				Log.e(D3Profile.class.getName(), "json failure: "+e.getMessage());
				Toast.makeText(HeroStripActivity.this, url+"\n"+getString(R.string.error_loading_hero), Toast.LENGTH_LONG).show();
			}
		});
	}

	/**
	 * Build a D3Hero from the JSON file and display the hero details.<br/>
	 * The JSON parsing is time consuming so we have to do it in background.
	 * @param obj a JSON object parsed from the file
	 */
	private void buildHero(JSONObject obj) {
		// Using an AsyncTask to parse a hero
		new AsyncTask<JSONObject, Void, Void>() {
			private JSONObject o;
			
			@Override
			protected void onPreExecute() {
				mProgressDialog.setMessage(getString(R.string.hero_parse_message));
				mProgressDialog.setCancelable(true);
				mHero = new D3Hero(mItem.battlehost);
			}

			@Override
			protected Void doInBackground(JSONObject... params) {
				o = params[0];
				mHero.jsonBuild(o);
				return null;
			}

			@Override
			protected void onPostExecute(Void v) {
				super.onPostExecute(v);
				mProgressDialog.dismiss();
				getActionBar().setTitle(mHero.name);
				mLoadBar.setVisibility(View.VISIBLE);
				mLoadMessage.setVisibility(View.VISIBLE);
				mHeroDetailsFrag.setHero(mHero);
				mHeroItemsFrag.setHero(mHero);
				mViewPager.setCurrentItem(0);
				loadItems();
			}
		}.execute(obj);	
	}
	
	/**
	 * Load all items with AsyncHTTP.
	 */
	private void loadItems() {
		mHero.items.buildItemArray();
//		mProgressDialog = new ProgressDialog(this);
//		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//		mProgressDialog.setMax(mHero.items.itemArray.length);
//		mProgressDialog.setProgress(mHero.items.itemArray.length);
//		mProgressDialog.setMessage(getString(R.string.items_load_message));
//		mProgressDialog.show();

		mLoadBar.setMax(mHero.items.itemArray.length);
		mLoadBar.setProgress(0);
		mLoadMessage.setText(getString(R.string.items_load_message));
		
		mJsonItems = new JSONObject[mHero.items.itemArray.length];
		mProgressValue = mHero.items.itemArray.length;
		for (int i = 0; i < mHero.items.itemArray.length; i++) {
			D3ItemLite item = mHero.items.itemArray[i];
			if (!(item instanceof D3Item)) {
				getUrlItem(D3Url.item2Url(item), i);
			}
		}
	}
	
	/**
	 * Get a JSON item (from D3api) to provide a hierarchical representation of this file in the form of D3Obj.<br/>
	 * The HttpRequest is asynchronous.
	 * @param url where to find the JSON file
	 * @return the player profile's instance
	 */
	public void getUrlItem(final String url, final int position) {
		D3json.get(url, null, new JsonHttpResponseHandler() {
			@Override
			public synchronized void onSuccess(JSONObject obj) {
				try {
					String code = obj.getString("code");
					if (code != null) {
						Log.w(this.getClass().getName(), "code="+code);
						Toast.makeText(HeroStripActivity.this, obj.getString("reason"), Toast.LENGTH_LONG).show();
						return;
					}
				}
				catch (JSONException e) {}
				mJsonItems[position] = obj;
				if (DEBUG) Log.i(HeroStripActivity.class.getName(), "Item("+position+","+mHero.items.itemArray[position].itemSlot+") loaded.");
				mProgressValue--;
//				mProgressDialog.setProgress(mProgressValue);
				mLoadBar.setProgress(mProgressValue);
				if (mProgressValue == 0) {
					buildItems();
				}
			}
			
			@Override
			public synchronized void onFailure(Throwable e, JSONObject obj) {
				Log.e(D3Profile.class.getSimpleName(), "json failure: "+e.getMessage());
				String msg = mHero.items.itemArray[position].name + " (" + mHero.items.itemArray[position].itemSlot + ")";
				Toast.makeText(HeroStripActivity.this, msg+"\n"+getString(R.string.error_loading_item), Toast.LENGTH_LONG).show();
			}
		});
	}
	
	/**
	 * Parse all items in background.
	 */
	private void buildItems() {
		// Using an AsyncTask to parse a hero
		new AsyncTask<JSONObject [], Integer, Integer>() {
			private JSONObject [] tab;
			
			@Override
			protected void onPreExecute () {
//				mProgressDialog.setMessage(getString(R.string.items_parse_message));
//				mProgressDialog.setProgress(0);
				mLoadMessage.setText(getString(R.string.items_parse_message));
				mLoadBar.setProgress(0);
			}

			@Override
			protected Integer doInBackground(JSONObject[]... params) {
				tab = params[0];
				for (int i = 0; i < tab.length; i++) {
					D3Item item = new D3Item(mHero.items.itemArray[i]);
					item.jsonBuild(tab[i]);
					mHero.items.itemArray[i] = item;
					publishProgress(i);
				}
				return tab.length;
			}
			
			@Override
			protected void onProgressUpdate(Integer... progress) {
//				mProgressDialog.setProgress(progress[0]);
				mLoadBar.setProgress(progress[0]);
				if (DEBUG) Log.d(this.getClass().getName(), "Parsing item ("+progress[0]+","+mHero.items.itemArray[progress[0]].itemSlot+")");
			}

			@Override
			protected void onPostExecute(Integer n) {
				super.onPostExecute(n);
				mLoadBar.setProgress(mLoadBar.getMax());
				// Undisplay the loading message and progress bar
				reduceView(mLoadBar);
				reduceView(mLoadMessage);
//				mProgressDialog.dismiss();
				mHeroItemsFrag.updateView();
				
				// CACHE
				D3Cache.writeHero(mHero);
			}
		}.execute(mJsonItems);
	}
	
	/**
	 * Animate a view to its minimum height size of 1 and make it invisible at the end.<br/>
	 * @param view the view to be reduced to none
	 * @see ValueAnimator
	 */
	private void reduceView(final View view) {
		final ViewGroup.LayoutParams lp = view.getLayoutParams();
		final int originalHeight = view.getHeight();
		ValueAnimator animator;
		int animationTime = HeroStripActivity.this.getResources().getInteger(
                android.R.integer.config_mediumAnimTime);
		animator = ValueAnimator.ofInt(originalHeight, 1).setDuration(animationTime);
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                lp.height = (Integer) valueAnimator.getAnimatedValue();
                view.setLayoutParams(lp);
            }
        });
		animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
            	view.setVisibility(View.INVISIBLE);
            }
		});
		animator.start();
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary sections of the app.
	 * (ie. Hero Details, Item List, ...)<br/>
	 * <br/>
	 * Careful : getItem is called once at creation time but it is not called if you only recreate the activity
	 * (ie. a change in the screen orientation). So if you manage fragments outside of the PagerAdapter then you have to
	 * add a setRetainInstance(true) in each of your fragment.<br/>
	 * If you do not do that then the fragments will be reallocated and the pager will display blank screens.
	 * @see HeroFragment#HeroFragment()
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
			}
			return fragment;
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		@SuppressLint("DefaultLocale")
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0: return getString(R.string.title_sectionDetails).toUpperCase();
			case 1: return getString(R.string.title_sectionItems).toUpperCase();
//			case 2: return getString(R.string.title_sectionShoulders).toUpperCase();
			}
			return null;
		}
	}
}
