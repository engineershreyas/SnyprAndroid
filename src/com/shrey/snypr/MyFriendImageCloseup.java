package com.shrey.snypr;

import java.util.List;



import com.example.snypr.MainActivity;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.shrey.pojos.Friend;
import com.shrey.pojos.Photo;
import com.shrey.pojos.Score;

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

public class MyFriendImageCloseup extends Activity {
	ActionBar actionbar;
	ParseImageView p;
	ParseObject ph;
	Context ctx;
	TextView commentCount;
	Button b,un;
	Friend friend;
	ParseObject like = new ParseObject("Like");
	//ParseObject scoreObject = new ParseObject("Score");
	Score scoreObject;
	ParseQuery<ParseObject> query = ParseQuery.getQuery("Like");
	ParseQuery<ParseUser> query1 = ParseUser.getQuery();
	ParseQuery<Friend> query2 = ParseQuery.getQuery("Friend");
	ParseQuery<Score> scoreQuery = ParseQuery.getQuery("Score");
	boolean go;
	boolean done;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.icl);
		View view = this.getWindow().getDecorView();
        done = false;
		ctx = this;
		p = (ParseImageView)findViewById(R.id.snyp_preview_image1);
		ph = MyFriendSnyps.returnPhoto();
		if(ph == null){
			ctx.startActivity(new Intent(ctx,MainActivity.class));
		}
		
		b = (Button)findViewById(R.id.likeButton);
		un = (Button)findViewById(R.id.unlikeButton);
		un.setVisibility(View.INVISIBLE);
		commentCount = (TextView)findViewById(R.id.likeCount);	
		setLikeCount();
		p.setParseFile(ph.getParseFile("photo"));
		p.loadInBackground(new GetDataCallback(){

			@Override
			public void done(byte[] arg0, ParseException arg1) {
				if(arg1 == null){
					p.setVisibility(View.VISIBLE);
				}
				
			}
			
		});
		
		query.whereEqualTo("filename", ph.getParseFile("photo").getName());
		//query.whereEqualTo("likedBy", ParseUser.getCurrentUser().getUsername());
		query.findInBackground(new FindCallback<ParseObject>(){

			@Override
			public void done(List<ParseObject> objs, ParseException e) {
				if(objs!=null){
					for(int i = 0;i<objs.size();i++){
						if(objs.get(i).getString("likedBy").equals(ParseUser.getCurrentUser().getUsername())){
							un.setVisibility(View.VISIBLE);
							b.setVisibility(View.INVISIBLE);
						}
					}
				}
				
			}
			
		});
		
		
		
		
		b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				
				
				ph.increment("likes",1);
				ph.saveEventually();
				like.put("likedBy", ParseUser.getCurrentUser().getUsername());
				like.put("filename", ph.getParseFile("photo").getName());
				like.saveEventually();
				
					query2.whereEqualTo("friendname", ph.getString("username"));
					query2.findInBackground(new FindCallback<Friend>(){

						@Override
						public void done(List<Friend> objs, ParseException e) {
							// TODO Auto-generated method stub
							if(e == null){
								for(int i = 0; i <objs.size();i++){
									friend = objs.get(i);
									friend.increment("friendScore",1);
									friend.saveEventually();
									scoreQuery.whereEqualTo("username", friend.getString("friendname"));
									Log.d("for this p",friend.getString("friendname"));
									scoreQuery.findInBackground(new FindCallback<Score>(){

										@Override
										public void done(List<Score> scores,
												ParseException e) {
											// TODO Auto-generated method stub
											if(e == null){
											for(int i = 0; i<scores.size();i++){
												scoreObject = scores.get(i);
												scoreObject.addScore(friend.getInt("friendScore"));
												scoreObject.saveEventually();
											}
											}
											else{
												Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
											}
										}
										
									});
									
								}
							}
							
						}
						
					});
				
				
				
				go = true;
				
					
				
		
				
				Toast.makeText(ctx,"You liked this Snyp!",Toast.LENGTH_SHORT).show();
				refresh();
			}
		});
		
		un.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ph.increment("likes",-1);
				ph.saveEventually();
				query.findInBackground(new FindCallback<ParseObject>(){

					@Override
					public void done(List<ParseObject> objs, ParseException e) {
						if(objs!=null){
							for(int i = 0;i<objs.size();i++){
								if(objs.get(i).getString("filename").equals(ph.getParseFile("photo").getName()) &&
										objs.get(i).getString("likedBy").equals(ParseUser.getCurrentUser().getUsername())){
									
									try{objs.get(i).delete();
									}
									catch(ParseException error){
										Log.e("error",error.getMessage());
									}
									objs.get(i).saveEventually();
								}
							}
							done = true;
						}
						
					}
					
				});
				
				
					query2.whereEqualTo("friendname", ph.getString("username"));
					query2.findInBackground(new FindCallback<Friend>(){

						@Override
						public void done(List<Friend> objs, ParseException e) {
							// TODO Auto-generated method stub
							if(e == null){
								for(int i = 0; i <objs.size();i++){
									friend = objs.get(i);
									friend.increment("friendScore",-1);
									friend.saveEventually();
									scoreQuery.findInBackground(new FindCallback<Score>(){

										@Override
										public void done(List<Score> scores,
												ParseException e) {
											// TODO Auto-generated method stub
											if(e == null){
												for(int i = 0;i<scores.size();i++){
													scoreObject = scores.get(i);
													scoreObject.increment("scoreNumber",-1);
													scoreObject.saveEventually();
												}
											}
											else{
												Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
											}
											
										}
										
									});
									
									
								}
							}
							
						}
						
					});
				
				
				
				go = false;
				refresh();
				
			}
		});
		
		actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setTitle(ParseUser.getCurrentUser().getUsername()+ "'s picture");
		
	}
		

		
	
	
	public void setLikeCount(){
		if(String.valueOf(ph.getInt("likes"))!=null){
		commentCount.setText(String.valueOf(ph.getInt("likes")));
		}
		else{
			commentCount.setText("0");
		}
	}
	
	private void refresh(){
		Intent intent = getIntent();
	    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	    
	    finish();
	    
	    startActivity(intent);
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
