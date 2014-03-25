package com.shrey.snypr;



import com.example.snypr.MainActivity;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class FriendSearch extends Activity{
	Context ctx;
	View hvw;
	EditText searchField;
	ListView l;
	static boolean go;
	public static String username;
	Button searchB;
	ParseQueryAdapter<ParseUser> adapter;
	ActionBar actionbar;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friendlistheader);
		View view = this.getWindow().getDecorView();
      
		ctx = this;
		go = false;
		
		
		
		searchField = (EditText)findViewById(R.id.searchBar);
		searchB = (Button)findViewById(R.id.searchButton);
		
		
		
		searchB.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				username = searchField.getText().toString();
				go = true;
				ctx.startActivity(new Intent(ctx,ShowFriends.class));
				
				
				
			}
		});
		
		actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setTitle("Search for a friend");
		actionbar.setBackgroundDrawable(new ColorDrawable(Color.BLUE));
		
	}
	
	

	public static String searchedUser(){
		if(go){
		return username;
		}
		else{
			return null;
		}
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
