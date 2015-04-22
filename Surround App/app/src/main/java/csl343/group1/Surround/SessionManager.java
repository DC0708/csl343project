/**
 * Author: Aditya Abhas
 * Entry No: 2012csb1002
 *
 * This class stores session information for the app using the SharedPreferences.
 * Boolean flag isLoggedIn stored in shared preferences is used to check the login status.
 * */

package csl343.group1.Surround;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionManager {

	private static String logTAG = SessionManager.class.getSimpleName();
	SharedPreferences preferences;
	Editor prefEditor;
	Context prefContext;

	// Shared preferences mode
	int PRIVATE_MODE = 0;

	// Shared preferences file name
	private static final String PREF_NAME = "SurroundLogin";
	private static final String IS_LOGGED_IN = "isLoggedIn";

	public SessionManager(Context currentContext) {
		this.prefContext = currentContext;
		preferences = prefContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		prefEditor = preferences.edit();
	}

	public void setLoginStatus(boolean isLoggedIn) {
		prefEditor.putBoolean(IS_LOGGED_IN, isLoggedIn);
		prefEditor.commit();
		Log.d(logTAG, "User login session changed!");
	}
	
	public boolean isLoggedIn(){
		return preferences.getBoolean(IS_LOGGED_IN, false);
	}
}
