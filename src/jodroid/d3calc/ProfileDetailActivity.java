package jodroid.d3calc;

import jodroid.d3calc.fragments.ProfileDetailFragment;
import jodroid.d3obj.D3Obj;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class ProfileDetailActivity extends FragmentActivity {

	private String id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		D3Obj.setContext(this);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_detail);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		if (savedInstanceState == null) {
			Bundle arguments = new Bundle();
			arguments.putString(ProfileDetailFragment.ARG_PROFILE_ID,
					id = getIntent().getStringExtra(ProfileDetailFragment.ARG_PROFILE_ID));
			arguments.putBoolean(ProfileDetailFragment.ARG_FORCE_LOAD, false);
			ProfileDetailFragment fragment = new ProfileDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
				.add(R.id.profile_detail_container, fragment)
				.commit();
		}
		
		if (getIntent().getBooleanExtra(ProfileMenuActivity.ARG_BACK, false))
			this.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
		else
			this.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_profile_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(this, ProfileMenuActivity.class);
			intent.putExtra(ProfileMenuActivity.ARG_BACK, true);
			NavUtils.navigateUpTo(this, intent);
			return true;
		case R.id.menu_reload_profile:
			Log.i(this.getClass().getSimpleName(), "Reload ...");
			Bundle arguments = new Bundle();
			arguments.putString(ProfileDetailFragment.ARG_PROFILE_ID, id);
			arguments.putBoolean(ProfileDetailFragment.ARG_FORCE_LOAD, true);
			ProfileDetailFragment fragment = new ProfileDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
				.replace(R.id.profile_detail_container, fragment)
				.commit();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
