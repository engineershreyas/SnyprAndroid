package com.shrey.util;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.content.Context;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.FindCallback;
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
import com.squareup.picasso.Picasso;

public class PicassoAdapter extends BaseAdapter {
	
	private Context myContext;
	LayoutInflater inflater;
	ParseFile p;
	String url;
	List<String> s = new ArrayList<String>();
	List<ParseFile> mpl;
	
	static class ViewHolder{
		ImageView i;
	}
	
	public PicassoAdapter(Context c,List<ParseFile> pl){
		this.myContext = c;
		inflater = LayoutInflater.from(this.myContext);
		this.mpl = pl;
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.mpl.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return this.mpl.get(position);
	}

	@Override
	public long getItemId(int id) {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {
		final ViewHolder viewHolder;
		p = (ParseFile)getItem(position);
		// TODO Auto-generated method stub
		if(v == null){
			v = inflater.inflate(R.layout.adapter_item3, null);
			viewHolder = new ViewHolder();
			viewHolder.i = (ImageView)v.findViewById(R.id.snyp_image_new);
			v.setTag(viewHolder);
		}
		else{
			viewHolder = (ViewHolder)v.getTag();
		}
		
		
		
		/*ParseQuery<ParseObject> query = ParseQuery.getQuery("Photo");
		query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
		query.findInBackground(new FindCallback<ParseObject>(){

			@Override
			public void done(List<ParseObject> objs, ParseException e) {
				// TODO Auto-generated method stub
				if(objs!=null){
					for(int i = 0; i < objs.size();i++){
						p = objs.get(i).getParseFile("photo");
						url = p.getUrl();
						s.add(url);
						
					}
				}
				
			}
			
		});
		
		for(int x = 0;x<s.size();x++){
			Picasso.with(myContext).load(s.get(x)).into(viewHolder.i);
		}
		*/
		Picasso.with(myContext).load(p.getUrl()).into(viewHolder.i);
		
		
		return v;
	}

}
