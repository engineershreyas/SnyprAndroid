package com.shrey.snypr;

import java.util.List;

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
import com.shrey.util.MyAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

public class MyFriendSnyps extends Activity {

	public static ParseObject ph;
	Context ctx;
	Adapter adapter;
	ParseQuery<ParseObject> query = ParseQuery.getQuery("Photo");
	ListView listview;
	ParseFile file;
	ParseObject friend;
	static boolean go;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_snyps);
		ph = null;
		ctx = this;
		//p = (ParseImageView)findViewById(R.id.snyp_preview_image);
		//p.setVisibility(View.GONE);
		friend = MyFriendPage.getFriendAgain();
		adapter = new Adapter(this);
		query.whereEqualTo("username", friend.getString("friendname"));
		
		
		
		listview = (ListView)findViewById(R.id.imageList);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position,
					long id) {
				// TODO Auto-generated method stub
				
				query.findInBackground(new FindCallback<ParseObject>(){

					@Override
					public void done(List<ParseObject> objs, ParseException e) {
						// TODO Auto-generated method stub
						if(e == null){
							ph = objs.get(position);
							file = ph.getParseFile("photo");
							if(file != null){
							Log.d("File name",file.getName());
							go = true;
							}
							ctx.startActivity(new Intent(ctx,MyFriendImageCloseup.class));
						}
						
					}
					
				});
				
			}
			
			
			
		});
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		/*query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
		query.findInBackground( new FindCallback<ParseObject>(){

			@Override
			public void done(List<ParseObject> objs, ParseException e) {
				// TODO Auto-generated method stub
				if(e == null){
					ph = objs.get(0);
					Log.d("uh",ph.getParseFile("photo").getName());
					file = ph.getParseFile("photo");
					go = true;
					p.setParseFile(file);
					p.loadInBackground(new GetDataCallback(){

						@Override
						public void done(byte[] arg0, ParseException arg1) {
							// TODO Auto-generated method stub
							if(arg1 == null){
								p.setVisibility(View.VISIBLE);
								p.setOnClickListener(new View.OnClickListener() {
									
									@Override
									public void onClick(View v) {
										ctx.startActivity(new Intent(ctx,ImageCloseup.class));
										
									}
								});
							}
						}
						
					});
				}
				
			}
			
		});
		
	
		
		
		
		
		
		
		
		
		
		
		
		*/
		}
		
	
		
	public static  ParseObject returnPhoto(){
		if(go){
		return ph;
		}
		else{
			return null;
		}
		
	}
	
	private class Adapter extends ParseQueryAdapter<Photo>{

		public Adapter(Context context) {
			super(context, new ParseQueryAdapter.QueryFactory<Photo>() {

				@Override
				public ParseQuery<Photo> create() {
					ParseQuery<Photo> query = ParseQuery.getQuery("Photo");
					query.whereEqualTo("username", friend.getString("friendname"));
					
					return query;
				}
				
				
			});
			// TODO Auto-generated constructor stub
		}
		
		public View getItemView(Photo photo, View v, ViewGroup parent){
			if(v == null){
				v = View.inflate(getContext(), R.layout.adapter_item, null);
				
			}
			super.getItemView(photo, v, parent);
			
			ParseImageView imageView = (ParseImageView)v.findViewById(R.id.snyp_image);
			ParseFile file = photo.getParseFile("photo");
			if(file!=null){
				imageView.setParseFile(file);
				imageView.loadInBackground(new GetDataCallback(){

					@Override
					public void done(byte[] arg0, ParseException arg1) {
						// TODO Auto-generated method stub
						if(arg1 == null){
							Log.d("success","file got");
						}
					}
					
				});
			}
			
			
			
			
			return v;
		}
		
	}
	
	
	}
	
