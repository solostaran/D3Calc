package jodroid.d3calc;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

public class PreferenceSettings extends PreferenceActivity {
	
	public static final String PREF_ALWAYS_LOAD = "alwaysload";

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
        	.replace(android.R.id.content ,new ArmorySettingsPreferenceFragment())
        	.commit();
    }
	
	public static class ArmorySettingsPreferenceFragment extends PreferenceFragment {
		@Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.pref_settings);
        }
	}
}
