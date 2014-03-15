package com.shrey.snypr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GoToFriends extends Activity {
	Button f,mf;
	Context ctx;
	public void onCreate(Bundle savedInstanceState){
	super.onCreate(savedInstanceState);
	setContentView(R.layout.go_to_friends);
	ctx = this;
		
	f = (Button)findViewById(R.id.button0);
	mf = (Button)findViewById(R.id.button2);
	
	f.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			ctx.startActivity(new Intent(ctx,FriendSearch.class));
		}
	});
	
	mf.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			ctx.startActivity(new Intent(ctx,MyFriends.class));
		}
	});
	
	
	
	
	}
}
