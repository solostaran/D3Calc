package jodroid.d3calc.fragments;

import jodroid.d3calc.ProfileListContent;
import jodroid.d3calc.R;
import jodroid.d3calc.adapters.ProfileListAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class ProfileListFragment extends ListFragment {

	//	private static final boolean DEVELOPER_MODE = true;
	
	private static boolean mResult;


//	private static final String TAG = ProfileListFragment.class.getName();

	private static final String STATE_ACTIVATED_POSITION = "activated_position";

	private Callbacks mCallbacks = sDummyCallbacks;
	private int mActivatedPosition = ListView.INVALID_POSITION;

	public interface Callbacks {

		public void onItemSelected(String id);
	}

	private static Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onItemSelected(String id) {
		}
	};

	public ProfileListFragment() {
	}

	// Not Working because container is null (issue : http://code.google.com/p/android/issues/detail?id=22564)
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		Log.i(TAG, "onCreateView:"+container);
//		if (container == null) {
//			return null;
//		}
//		View v = inflater.inflate(R.layout.activity_profile_listfrag, null, false);
//		final GestureDetector gesture = new GestureDetector(getActivity(),
//				new GestureDetector.SimpleOnGestureListener() {
//
//			@Override
//			public boolean onDown(MotionEvent e) {
//				return true;
//			}
//
//			@Override
//			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//				Log.i(TAG, "onFling has been called!");
//
//				final int SWIPE_MIN_DISTANCE = 120;
//				final int SWIPE_MAX_OFF_PATH = 250;
//				final int SWIPE_THRESHOLD_VELOCITY = 200;
//				try {
//					if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
//						return false;
//					if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
//							&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//						Log.i(TAG, "Right to Left");
//					} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
//							&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//						Log.i(TAG, "Left to Right ");
//						// titles.showDetails(getPosition() - 1);
//					}
//				} catch (Exception e) {
//					// nothing
//				}
//				return super.onFling(e1, e2, velocityX, velocityY);
//			}
//		});
//
//		v.setOnTouchListener(new View.OnTouchListener() {
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				return gesture.onTouchEvent(event);
//			}
//		});
//
//		return v;
//	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		// le detectNetworks() + build sont à activer si opérations réseau synchrones
		// notamment quand je n'utilisais pas le AsyncHttpClient.
		//    	if (DEVELOPER_MODE) {
		//	         StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		//	                 .detectDiskReads()
		//	                 .detectDiskWrites()
		//	                 .detectNetwork()   // or .detectAll() for all detectable problems
		//	                 .penaltyLog()
		//	                 .build());
		//	         StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
		//	                 .detectLeakedSqlLiteObjects()
		//	                 .detectLeakedClosableObjects()
		//	                 .penaltyLog()
		//	                 .penaltyDeath()
		//	                 .build());
		//	     }

		super.onCreate(savedInstanceState);
		//        setListAdapter(new ArrayAdapter<DummyContent.DummyItem>(getActivity(),
		ProfileListContent.adapter = new ProfileListAdapter(getActivity(),
				R.layout.profile_list_item,
				ProfileListContent.ITEMS);
		setListAdapter(ProfileListContent.adapter);	
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (savedInstanceState != null && savedInstanceState
				.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
		}
		
		// On long click : delete the selected profile
		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> paramAdapterView, View paramView, int position, long paramLong) {

				boolean b = getYesNoWithExecutionStop(
						getString(R.string.deletion_title),
						getString(R.string.deletion_message)+"\n"+ProfileListContent.ITEMS.get(position).toString(),
						getActivity());
				if (b) {
//							Toast.makeText(getActivity(),
//									ProfileListContent.ITEMS.get(position).toString()+" "+getText(R.string.deletion_ok),
//									Toast.LENGTH_LONG).show();
					ProfileListContent.removeItem(ProfileListContent.ITEMS.get(position));
					ProfileListContent.adapter.notifyDataSetChanged();
//					((ProfileListActivity)getActivity()).refreshList();

				} else {
					Toast.makeText(getActivity(), getText(R.string.deletion_canceled), Toast.LENGTH_LONG).show();
				}


				return true;
			}

		});
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException("Activity must implement fragment's callbacks.");
		}

		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = sDummyCallbacks;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);
		mCallbacks.onItemSelected(ProfileListContent.ITEMS.get(position).id);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != ListView.INVALID_POSITION) {
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}

	public void setActivateOnItemClick(boolean activateOnItemClick) {
		getListView().setChoiceMode(activateOnItemClick ? ListView.CHOICE_MODE_SINGLE : ListView.CHOICE_MODE_NONE);
	}

	public void setActivatedPosition(int position) {
		if (position == ListView.INVALID_POSITION) {
			getListView().setItemChecked(mActivatedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}

		mActivatedPosition = position;
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
	        @Override
			public void onClick(DialogInterface dialog, int whichButton) {
	            mResult = true;
	            handler.sendMessage(handler.obtainMessage());
	        }
	    });
	    alert.setNegativeButton(context.getText(R.string.cancel), new DialogInterface.OnClickListener() {
	        @Override
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

}
