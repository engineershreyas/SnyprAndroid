package com.shrey.snypr;

import java.util.List;

import com.example.snypr.MainActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shrey.pojos.Score;
import com.shrey.snypr.Intro;
import com.shrey.snypr.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class SplashScreen extends Activity {
	Context ctx;
	ParseQuery<Score> scoreQuery = ParseQuery.getQuery("Score");
	static Score scoreObject;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		ctx = this;
		setContentView(R.layout.splash);
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			scoreQuery.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
			scoreQuery.findInBackground(new FindCallback<Score>(){

				@Override
				public void done(
						List<Score> objs,
						ParseException e) {
					// TODO Auto-generated method stub
					if(objs != null){
						for(int i = 0;i<objs.size();i++){
							//scoreObject = objs.get(i);
							
							objs.get(i).addScore(ParseUser.getCurrentUser().getInt("score"));
							objs.get(i).saveEventually();
							scoreObject = objs.get(i);
							
						}
						
					}
					else{
					
						scoreObject = new Score();
						scoreObject.addUsername(ParseUser.getCurrentUser().getUsername());
						scoreObject.addScore(ParseUser.getCurrentUser().getInt("score"));
						scoreObject.saveEventually();
					}
					
				}
				
			});
			
		  ctx.startActivity(new Intent(ctx,MainActivity.class));
		} else {
		  // show the signup or login screen
			ctx.startActivity(new Intent(ctx,Intro.class));
		}
		
	}
	
	public static Score getScoreObject(){
		return scoreObject;
	}
	
}
