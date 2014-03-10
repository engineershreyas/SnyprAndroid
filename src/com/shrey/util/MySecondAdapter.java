package com.shrey.util;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.shrey.pojos.Friend;
import com.shrey.pojos.Photo;
import com.shrey.snypr.R;

public class MySecondAdapter extends ParseQueryAdapter<Friend> {

	public MySecondAdapter(Context context
			) {
		super(context, new ParseQueryAdapter.QueryFactory<Friend>(){

			@Override
			public ParseQuery<Friend> create() {
				ParseQuery<Friend> query = ParseQuery.getQuery("Friend");
				query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
				return query;
			}
			
		});
	}
		
		
		public View getItemView(Friend friend, View v, ViewGroup parent){
			if(v == null){
				v = View.inflate(getContext(), R.layout.adapter_item2, null);
			}
			
			super.getItemView(friend, v, parent);
			
			TextView txtView = (TextView)v.findViewById(R.id.viewUserName);
			String friendname = friend.getString("friendname");
			txtView.setText(friendname);
			Log.d("name",friendname);
			
			
			
			return v;
		}
	
	
	
	

}
