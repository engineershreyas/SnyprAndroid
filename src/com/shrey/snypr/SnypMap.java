package com.shrey.snypr;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.example.snypr.Snypr;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseQueryAdapter.QueryFactory;
import com.parse.ParseUser;
import com.parse.SaveCallback;


import com.shrey.pojos.SnypPoint;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SnypMap extends FragmentActivity implements LocationListener,GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

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
	  private static final int MAX_POST_SEARCH_DISTANCE = 1000;

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
	  private Location lastLocation = null;
	  private Location currentLocation = null;

	  // A request to connect to Location Services
	  private LocationRequest locationRequest;

	  // Stores the current instantiation of the location client in this object
	  private LocationClient locationClient;

	  // Adapter for the Parse query
	  private ParseQueryAdapter<SnypPoint> posts;

	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.snyp_map);
		radius = Snypr.getSearchDistance();
		lastRadius = radius;
		
		// Create a new global location parameters object
	    locationRequest = LocationRequest.create();

	    // Set the update interval
	    locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

	    // Use high accuracy
	    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

	    // Set the interval ceiling to one minute
	    locationRequest.setFastestInterval(FAST_INTERVAL_CEILING_IN_MILLISECONDS);

	    // Create a new location client, using the enclosing class to handle callbacks.
	    locationClient = new LocationClient(this, this, this);
	    
	    /*QueryFactory<SnypPoint> factory =
	            new ParseQueryAdapter.QueryFactory<SnypPoint>() {
	              public ParseQuery<SnypPoint> create() {
	                Location myLoc = (currentLocation == null) ? lastLocation : currentLocation;
	                ParseQuery<SnypPoint> query = SnypPoint.getQuery();
	                query.include("user");
	                query.orderByDescending("createdAt");
	                query.whereWithinKilometers("location", geoPointFromLocation(myLoc), radius
	                    * METERS_PER_FEET / METERS_PER_KILOMETER);
	                query.setLimit(MAX_POST_SEARCH_RESULTS);
	                return query;
	              }
	            };
	            
	            // Set up the query adapter
	            posts = new ParseQueryAdapter<SnypPoint>(this, factory) {
	              @Override
	              public View getItemView(SnypPoint post, View view, ViewGroup parent) {
	                if (view == null) {
	                  view = View.inflate(getContext(), R.layout.snyp_point_item, null);
	                }
	                TextView contentView = (TextView) view.findViewById(R.id.contentView);
	                TextView usernameView = (TextView) view.findViewById(R.id.usernameView);
	                contentView.setText(post.getMessage());
	                usernameView.setText(post.getUser().getUsername());
	                return view;
	              }
	            };
	            */
	    
	    map = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map);
	    // Enable the current location "blue dot"
	    map.getMap().setMyLocationEnabled(true);
	    // Set up the camera change handler
	    map.getMap().setOnCameraChangeListener(new OnCameraChangeListener() {
	      public void onCameraChange(CameraPosition position) {
	        // When the camera changes, update the query
	        doMapQuery();
	      }
	    });
	}
	
	public void onStop(){
		if (locationClient.isConnected()) {
		      stopPeriodicUpdates();
		    }

		    // After disconnect() is called, the client is considered "dead".
		    locationClient.disconnect();

		    super.onStop();
	}
	
	public void onStart(){
		super.onStart();

	    // Connect to the location services client
	    locationClient.connect();
	}
	
	protected void onResume(){
		 super.onResume();

		    // Get the latest search distance preference
		    radius = Snypr.getSearchDistance();
		    // Checks the last saved location to show cached data if it's available
		    if (lastLocation != null) {
		      LatLng myLatLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
		      // If the search distance preference has been changed, move
		      // map to new bounds.
		      if (lastRadius != radius) {
		        updateZoom(myLatLng);
		      }
		      // Update the circle map
		      updateCircle(myLatLng);
		    }
		    // Save the current radius
		    lastRadius = radius;
		    // Query for the latest data to update the views.
		    doMapQuery();
		    //doListQuery();
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
	    // Choose what to do based on the request code
	    switch (requestCode) {

	    // If the request code matches the code sent in onConnectionFailed
	    case CONNECTION_FAILURE_RESOLUTION_REQUEST:

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
	
	 private boolean servicesConnected() {
		    // Check that Google Play services is available
		    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

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
		      Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0);
		      if (dialog != null) {
		        Log.d("uh oh", "no google services");
		      }
		      return false;
		    }
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
	      updateZoom(myLatLng);
	      hasSetUpInitialLocation = true;
	    }
	    // Update map radius indicator
	    updateCircle(myLatLng);
	    doMapQuery();
	    //doListQuery();
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		// TODO Auto-generated method stub
		if (connectionResult.hasResolution()) {
		      try {

		        // Start an Activity that tries to resolve the error
		        connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);

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
		if (Snypr.APPDEBUG) {
		      Log.d("Connected to location services", Snypr.APPTAG);
		    }
		    currentLocation = getLocation();
		    startPeriodicUpdates();
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		if (Snypr.APPDEBUG) {
		      Log.d("Disconnected from location services", Snypr.APPTAG);
		    }
	}
	
	private void startPeriodicUpdates() {
	    locationClient.requestLocationUpdates(locationRequest, this);
	  }

	  /*
	   * In response to a request to stop updates, send a request to Location Services
	   */
	  private void stopPeriodicUpdates() {
	    locationClient.removeLocationUpdates(this);
	  }

	  /*
	   * Get the current location
	   */
	  private Location getLocation() {
	    // If Google Play Services is available
	    if (servicesConnected()) {
	      // Get the current location
	      return locationClient.getLastLocation();
	    } else {
	      return null;
	    }
	  }

	  /*
	   * Set up a query to update the list view
	   */
	  private void doListQuery() {
	    Location myLoc = (currentLocation == null) ? lastLocation : currentLocation;
	    // If location info is available, load the data
	    if (myLoc != null) {
	      // Refreshes the list view with new data based
	      // usually on updated location data.
	      posts.loadObjects();
	    }
	  }

	  /*
	   * Set up the query to update the map view
	   */
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
	    //mapQuery.orderByDescending("createdAt");
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

	  /*
	   * Helper method to clean up old markers
	   */
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

	  /*
	   * Helper method to get the Parse GEO point representation of a location
	   */
	  private ParseGeoPoint geoPointFromLocation(Location loc) {
	    return new ParseGeoPoint(loc.getLatitude(), loc.getLongitude());
	  }

	  /*
	   * Displays a circle on the map representing the search radius
	   */
	  private void updateCircle(LatLng myLatLng) {
	    if (mapCircle == null) {
	      mapCircle =
	          map.getMap().addCircle(
	              new CircleOptions().center(myLatLng).radius(radius * METERS_PER_FEET));
	      int baseColor = Color.DKGRAY;
	      mapCircle.setStrokeColor(baseColor);
	      mapCircle.setStrokeWidth(2);
	      mapCircle.setFillColor(Color.argb(50, Color.red(baseColor), Color.green(baseColor),
	          Color.blue(baseColor)));
	    }
	    mapCircle.setCenter(myLatLng);
	    mapCircle.setRadius(radius * METERS_PER_FEET); // Convert radius in feet to meters.
	  }

	  /*
	   * Zooms the map to show the area of interest based on the search radius
	   */
	  private void updateZoom(LatLng myLatLng) {
	    // Get the bounds to zoom to
	    LatLngBounds bounds = calculateBoundsWithCenter(myLatLng);
	    // Zoom to the given bounds
	    map.getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 5));
	  }

	  /*
	   * Helper method to calculate the offset for the bounds used in map zooming
	   */
	  private double calculateLatLngOffset(LatLng myLatLng, boolean bLatOffset) {
	    // The return offset, initialized to the default difference
	    double latLngOffset = OFFSET_CALCULATION_INIT_DIFF;
	    // Set up the desired offset distance in meters
	    float desiredOffsetInMeters = radius * METERS_PER_FEET;
	    // Variables for the distance calculation
	    float[] distance = new float[1];
	    boolean foundMax = false;
	    double foundMinDiff = 0;
	    // Loop through and get the offset
	    do {
	      // Calculate the distance between the point of interest
	      // and the current offset in the latitude or longitude direction
	      if (bLatOffset) {
	        Location.distanceBetween(myLatLng.latitude, myLatLng.longitude, myLatLng.latitude
	            + latLngOffset, myLatLng.longitude, distance);
	      } else {
	        Location.distanceBetween(myLatLng.latitude, myLatLng.longitude, myLatLng.latitude,
	            myLatLng.longitude + latLngOffset, distance);
	      }
	      // Compare the current difference with the desired one
	      float distanceDiff = distance[0] - desiredOffsetInMeters;
	      if (distanceDiff < 0) {
	        // Need to catch up to the desired distance
	        if (!foundMax) {
	          foundMinDiff = latLngOffset;
	          // Increase the calculated offset
	          latLngOffset *= 2;
	        } else {
	          double tmp = latLngOffset;
	          // Increase the calculated offset, at a slower pace
	          latLngOffset += (latLngOffset - foundMinDiff) / 2;
	          foundMinDiff = tmp;
	        }
	      } else {
	        // Overshot the desired distance
	        // Decrease the calculated offset
	        latLngOffset -= (latLngOffset - foundMinDiff) / 2;
	        foundMax = true;
	      }
	    } while (Math.abs(distance[0] - desiredOffsetInMeters) > OFFSET_CALCULATION_ACCURACY);
	    return latLngOffset;
	  }

	  /*
	   * Helper method to calculate the bounds for map zooming
	   */
	  public LatLngBounds calculateBoundsWithCenter(LatLng myLatLng) {
	    // Create a bounds
	    LatLngBounds.Builder builder = LatLngBounds.builder();

	    // Calculate east/west points that should to be included
	    // in the bounds
	    double lngDifference = calculateLatLngOffset(myLatLng, false);
	    LatLng east = new LatLng(myLatLng.latitude, myLatLng.longitude + lngDifference);
	    builder.include(east);
	    LatLng west = new LatLng(myLatLng.latitude, myLatLng.longitude - lngDifference);
	    builder.include(west);

	    // Calculate north/south points that should to be included
	    // in the bounds
	    double latDifference = calculateLatLngOffset(myLatLng, true);
	    LatLng north = new LatLng(myLatLng.latitude + latDifference, myLatLng.longitude);
	    builder.include(north);
	    LatLng south = new LatLng(myLatLng.latitude - latDifference, myLatLng.longitude);
	    builder.include(south);

	    return builder.build();
	  }

	  @Override
	  public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu; this adds items to the action bar if it is present.
	    getMenuInflater().inflate(R.menu.main, menu);

	    menu.findItem(R.id.action_settings).setOnMenuItemClickListener(new OnMenuItemClickListener() {
	      public boolean onMenuItemClick(MenuItem item) {
	        
	        return true;
	      }
	    });
	    return true;
	  }

	  /*
	   * Show a dialog returned by Google Play services for the connection error code
	   */
	  private void showErrorDialog(int errorCode) {
	    // Get the error dialog from Google Play services
	    Dialog errorDialog =
	        GooglePlayServicesUtil.getErrorDialog(errorCode, this,
	            CONNECTION_FAILURE_RESOLUTION_REQUEST);

	    // If Google Play services can provide an error dialog
	    if (errorDialog != null) {

	      // Create a new DialogFragment in which to show the error dialog
	      ErrorDialogFragment errorFragment = new ErrorDialogFragment();

	      // Set the dialog in the DialogFragment
	      errorFragment.setDialog(errorDialog);

	      // Show the error dialog in the DialogFragment
	      errorFragment.show(getSupportFragmentManager(), Snypr.APPTAG);
	    }
	  }
	  
	  public static class ErrorDialogFragment extends DialogFragment {
		    // Global field to contain the error dialog
		    private Dialog mDialog;

		    /**
		     * Default constructor. Sets the dialog field to null
		     */
		    public ErrorDialogFragment() {
		      super();
		      mDialog = null;
		    }

		    /*
		     * Set the dialog to display
		     * 
		     * @param dialog An error dialog
		     */
		    public void setDialog(Dialog dialog) {
		      mDialog = dialog;
		    }
		    public Dialog onCreateDialog(Bundle savedInstanceState) {
		        return mDialog;
		      }
	  }
	

}