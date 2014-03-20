package com.shrey.snypr;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

public class GoToCameraTwo extends Activity {
	
	public void onCreate(Bundle savedInstanceState){
	super.onCreate(savedInstanceState);
	setContentView(R.layout.go_to_camera);
	FragmentManager manager = getFragmentManager();
	Fragment fragment = manager.findFragmentById(R.id.fragmentContainer);
	
	
	if (fragment == null) {
		fragment = new CameraFragmentTwo();
		manager.beginTransaction().add(R.id.fragmentContainer, fragment)
				.commit();
	}
	}

}
