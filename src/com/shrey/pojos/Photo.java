package com.shrey.pojos;

import java.util.ArrayList;


import java.util.List;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;


@ParseClassName("Photo")
public class Photo extends ParseObject{
	
	public Photo(){}
	
	public void addUser(String u){
		put("username",u);
		
	}
	
	public void getUser(){
		getString("username");
	}
	
	public void addFile(ParseFile p){
		put("photo",p);
	}
	
	public void addList(List<ParseFile> a){
		put("photos",a);
	}
	
	public void getFile(){
		getParseFile("photo");
	}
	
	
	
	

}
