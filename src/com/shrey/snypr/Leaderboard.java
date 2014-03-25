package com.shrey.snypr;

import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class Leaderboard extends Activity {
	View v;
	ListView l;
	RadioGroup listChoice;
	RadioButton rb1,rb2;
	Adapter adapter;
	SecondAdapter adapter2;
	Context ctx;
	boolean go;
	ActionBar actionbar;
	int score;
	int s;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.leaderboard);
		View view = this.getWindow().getDecorView();
		actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setTitle("Leaderboard (all users)");
		actionbar.setBackgroundDrawable(new ColorDrawable(Color.BLUE));
		ctx = this;
		adapter = new Adapter(ctx);
		adapter2 = new SecondAdapter(ctx);
		
		l = (ListView)findViewById(R.id.scoreList);
		v = View.inflate(getApplicationContext(), R.layout.leaderboard_header, null);
		rb1 = (RadioButton)v.findViewById(R.id.allBoard);
		rb2 = (RadioButton)v.findViewById(R.id.friendBoard);
		rb1.setTextColor(Color.BLACK);
		rb2.setTextColor(Color.BLACK);
		listChoice =(RadioGroup)v.findViewById(R.id.radioGroup1);
		l.addHeaderView(v);
		l.setAdapter(adapter);
		
		listChoice.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(RadioGroup r, int which) {
				// TODO Auto-generated method stub
				switch(which){
				case R.id.allBoard:
					Intent intent = getIntent();
				    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				    
				    finish();
				    
				    startActivity(intent);
					go = false;
					break;
				case R.id.friendBoard:
					actionbar.setTitle("Leaderboard(friends)");
					l.setAdapter(adapter2);
					break;
				
				}
				
			}
			
		});
	}
	
	private class Adapter extends ParseQueryAdapter<Score>{
		
		public Adapter(Context context) {
			super(context, new ParseQueryAdapter.QueryFactory<Score>() {

				@Override
				public ParseQuery<Score> create() {
					final ParseQuery<Score> query1 = ParseQuery.getQuery("Score");
					//ParseQuery<ParseUser> query = ParseUser.getQuery();
					//query.addDescendingOrder("score");
					query1.addDescendingOrder("scoreNumber");
					
					
					
					return query1;
				}
				
				
			});
			// TODO Auto-generated constructor stub
		}
		
		public View getItemView(final Score score, View v, ViewGroup parent){
			s=0;
			if(v == null){
				v = View.inflate(getApplicationContext(), R.layout.adapter_item2, null);
			}
			
			super.getItemView(score,v,parent);
			
			//ParseQuery<Photo> photoQuery = ParseQuery.getQuery("Photo");
			final TextView t = (TextView)v.findViewById(R.id.viewUserName);
			
			
			
			t.setText("Username: " + score.getUsername() + "\nScore: " +String.valueOf(score.getScore()));
			t.setTextColor(Color.BLACK);
			
			return v;
		}
		
		
	}
	
	private class SecondAdapter extends ParseQueryAdapter<Friend>{

		public SecondAdapter(Context context
				) {
			super(context, new ParseQueryAdapter.QueryFactory<Friend>() {

				@Override
				public ParseQuery<Friend> create() {
					// TODO Auto-generated method stub
					ParseQuery query = ParseQuery.getQuery("Friend");
					query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
					query.addDescendingOrder("friendScore");
					return query;
				}
				
			});
			// TODO Auto-generated constructor stub
		}
		
		public View getItemView(final Friend friend, View v, ViewGroup parent){
			int s = 0;
			if(v == null){
				v = View.inflate(getApplicationContext(), R.layout.adapter_item2, null);
			}
			
			super.getItemView(friend,v,parent);
			
			TextView t = (TextView)v.findViewById(R.id.viewUserName);
			ParseQuery<ParseObject> query = ParseQuery.getQuery("Score");
			query.findInBackground(new FindCallback<ParseObject>(){

				@Override
				public void done(List<ParseObject> objects, ParseException arg1) {
					// TODO Auto-generated method stub
					for(int i = 0;i<objects.size();i++){
						if(objects.get(i).getString("username").equals(friend.getString("friendname"))){
							friend.put("friendScore", objects.get(i).getInt("scoreNumber"));
							friend.saveEventually();
						}
					}
				}

				
				
			});
			t.setText("Username: " + friend.getString("friendname") + "\nScore: " +String.valueOf( friend.getInt("friendScore")));
			t.setTextColor(Color.BLACK);
			
			return v;
		}
		
	}

}
