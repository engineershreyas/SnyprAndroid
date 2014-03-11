package com.example.snypr;

import java.util.List;


import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import com.shrey.pojos.Photo;
import com.shrey.pojos.Storage;
import com.shrey.pojos.User;
import com.shrey.snypr.FriendSearch;
import com.shrey.snypr.GoToCamera;
import com.shrey.snypr.Intro;
import com.shrey.snypr.MyFriends;
import com.shrey.snypr.Picture;
import com.shrey.snypr.R;
import com.shrey.snypr.Register;
import com.shrey.snypr.SignIn;
import com.shrey.snypr.SnypMap;
import com.shrey.snypr.ViewSnyps;




import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	Button s,l,v,f,mf,sm;
	public static Context ctx;
	TextView scoreT;
	User u;
	Register r;
	String scoreInfo;
	
	private Photo photo;
	ParseQuery<ParseObject> query = ParseQuery.getQuery("Photo");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         
        
        ctx = this;
        u = Storage.getInstance().user;
		s = (Button)findViewById(R.id.snipe);
		l = (Button)findViewById(R.id.lo);
		v = (Button)findViewById(R.id.vw);
		f = (Button)findViewById(R.id.button1);
		mf = (Button)findViewById(R.id.button2);
		sm = (Button)findViewById(R.id.mapButton);
		
		Toast.makeText(ctx, "Welcome " + ParseUser.getCurrentUser().getUsername(), Toast.LENGTH_SHORT).show();
		scoreT = (TextView)findViewById(R.id.textView1);
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
					
					
					
				}
				//Log.d("score",String.valueOf(x));
				ParseUser.getCurrentUser().put("score", x);
				ParseUser.getCurrentUser().saveEventually();
				
				}
				else{
					ParseUser.getCurrentUser().put("score", 0);
					ParseUser.getCurrentUser().saveEventually();
				}
				
			}
			
		});
		scoreInfo = String.valueOf(ParseUser.getCurrentUser().getInt("score"));
		Log.d("score",scoreInfo);
		scoreT.setText("Your Score: " + scoreInfo);
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
		
		f.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ctx.startActivity(new Intent(ctx,FriendSearch.class));
			}
		});
		
		mf.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ctx.startActivity(new Intent(ctx,MyFriends.class));
			}
		});
		
		sm.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ctx.startActivity(new Intent(ctx,SnypMap.class));
			}
		});
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
