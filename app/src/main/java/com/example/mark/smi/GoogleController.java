package com.example.mark.smi;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

//This activity is the controller for the activity_gps layout using Google Play Services
public class GoogleController extends com.example.mark.smi.Menu implements ConnectionCallbacks,
        OnConnectionFailedListener, LocationListener {

    //global variables to handle Google API
    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 60000; // 60 sec
    private static int FATEST_INTERVAL = 10000; // 10 sec
    private static int DISPLACEMENT = 50; // 50 meters
    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private Location mLastLocation;

    //Other variables
    private String name_selected;
    private String comment_selected;
    private TextView actual_location;
    //Flag to check if it's the first time to get the location
    private int flag = 0;
    public ArrayList<Point> points;
    public final static String MESSAGE_NAME = "com.example.mark.smi.MESSAGE_NAME";
    public final static String MESSAGE_COMMENT = "com.example.mark.smi.MESSAGE_COMMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        //first we recieve the extra data
        Intent intent = getIntent();
        name_selected = intent.getStringExtra(NewTrackController.MESSAGE_NAME);
        TextView textView = (TextView) findViewById(R.id.gps_name_selected);
        textView.setText(name_selected);

        comment_selected = intent.getStringExtra(NewTrackController.MESSAGE_COMMENT);
        TextView textView2 = (TextView) findViewById(R.id.gps_comment_selected);
        textView2.setText(comment_selected);

        actual_location = (TextView) findViewById(R.id.gps_location_selected);
        actual_location.setText(R.string.gps_wait);

        points = new ArrayList<Point>();

        // First we need to check availability of play services
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();
            createLocationRequest();
        }

    }

     // Method to verify if google play services is available on the device
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        R.string.not_supported, Toast.LENGTH_LONG)
                        .show();
                actual_location.setText(R.string.not_supported);
                finish();
            }
            return false;
        }
        return true;
    }


     // Creating google api client object
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

     // Creating location request object
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    //On start we connect the API
    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();

        // Resuming the periodic location updates
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            actual_location.setText("Gps listo. Pulse start");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            //stopLocationUpdates();
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocation();
    }

     //Google api callback methods
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(null,"Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {
        // Once connected with google api, get the location
        mRequestingLocationUpdates = true;
        startLocation();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    //Start requesting location updates
    protected void startLocation() {
        if (!mGoogleApiClient.isConnected()) {
            actual_location.setText(R.string.still_activating);
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }
    }


     //Stop rquesting location updates
    protected void stopLocation() {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }
    }

    //Method to start the location
    public void start(View view) {
        ImageButton btn_stop = (ImageButton)findViewById(R.id.newtrack_stop);
        btn_stop.setImageResource(R.drawable.ic_action_stop);
        ImageButton btn_pause = (ImageButton)findViewById(R.id.newtrack_pause);
        btn_pause.setImageResource(R.drawable.ic_action_pause);
        ImageButton btn_start = (ImageButton)findViewById(R.id.newtrack_play);
        btn_start.setImageResource(R.drawable.ic_action_play_false);

        mRequestingLocationUpdates = true;

        startLocation();
    }

    //Pause the location updates
    public void pauseLocation(View view) {
        ImageButton btn_pause = (ImageButton)findViewById(R.id.newtrack_pause);
        btn_pause.setImageResource(R.drawable.ic_action_pause_false);
        ImageButton btn_start = (ImageButton)findViewById(R.id.newtrack_play);
        btn_start.setImageResource(R.drawable.ic_action_play);

        mRequestingLocationUpdates = false;

        stopLocation();
        actual_location.setText(R.string.paused);
    }

    //Stop requesting location, start next activity
    public void stop(View view) {
        mRequestingLocationUpdates = false;

        stopLocation();

        Intent intent = new Intent(this, FinalTrack.class);
        //Bundle bundle = new Bundle();
        intent.putExtra(MESSAGE_NAME, name_selected);
        intent.putExtra(MESSAGE_COMMENT, comment_selected);
        intent.putParcelableArrayListExtra("points", points);
        startActivity(intent);
    }

    //What to do when the location change
    @Override
    public void onLocationChanged(Location location) {
        //At first time, we say we are reading to start the track
        if (flag == 0) {
            actual_location.setText(R.string.ready);
                flag = 1;
            stopLocation();
        } else {
            mLastLocation = location;

            mLastLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);

            Toast.makeText(getApplicationContext(), R.string.changed,
                    Toast.LENGTH_SHORT).show();

            Time today = new Time(Time.getCurrentTimezone());
            today.setToNow();
            String minuto = Integer.toString(today.minute);
            if (today.minute < 10) { minuto = "0" + today.minute; }
            String time = today.hour + ":" + minuto;
            Point p = new Point(mLastLocation.getLatitude(),mLastLocation.getLongitude(),time);
            addPoint(p);

            actual_location.setText("Hora: " + time + "\nLat: " + mLastLocation.getLatitude() + ", Long: " + mLastLocation.getLongitude());
        }

        mLastLocation= null;
    }

    private void addPoint(Point point) {
        points.add(point);
    }
}
