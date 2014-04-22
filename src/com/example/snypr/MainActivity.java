package com.example.snypr;

import java.util.List;



import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseImageView;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import com.shrey.pojos.Photo;
import com.shrey.pojos.Score;
import com.shrey.pojos.Storage;
import com.shrey.pojos.User;
import com.shrey.snypr.FriendSearch;
import com.shrey.snypr.GoToCamera;
import com.shrey.snypr.GoToCameraTwo;
import com.shrey.snypr.GoToFriends;
import com.shrey.snypr.Intro;
import com.shrey.snypr.Leaderboard;
import com.shrey.snypr.MyFriends;
import com.shrey.snypr.SplashScreen;

import com.shrey.snypr.R;
import com.shrey.snypr.Register;
import com.shrey.snypr.SignIn;
import com.shrey.snypr.SnypMap;
import com.shrey.snypr.ViewSnyps;




import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	ActionBar actionbar;
	Button s,l,v,f,mf,sm,lb;
	public static Context ctx;
	TextView scoreT;
	User u;
	Register r;
	String scoreInfo;
	int x;
	ParseImageView profile;
	private Photo photo;
	ParseQuery<ParseObject> query = ParseQuery.getQuery("Photo");
	ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Friend");
	ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
	ParseQuery<Score> scoreQuery = ParseQuery.getQuery("Score");
	Score scoreObject;
	boolean go;
	ParseInstallation installation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ctx = this;
        if(ParseUser.getCurrentUser() == null){
        	ctx.startActivity(new Intent(ctx,Intro.class));
        	Toast.makeText(ctx, "sorry, session expired!", Toast.LENGTH_SHORT).show();
        }
         View view = this.getWindow().getDecorView();
         scoreObject = SplashScreen.getScoreObject();
        go = false;
        
        u = Storage.getInstance().user;
		s = (Button)findViewById(R.id.snipe);
		
		l = (Button)findViewById(R.id.lo);
		
		v = (Button)findViewById(R.id.vw);
		
		f = (Button)findViewById(R.id.button0);
		
		
		
		sm = (Button)findViewById(R.id.mapButton);
		
	
		lb = (Button)findViewById(R.id.lead);
		
		
		Toast.makeText(ctx, "Welcome " + ParseUser.getCurrentUser().getUsername(), Toast.LENGTH_SHORT).show();
		scoreT = (TextView)findViewById(R.id.fp);
	
		query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
		query.findInBackground(new FindCallback<ParseObject>(){

			@Override
			public void done(final List<ParseObject> photos, ParseException arg1) {
				// TODO Auto-generated method stub
				if(photos!=null){
				 x = 0;
				for(int i = 0; i<photos.size();i++){
					
					int s = photos.get(i).getInt("likes");
					Log.d("likes",String.valueOf(s));
					x+=s;
					Log.d("total score",String.valueOf(x));
					
					
					
					
				}
				//Log.d("score",String.valueOf(x));
				ParseUser.getCurrentUser().put("score", x);
				ParseUser.getCurrentUser().saveEventually();
				
				scoreInfo = String.valueOf(ParseUser.getCurrentUser().getInt("score"));
				Log.d("scoreInfo",scoreInfo);
				scoreT.setText("Your Score: " + scoreInfo);
				
				}
				else{
					ParseUser.getCurrentUser().put("score", 0);
					ParseUser.getCurrentUser().saveEventually();
					
					scoreInfo = String.valueOf(ParseUser.getCurrentUser().getInt("score"));
					Log.d("scoreInfo",scoreInfo);
					scoreT.setText("Your Score: " + scoreInfo);
					
				}
				
			}
			
		});
		
		
		
		//go = true;

		
		
		
			//Log.d("mission","failed");
			//Toast.makeText(ctx, "mission failed", Toast.LENGTH_SHORT).show();
		
		
		scoreT.setTextColor(Color.RED);
		s.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ctx.startActivity(new Intent(ctx,GoToCamera.class));
				
			}
		});
		
		l.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ParseUser.logOut();
				ParseUser currentUser = ParseUser.getCurrentUser();
				ctx.startActivity(new Intent(ctx,Intro.class));
			}
		});
		
		v.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ctx.startActivity(new Intent(ctx,ViewSnyps.class));
			}
		});
		
		
		sm.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ctx.startActivity(new Intent(ctx,SnypMap.class));
			}
		});
		
		lb.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ctx.startActivity(new Intent(ctx,Leaderboard.class));
			}
		});
		
		f.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ctx.startActivity(new Intent(ctx,GoToFriends.class));
			}
		});
		
		actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setTitle("Snypr");
		//actionbar.setBackgroundDrawable(new ColorDrawable(Color.BLUE));
		
		
    }
    
    public void onRestart(){
    	super.onRestart();
    	query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
		query.findInBackground(new FindCallback<ParseObject>(){

			@Override
			public void done(List<ParseObject> objs, ParseException arg1) {
				// TODO Auto-generated method stub
				if(objs!=null){
				int x = 0;
				for(int i = 0; i<objs.size();i++){
					int s = objs.get(i).getInt("likes");
					Log.d("likes",String.valueOf(s));
					x+=s;
					Log.d("total score",String.valueOf(x));
					go = true;
					
					
				}
				//Log.d("score",String.valueOf(x));
				ParseUser.getCurrentUser().put("score", x);
				ParseUser.getCurrentUser().saveEventually();
				scoreInfo = String.valueOf(ParseUser.getCurrentUser().getInt("score"));
				Log.d("scoreInfo",scoreInfo);
				scoreT.setText("Your Score: " + scoreInfo);
				
				}
				else{
					ParseUser.getCurrentUser().put("score", 0);
					ParseUser.getCurrentUser().saveEventually();
					scoreInfo = String.valueOf(ParseUser.getCurrentUser().getInt("score"));
					Log.d("scoreInfo",scoreInfo);
					scoreT.setText("Your Score: " + scoreInfo);
					go = true;
				}
				
			}
			
		});
		

		
		
    	
		scoreT.setTextColor(Color.RED);
    	
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public Photo currentPhoto(){
    	
    	return photo;
    	
    }
    
}
