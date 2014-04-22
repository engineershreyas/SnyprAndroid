package com.shrey.snypr;

import java.util.List;

import com.example.snypr.MainActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shrey.pojos.Friend;
import com.shrey.util.GMailSender;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FriendPage extends Activity {
	public static ParseUser u;
	Context ctx;
	TextView n;
	Button f,v,unf;
	Friend friend;
	String friendEmail;
	int score;
	ParseQuery<ParseObject> query = ParseQuery.getQuery("Friend");
	ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Photo");
	ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
	ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
	ActionBar actionbar;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend_page);
		View view = this.getWindow().getDecorView();
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
      
		n = (TextView)findViewById(R.id.name);
		f = (Button)findViewById(R.id.Add);
		f.setEnabled(true);
		unf = (Button)findViewById(R.id.unfollow);
		unf.setEnabled(false);
		v = (Button)findViewById(R.id.View);
		u = ShowFriends.getUser();
		ctx = this;
		
		if(u == null){
			ctx.startActivity(new Intent(ctx,MainActivity.class));
		}
		else{
			  pushQuery.whereEqualTo("username", u.getUsername());
		actionbar = getActionBar();
		
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setTitle(u.getUsername());
		///actionbar.setBackgroundDrawable(new ColorDrawable(Color.BLUE));
		query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
		query.whereEqualTo("friendname", u.getUsername());
		if(u.getUsername().equals(ParseUser.getCurrentUser().getUsername())){
			ctx.startActivity(new Intent(ctx,MainActivity.class));
		}
		
		else{
			n.setText("Score: 0");
		query.findInBackground(new FindCallback<ParseObject>(){

			@Override
			public void done(List<ParseObject> objs, ParseException e) {
				// TODO Auto-generated method stub
				if(objs!=null){
					//f.setEnabled(false);
					for(int i = 0; i<objs.size();i++){
						if((objs.get(i).getString("friendname").equals(u.getUsername()) && 
								objs.get(i).getString("username").equals(ParseUser.getCurrentUser().getUsername())) ){
							f.setText("Following");
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
						int x = 0;
						x+=objs.get(i).getInt("likes");
						score = x;
						n.setText("Score: "+String.valueOf(score));
						//u.put("score", score);
						//u.saveEventually();
					}
				}
				else{
					score = 0;
					n.setText("Score: "+String.valueOf(score));
					//u.put("score", score);
				}
			}
			
		});
		
		}
		}
		
		f.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				friend = new Friend();
				friend.addFriendName(u.getUsername());
				friend.addUserName(ParseUser.getCurrentUser().getUsername());
				friend.saveEventually();
				friendEmail = u.getEmail();
				
				new send(friendEmail).execute();
				
				
				
				
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
						u.put("score",score);
					}
				}
				else{
					score = 0;
					n.setText("Score: "+String.valueOf(score));
					u.put("score", score);
				}
			}
			
		});
		
	}
	
	public static ParseUser getUserAgain(){
		return u;
		
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
	
	private class send extends AsyncTask<Object,Void,Boolean>{
		String email;
		send(String emailString){
			email = emailString;
		}
		
		@Override
		protected Boolean doInBackground(Object... arg0) {
			// TODO Auto-generated method stub
			try {   
                GMailSender sender = new GMailSender("snyprapp@gmail.com", "shreyas1");
                sender.sendMail("Someone followed you",   
                        ParseUser.getCurrentUser().getUsername() + " followed you!",
                        "snyprapp@gmail.com", email   
                        );
                Log.d("email sent to",email);
                return true;
            } catch (Exception en) {   
                Log.e("SendMail failure", en.getMessage());  
                return false;
            }
			
			
		}
		
	}
	
	
}
