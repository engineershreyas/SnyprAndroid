package com.shrey.snypr;

import java.text.ParseException;



import java.text.SimpleDateFormat;
import java.util.Calendar;

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
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import com.shrey.pojos.Score;
import com.shrey.pojos.User;
import com.shrey.snypr.R;
import com.shrey.snypr.SignIn;
import com.shrey.snypr.Register;

import com.shrey.util.GMailSender;



public class Register extends Activity {

	Button b;
	EditText fn,ln,dob,e,u,p,cp;
	ActionBar actionbar;
	Context ctx;
	
	Calendar c;
	ParseUser pu = new ParseUser();
	User user = new User();
	String f,l,d,em,un,pw,cpw;
	boolean success = true;
	boolean young = false;
	boolean nomatch = false;
	boolean go = false;
	boolean boo = true;
	Score gameScore;
	ParseInstallation installation;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		ctx = this;
		c = Calendar.getInstance();
		gameScore = new Score();
		
		
		b= (Button)findViewById(R.id.r1);
		fn = (EditText)findViewById(R.id.fn);
		ln = (EditText)findViewById(R.id.ln);
		dob = (EditText)findViewById(R.id.dob);
		e = (EditText)findViewById(R.id.e);
		u = (EditText)findViewById(R.id.us);
		p = (EditText)findViewById(R.id.pd);
		cp = (EditText)findViewById(R.id.cp);
		ParseInstallation.getCurrentInstallation().saveEventually();
		installation = ParseInstallation.getCurrentInstallation();
		
		
		b.setOnClickListener(new View.OnClickListener() {
			
			
			@Override
			public void onClick(View arg0) {
				f = fn.getText().toString();
				l = ln.getText().toString();
				d = dob.getText().toString();
				em = e.getText().toString();
				un = u.getText().toString();
				pw = p.getText().toString();
				cpw = cp.getText().toString();
				// TODO Auto-generated method stub
				if(f == null){
					success = false;
					Log.d("f","no first name");
				}
				
				else if(l == null  ){
					success = false;
					Log.d("no last name","no last name");
					
				}
				
				else if(d == null )
				{
					success = false;
					boo = false;
					Log.d("dob","no date");
					
				}
				
				else if(boo){
					if(Integer.parseInt(d) < 13){
						success = false;
						young = true;
						Log.d("dob",d);
					}
					
				}
				
				else if(em == null ){
					success = false;
					Log.d("email", "no email");
				}
				
				else if(un == null){
					success = false;
					Log.d("username","no username");
				}
				
				else if(pw == null ){
					success = false;
					Log.d("password","no password");
					
				}
				
				else if(!(cpw.equals(pw))){
					success = false;
					nomatch = true;
					Log.d("no match","no match");
					
				}
				if(success){
					Log.d("success","success!");
					user.setuserName(un);
					user.setpassWord(pw);
					user.seteMail(em);
					gameScore.addUsername(un);
	                gameScore.addScore(0);
	                gameScore.saveInBackground();
					installation.put("username", un);
					installation.saveEventually();
					user.signUpInBackground(new SignUpCallback(){

						@Override
						public void done(com.parse.ParseException er) {
							if(er == null){
								
								ctx.startActivity(new Intent(ctx,SignIn.class));
				              
								
							}
								else{
									Log.e("error",er.getMessage());
									Toast.makeText(ctx, er.getMessage(), Toast.LENGTH_SHORT).show();
									go = true;
						}
						}
						
					});
				}
					
						if(!go){
						new register().execute();
						}
					
			
				
				 if(!success){
					if(young){
						Toast.makeText(ctx, "Sorry, you're too young to join. Don't worry we'll still be here!", Toast.LENGTH_SHORT ).show();
					}
					else if(nomatch){
						Toast.makeText(ctx, "Your passwords do not match.", Toast.LENGTH_SHORT ).show();
					}
					else{
					Toast.makeText(ctx, "Sorry,something went wrong!", Toast.LENGTH_SHORT).show();
					}
				
				
				
				
				
				
				
			}
			
		}
		
		});
		actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setTitle("Register");
		//actionbar.setBackgroundDrawable(new ColorDrawable(Color.BLUE));
	}
		
	
	
	
	private class register extends AsyncTask<Object,Void,Boolean>{
		
		
		
		
		
		
		@Override
		protected Boolean doInBackground(Object... arg0) {
			// TODO Auto-generated method stub
			
			try {   
                GMailSender sender = new GMailSender("snyprapp@gmail.com", "firstapp");
                sender.sendMail("Welcome",   
                        "Welcome to Snypr, " + un + "!\n" + "Sincerly,\n" + "The Snypr Team",
                        "snyprapp@gmail.com",   
                        em);
                
            } catch (Exception en) {   
                Log.e("SendMail", en.getMessage());   
            }

			
				
				
			
			
			return true;
		}
		
		
		protected void onPostExecute(Boolean results){
			
			
			
			
			
			}
			
			
			
			
	}
	
	
	public String objectId(){
		String s = gameScore.getObjectId();
		return s;
	}
	
	

		
		
		
	
	
	
	
		

	
	
		
}
	
		


