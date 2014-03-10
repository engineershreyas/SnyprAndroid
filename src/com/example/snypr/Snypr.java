package com.example.snypr;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;
import com.shrey.pojos.Friend;
import com.shrey.pojos.Photo;
import com.shrey.pojos.SnypPoint;
import com.shrey.pojos.User;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class Snypr extends Application {
	private static SharedPreferences preferences;
	 private static final String KEY_SEARCH_DISTANCE = "searchDistance";
	 public static final boolean APPDEBUG = false;
	  
	  // Debugging tag for the application
	  public static final String APPTAG = "Snypr";
	public void onCreate(){
		super.onCreate();
		ParseObject.registerSubclass(User.class);
		ParseObject.registerSubclass(Photo.class);
		ParseObject.registerSubclass(Friend.class);
		ParseObject.registerSubclass(SnypPoint.class);
		Parse.initialize(this, "nFyMh0pa2lxBJrgJNTEzRhPEar4DNSjLDEEgua7Y", "XeSrJIOg5mlMbO8zdytP9Z5jNHYLSG0SDhn4zu77");
		preferences = getSharedPreferences("com.example.snypr", Context.MODE_PRIVATE);
	}
	
	 public static float getSearchDistance() {
		    return preferences.getFloat(KEY_SEARCH_DISTANCE, 250);
		  }

		  public static void setSearchDistance(float value) {
		    preferences.edit().putFloat(KEY_SEARCH_DISTANCE, value).commit();
		  }

	
	
}
