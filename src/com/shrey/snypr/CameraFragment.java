package com.shrey.snypr;

import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.example.snypr.MainActivity;
import com.example.snypr.Snypr;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;

import com.shrey.pojos.Photo;
import com.shrey.pojos.SnypPoint;
import com.shrey.pojos.User;
import com.shrey.snypr.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.location.Location;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.SurfaceHolder.Callback;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class CameraFragment extends Fragment implements GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener  {
	public static final String TAG = "CameraFragment";

	  /*
	   * Constants for location update parameters
	   */
	  // Milliseconds per second
	  private static final int MILLISECONDS_PER_SECOND = 1000;

	  // The update interval
	  private static final int UPDATE_INTERVAL_IN_SECONDS = 5;

	  // A fast interval ceiling
	  private static final int FAST_CEILING_IN_SECONDS = 1;

	  // Update interval in milliseconds
	  private static final long UPDATE_INTERVAL_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
	      * UPDATE_INTERVAL_IN_SECONDS;

	  // A fast ceiling of update intervals, used when the app is visible
	  private static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
	      * FAST_CEILING_IN_SECONDS;

	  /*
	   * Constants for handling location results
	   */
	  // Conversion from feet to meters
	  private static final float METERS_PER_FEET = 0.3048f;

	  // Conversion from kilometers to meters
	  private static final int METERS_PER_KILOMETER = 1000;

	  // Initial offset for calculating the map bounds
	  private static final double OFFSET_CALCULATION_INIT_DIFF = 1.0;

	  // Accuracy for calculating the map bounds
	  private static final float OFFSET_CALCULATION_ACCURACY = 0.01f;

	  // Maximum results returned from a Parse query
	  private static final int MAX_POST_SEARCH_RESULTS = 20;

	  // Maximum post search radius for map in kilometers
	  private static final int MAX_POST_SEARCH_DISTANCE = 100;

	  /*
	   * Other class member variables
	   */
	  // Map fragment
	  private SupportMapFragment map;

	  // Represents the circle around a map
	  private Circle mapCircle;

	  // Fields for the map radius in feet
	  private float radius;
	  private float lastRadius;

	  // Fields for helping process map and location changes
	  private final Map<String, Marker> mapMarkers = new HashMap<String, Marker>();
	  private int mostRecentMapUpdate = 0;
	  private boolean hasSetUpInitialLocation = false;
	  private String selectedObjectId;
	 

	  // A request to connect to Location Services
	  private LocationRequest locationRequest;

	  // Stores the current instantiation of the location client in this object
	  private LocationClient locationClient;

	  // Adapter for the Parse query
	  private ParseQueryAdapter<SnypPoint> posts;

	private Camera camera;
	private SurfaceView surfaceView;
	private ParseFile photoFile;
	private ImageButton photoButton;
	Register r;
	User user = new User();
	Context ctx;
	MainActivity ma;
	Photo photo;
	List<ParseFile> phos = new ArrayList<ParseFile>();
	 private Location lastLocation = null;
	  private Location currentLocation = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_camera, parent, false);
		ctx = this.getActivity();
		photoButton = (ImageButton) v.findViewById(R.id.camera_photo_button);
		locationClient = new LocationClient(this.getActivity().getApplicationContext(), this, this);
		locationRequest = LocationRequest.create();

	    // Set the update interval
	    locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

	    // Use high accuracy
	    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

	    // Set the interval ceiling to one minute
	    locationRequest.setFastestInterval(FAST_INTERVAL_CEILING_IN_MILLISECONDS);

		photo = new Photo();
		if (camera == null) {
			try {
				camera = Camera.open();
				photoButton.setEnabled(true);
			} catch (Exception e) {
				Log.e(TAG, "No camera with exception: " + e.getMessage());
				photoButton.setEnabled(false);
				Toast.makeText(getActivity(), "No camera detected",
						Toast.LENGTH_LONG).show();
			}
		}

		photoButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (camera == null)
					return;
				camera.takePicture(new Camera.ShutterCallback() {

					@Override
					public void onShutter() {
						// nothing to do
					}

				}, null, new Camera.PictureCallback() {

					@Override
					public void onPictureTaken(byte[] data, Camera camera) {
						saveScaledPhoto(data);
						//ParseUser.getCurrentUser().increment("score",30);
						Toast.makeText(getActivity(), "Snypd!", Toast.LENGTH_SHORT).show();
						 Location myLoc = (currentLocation == null) ? lastLocation : currentLocation;
					        if (myLoc == null) {
					          Toast.makeText(ctx,
					              "Please try again after your location appears on the map.", Toast.LENGTH_LONG).show();
					          return;
					        }
					        final ParseGeoPoint myPoint = geoPointFromLocation(myLoc);
					        final SnypPoint post = new SnypPoint();
				            // Set the location to the current user's location
					        AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
					        alert.setTitle("Who did you snyp?");
					        final EditText input = new EditText(ctx);
					        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
					        alert.setView(input);
					        alert.setPositiveButton("This is who I snypd", new DialogInterface.OnClickListener(){

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									 post.setLocation(myPoint);
							            post.setMessage(input.getText().toString());
							            post.setUser(ParseUser.getCurrentUser());
							            ParseACL acl = new ParseACL();
							            // Give public read access
							            acl.setPublicReadAccess(true);
							            post.setACL(acl);
							            // Save the post
							            post.saveEventually();
							            ctx.startActivity(new Intent(ctx,MainActivity.class));
								}
					        
					        });
					        
					        alert.setNegativeButton("I don't know", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									post.setLocation(myPoint);
						            post.setMessage(input.getText().toString());
						            post.setUser(ParseUser.getCurrentUser());
						            ParseACL acl = new ParseACL();
						            // Give public read access
						            acl.setPublicReadAccess(true);
						            post.setACL(acl);
						            // Save the post
						            post.saveEventually();
						            ctx.startActivity(new Intent(ctx,MainActivity.class));
								}
							});
				           alert.create().show();
					        
						
						
					}

				});

			}
		});

		surfaceView = (SurfaceView) v.findViewById(R.id.camera_surface_view);
		SurfaceHolder holder = surfaceView.getHolder();
		holder.addCallback(new Callback() {

			public void surfaceCreated(SurfaceHolder holder) {
				try {
					if (camera != null) {
						camera.setDisplayOrientation(90);
						camera.setPreviewDisplay(holder);
						camera.startPreview();
					}
				} catch (IOException e) {
					Log.e(TAG, "Error setting up preview", e);
				}
			}

			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
				// nothing to do here
			}

			public void surfaceDestroyed(SurfaceHolder holder) {
				// nothing here
			}

		});

		return v;
	}

	/*
	 * ParseQueryAdapter loads ParseFiles into a ParseImageView at whatever size
	 * they are saved. Since we never need a full-size image in our app, we'll
	 * save a scaled one right away.
	 */
	private void saveScaledPhoto(byte[] data) {

		// Resize photo from camera byte array
		Bitmap snypImage = BitmapFactory.decodeByteArray(data, 0, data.length);
		Bitmap snypImageScaled = Bitmap.createScaledBitmap(snypImage, 2000, 2000
				* snypImage.getHeight() / snypImage.getWidth(), false);

		// Override Android default landscape orientation and save portrait
		Matrix matrix = new Matrix();
		matrix.postRotate(90);
		Bitmap rotatedScaledMealImage = Bitmap.createBitmap(snypImageScaled, 0,
				0, snypImageScaled.getWidth(), snypImageScaled.getHeight(),
				matrix, true);

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		rotatedScaledMealImage.compress(Bitmap.CompressFormat.JPEG, 100, bos);

		byte[] scaledData = bos.toByteArray();

		// Save the scaled image to Parse
		photoFile = new ParseFile("snyp.jpg", scaledData);
		photoFile.saveInBackground(new SaveCallback() {

			public void done(ParseException e) {
				if (e == null) {
					ParseUser.getCurrentUser().add("photos",photoFile);
					ParseUser.getCurrentUser().saveEventually();
					photo.addFile(photoFile);
					photo.addUser(ParseUser.getCurrentUser().getUsername());
					photo.saveEventually();
					
					Log.d("save status",photoFile.getName() + " is saved!");
				} else {
					
					Toast.makeText(getActivity(),
							"Error saving: " + e.getMessage(),
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	/*
	 * Once the photo has saved successfully, we're ready to return to the
	 * NewMealFragment. When we added the CameraFragment to the back stack, we
	 * named it "NewMealFragment". Now we'll pop fragments off the back stack
	 * until we reach that Fragment.
	 */
	/**private void addPhotoToMealAndReturn(ParseFile photoFile) {
		((NewMealActivity) getActivity()).getCurrentMeal().setPhotoFile(
				photoFile);
		FragmentManager fm = getActivity().getFragmentManager();
		fm.popBackStack("NewMealFragment",
				FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}
	*/

	
	public void onStart(){
		super.onStart();
		 locationClient.connect();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (camera == null) {
			try {
				camera = Camera.open();
				photoButton.setEnabled(true);
			} catch (Exception e) {
				Log.i(TAG, "No camera: " + e.getMessage());
				photoButton.setEnabled(false);
				Toast.makeText(getActivity(), "No camera detected",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public void onPause() {
		if (camera != null) {
			camera.stopPreview();
			camera.release();
		}
		super.onPause();
	}
	
	 private void doMapQuery() {
		    final int myUpdateNumber = ++mostRecentMapUpdate;
		    Location myLoc = (currentLocation == null) ? lastLocation : currentLocation;
		    // If location info isn't available, clean up any existing markers
		    if (myLoc == null) {
		      cleanUpMarkers(new HashSet<String>());
		      return;
		    }
		    final ParseGeoPoint myPoint = geoPointFromLocation(myLoc);
		    // Create the map Parse query
		    ParseQuery<SnypPoint> mapQuery = SnypPoint.getQuery();
		    // Set up additional query filters
		    mapQuery.whereWithinKilometers("location", myPoint, MAX_POST_SEARCH_DISTANCE);
		    mapQuery.include("user");
		    mapQuery.orderByDescending("createdAt");
		    mapQuery.setLimit(MAX_POST_SEARCH_RESULTS);
		    // Kick off the query in the background
		    mapQuery.findInBackground(new FindCallback<SnypPoint>() {
		      
			

			@Override
			public void done(List<SnypPoint> objects, ParseException e) {
				// TODO Auto-generated method stub
				if (e != null) {
			          if (Snypr.APPDEBUG) {
			            Log.d(Snypr.APPTAG, "An error occurred while querying for map posts.", e);
			          }
			          return;
			        }
			        /*
			         * Make sure we're processing results from
			         * the most recent update, in case there
			         * may be more than one in progress.
			         */
			        if (myUpdateNumber != mostRecentMapUpdate) {
			          return;
			        }
			        // Posts to show on the map
			        Set<String> toKeep = new HashSet<String>();
			        // Loop through the results of the search
			        for (SnypPoint post : objects ) {
			          // Add this post to the list of map pins to keep
			          toKeep.add(post.getObjectId());
			          // Check for an existing marker for this post
			          Marker oldMarker = mapMarkers.get(post.getObjectId());
			          // Set up the map marker's location
			          MarkerOptions markerOpts =
			              new MarkerOptions().position(new LatLng(post.getLocation().getLatitude(), post
			                  .getLocation().getLongitude()));
			          // Set up the marker properties based on if it is within the search radius
			          if (post.getLocation().distanceInKilometersTo(myPoint) > radius * METERS_PER_FEET
			              / METERS_PER_KILOMETER) {
			            // Check for an existing out of range marker
			            if (oldMarker != null) {
			              if (oldMarker.getSnippet() == null) {
			                // Out of range marker already exists, skip adding it
			                continue;
			              } else {
			                // Marker now out of range, needs to be refreshed
			                oldMarker.remove();
			              }
			            }
			            // Display a red marker with a predefined title and no snippet
			            markerOpts =
			                markerOpts.title(getResources().getString(R.string.post_out_of_range)).icon(
			                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
			          } else {
			            // Check for an existing in range marker
			            if (oldMarker != null) {
			              if (oldMarker.getSnippet() != null) {
			                // In range marker already exists, skip adding it
			                continue;
			              } else {
			                // Marker now in range, needs to be refreshed
			                oldMarker.remove();
			              }
			            }
			            // Display a green marker with the post information
			            markerOpts =
			                markerOpts.title(post.getMessage()).snippet(post.getUser().getUsername())
			                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
			          }
			          // Add a new marker
			          Marker marker = map.getMap().addMarker(markerOpts);
			          mapMarkers.put(post.getObjectId(), marker);
			          if (post.getObjectId().equals(selectedObjectId)) {
			            marker.showInfoWindow();
			            selectedObjectId = null;
			          }
			        }
			        // Clean up old markers.
			        cleanUpMarkers(toKeep);
			}
		    });
		  }
	 private void cleanUpMarkers(Set<String> markersToKeep) {
		    for (String objId : new HashSet<String>(mapMarkers.keySet())) {
		      if (!markersToKeep.contains(objId)) {
		        Marker marker = mapMarkers.get(objId);
		        marker.remove();
		        mapMarkers.get(objId).remove();
		        mapMarkers.remove(objId);
		      }
		    }
		  }
	 private ParseGeoPoint geoPointFromLocation(Location loc) {
		    return new ParseGeoPoint(loc.getLatitude(), loc.getLongitude());
		  }

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		currentLocation = location;
	    if (lastLocation != null
	        && geoPointFromLocation(location)
	            .distanceInKilometersTo(geoPointFromLocation(lastLocation)) < 0.01) {
	      // If the location hasn't changed by more than 10 meters, ignore it.
	      return;
	    }
	    lastLocation = location;
	    LatLng myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
	    if (!hasSetUpInitialLocation) {
	      // Zoom to the current location.
	      //updateZoom(myLatLng);
	      hasSetUpInitialLocation = true;
	    }
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		// TODO Auto-generated method stub
		if (connectionResult.hasResolution()) {
		      try {

		        // Start an Activity that tries to resolve the error
		        connectionResult.startResolutionForResult(this.getActivity(), 9000);

		      } catch (IntentSender.SendIntentException e) {

		        if (Snypr.APPDEBUG) {
		          // Thrown if Google Play services canceled the original PendingIntent
		          Log.d(Snypr.APPTAG, "An error occurred when connecting to location services.", e);
		        }
		      }
		    } else {

		      // If no resolution is available, display a dialog to the user with the error.
		      Log.e("error",String.valueOf(connectionResult.getErrorCode()));
		    }
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		currentLocation = getLocation();
		 startPeriodicUpdates();
	}
	
	private Location getLocation() {
	    // If Google Play Services is available
	    if (servicesConnected()) {
	      // Get the current location
	      return locationClient.getLastLocation();
	    } else {
	      return null;
	    }
	  }
	
	private boolean servicesConnected() {
	    // Check that Google Play services is available
	    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.getActivity());

	    // If Google Play services is available
	    if (ConnectionResult.SUCCESS == resultCode) {
	      if (Snypr.APPDEBUG) {
	        // In debug mode, log the status
	        Log.d(Snypr.APPTAG, "Google play services available");
	      }
	      // Continue
	      return true;
	      // Google Play services was not available for some reason
	    } else {
	      // Display an error dialog
	      //Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0);
	      //if (dialog != null) {
	        //Log.d("uh oh", "no google services");
	      //}
	      return false;
	    }
	  }
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
	    // Choose what to do based on the request code
	    switch (requestCode) {

	    // If the request code matches the code sent in onConnectionFailed
	    case 9000:

	      switch (resultCode) {
	      // If Google Play services resolved the problem
	      case Activity.RESULT_OK:

	        if (Snypr.APPDEBUG) {
	          // Log the result
	          Log.d(Snypr.APPTAG, "Connected to Google Play services");
	        }

	        break;

	      // If any other result was returned by Google Play services
	      default:
	        if (Snypr.APPDEBUG) {
	          // Log the result
	          Log.d(Snypr.APPTAG, "Could not connect to Google Play services");
	        }
	        break;
	      }

	      // If any other request code was received
	    default:
	      if (Snypr.APPDEBUG) {
	        // Report that this Activity received an unknown requestCode
	        Log.d(Snypr.APPTAG, "Unknown request code received for the activity");
	      }
	      break;
	    }
	  }
	
	

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}
	private void startPeriodicUpdates() {
	    locationClient.requestLocationUpdates(locationRequest, this);
	  }


}
