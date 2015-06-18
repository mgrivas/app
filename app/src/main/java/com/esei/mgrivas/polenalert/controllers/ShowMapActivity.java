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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.esei.mgrivas.polenalert.Point;
import com.esei.mgrivas.polenalert.R;
import com.esei.mgrivas.polenalert.SqlDAO;
import com.esei.mgrivas.polenalert.Zone;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.healthmarketscience.jackcess.*;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//This activity is the controller for the activity_gps layout using Gps service
public class ShowMapActivity extends com.esei.mgrivas.polenalert.Menu {
    GpsTracker gps;
    private GoogleMap map;
    Marker now;
    LatLng actual;

    //Global variables
    private String name;
    private String comment_selected;
    private TextView actual_location;
    public final static String MESSAGE_NAME = "com.example.mark.smi.MESSAGE_NAME";
    public final static String MESSAGE_COMMENT = "com.example.mark.smi.MESSAGE_COMMENT";

    public ArrayList<Point> points;
    //Flag to check if it's the first time to get the location
    private int flag = 0;
    List<Zone> zones;
    Database db;
    Polygon polygon;
    int value;
    int color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);

        gps = new GpsTracker(getApplicationContext());

        Location location = gps.getLocation();

        if (location != null) {
        } else {
            // leads to the settings because there is no last known location
            showSettingsAlert();
        }

        color = 0;
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();
        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

        final ArrayList<String> valores = new ArrayList<String>();
        try {
            db = DatabaseBuilder.open(new File("/storage/sdcard0/Download/REAdb.mdb"));
            Table table = db.getTable("Orense");
            for (Column column : table.getColumns()) {
                String columnName = column.getName();
                valores.add(columnName);
            }
            valores.remove(0);
            valores.remove(0);
            valores.remove(valores.size() - 1);

            SqlDAO sql = new SqlDAO(this);
            sql.open();

            zones = sql.getAllZones();

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, valores);
            ListView list = (ListView) findViewById(R.id.list_point);
            list.setAdapter(adapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    RemoveDraw();
                    extractData(valores.get(position));
                   //Dibujar();
                }
            });

        } catch (IOException e) {

        }
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

    private void extractData(String polen) {
        try {
            Table table_zone = db.getTable(name);
            Cursor cursor = CursorBuilder.createCursor(table_zone);
            Time today = new Time(Time.getCurrentTimezone());
            today.setToNow();
            Calendar calendar = Calendar.getInstance();
            calendar.set(today.year - 1, today.month, today.monthDay, 00, 00, 00);
            calendar.set(Calendar.HOUR_OF_DAY,0);
            calendar.set(Calendar.MILLISECOND,0);

            Date d = new Date();
            Date date2 = new Date();
            /*long milliseconds = 0;

            SimpleDateFormat f = new SimpleDateFormat("dd-MMM-yyyy");
            try {
                d = (Date) f.parse("01-January-2001");
                milliseconds = d.getTime();

            } catch (ParseException e) {

            }*/

            SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
            try {
                date2 = sdf.parse("01/01/2001");
            }catch (ParseException e) {

            }
            int min = 0;
            int max = 0;
            Date date = new Date(calendar.getTimeInMillis());
            for (Row row : table_zone) {
                if (((Integer) row.get(polen)) > (max)) {
                    max = (Integer) row.get(polen);
                }
            }
            boolean found = cursor.findFirstRow(table_zone.getColumn("Fecha"),new java.sql.Timestamp(date.getTime()));
            if (found) {
                Toast.makeText(getApplicationContext(), "Se ha encontrado la fecha!!!",
                        Toast.LENGTH_SHORT).show();
                value = (Integer) cursor.getCurrentRowValue(table_zone.getColumn(polen));
                Toast.makeText(getApplicationContext(), "Valor: " + value,
                        Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "El menor es: " + min,
                        Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "El mayor es: " + max,
                        Toast.LENGTH_SHORT).show();
                if (value < ((max*30)/100) || value == 0) {
                    color = 0x5561cc1e; //green
                } else if (value < ((max*70)/100)) {
                    color = 0x55d7e74d; //yellow
                } else {
                    color = 0x55f40743; //red
                }
                Toast.makeText(getApplicationContext(), "Fecha: " + date,
                        Toast.LENGTH_SHORT).show();

            }
        } catch (IOException e) {

        }
        Draw();
    }

    public void Draw() {
        // Instantiates a new Polyline object and adds points to define a rectangle
        polygon = map.addPolygon(new PolygonOptions()
                .add(new LatLng(actual.latitude - 0.1, actual.longitude - 0.1), new LatLng(actual.latitude + 0.1, actual.longitude - 0.1), new LatLng(actual.latitude + 0.1, actual.longitude + 0.1), new LatLng(actual.latitude - 0.1, actual.longitude + 0.1))
                .strokeWidth(0)
                .fillColor(color));
    }

    public void RemoveDraw() {
        if (color != 0) {
            polygon.remove();
        }
    }

    private void selectZone(){
        double x = 100;
        double y = 100;
        name = "nothing";

        //Eliminate the negative numbers
        for (Zone zone : zones) {
            zone.setLatitude(Math.abs(zone.getLatitude()));
            zone.setLongitude(Math.abs(zone.getLongitude()));
            }

        for (Zone zone : zones) {
            if ((x > Math.abs(gps.getLatitude() - zone.getLatitude())) && (y > Math.abs(gps.getLongitude() - zone.getLongitude()))) {
                x = gps.getLatitude() - zone.getLatitude();
                y = gps.getLongitude() - zone.getLongitude();
                name = zone.getName();
            }
        }

        Toast.makeText(getApplicationContext(), "Zona final: " + name,
                Toast.LENGTH_SHORT).show();
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
            Toast.makeText(mContext, "Location changed!",
                    Toast.LENGTH_SHORT).show();

            actual = new LatLng(location.getLatitude(), location.getLongitude());

            map.clear();

            now = map.addMarker(new MarkerOptions().position(actual).title("Actual"));
            // Move the camera instantly to hamburg with a zoom of 15.
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(actual, 15));


            selectZone();
            if (color != 0) {
                Draw();
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



