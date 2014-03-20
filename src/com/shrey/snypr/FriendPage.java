package com.shrey.snypr;

import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shrey.pojos.Friend;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FriendPage extends Activity {
	public static ParseUser u;
	Context ctx;
	TextView n;
	Button f,v;
	Friend friend;
	int score;
	ParseQuery<ParseObject> query = ParseQuery.getQuery("Friend");
	ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Photo");
	ActionBar actionbar;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend_page);
		View view = this.getWindow().getDecorView();
        
		n = (TextView)findViewById(R.id.name);
		f = (Button)findViewById(R.id.Add);
		f.setEnabled(true);
		v = (Button)findViewById(R.id.View);
		u = ShowFriends.getUser();
		ctx = this;
		if(u == null){
			ctx.startActivity(new Intent(ctx,FriendSearch.class));
		}
		else{
		
		actionbar = getActionBar();
		
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setTitle(u.getUsername());
		actionbar.setBackgroundDrawable(new ColorDrawable(Color.BLUE));
		query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
		query.whereEqualTo("friendname", u.getUsername());
		
		
		
		query.findInBackground(new FindCallback<ParseObject>(){

			@Override
			public void done(List<ParseObject> objs, ParseException e) {
				// TODO Auto-generated method stub
				if(objs!=null){
					//f.setEnabled(false);
					for(int i = 0; i<objs.size();i++){
						if(objs.get(i).getString("friendname").equals(u.getUsername()) && 
								objs.get(i).getString("username").equals(ParseUser.getCurrentUser().getUsername())){
							f.setEnabled(false);
							Log.d("username",objs.get(i).getString("friendname"));
							
							
						}
						
					}
				}
					
				
			}
			
		});
		
		query1.whereEqualTo("username", u.getUsername());
		query1.findInBackground(new FindCallback<ParseObject>(){

			@Override
			public void done(List<ParseObject> objs, ParseException e) {
				// TODO Auto-generated method stub
				if(objs!=null){
					for(int i = 0;i<objs.size();i++){
						score+=objs.get(i).getInt("likes");
						n.setText("Score: "+String.valueOf(score));
					}
				}
				else{
					score = 0;
					n.setText("Score: "+String.valueOf(score));
				}
			}
			
		});
		
		}
		
		
		f.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				friend = new Friend();
				friend.addFriendName(u.getUsername());
				friend.addUserName(ParseUser.getCurrentUser().getUsername());
				friend.saveEventually();
				Toast.makeText(ctx, "You are now following " + u.getUsername(), Toast.LENGTH_SHORT).show();
				refresh();
			}
		});
		
		v.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ctx.startActivity(new Intent(ctx,FriendSnyps.class));
			}
		});
		
	}
	
	
	public void onRestart(){
		super.onRestart();
		score = 0;
		query1.whereEqualTo("username", u.getUsername());
		query1.findInBackground(new FindCallback<ParseObject>(){

			@Override
			public void done(List<ParseObject> objs, ParseException e) {
				// TODO Auto-generated method stub
				if(objs!=null){
					for(int i = 0;i<objs.size();i++){
						score+=objs.get(i).getInt("likes");
						n.setText("Score: "+String.valueOf(score));
					}
				}
				else{
					score = 0;
					n.setText("Score: "+String.valueOf(score));
				}
			}
			
		});
		
	}
	
	public static ParseUser getUserAgain(){
		return u;
		
	}
	
	private void refresh(){
		Intent intent = getIntent();
	    //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	    
	    finish();
	    
	    startActivity(intent);
	}

}
