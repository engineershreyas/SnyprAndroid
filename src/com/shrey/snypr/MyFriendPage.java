package com.shrey.snypr;

import java.util.List;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shrey.pojos.Friend;
import com.shrey.pojos.Photo;

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

public class MyFriendPage extends Activity {
	public static ParseObject friend;
	TextView n;
	Context ctx;
	Button f,v;
	ActionBar actionbar;
	ParseUser friendUser;
	int friendscore;
	ParseQuery<Photo> query = ParseQuery.getQuery("Photo");
public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend_page);
		View view = this.getWindow().getDecorView();
        
		friend = MyFriends.getFriend();
		Log.d("friendname in my friend page",friend.getString("friendname"));
		n = (TextView)findViewById(R.id.name);
		actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setTitle(friend.getString("friendname"));
		actionbar.setBackgroundDrawable(new ColorDrawable(Color.BLUE));
		ctx = this;
		query.whereEqualTo("username",friend.getString("friendname"));
		
		query.findInBackground(new FindCallback<Photo>(){

			@Override
			public void done(List<Photo> objs, ParseException e) {
				// TODO Auto-generated method stub
				if(objs!=null){
					for(int i = 0;i<objs.size();i++){
						friendscore+= objs.get(i).getInt("likes");
						Log.d("score of friend",String.valueOf(friendscore));
						n.setText("Score: " + String.valueOf(friendscore));
					}
				}
				else{
					friendscore = 0;
					Log.d("score of friend","is zero");
					n.setText("Score: " + String.valueOf(friendscore));
				}
				
			}
			
		});
		
		
		
		
		
		f = (Button)findViewById(R.id.Add);
		v = (Button)findViewById(R.id.View);
		f.setEnabled(false);
		f.setVisibility(View.INVISIBLE);
		
		if(friend == null){
			ctx.startActivity(new Intent(ctx,MyFriends.class));
		}
		
v.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ctx.startActivity(new Intent(ctx,MyFriendSnyps.class));
			}
		});
		
		
	}

	
	public void onRestart(){
		super.onRestart();
		friendscore = 0;
query.whereEqualTo("username",friend.getString("friendname"));
		
		query.findInBackground(new FindCallback<Photo>(){

			@Override
			public void done(List<Photo> objs, ParseException e) {
				// TODO Auto-generated method stub
				if(objs!=null){
					for(int i = 0;i<objs.size();i++){
						friendscore+= objs.get(i).getInt("likes");
						Log.d("score of friend",String.valueOf(friendscore));
						n.setText("Score: " + String.valueOf(friendscore));
					}
				}
				else{
					friendscore = 0;
					Log.d("score of friend","is zero");
					n.setText("Score: " + String.valueOf(friendscore));
				}
				
			}
			
		});
		
	}
	
	
	public static ParseObject getFriendAgain(){
		return friend;
	}

}
