package com.shrey.snypr;

import java.util.ArrayList;

import java.util.List;



import com.example.snypr.MainActivity;
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
import com.shrey.util.PicassoAdapter;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ListView;
import android.widget.TextView;


public class ViewSnyps extends Activity{
	ActionBar actionbar;
	ParseImageView p;
	ParseQuery<ParseObject> query = ParseQuery.getQuery("Photo");
	MyAdapter adapter;
	//PicassoAdapter adapter;
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
	List<ParseFile> parseList = new ArrayList<ParseFile>();
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_snyps);
		View view = this.getWindow().getDecorView();
		actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setTitle("Your Snyps");
		//actionbar.setBackgroundDrawable(new ColorDrawable(Color.BLUE));
		ph = null;
		ctx = this;
		
		adapter = new MyAdapter(this);
		
		
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
							ph = objs.get(objs.size()-1-position);
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
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
		
		
		
		
		
	
		
	public static  ParseObject returnPhoto(){
		if(go){
		return ph;
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
	
	

	
	
	
	
	
	
	
	



