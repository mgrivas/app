package com.example.mark.smi;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

//This activity is the controller for the activity_gps layout using Gps service
public class GpsController extends Activity {
    GpsTracker gps;

    //Global variables
    private String name_selected;
    private String comment_selected;
    private TextView actual_location;
    public final static String MESSAGE_NAME = "com.example.mark.smi.MESSAGE_NAME";
    public final static String MESSAGE_COMMENT = "com.example.mark.smi.MESSAGE_COMMENT";

    public ArrayList<Point> points;
    //Flag to check if it's the first time to get the location
    private int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        //first we recieve the extra data
        setVariables();

        points = new ArrayList<Point>();

        gps = new GpsTracker(getApplicationContext());

        Location location = gps.getLocation();

        if (location != null) {
            actual_location.setText(R.string.ready);
        } else {
            // leads to the settings because there is no last known location
            gps.showSettingsAlert();
        }
    }

    private void setVariables() {
        Intent intent = getIntent();
        name_selected = intent.getStringExtra(NewTrackController.MESSAGE_NAME);
        TextView textView = (TextView) findViewById(R.id.gps_name_selected);
        textView.setText(name_selected);

        comment_selected = intent.getStringExtra(NewTrackController.MESSAGE_COMMENT);
        TextView textView2 = (TextView) findViewById(R.id.gps_comment_selected);
        textView2.setText(comment_selected);

        actual_location = (TextView) findViewById(R.id.gps_location_selected);
        actual_location.setText(R.string.gps_wait);
    }

    //Method to start the location
    public void start(View view) {
        ImageButton btn_stop = (ImageButton)findViewById(R.id.newtrack_stop);
        btn_stop.setImageResource(R.drawable.ic_action_stop);
        ImageButton btn_pause = (ImageButton)findViewById(R.id.newtrack_pause);
        btn_pause.setImageResource(R.drawable.ic_action_pause);
        ImageButton btn_start = (ImageButton)findViewById(R.id.newtrack_play);
        btn_start.setImageResource(R.drawable.ic_action_play_false);

        gps.getLocation();

    }

    //Pause the location updates
    public void pauseLocation(View view) {
        ImageButton btn_pause = (ImageButton)findViewById(R.id.newtrack_pause);
        btn_pause.setImageResource(R.drawable.ic_action_pause_false);
        ImageButton btn_start = (ImageButton)findViewById(R.id.newtrack_play);
        btn_start.setImageResource(R.drawable.ic_action_play);

        gps.stopUsingGPS();
        actual_location.setText(R.string.paused);
    }

    //Stop requesting location, start next activity
    public void stop(View view) {
        gps.stopUsingGPS();

        Intent intent = new Intent(this, FinalTrack.class);
        intent.putExtra(MESSAGE_NAME, name_selected);
        intent.putExtra(MESSAGE_COMMENT, comment_selected);
        intent.putParcelableArrayListExtra("points", points);
        startActivity(intent);
    }

    public void addPoint(Point point) {
        points.add(point);
    }


    public class GpsTracker extends Service implements LocationListener {

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
            getLocation();
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
                        Log.d("Network", "Network");
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
                            Log.d("GPS Enabled", "GPS Enabled");
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

        /**
         * Stop using GPS listener
         * Calling this function will stop using GPS in your app
         * */
        public void stopUsingGPS(){
            if(locationManager != null){
                locationManager.removeUpdates(GpsTracker.this);
            }
        }

        /**
         * Function to get latitude
         * */
        public double getLatitude(){
            if(location != null){
                latitude = location.getLatitude();
            }

            // return latitude
            return latitude;
        }

        /**
         * Function to get longitude
         * */
        public double getLongitude(){
            if(location != null){
                longitude = location.getLongitude();
            }

            // return longitude
            return longitude;
        }

        /**
         * Function to check GPS/wifi enabled
         * @return boolean
         * */
        public boolean canGetLocation() {
            return this.canGetLocation;
        }

        /**
         * Function to show settings alert dialog
         * On pressing Settings button will lauch Settings Options
         * */
        public void showSettingsAlert(){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

            // Setting Dialog Title
            alertDialog.setTitle("GPS is settings");

            // Setting Dialog Message
            alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

            // On pressing Settings button
            alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    mContext.startActivity(intent);
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

        @Override
        public void onLocationChanged(Location location) {
            if (flag == 0) {
                actual_location.setText(R.string.ready);
                flag = 1;
                stopUsingGPS();
            } else {

                if (gps.canGetLocation()) {
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();

                    Toast.makeText(mContext, "Location changed!",
                            Toast.LENGTH_SHORT).show();

                    Time today = new Time(Time.getCurrentTimezone());
                    today.setToNow();
                    String minuto = Integer.toString(today.minute);
                    if (today.minute < 10) {
                        minuto = "0" + today.minute;
                    }
                    String time = today.hour + ":" + minuto;
                    Point p = new Point(latitude, longitude, time);
                    addPoint(p);
                    actual_location.setText("Hora: " + today.hour + ":" + today.minute + "\nLat: " + latitude + ", Long: " + longitude);
                } else {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }
            }
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
