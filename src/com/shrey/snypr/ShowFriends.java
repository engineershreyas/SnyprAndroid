package com.shrey.snypr;

import java.util.List;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.shrey.pojos.Photo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ShowFriends extends Activity {
	ListView l;
	ParseQueryAdapter.QueryFactory<ParseUser> qf;
	String username;
	Context ctx;
	Adapter adapter;
	ParseQuery<ParseUser> query = ParseUser.getQuery();
	public static ParseUser u;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend_search);
		l = (ListView)findViewById(R.id.friendList);
		ctx = this;
		username = FriendSearch.searchedUser();
		if(username == null){
			ctx.startActivity(new Intent(ctx,FriendSearch.class));
		}
		else{
			Log.d("search",username);
		}
		
		qf = new ParseQueryAdapter.QueryFactory<ParseUser>() {

			@Override
			public ParseQuery<ParseUser> create() {
				// TODO Auto-generated method stub
				ParseQuery query = ParseUser.getQuery();
				query.whereStartsWith("username", username);
				
				return query;
			}
			
		
			
		};
		
		adapter = new Adapter(ctx,qf);
		
		
		l.setAdapter(adapter);
		
		l.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int position,
					long arg3) {
				query.whereStartsWith("username", username);
				query.findInBackground(new FindCallback<ParseUser>(){

					@Override
					public void done(List<ParseUser> users, ParseException arg1) {
						
						if(users!=null){
						u = users.get(position);
						ctx.startActivity(new Intent(ctx,FriendPage.class));
						Log.d("username",u.getUsername());
						}
						else{
							Toast.makeText(ctx, "Nothing here!", Toast.LENGTH_SHORT).show();
						}
						
					}
					
				});
				
			}
			
			
		});
	}
		
		
	private class Adapter extends ParseQueryAdapter<ParseUser>{

		public Adapter(Context context,
				com.parse.ParseQueryAdapter.QueryFactory<ParseUser> queryFactory) {
			super(context, queryFactory);
			// TODO Auto-generated constructor stub
			
			
		}
		
		public View getItemView(ParseUser user,View v,ViewGroup parent){
			if(v ==null){
				v = View.inflate(getContext(), R.layout.adapter_item2, null);
			}
			
			super.getItemView(user, v, parent);
			
			TextView txtView = (TextView)v.findViewById(R.id.viewUserName);
			txtView.setText(user.getUsername());
			
			
			
			
			
			return v;
			
		}
		
	}

	public static ParseUser getUser(){
		return u;
	}

	}

