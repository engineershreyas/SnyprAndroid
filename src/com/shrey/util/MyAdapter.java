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
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import com.shrey.pojos.Photo;
import com.shrey.snypr.R;

public class MyAdapter extends ParseQueryAdapter<Photo> {

	static class ViewHolder{
		ParseImageView p;
	}
	
	public MyAdapter(Context context
			) {
		super(context, new ParseQueryAdapter.QueryFactory<Photo>() {

			@Override
			public ParseQuery<Photo> create() {
				// TODO Auto-generated method stub
				ParseQuery<Photo> query = new ParseQuery<Photo>("Photo");
				
				query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
				query.addDescendingOrder("createdAt");
				
				return query;
			}
			
		});
		
		
		
	}
	
	@Override
	public View getItemView(Photo photo, View v, ViewGroup parent){
		ViewHolder viewHolder = new ViewHolder();
		if(v ==null){
			v = View.inflate(getContext(), R.layout.adapter_item, null);
			v.setTag(viewHolder);
			
			//ParseImageView imageView = (ParseImageView)v.findViewById(R.id.snyp_image);
			
		}
		
		super.getItemView(photo, v, parent);
		viewHolder.p = (ParseImageView)v.findViewById(R.id.snyp_image);
		ParseFile file = photo.getParseFile("photo");
		if(file!=null){
			viewHolder.p.setParseFile(file);
			viewHolder.p.loadInBackground(new GetDataCallback(){

				@Override
				public void done(byte[] arg0, ParseException e) {
					// TODO Auto-generated method stub
					if(e == null){
					Log.d("success","file got");
					}
				}
				
			});
		}
		
		return v;
	}

}
