package com.shrey.pojos;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("SnypPoint")
public class SnypPoint extends ParseObject {
	public void setMessage(String t){
		if(t == null || t.equalsIgnoreCase("")){
		put("message",ParseUser.getCurrentUser().getUsername() + " snypd someone at this location!");
		}
		else{
			put("message",ParseUser.getCurrentUser().getUsername() + " snypd " + t + " at this location!");
		}
		
	}
	
	public String getMessage(){
		return getString("message");
	}
	
	public ParseUser getUser() {
	    return getParseUser("user");
	  }

	  public void setUser(ParseUser value) {
	    put("user", value);
	  }
	
	public void setLocation(ParseGeoPoint value){
		put("location",value);
	}
	
	public ParseGeoPoint getLocation(){
		return getParseGeoPoint("location");
	}
	
	
	
	public static ParseQuery<SnypPoint> getQuery(){
		
		return ParseQuery.getQuery(SnypPoint.class);
	}
	
	

}
