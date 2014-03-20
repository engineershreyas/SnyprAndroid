package com.shrey.snypr;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.shrey.pojos.Friend;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
	
	private class Adapter extends ParseQueryAdapter<ParseUser>{

		public Adapter(Context context) {
			super(context, new ParseQueryAdapter.QueryFactory<ParseUser>() {

				@Override
				public ParseQuery<ParseUser> create() {
					ParseQuery query1 = ParseQuery.getQuery("Friend");
					ParseQuery query = ParseUser.getQuery();
					query.addDescendingOrder("score");
					
					return query;
				}
				
				
			});
			// TODO Auto-generated constructor stub
		}
		
		public View getItemView(ParseUser user, View v, ViewGroup parent){
			if(v == null){
				v = View.inflate(getApplicationContext(), R.layout.adapter_item2, null);
			}
			
			super.getItemView(user,v,parent);
			
			TextView t = (TextView)v.findViewById(R.id.viewUserName);
			t.setText("Username: " + user.getUsername() + "\nScore: " +String.valueOf( user.getInt("score")));
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
		
		public View getItemView(Friend friend, View v, ViewGroup parent){
			if(v == null){
				v = View.inflate(getApplicationContext(), R.layout.adapter_item2, null);
			}
			
			super.getItemView(friend,v,parent);
			
			TextView t = (TextView)v.findViewById(R.id.viewUserName);
			t.setText("Username: " + friend.getString("friendname") + "\nScore: " +String.valueOf( friend.getInt("friendScore")));
			t.setTextColor(Color.BLACK);
			
			return v;
		}
		
	}

}
