package jodroid.util;

import jodroid.d3calc.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

public class EditTextActivity extends Activity {
	public static final String TAG = "EditText";
	public static final int EDITTEXT_REQUESTCODE = 3217;
	public static final String EDIT_TEXT_TITLE = "EditTextTitle";
	public static final String EDIT_TEXT_VALUE = "TextValue";
	
	public EditText editText;
	
	@Override
	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.edittext);
        
        Intent requestIntent = this.getIntent();
		Bundle b = requestIntent.getExtras();
        setTitle(b.getString(EDIT_TEXT_TITLE));
        
        getWindow().setFlags(
        		WindowManager.LayoutParams.FLAG_BLUR_BEHIND,  
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        
        editText = (EditText)findViewById(R.id.editTextValue);
        editText.setText(b.getString(EDIT_TEXT_VALUE));
	}
	
	public void btOkClick(View v) {
		String res = editText.getText().toString();
		if (res == null || res.isEmpty()) return;
		Intent resultIntent = new Intent();
		resultIntent.putExtra(EDIT_TEXT_VALUE, res);
		setResult(RESULT_OK, resultIntent);
		finish();
	}
	
	public void btCancelClick(View v) {
		setResult(RESULT_CANCELED);
		finish();
	}
}
