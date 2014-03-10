package com.shrey.pojos;

import java.util.List;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;


@ParseClassName("Friend")
public class Friend extends ParseObject {
	
	public Friend(){}
	
	public void addUserName(String u){
		put("username",u);
		
	}
	
	
	public void addFriendName(String f){
		put("friendname",f);
	}
	
	
	
	
}
	