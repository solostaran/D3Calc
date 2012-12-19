package jodroid.d3calc;

import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

public class PreferenceSettings extends PreferenceActivity {
	
	public static final String PREF_LOAD_ON_DEMAND = "ondemandload";
	public static final String PREF_LOAD_IF_OLD = "loadifold";
	public static final String PREF_OLDER_THAN = "olderthan";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getFragmentManager().beginTransaction()
        	.replace(android.R.id.content ,new ArmorySettingsPreferenceFragment())
        	.commit();
        
        this.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
    }
	
	public static class ArmorySettingsPreferenceFragment extends PreferenceFragment implements OnPreferenceChangeListener {
		
		EditTextPreference olderthan = null;
		
		@Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.pref_settings);
            
            olderthan = (EditTextPreference)findPreference(PREF_OLDER_THAN);
            olderthan.setOnPreferenceChangeListener(this);
            olderthan.setSummary(getString(R.string.older_than_1)+" "+olderthan.getText()+" "+getString(R.string.older_than_2));
       }

		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			if (preference == olderthan) {
				if (newValue == null || ((String)newValue).isEmpty() || Integer.parseInt((String)newValue) == 0) return false;
				olderthan.setSummary(getString(R.string.older_than_1)+" "+(String)newValue+" "+getString(R.string.older_than_2));
			}
			return true;
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent profileIntent = new Intent(this, ProfileMenuActivity.class);
			NavUtils.navigateUpTo(this, profileIntent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public static boolean forceload(Context context, boolean currentval) {
		if (currentval) return true;
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		if (!sharedPref.getBoolean(PreferenceSettings.PREF_LOAD_ON_DEMAND, true)) return true;
		return currentval;
	}
	
	public static boolean loadold(Context context, boolean currentval, long lastupdated) {
		if (forceload(context, currentval)) return true;
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		if (sharedPref.getBoolean(PREF_LOAD_IF_OLD, false)){
			long days = Long.parseLong(sharedPref.getString(PREF_OLDER_THAN, "10"));
			long today = (new Date().getTime())/1000; // value in seconds
			long diff = (today - lastupdated)/86400; // difference in days
			if (diff >= days) return true;
		}
		return currentval;
	}
}
