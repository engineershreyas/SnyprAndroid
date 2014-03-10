package com.shrey.snypr;

import java.util.ArrayList;

import java.util.List;



import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseQueryAdapter.QueryFactory;
import com.parse.ParseUser;
import com.shrey.pojos.Photo;
import com.shrey.util.MyAdapter;
import com.shrey.util.PagerContainer;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ListView;
import android.widget.TextView;


public class ViewSnyps extends Activity{
	ParseImageView p;
	ParseQuery<ParseObject> query = ParseQuery.getQuery("Photo");
	MyAdapter adapter;
	ParseObject[] o;
	List<ParseFile> pf = new ArrayList<ParseFile>();
	ParseFile[] pfa;
	public static ParseFile file;
	public static ParseObject ph;
	static boolean go;
	Context ctx;
	ListView listview;
	ParseFile parseFile;
	QueryFactory<ParseObject> pqf;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_snyps);
		ph = null;
		ctx = this;
		//p = (ParseImageView)findViewById(R.id.snyp_preview_image);
		//p.setVisibility(View.GONE);
		
		adapter = new MyAdapter(this);
		query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
		
		
		
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
							ctx.startActivity(new Intent(ctx,ImageCloseup.class));
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
	
	

	
	
	}
	
	

	
	
	
	
	
	
	
	



