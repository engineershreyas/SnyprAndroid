package com.shrey.snypr;

import com.example.snypr.MainActivity;
import com.parse.ParseUser;
import com.shrey.snypr.Intro;
import com.shrey.snypr.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class SplashScreen extends Activity {
	Context ctx;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		ctx = this;
		setContentView(R.layout.splash);
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
		  ctx.startActivity(new Intent(ctx,MainActivity.class));
		} else {
		  // show the signup or login screen
			ctx.startActivity(new Intent(ctx,Intro.class));
		}
		
	}
	
}
