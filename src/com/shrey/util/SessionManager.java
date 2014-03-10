package com.shrey.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionManager {
	private static final String PREF_NAME = "com.example.snipeshot";

	private static final String SESSION_TOKEN = "sessionToken";

	private static final String SESSION_TOKEN_EXPIRES = "sessionTokenExpires";
	
	private static final String USER_NAME = "userName";

	public static void createLoginSession(Context ctx, String sessionToken,
			String sessionTokenExpires, String userName) {
		SharedPreferences pref = ctx.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		Editor editor = pref.edit();

		editor.putString(SESSION_TOKEN, sessionToken);

		editor.putString(SESSION_TOKEN_EXPIRES, sessionTokenExpires);
		
		editor.putString(USER_NAME, userName);

		// commit changes
		editor.commit();
		Log.d("SessionManager.createLoginSession", "token: "+pref.getString(SESSION_TOKEN, null)+", expires: "+pref.getString(SESSION_TOKEN_EXPIRES, null));
	}
	
	public static boolean isLoggedIn(Context ctx) {
		SharedPreferences pref = ctx.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		Log.d("SessionManager.isLoggedIn", "token: "+pref.getString(SESSION_TOKEN, null)+", expires: "+pref.getString(SESSION_TOKEN_EXPIRES, null));
		boolean loggedIn = false;
		String token = pref.getString(SESSION_TOKEN, null);
		if (token != null) {
			String expires = pref.getString(SESSION_TOKEN_EXPIRES, null);
			if (expires != null) {
				try {
					SimpleDateFormat format = new SimpleDateFormat(
							"yyyy-MM-dd'T'HH:mm:ss.SSSZZZZ",
							Locale.getDefault());
					Date expiresDate = format.parse(expires);
					if (new Date().before(expiresDate)) {
						loggedIn = true;
					}
				} catch (ParseException e) {
					Log.e("SessionManager.isLoggedIn", "Could not parse date",
							e);
				}
			}
		}
		return loggedIn;
	}

	// clear values. should be called on successful logout
	public static void logout(Context ctx) {
		SharedPreferences pref = ctx.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.remove(SESSION_TOKEN);
		editor.remove(SESSION_TOKEN_EXPIRES);
		editor.commit();
		Log.d("SessionManager.logout", "token: "+pref.getString(SESSION_TOKEN, null)+", expires: "+pref.getString(SESSION_TOKEN_EXPIRES, null));
	}
	
	public static String getSessionToken(Context ctx){
		SharedPreferences pref = ctx.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		return pref.getString(SESSION_TOKEN, null);
	}
	
	public static String getUserName(Context ctx){
		SharedPreferences pref = ctx.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		return pref.getString(USER_NAME, null);
	}
}
