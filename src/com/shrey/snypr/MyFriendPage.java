package com.shrey.snypr;

import java.util.List;

import com.example.snypr.MainActivity;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseImageView;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MyFriendPage extends Activity {
	public static ParseObject friend;
	TextView n;
	Context ctx;
	Button f,v,unf;
	
	ActionBar actionbar;
	ParseUser friendUser;
	Photo topPhoto;
	int friendscore;
	ParseQuery<Photo> query = ParseQuery.getQuery("Photo");
	ParseQuery<Photo> query1 = ParseQuery.getQuery("Photo");
	ParseImageView top;
public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend_page);
		View view = this.getWindow().getDecorView();
        ctx = this;
        
		friend = MyFriends.getFriend();
		top = (ParseImageView)findViewById(R.id.top_image);

				if(friend == null){
			ctx.startActivity(new Intent(ctx,MainActivity.class));
		}else{
		Log.d("friendname in my friend page",friend.getString("friendname"));
		n = (TextView)findViewById(R.id.name);
		actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setTitle(friend.getString("friendname"));
		//actionbar.setBackgroundDrawable(new ColorDrawable(Color.BLUE));
		ctx = this;
		
		query1.whereEqualTo("username",friend.getString("friendname"));
		query1.addDescendingOrder("likes");
		try {
			topPhoto = query1.getFirst();
			top.setParseFile(topPhoto.getParseFile("photo"));
			top.loadInBackground(new GetDataCallback(){

				@Override
				public void done(byte[] data, ParseException arg1) {
					// TODO Auto-generated method stub
					
				}
				
			});
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
		}
		
		top.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(ctx, "This is " + friend.getString("friendname") + "'s top score with " + topPhoto.getInt("likes") + " likes!", Toast.LENGTH_SHORT).show();
			}
		});
		
		query.whereEqualTo("username",friend.getString("friendname"));
		
		query.findInBackground(new FindCallback<Photo>(){

			@Override
			public void done(List<Photo> objs, ParseException e) {
				// TODO Auto-generated method stub
				if(objs!=null){
					for(int i = 0;i<objs.size();i++){
						friendscore+= objs.get(i).getInt("likes");
						Log.d("score of friend",String.valueOf(friendscore));
						n.setText(friend.getString("friendname") + "\nScore: " + String.valueOf(friendscore));
						friend.put("friendScore", friendscore);
						friend.saveEventually();
					}
				}
				else{
					friendscore = 0;
					Log.d("score of friend","is zero");
					n.setText("Score: " + String.valueOf(friendscore));
					friend.put("friendScore", friendscore);
					friend.saveEventually();
				}
				
			}
			
		});
		
		
		
		
		
		f = (Button)findViewById(R.id.Add);
		v = (Button)findViewById(R.id.View);
		unf = (Button)findViewById(R.id.unfollow);
		unf.setVisibility(View.VISIBLE);
		f.setEnabled(false);
		f.setVisibility(View.INVISIBLE);
		
		
		
		unf.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(friend!=null){
	                try {
						friend.delete();
						//friend.saveEventually();
						Toast.makeText(ctx, "Unfollowed!", Toast.LENGTH_SHORT).show();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
					}
	                }
				ctx.startActivity(new Intent(ctx,MyFriends.class));
			}
		});
		
v.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ctx.startActivity(new Intent(ctx,MyFriendSnyps.class));
			}
		});
		}
		
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
						n.setText(friend.getString("friendname") + "\nScore: " + String.valueOf(friendscore));
						friend.put("friendScore", friendscore);
						friend.saveEventually();
					}
				}
				else{
					friendscore = 0;
					Log.d("score of friend","is zero");
					n.setText("Score: " + String.valueOf(friendscore));
					friend.put("friendScore", friendscore);
					friend.saveEventually();
				}
				
			}
			
		});
		
	}
	
	
	public static ParseObject getFriendAgain(){
		return friend;
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    getMenuInflater().inflate(R.menu.main, menu);
	     super.onCreateOptionsMenu(menu);
	     
	     return true;
	}

	
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        
	        case R.id.action_gohome:
	        	ctx.startActivity(new Intent(ctx, MainActivity.class));
	            return true;
	       
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

}
