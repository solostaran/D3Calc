package jodroid.d3calc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * This is a special activity that provide a profile player input and send it as result.
 * @author JRD
 * @see ProfileListActivity#onClickNewProfile(View)
 * @see ProfileListActivity#onActivityResult(int,int,Intent)
 * @TODO fill menu or clean up menu
 */
public class NewProfileActivity extends Activity {
	
	public static final int NEWPROFILE_REQUESTCODE = 3218;
	
	private EditText editName;
	private EditText editTag;
	private Spinner spinHost;
	
	public static final String BATTLEHOST_VALUE = "battlehost";
	public static final String BATTLENAME_VALUE = "battlename";
	public static final String BATTLETAG_VALUE = "battleTag";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        getWindow().setFlags(
        		WindowManager.LayoutParams.FLAG_DIM_BEHIND,  
                WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        
        spinHost = (Spinner) findViewById(R.id.spinHosts);
        ArrayAdapter<CharSequence> adapter =
        		ArrayAdapter.createFromResource(this, R.array.battlehosts, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinHost.setAdapter(adapter);
        
        editName = (EditText)findViewById(R.id.editName);
        editTag = (EditText)findViewById(R.id.editTag);
        
        this.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.activity_new_profile, menu);
//        return true;
//    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//            	setResult(RESULT_CANCELED); // no use, the onActivityResult method won't be called
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    /**
     * Verify inputs and send them as activity result.
     * @param v
     */
    public void onClickValidate(View v) {
    	
    	String host = spinHost.getSelectedItem().toString();
    	
    	String name = editName.getText().toString();
    	if (name == null || name.isEmpty()) {
    		Toast t = Toast.makeText(this, "You must provide a Name", Toast.LENGTH_SHORT);
    		t.show();
    		return;
    	}
    	
    	String tag = editTag.getText().toString();
    	if (tag == null || tag.isEmpty() || tag.length() != 4) {
    		Toast t = Toast.makeText(this, "You must provide a 4 digit tag", Toast.LENGTH_SHORT);
    		t.show();
    		return;
    	}
//    	String res = editText.getText().toString();
//		if (res == null || res.isEmpty()) return;		
//		resultIntent.putExtra(EDIT_TEXT_VALUE, res);
    	Intent resultIntent = new Intent();
    	resultIntent.putExtra(BATTLEHOST_VALUE, host);
    	resultIntent.putExtra(BATTLENAME_VALUE, name);
    	resultIntent.putExtra(BATTLETAG_VALUE, tag);
		setResult(RESULT_OK, resultIntent);
		finish();
    }

}
