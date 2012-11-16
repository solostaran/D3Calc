package jodroid.d3calc.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class D3CalcDBHelper extends SQLiteOpenHelper {
	
	public static final String TABLE_PROFILE = "tabProfile";
	
	public static final String PROFILE_ID = "id";
	public static final String PROFILE_HOST = "host";
	public static final String PROFILE_NAME = "name";
	public static final String PROFILE_TAG = "tag";
	
	private static final String CREATE_TABLE_PROFILE = "CREATE TABLE "+TABLE_PROFILE+" (" +
			PROFILE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+ PROFILE_HOST + " TEXT NOT NULL, " +
			PROFILE_NAME+" TEXT NOT NULL, "+PROFILE_TAG+" TEXT NOT NULL);";
	
	public D3CalcDBHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_PROFILE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
