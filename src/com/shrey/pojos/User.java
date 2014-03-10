package com.shrey.pojos;

import java.util.ArrayList;
import java.util.List;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseUser;

@ParseClassName("User")
public class User extends ParseUser {
	String _username;
	String _password;
	int _id;
	int _score;
	ParseUser us;
	List<ParseFile> photos = new ArrayList<ParseFile>();


	public User(){}

	
	

	
	
	
	
	


	public String getuserName(){
		return getUsername();
	}

	

	public void setuserName(String u){
		setUsername(u);
	}

	public void setpassWord(String p){
		setPassword(p);
	}
	public void seteMail(String e){
		setEmail(e);
	}
	
	public void setList(List<ParseFile> l){
		put("PhotoList",photos);
		
	}
	
	public void addPhoto(ParseFile p){
		put("photo",p);
		
	}
	
	public List<ParseFile> getPhotos(){
		
		return getList("Photolist");
		
		
	}
	
}
