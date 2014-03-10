package com.shrey.snypr;



import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
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
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friendlistheader);
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
		
	}
	
	

	public static String searchedUser(){
		if(go){
		return username;
		}
		else{
			return null;
		}
	}
}
