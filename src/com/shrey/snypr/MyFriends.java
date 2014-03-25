package com.shrey.snypr;

import java.util.List;

import com.example.snypr.MainActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseQueryAdapter.QueryFactory;
import com.parse.ParseUser;
import com.shrey.pojos.Friend;
import com.shrey.util.MySecondAdapter;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MyFriends extends Activity {
	QueryFactory<Friend> qf;
	ListView l;
	ParseUser u;
	public static ParseObject friend;
	Context ctx;
	ParseQuery<ParseObject> query = ParseQuery.getQuery("Friend");
	MySecondAdapter adapter1;
	ActionBar actionbar;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend_search);
		actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setTitle("Your friends");
		actionbar.setBackgroundDrawable(new ColorDrawable(Color.BLUE));
		ctx = this;
		qf = new ParseQueryAdapter.QueryFactory<Friend>() {

			@Override
			public ParseQuery<Friend> create() {
				ParseQuery<Friend> query = ParseQuery.getQuery("Friend");
				query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
				query.addAscendingOrder("friendname");
				
				return query;
			}
			
		};
	
		
		adapter1 = new MySecondAdapter(this);
		
		
		l = (ListView)findViewById(R.id.friendList);
		l.setAdapter(adapter1);
		l.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int position,
					long arg3) {
				// TODO Auto-generated method stub
				
				query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
                query.findInBackground(new FindCallback<ParseObject>(){

					@Override
					public void done(List<ParseObject> friends, ParseException arg1) {
						// TODO Auto-generated method stub
						
							friend = friends.get(position);
						
					}
                	
                });
                ctx.startActivity(new Intent(ctx,MyFriendPage.class));
			}
		});
		
		
		
	}
	
	private class Adapter extends ParseQueryAdapter<Friend>{

		public Adapter(Context context,
				com.parse.ParseQueryAdapter.QueryFactory<Friend> queryFactory) {
			super(context, queryFactory);
			// TODO Auto-generated constructor stub
			
			
		}
		
		public View getItemView(Friend friend,View v,ViewGroup parent){
			if(v ==null){
				v = View.inflate(getContext(), R.layout.adapter_item2, null);
			}
			
			super.getItemView(friend, v, parent);
			
			TextView txtView = (TextView)v.findViewById(R.id.viewUserName);
			
			
			
			
			
			
			return v;
			
		}
		

		
	}
	
	public static ParseObject getFriend(){
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
	private void refresh(){
		Intent intent = getIntent();
	    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	    
	    finish();
	    
	    startActivity(intent);
	}
}




