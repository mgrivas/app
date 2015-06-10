package com.example.mark.smi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.healthmarketscience.jackcess.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ShowMapActivity extends com.example.mark.smi.Menu implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    static final LatLng HAMBURG = new LatLng(53.558, 9.927);
    private GoogleMap map;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);

        // First we need to check availability of play services
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();
            createLocationRequest();
        }

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();

        ArrayList<String> valores = new ArrayList<String>();
        try {
            Database db = DatabaseBuilder.open(new File("/storage/sdcard0/Download/REAdb.mdb"));
            Table table = db.getTable("Orense");
            for(Column column : table.getColumns()) {
                String columnName = column.getName();
                valores.add(columnName);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, valores);
            ListView list = (ListView) findViewById(R.id.list_point);
            list.setAdapter(adapter);
        } catch (IOException e) {

        }

        //Dibujar();
    }

    public void Dibujar() {
        // Instantiates a new Polyline object and adds points to define a rectangle
        Polygon polygon = map.addPolygon(new PolygonOptions()
                .add(new LatLng(53.558, 9.927), new LatLng(53.568, 9.927), new LatLng(53.568, 9.937))
                .strokeColor(Color.RED)
                .fillColor(Color.BLUE));
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
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
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
            startLocation();
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
        mRequestingLocationUpdates = false;
        stopLocationUpdates();
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(null, "Connection failed: ConnectionResult.getErrorCode() = "
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

    protected void startLocation() {
        if (!mGoogleApiClient.isConnected()) {
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }
    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
            mLastLocation = location;

            mLastLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);

            Toast.makeText(getApplicationContext(), "Location changed!",
                    Toast.LENGTH_SHORT).show();

        LatLng actual = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

        Marker now = map.addMarker(new MarkerOptions().position(actual).title("Actual"));
        // Move the camera instantly to hamburg with a zoom of 15.
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(actual, 15));

        // Zoom in, animating the camera.
        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

        mLastLocation= null;
    }
}



