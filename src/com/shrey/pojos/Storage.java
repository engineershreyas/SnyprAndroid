package com.shrey.pojos;

public class Storage {
	private static Storage instance= null;
	public String username;
	public String password;
	public int score;
	public int picCount;
	public User user;
	
	 private Storage(){}

	 
	 
	    public static synchronized Storage getInstance(){
	    	if(null == instance){
	    		instance = new Storage();
	    		
	    	}
	    	return instance;
	    }
	
}
