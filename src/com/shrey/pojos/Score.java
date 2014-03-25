package com.shrey.pojos;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Score")
public class Score extends ParseObject {

	public Score(){}
	
	public void addScore(int x){
		put("scoreNumber",x);
	}
	
	public void addUsername(String u){
		put("username",u);
	}
	
	public int getScore(){
		 return getInt("scoreNumber");
	}
	
	public String getUsername(){
		return getString("username");
	}
	
	
}
