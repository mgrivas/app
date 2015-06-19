package com.esei.mgrivas.polenalert.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.esei.mgrivas.polenalert.Support.Menu;
import com.esei.mgrivas.polenalert.entities.Point;
import com.esei.mgrivas.polenalert.R;
import com.esei.mgrivas.polenalert.Support.SqlDAO;
import com.esei.mgrivas.polenalert.entities.Track;

import java.util.List;

//This activity is the controller for the activity_main layout
public class FinalTrackController extends Menu {
    private String name_selected;
    private String comment_selected;
    List<Point> points;
    Track track;
    private SqlDAO sql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_track);

        //First we recieve the intent and set the variables
        Intent intent = getIntent();
        name_selected = intent.getStringExtra(GpsController.MESSAGE_NAME);
        TextView textView = (TextView) findViewById(R.id.final_name_selected);
        textView.setText(name_selected);

        comment_selected = intent.getStringExtra(GpsController.MESSAGE_COMMENT);
        TextView textView2 = (TextView) findViewById(R.id.final_comment_selected);
        textView2.setText(comment_selected);

        points = getIntent().getExtras().getParcelableArrayList("points");

        ArrayAdapter<Point> adapter = new ArrayAdapter<Point>(this, R.layout.list_layout, points);
        ListView list = (ListView) findViewById(R.id.list_point);
        list.setAdapter(adapter);

        //open the database connection
        sql = new SqlDAO(this);
        sql.open();
    }

    //Hide the language option in the menu
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action_language).setVisible(false);
        return true;
    }

    //If cancel pressed go back to gps
    public void Cancel(View view) {
        super.onBackPressed();
    }

    //If the user accepts, insert the tracks and points in the db and return to Main Menu
    public void Accept (View view) {
        insertTrack();
        insertPoints();

        Intent intent = new Intent(getApplicationContext(), MainController.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    //Method to complet the track values and insert it in the db
    private void insertTrack() {
        track = new Track(name_selected);
        track.setComment(comment_selected);
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        String date = today.monthDay + " / " + (today.month+1);
        track.setDate(date);

        track = sql.createTrack(track);
    }

    //Method to insert all the poinst in the db
    private void insertPoints() {
        for (Point point : points) {
            point.setTrackId(track.getId());
            sql.createPoint(point);
        }
    }
}
