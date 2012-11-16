package jodroid.d3calc;

import jodroid.d3calc.fragments.ProfileDetailFragment;
import jodroid.d3obj.D3Obj;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

public class ProfileDetailActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	D3Obj.setContext(this);
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString(ProfileDetailFragment.ARG_PROFILE_ID,
                    getIntent().getStringExtra(ProfileDetailFragment.ARG_PROFILE_ID));
            ProfileDetailFragment fragment = new ProfileDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.profile_detail_container, fragment)
                    .commit();
        }
        
        this.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, ProfileListActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
