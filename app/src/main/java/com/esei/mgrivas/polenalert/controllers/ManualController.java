package com.esei.mgrivas.polenalert.controllers;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.text.format.Time;
import android.view.View;
import android.widget.Toast;

import com.esei.mgrivas.polenalert.Support.Menu;
import com.esei.mgrivas.polenalert.entities.Point;
import com.esei.mgrivas.polenalert.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class ManualController extends Menu {

    GpsTracker gps;
    private GoogleMap map;
    Marker now;
    int flag;
    Location previa;
    Location actual;
    Point p;
    float minute;
    int hour;

    private String name_selected;
    private String comment_selected;

    public ArrayList<Point> points;
    public final static String MESSAGE_NAME = "com.example.mark.smi.MESSAGE_NAME";
    public final static String MESSAGE_COMMENT = "com.example.mark.smi.MESSAGE_COMMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);
        flag = 0;
        points = new ArrayList<Point>();

        previa = new Location("previa");
        actual = new Location("actual");

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();

        Intent intent = getIntent();
        name_selected = intent.getStringExtra(NewTrackController.MESSAGE_NAME);
        comment_selected = intent.getStringExtra(NewTrackController.MESSAGE_COMMENT);

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng loc) {
                if (flag == 0) {
                    map.clear();
                    map.addMarker(new MarkerOptions().position(loc).title("Actual"));
                    // TODO Auto-generated method stub
                    Toast.makeText(getApplicationContext(), "Latitud: " + loc.latitude + "longitud: " + loc.longitude,
                            Toast.LENGTH_SHORT).show();
                    Time today = new Time(Time.getCurrentTimezone());
                    today.setToNow();
                    minute = Math.round(today.minute);
                    hour = today.hour;
                    Point p = new Point(loc.latitude,loc.longitude,today.hour,today.minute);
                    addPoint(p);
                    Toast.makeText(getApplicationContext(), "Minuto actual: " + today.minute + " y miniuto mas 3: " + minute,
                            Toast.LENGTH_SHORT).show();
                    previa.setLatitude(loc.latitude);
                    previa.setLongitude(loc.longitude);
                    flag = 1;
                } else {
                    actual.setLatitude(loc.latitude);
                    actual.setLongitude(loc.longitude);
                    float tiempo = distance() / 166;
                    Time today = new Time(Time.getCurrentTimezone());
                    today.setToNow();
                    minute = today.minute;
                    hour = today.hour;
                    if ((minute + tiempo) >= 60) {
                        minute = 60 - today.minute;
                        minute = tiempo - minute;
                        minute = 0 + minute;
                        if (today.hour == 24) {
                            hour = 00;
                        } else {
                            hour = today.hour + 1;
                        }
                    } else {
                        minute = minute + tiempo;
                    }
                    Point p = new Point(loc.latitude,loc.longitude,hour,Math.round(minute));
                    addPoint(p);
                    map.clear();
                    map.addMarker(new MarkerOptions().position(loc).title("Actual"));
                    // TODO Auto-generated method stub
                    Toast.makeText(getApplicationContext(), "" + tiempo,
                            Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "Hora : " + hour + " y minuto: " + minute,
                            Toast.LENGTH_SHORT).show();
                    previa.setLatitude(loc.latitude);
                    previa.setLongitude(loc.longitude);
                    flag = 1;
                }
            }
        });

        gps = new GpsTracker(getApplicationContext());

        Location location = gps.getLocation();

        if (location != null) {
        } else {
            // leads to the settings because there is no last known location
            showSettingsAlert();
        }


        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
    }

    //Stop requesting location, start next activity
    public void stop(View view) {
        gps.stopUsingGPS();

        Intent intent = new Intent(this, FinalTrackController.class);
        intent.putExtra(MESSAGE_NAME, name_selected);
        intent.putExtra(MESSAGE_COMMENT, comment_selected);
        intent.putParcelableArrayListExtra("points", points);
        startActivity(intent);
    }

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public void addPoint(Point point) {
        points.add(point);
    }

    private float distance() {


        return previa.distanceTo(actual);
    }

    public class GpsTracker extends Service implements android.location.LocationListener {

        private final Context mContext;

        // flag for GPS status
        boolean isGPSEnabled = false;

        // flag for network status
        boolean isNetworkEnabled = false;

        // flag for GPS status
        boolean canGetLocation = false;

        Location location; // location
        double latitude; // latitude
        double longitude; // longitude

        // The minimum distance to change Updates in meters
        private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 10 meters

        // The minimum time between updates in milliseconds
        private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

        // Declaring a Location Manager
        protected LocationManager locationManager;

        public GpsTracker(Context context) {
            this.mContext = context;
        }

        public Location getLocation() {
            try {
                locationManager = (LocationManager) mContext
                        .getSystemService(LOCATION_SERVICE);

                // getting GPS status
                isGPSEnabled = locationManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);

                // getting network status
                isNetworkEnabled = locationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (!isGPSEnabled && !isNetworkEnabled) {
                    // no network provider is enabled
                } else {
                    this.canGetLocation = true;
                    // First get location from Network Provider
                    if (isNetworkEnabled) {
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                    // if GPS Enabled get lat/long using GPS Services
                    if (isGPSEnabled) {
                        if (location == null) {
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            if (locationManager != null) {
                                location = locationManager
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return location;
        }

        //Stop the GPS
        public void stopUsingGPS() {
            if (locationManager != null) {
                locationManager.removeUpdates(GpsTracker.this);
            }
        }

        public double getLatitude() {
            if (location != null) {
                latitude = location.getLatitude();
            }

            // return latitude
            return latitude;
        }

        public double getLongitude() {
            if (location != null) {
                longitude = location.getLongitude();
            }
            return longitude;
        }

        public boolean canGetLocation() {
            return this.canGetLocation;
        }

        //What to do when the location change
        @Override
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            LatLng actual = new LatLng(location.getLatitude(), location.getLongitude());

            now = map.addMarker(new MarkerOptions().position(actual).title("Actual"));
            // Move the camera instantly to hamburg with a zoom of 15.
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(actual, 15));

            stopUsingGPS();
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public IBinder onBind(Intent arg0) {
            return null;
        }

    }
}
