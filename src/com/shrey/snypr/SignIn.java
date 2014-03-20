package com.shrey.snypr;

import java.util.ArrayList;



import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import com.parse.ParseQuery;
import com.shrey.pojos.Storage;
import com.shrey.pojos.User;
import com.example.snypr.MainActivity;
import com.shrey.snypr.R;
import com.shrey.snypr.SignIn;


public class SignIn extends Activity{
	Button b;
	EditText u,p;
	List<User> l = new ArrayList<User>();
	ActionBar actionbar;
	User[] la;
	Context ctx;
	User currentUser;
	String un,pw;
	boolean success;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signin);
		 
		ctx = this;
		b = (Button)findViewById(R.id.snin);
		u = (EditText)findViewById(R.id.usn);
		p = (EditText)findViewById(R.id.pwd);
		
		b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				un = u.getText().toString();
				pw = p.getText().toString();
				// TODO Auto-generated method stub
				ParseUser.logInInBackground(un, pw, new LogInCallback() {

					@Override
					public void done(ParseUser user, ParseException e) {
						// TODO Auto-generated method stub
						if(user!=null){
							ctx.startActivity(new Intent(ctx,MainActivity.class));
						}
						else{
							Log.e("Sign in failed",e.getMessage());
							Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
						}
						
					}
				});
			}
		});
		actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setTitle("Sign In");
		actionbar.setBackgroundDrawable(new ColorDrawable(Color.BLUE));
		
	}
	
}
					
					
					
			
		
		
		
	
	
	
		
		
		
					
		
		
	
			 
			 
			 
				
					 
				
				 
			
			 
			 
		
		
		
			  
	        
			
			
		
		
	
	
		
	
	

