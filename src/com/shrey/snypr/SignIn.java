package com.shrey.snypr;

import java.util.ArrayList;



import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.RequestPasswordResetCallback;

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
	TextView forgot;
	String email;
	boolean success;
	ParseInstallation installation;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signin);
		 ParsePush push = new ParsePush();
		ctx = this;
		b = (Button)findViewById(R.id.snin);
		u = (EditText)findViewById(R.id.usn);
		p = (EditText)findViewById(R.id.pwd);
		forgot = (TextView)findViewById(R.id.fp);
		
		b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				un = u.getText().toString();
				pw = p.getText().toString();
				// TODO Auto-generated method stub
				ParseInstallation.getCurrentInstallation().saveEventually();
				installation = ParseInstallation.getCurrentInstallation();
				installation.put("username", un);
				PushService.subscribe(ctx, "appUser", MainActivity.class);
				installation.saveEventually();
				ParseUser.logInInBackground(un, pw, new LogInCallback() {

					@Override
					public void done(ParseUser user, ParseException e) {
						// TODO Auto-generated method stub
						if(user!=null){
							ctx.startActivity(new Intent(ctx,MainActivity.class));
							Log.d("success","sign in succeeded");
						}
						else{
							Log.e("Sign in failed",e.getMessage());
							Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
						}
						
					}
				});
			}
		});
		
		forgot.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
				alert.setTitle("What is your email?");
				final EditText input = new EditText(ctx);
		        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
		        alert.setView(input);
		        alert.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface d, int which) {
						// TODO Auto-generated method stub
						email = input.getText().toString();
						if(email!=null||!email.equalsIgnoreCase("")){
						ParseUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback(){

							@Override
							public void done(ParseException e) {
								// TODO Auto-generated method stub
								if(e == null){
									Toast.makeText(ctx, "An e-mail has been sent with instructions to reset your password", Toast.LENGTH_LONG).show();
								}
								else{
									Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
								}
							}
							
						});
						}
						else{
							Toast.makeText(ctx, "Please enter an email or press cancel", Toast.LENGTH_SHORT).show();
						}
					}
				});
		        
		        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						//do nothing lols
					}
				});
		        
		        alert.create().show();
				
			}
		});
		actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setTitle("Sign In");
		actionbar.setBackgroundDrawable(new ColorDrawable(Color.BLUE));
		
	}
	
}
					
					
					
			
		
		
		
	
	
	
		
		
		
					
		
		
	
			 
			 
			 
				
					 
				
				 
			
			 
			 
		
		
		
			  
	        
			
			
		
		
	
	
		
	
	

