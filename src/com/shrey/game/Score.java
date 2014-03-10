package com.shrey.game;

import com.shrey.pojos.Storage;

public class Score {
	public static int s;
	public static int makeScore(int x){
		
		s = 1;
		s*=30;
		Storage.getInstance().score = s;
		
		return s;
	}
	
	public static int getScore(){
		
		return s;
		
	}

}
