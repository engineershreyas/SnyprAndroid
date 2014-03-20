package com.shrey.snypr;

import java.util.List;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseImageView;
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

public class FriendImageCloseup extends Activity {
	ActionBar actionbar;
	ParseImageView p;
	Context ctx;
	ParseObject ph;
	TextView commentCount;
	ParseUser u;
	Button b,un;
	Friend friend;
	ParseObject like = new ParseObject("Like");
	ParseQuery<ParseObject> query = ParseQuery.getQuery("Like");
	ParseQuery<ParseUser> query1 = ParseUser.getQuery();
	ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Friend");
	boolean go;
	boolean done;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.icl);
		View view = this.getWindow().getDecorView();
        u = FriendSnyps.getUserThirdTime();
		ctx = this;
		done = false;
		p = (ParseImageView)findViewById(R.id.snyp_preview_image1);
		p.setVisibility(View.GONE);
		
		ph = FriendSnyps.returnFriendPhoto();
		
		
		
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
				done = true;
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
								if(objs.get(i).getString("likedBy").equals(ParseUser.getCurrentUser().getUsername())){
									
									try{objs.get(i).delete();
									}
									catch(ParseException error){
										Log.e("error",error.getMessage());
									}
									objs.get(i).saveEventually();
								}
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
		actionbar.setBackgroundDrawable(new ColorDrawable(Color.BLUE));
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
	    //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	    
	    finish();
	    
	    startActivity(intent);
	}
	

}
