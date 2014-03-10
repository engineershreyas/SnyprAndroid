package com.shrey.snypr;

import com.parse.ParseObject;
import com.parse.ParseUser;
import com.shrey.pojos.Friend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MyFriendPage extends Activity {
	public static ParseObject friend;
	TextView n;
	Context ctx;
	Button f,v;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend_page);
		friend = MyFriends.getFriend();
		ctx = this;
		n = (TextView)findViewById(R.id.name);
		n.setText(friend.getString("friendname"));
		f = (Button)findViewById(R.id.Add);
		v = (Button)findViewById(R.id.View);
		f.setEnabled(false);
		f.setVisibility(View.INVISIBLE);
		
		if(friend == null){
			ctx.startActivity(new Intent(ctx,MyFriends.class));
		}
		
v.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ctx.startActivity(new Intent(ctx,MyFriendSnyps.class));
			}
		});
		
		
	}
	
	public static ParseObject getFriendAgain(){
		return friend;
	}

}
