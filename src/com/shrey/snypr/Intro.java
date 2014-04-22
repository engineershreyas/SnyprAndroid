package com.shrey.snypr;

import android.app.ActionBar;
import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.shrey.snypr.R;
import com.shrey.snypr.Register;
import com.shrey.snypr.SignIn;

public class Intro extends Activity {
	
		Button a,b;
		Context ctx;
		ActionBar actionbar;
		
		public void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
			setContentView(R.layout.intro);
			actionbar = getActionBar();
			actionbar.setDisplayHomeAsUpEnabled(true);
			actionbar.setTitle("Welcome to Snypr!");
			//actionbar.setBackgroundDrawable(new ColorDrawable(Color.BLUE));
			
			ctx = this;
			a = (Button)findViewById(R.id.r);
			b = (Button)findViewById(R.id.si);
			//a.getBackground().setColorFilter(0xFFFFBB33, PorterDuff.Mode.MULTIPLY);
			//b.getBackground().setColorFilter(0xFFFFBB33, PorterDuff.Mode.MULTIPLY);
			
			a.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					ctx.startActivity(new Intent(ctx,Register.class));
				}
			});
			
			b.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ctx.startActivity(new Intent(ctx,SignIn.class));
				}
			});
		}
		
}
