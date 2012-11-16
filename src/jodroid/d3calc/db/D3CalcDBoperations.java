package jodroid.d3calc.db;

import jodroid.d3calc.ProfileListContent;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class D3CalcDBoperations {
	
	private D3CalcDBHelper d3calcDatabase;
	private SQLiteDatabase db;
	
	private static final int DB_VERSION = 1;
	private static final String DB_NAME = "d3calc.db";
	
	public D3CalcDBoperations(Context context) {
		d3calcDatabase = new D3CalcDBHelper(context, DB_NAME, null, DB_VERSION);
	}
	
	/**
	 * open gateway (read/write DB access)
	 */
	public void open() {
		if (db == null || !db.isOpen()) db = d3calcDatabase.getWritableDatabase();
	}
	
	/**
	 * close gateway
	 */
	public void close() {
		if (db != null && db.isOpen()) db.close();
	}
	
	public boolean isOpen() {
		return (db == null ? false : db.isOpen());
	}
	
	/**
	 * in case the DB reference is needed.
	 * @return
	 */
	public SQLiteDatabase getDB() {
		return db;
	}
	
	public Cursor getProfiles() {
		return db.query(D3CalcDBHelper.TABLE_PROFILE,
				new String[] {D3CalcDBHelper.PROFILE_ID, D3CalcDBHelper.PROFILE_HOST, D3CalcDBHelper.PROFILE_NAME, D3CalcDBHelper.PROFILE_TAG},
				null, null, null, null, null, null);
	}
	
	/**
	 * SQL insertion of a Player Profile.
	 * @param profile player profile (host,name,tag)
	 * @see ProfileListContent.ProfileItem
	 */
	public void insertProfile(ProfileListContent.ProfileItem profile) {
		// Hashmap like object
		ContentValues values = new ContentValues();
		// values definition
		values.put(D3CalcDBHelper.PROFILE_HOST, profile.battlehost);
		values.put(D3CalcDBHelper.PROFILE_NAME, profile.battlename);
		values.put(D3CalcDBHelper.PROFILE_TAG, profile.battletag);
		long ret = db.insert(D3CalcDBHelper.TABLE_PROFILE, null, values);
		profile.id = ""+ret;
	}
	
	public int removeProfile(ProfileListContent.ProfileItem profile) {
		return db.delete(D3CalcDBHelper.TABLE_PROFILE, D3CalcDBHelper.PROFILE_ID + " = "+profile.id, null);
	}

}
