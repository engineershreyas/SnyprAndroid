package com.shrey.snypr;

import java.io.File;

import java.io.IOException;
import java.util.List;

import com.example.snypr.MainActivity;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shrey.game.Score;
import com.shrey.pojos.Storage;
import com.shrey.pojos.User;
import com.shrey.util.CreateFile;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;



public class Picture extends Activity {
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	public static final int MEDIA_TYPE_IMAGE = 1;
	static final int REQUEST_TAKE_PHOTO = 1;
	Context ctx;
	public int pics;
	Register r;
	private Uri fileUri;
	
	User cu;
	ParseFile photoFile;
	ParseQuery<ParseObject> query = ParseQuery.getQuery("Score");
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.pic);
	    ctx = this;
	    
	    cu = Storage.getInstance().user;
	    pics = Storage.getInstance().picCount;
	    
	    //create new Intent
	    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

	    fileUri = CreateFile.getOutputMediaFileUri(MEDIA_TYPE_IMAGE);  // create a file to save the video
	    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);  // set the image file name

	    intent.putExtra(MediaStore.EXTRA_OUTPUT, 1); // set the video image quality to high

	    // start the Video Capture Intent
	    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	    
	    
	    //dispatchTakePictureIntent();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
	        if (resultCode == RESULT_OK) {
	            // Image captured and saved to fileUri specified in the Intent
	        	query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
	        	query.getInBackground(r.objectId(),new GetCallback<ParseObject>() {

					@Override
					public void done(ParseObject obj, ParseException arg1) {
						// TODO Auto-generated method stub
						obj.increment("score",30);
					}

					
					
	        		
	        	});
					
	        
	        	
	        	
	            Toast.makeText(this, "Image saved to:\n" +
	                     data.getData(), Toast.LENGTH_LONG).show();
	            Toast.makeText(ctx, "Sniped!", Toast.LENGTH_SHORT);
	            ctx.startActivity(new Intent(ctx, MainActivity.class));
	        }
	        } else if (resultCode == RESULT_CANCELED) {
	            // User cancelled the image capture
	        	ctx.startActivity(new Intent(ctx, MainActivity.class));
	        	Toast.makeText(ctx, "Better luck next time!", Toast.LENGTH_SHORT).show();
	        	
	        	
	        } else {
	            // Image capture failed, advise user
	        	Toast.makeText(ctx, "Sorry, couldn't take picture at this time", Toast.LENGTH_SHORT).show();
	        	ctx.startActivity(new Intent(ctx, MainActivity.class));
	        }
	    }

	    
	
	
	private void dispatchTakePictureIntent() {
	    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    // Ensure that there's a camera activity to handle the intent
	    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
	        // Create the File where the photo should go
	        File photoFile = null;
	        try {
	            photoFile = CreateFile.createImageFile();
	        } catch (IOException ex) {
	            // Error occurred while creating the File
	            
	        }
	        // Continue only if the File was successfully created
	        if (photoFile != null) {
	            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
	                    Uri.fromFile(photoFile));
	            startActivityForResult(takePictureIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	        }
	        
	    }
	    
	    
	}
	
}
