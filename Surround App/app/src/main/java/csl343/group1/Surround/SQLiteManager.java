/**
 * Author: Aditya Abhas
 * Entry No: 2012csb1002
 *
 * This class stores the user's information in the device's SQLite database.
 * User information is retrieved from the database instead of requesting the server.
 * */

package csl343.group1.Surround;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;



public class SQLiteManager extends SQLiteOpenHelper {

	private static final String logTAG = SQLiteManager.class.getSimpleName();
	private static final int DB_VERSION = 1;
	private static final String DB_NAME = "android_api";

	// Login table name
	private static final String LOGIN_TABLE = "login";

	// Name of the columns
	private static final String id = "id";
	private static final String username = "name";
	private static final String email = "email";
	private static final String uid = "uid";
	private static final String created_at = "created_at";

	public SQLiteManager(Context sqliteContext) {
		super(sqliteContext, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL("CREATE TABLE " + LOGIN_TABLE + "("
                + id + " INTEGER PRIMARY KEY," + username + " TEXT,"
                + email + " TEXT UNIQUE," + uid + " TEXT,"
                + created_at + " TEXT" + ")");

		Log.d(logTAG, "Database tables created");
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int o, int n) {
		database.execSQL("DROP TABLE IF EXISTS " + LOGIN_TABLE);
		onCreate(database);
	}

	 // Insert user information in SQLite database
	public void addUser(String name, String email, String uid, String created_at) {
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(username, name);
		contentValues.put(SQLiteManager.email, email);
		contentValues.put(SQLiteManager.uid, uid);
		contentValues.put(SQLiteManager.created_at, created_at);

		long id = database.insert(LOGIN_TABLE, null, contentValues);
		database.close();

		Log.d(logTAG, "New user insertion into sqlite database is successful: " + id);
	}


	// Retrieve user information from SQLite database
	public HashMap<String, String> getUserDetails() {
		HashMap<String, String> userData = new HashMap<String, String>();
		String selectQuery = "SELECT  * FROM " + LOGIN_TABLE;

		SQLiteDatabase database = this.getReadableDatabase();
		Cursor cursor = database.rawQuery(selectQuery, null);

		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			userData.put("name", cursor.getString(1));
			userData.put("email", cursor.getString(2));
			userData.put("uid", cursor.getString(3));
			userData.put("created_at", cursor.getString(4));
		}
		cursor.close();
		database.close();

		Log.d(logTAG, "Retrieving user data from Sqlite: " + userData.toString());

		return userData;
	}


	// Check user's login status
	public int getRowCount() {
		SQLiteDatabase database = this.getReadableDatabase();
		Cursor cursor = database.rawQuery("SELECT  * FROM " + LOGIN_TABLE, null);
		int numRows = cursor.getCount();
		database.close();
		cursor.close();
		return numRows;
	}

	// Delete all user tables
	public void deleteUsers() {
		SQLiteDatabase database = this.getWritableDatabase();
		database.delete(LOGIN_TABLE, null, null);
		database.close();
        Log.d(logTAG, "Deleted all users data from sqlite database");
	}

}
