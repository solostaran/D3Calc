package jodroid.d3calc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.swipedismiss.SwipeListViewTouchListener;

public class ProfileMenuActivity extends FragmentActivity implements OnItemClickListener {
	
	private static boolean mResult;
	
	/**
	 * Large screen management (true = Large Screen and a two pane layout).
	 */
	private boolean mTwoPane;

	/**
	 * Provide context to the ProfileListContent so the database can preload profiles.<br/>
	 * Layout selection by screenLayout properties between large screens (tablet) or other screens.<br/>
	 * Screen orientation depends on layout.<br/>
	 * Provide an adapter and listeners to the ListView.
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	ProfileListContent.setContext(this); // before fragment allocation
    	
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_profiles);

        // layout depends on large screen or not
        // another way to determine this would be : findViewById(R.id.profile_detail_container) != null
        // but it does not work in my case
        if ((this.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE) {
        	mTwoPane = true;
        	setContentView(R.layout.activity_profiles_twopane);
        	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        else {
        	setContentView(R.layout.activity_profiles);
        	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        
        this.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        
        // ListView management (adapter, swipe and click listeners)
        ListView listView = (ListView)findViewById(R.id.profileListView);
        if (mTwoPane) listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        
        ProfileListContent.adapter = new ProfileListAdapter(this,
				R.layout.profile_list_item,
				ProfileListContent.ITEMS);
        listView.setAdapter(ProfileListContent.adapter);
        listView.setOnItemClickListener(this);
        
        // Create a ListView-specific touch listener. ListViews are given special treatment because
        // by default they handle touches for their list items... i.e. they're in charge of drawing
        // the pressed state (the list selector), handling list item clicks, etc.
        SwipeListViewTouchListener touchListener =
                new SwipeListViewTouchListener(
                		listView,
                        new SwipeListViewTouchListener.OnSwipeCallback() {
							@Override
							public void onSwipeLeft(ListView listView, int [] reverseSortedPositions) {
//								Log.i(this.getClass().getName(), "swipe left : pos="+reverseSortedPositions[0]);
								for (int pos : reverseSortedPositions ) {
									removeItem(pos);
								}
                                ProfileListContent.adapter.notifyDataSetChanged();
							}

							@Override
							public void onSwipeRight(ListView listView, int [] reverseSortedPositions) {
//								Log.i(ProfileMenuActivity.class.getClass().getName(), "swipe right : pos="+reverseSortedPositions[0]);
								onItemClick(null, null, reverseSortedPositions[0], 0);
							}
                        },
                        true,
                        false);
        listView.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        listView.setOnScrollListener(touchListener.makeScrollListener());
        
        getActionBar().setTitle("D3Calc YODA");
        getActionBar().setSubtitle("Yet anOther Diablo App");
        
    }
    
    /**
     * Button "New Profile" starts a "new player profile input" activity with an intent.
     * @param v
     */
    public void onClickNewProfile(View v) {
    	Intent requestIntent = new Intent(this, jodroid.d3calc.NewProfileActivity.class);
    	startActivityForResult(requestIntent, jodroid.d3calc.NewProfileActivity.NEWPROFILE_REQUESTCODE);
    }
    
    /**
     * This callback for the startActivityForResult above will insert a new player profile in the list and in the database.<br/>
     * It uses the data provided by the NewProfileActivity.
     * @param requestCode identifies the request code provided by the startActivityForResult
     * @param resultCode result ok or not
     * @param data contains the response datas 
     * @see NewProfileActivity#onClickValidate(View)
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch (requestCode) {
    	case NewProfileActivity.NEWPROFILE_REQUESTCODE:
    		if (resultCode == RESULT_OK) {
    			ProfileListContent.ProfileItem item = new ProfileListContent.ProfileItem(null,
    					data.getStringExtra(NewProfileActivity.BATTLEHOST_VALUE),
    					data.getStringExtra(NewProfileActivity.BATTLENAME_VALUE),
    					data.getStringExtra(NewProfileActivity.BATTLETAG_VALUE));
    			ProfileListContent.insertProfile(item);
    			ProfileListContent.adapter.notifyDataSetChanged();
//    			refreshList(); // would work but too much code and the notifyDataSetChanged is sufficent
    		} else {
//    			Log.w(this.getClass().getName(), "response cancel");
    		}
    		break;
    	}
    }
    
    /**
	 * Modal style dialog box.<br/>
	 * Not the best way to provide user interaction in Android, but an interesting fact.<br/>
	 * Found it here : <a href="http://stackoverflow.com/questions/2028697/dialogs-alertdialogs-how-to-block-execution-while-dialog-is-up-net-style">Dialogs / AlertDialogs: How to “block execution” while dialog is up (.NET-style)</a>
	 * @param title Dialog box's title
	 * @param message Dialog box's main text
	 * @param context context need for display (generally getActivity())
	 * @return true = yes/ok, false = no/nok
	 * @see 
	 */
	public static boolean getYesNoWithExecutionStop(String title, String message, Context context) {
	    // make a handler that throws a runtime exception when a message is received
	    final Handler handler = new Handler() {
	        @Override
	        public void handleMessage(Message mesg) {
	            throw new RuntimeException();
	        } 
	    };

	    // make a text input dialog and show it
	    AlertDialog.Builder alert = new AlertDialog.Builder(context);
	    alert.setTitle(title);
	    alert.setMessage(message);
	    alert.setPositiveButton(context.getText(R.string.delete), new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	            mResult = true;
	            handler.sendMessage(handler.obtainMessage());
	        }
	    });
	    alert.setNegativeButton(context.getText(R.string.cancel), new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	            mResult = false;
	            handler.sendMessage(handler.obtainMessage());
	        }
	    });
	    alert.show();

	    // loop till a runtime exception is triggered.
	    try { Looper.loop(); }
	    catch(RuntimeException e2) {}

	    return mResult;
	}
	
	/**
	 * Modal box for item removal.
	 * @param position
	 */
	public void removeItem(int position) {
		boolean b = getYesNoWithExecutionStop(
				getText(R.string.deletion_title).toString(),
				getText(R.string.deletion_message).toString()+"\n"+ProfileListContent.ITEMS.get(position).toString(),
				this);
		if (b) {
			Toast.makeText(this,
					ProfileListContent.ITEMS.get(position).toString()+" "+getText(R.string.deletion_ok),
					Toast.LENGTH_LONG).show();
			ProfileListContent.removeItem(ProfileListContent.ITEMS.get(position));
			ProfileListContent.adapter.notifyDataSetChanged();

		} else {
			Toast.makeText(this, getText(R.string.deletion_canceled), Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * As a profile is selected, displays the profile details.<br/>
	 * On large screen, set the fragment view, on small screen call a new activity. 
	 * @see OnItemClickListener
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
		String itemId = ProfileListContent.ITEMS.get(position).id;
		if (mTwoPane) {
			ListView lv = (ListView)findViewById(R.id.profileListView);
			lv.setItemChecked(position, true);
			Bundle arguments = new Bundle();
            arguments.putString(ProfileDetailFragment.ARG_ITEM_ID, itemId);
            ProfileDetailFragment fragment = new ProfileDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.profile_detail_container, fragment)
                    .commit();

        } else {
            Intent detailIntent = new Intent(this, ProfileDetailActivity.class);
            detailIntent.putExtra(ProfileDetailFragment.ARG_ITEM_ID, itemId);
            startActivity(detailIntent);
        }
	}
}
