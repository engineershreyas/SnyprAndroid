package com.shrey.snypr;

import java.util.List;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ImageCloseup extends Activity{
	ParseImageView p;
	ParseFile f;
	ParseObject l = new ParseObject("Like");
	ParseObject ph;
	Button b,un;
	Context ctx;
	ParseQuery<ParseObject> query = ParseQuery.getQuery("Photo");
	ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Like");
	TextView commentCount;
	ActionBar actionbar;
	public void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.icl);
		View view = this.getWindow().getDecorView();
        
		ctx = this;
		p = (ParseImageView)findViewById(R.id.snyp_preview_image1);
		p.setVisibility(View.GONE);
		
		
			ph = ViewSnyps.returnPhoto();
		
		
		
		b = (Button)findViewById(R.id.likeButton);
		b.setEnabled(false);
		b.setVisibility(View.INVISIBLE);
		un = (Button)findViewById(R.id.unlikeButton);
		un.setEnabled(false);
		un.setVisibility(View.INVISIBLE);
		
		commentCount = (TextView)findViewById(R.id.likeCount);
		//commentCount.setVisibility(View.GONE);
		
		setLikeCount();
		p.setParseFile(ph.getParseFile("photo"));
		p.loadInBackground(new GetDataCallback(){

			@Override
			public void done(byte[] arg0, ParseException arg1) {
				if(arg1 == null){
					p.setVisibility(View.VISIBLE);
				}
				
			}
			
		});
		b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ph.increment("likes",1);
				ph.saveEventually();
					
				
		
				
				Toast.makeText(ctx,"You liked this Snyp!",Toast.LENGTH_SHORT).show();
				refresh();
			}
		});
		
		actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setTitle("Your picture");
		actionbar.setBackgroundDrawable(new ColorDrawable(Color.BLUE));
	}
		

		
	
	
	public void setLikeCount(){
		if(String.valueOf(ph.getInt("likes"))!=null){
		commentCount.setText("Likes: "+ String.valueOf(ph.getInt("likes")));
		}
		else{
			commentCount.setText("0");
		}
	}
	
	private void refresh(){
		Intent intent = getIntent();
	    //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	    
	    finish();
	    
	    startActivity(intent);
	}
	

}
