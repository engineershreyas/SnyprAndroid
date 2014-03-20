package com.shrey.snypr;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GoToFriends extends Activity {
	Button f,mf;
	Context ctx;
	ActionBar actionbar;
	public void onCreate(Bundle savedInstanceState){
	super.onCreate(savedInstanceState);
	setContentView(R.layout.go_to_friends);
	View view = this.getWindow().getDecorView();
	actionbar = getActionBar();
	actionbar.setDisplayHomeAsUpEnabled(true);
	actionbar.setTitle("Friend Options");
	actionbar.setBackgroundDrawable(new ColorDrawable(Color.BLUE));
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
