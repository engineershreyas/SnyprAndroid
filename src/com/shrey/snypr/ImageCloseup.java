package com.shrey.snypr;

import java.util.List;

import com.example.snypr.MainActivity;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shrey.pojos.Score;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
	ParseQuery<Score> scoreQuery = ParseQuery.getQuery("Score");
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
			if(ph == null){
				ctx.startActivity(new Intent(ctx,MainActivity.class));
			}
		
		
		
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
		p.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
				alert.setTitle("Delete Photo?");
				alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						try {
							scoreQuery.whereEqualTo("username", ph.getString("username"));
							scoreQuery.findInBackground(new FindCallback<Score>(){

								@Override
								public void done(List<Score> scores,
										ParseException e) {
									// TODO Auto-generated method stub
									if(e == null)
									for(int i = 0; i < scores.size();i++){
										scores.get(i).increment("scoreNumber",(-1*ph.getInt("likes")));
									}
									else{
										Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
									}
								}
								
							});
							ph.delete();
							Toast.makeText(ctx, "Picture deleted!",Toast.LENGTH_SHORT ).show();
							ctx.startActivity(new Intent(ctx,MainActivity.class));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
						}
						
						
					}
				});
				
				alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						//do nothing
					}
				});
				alert.create().show();
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
