package com.shrey.snypr;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.shrey.snypr.R;
import com.shrey.snypr.Register;
import com.shrey.snypr.SignIn;

public class Intro extends Activity {
	
		Button a,b;
		Context ctx;
		public void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
			setContentView(R.layout.intro);
			ctx = this;
			a = (Button)findViewById(R.id.r);
			b = (Button)findViewById(R.id.si);
			
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
