package com.example.mark.smi;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.text.format.Time;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

//This activity is the controller for the activity_gps layout using Gps service
public class ObserverMode extends com.example.mark.smi.Menu {
    GpsTracker gps;

    //Global variables
    private String name_selected;
    private String comment_selected;
    private TextView actual_location;
    private SqlDAO sql;

    public List<Point> points;
    //Flag to check if it's the first time to get the location
    private int flag = 0;
    //Counter to count the number of total unmatches
    private int count = 0;
    Track track;

    private int flag_accept = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        //first we recieve the extra data
        setVariables();

        sql = new SqlDAO(this);
        sql.open();

        points = sql.getPoints(track.getId());

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
        track = (Track) intent.getExtras().getSerializable("track");
        name_selected = track.getName();
        TextView textView = (TextView) findViewById(R.id.gps_name_selected);
        textView.setText(name_selected);

        comment_selected = track.getComment();
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

        Intent intent = new Intent(this, FinalTrackController.class);
        //intent.putExtra(MESSAGE_NAME, name_selected);
        //intent.putExtra(MESSAGE_COMMENT, comment_selected);
        //intent.putParcelableArrayListExtra("points", points);
        startActivity(intent);
    }

    public void addPoint(Point point) {
        points.add(point);
    }


    public class GpsTracker extends Service implements LocationListener {

        private final Context mContext;

        // flag for GPS status
        boolean isGPSEnabled = false;
        int minuto_actual;
        int hora_actual;

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
        public void stopUsingGPS(){
            if(locationManager != null){
                locationManager.removeUpdates(GpsTracker.this);
            }
        }

        public double getLatitude(){
            if(location != null){
                latitude = location.getLatitude();
            }

            // return latitude
            return latitude;
        }

        public double getLongitude(){
            if(location != null){
                longitude = location.getLongitude();
            }
            return longitude;
        }

        public boolean canGetLocation() {
            return this.canGetLocation;
        }

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

        private boolean compareTimes(Point p) {
           int minute_alt;
            if (minuto_actual < 10) {
                minute_alt = 10 - minuto_actual;
                Toast.makeText(mContext, "" + minute_alt,
                        Toast.LENGTH_SHORT).show();
                minute_alt = 60 - minute_alt;
                Toast.makeText(mContext, "" + minute_alt,
                        Toast.LENGTH_SHORT).show();
                if ((p.getHour() == hora_actual && p.getMinute() <= minuto_actual) || (p.getHour() == hora_actual - 1 && p.getMinute() >= minute_alt)) {
                    Toast.makeText(mContext, "dentro",
                            Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                    return false;
                }
            }

            if (minuto_actual > 50) {
                minute_alt = 60 - minuto_actual;
                Toast.makeText(mContext, "" + minute_alt,
                        Toast.LENGTH_SHORT).show();
                minute_alt = 10 - minute_alt;
                Toast.makeText(mContext, "" + minute_alt,
                        Toast.LENGTH_SHORT).show();
                if ((p.getHour() == hora_actual && p.getMinute() >= minuto_actual) || (p.getHour() == hora_actual + 1 && p.getMinute() <= minute_alt)) {
                    Toast.makeText(mContext, "dentro",
                            Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                    return false;
                }
            }

           if (p.getHour() == hora_actual && (p.getMinute() >= minuto_actual - 10 && p.getMinute() <= minuto_actual + 10)) {
                return true;
            }
            return false;
        }

        //What to do when the location change
        @Override
        public void onLocationChanged(Location location) {
            if (flag == 0) {
                actual_location.setText(R.string.ready);
                flag = 1;
                stopUsingGPS();
            } else {
                if (gps.canGetLocation()) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                    Toast.makeText(mContext, R.string.changed,
                            Toast.LENGTH_SHORT).show();

                    Time today = new Time(Time.getCurrentTimezone());
                    today.setToNow();
                    String minuto = Integer.toString(today.minute);
                    if (today.minute < 10) {
                        minuto = "0" + today.minute;
                    }
                    String hora = Integer.toString(today.hour);
                    if (today.hour < 10) {
                        hora = "0" + today.hour;
                    }
                    String time = hora + ":" + minuto;
                    String time2 = hora + ":" + minuto;

                    minuto_actual = today.minute;
                    hora_actual = today.hour;

                    for (Point point : points) {
                        if (compareTimes(point)){
                            Toast.makeText(mContext, "Punto encontrado",
                                    Toast.LENGTH_SHORT).show();
                            if (point.getLatitude() == latitude && point.getLongitude() == longitude) {
                                flag_accept = 1;
                            }
                        } else {

                        }
                    }

                    if (flag_accept == 0) {
                        count += 1;
                        Toast.makeText(mContext, "Fuera de lugar \nAviso: " + count,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, "Todo correcto \nAvisos: " + count,
                                Toast.LENGTH_SHORT).show();
                    }


                    Point punto = new Point(latitude, longitude, today.hour,today.minute);
                    addPoint(punto);
                    actual_location.setText("Hora: " + time + "\nLat: " + latitude + ", Long: " + longitude);
                    flag_accept = 0;
                } else {
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