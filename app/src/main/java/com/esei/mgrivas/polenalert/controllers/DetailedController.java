package com.esei.mgrivas.polenalert.controllers;

import android.content.Intent;
import android.os.Bundle;
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

//This activity is the controller for the detailed_list activity
public class DetailedController extends Menu {
    private String name_selected;
    private String comment_selected;
    List<Point> points;

    Track track;

    private SqlDAO sql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_list);

        //first we recieve the intent and set the UI elements with the content

        Intent intent = getIntent();

        track = (Track) intent.getExtras().getSerializable("track");
        name_selected = track.getName();
        TextView textView = (TextView) findViewById(R.id.final_name_selected);
        textView.setText(name_selected);

        comment_selected = track.getComment();
        TextView textView2 = (TextView) findViewById(R.id.final_comment_selected);
        textView2.setText(comment_selected);

        sql = new SqlDAO(this);
        sql.open();

        points = sql.getPoints(track.getId());

        ArrayAdapter<Point> adapter = new ArrayAdapter<Point>(this, R.layout.list_layout, points);
        ListView list = (ListView) findViewById(R.id.list_point);
        list.setAdapter(adapter);

    }

    //Hide the language option in the menu
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action_language).setVisible(false);
        return true;
    }

    //If the user press Cancel we go back to the List activity
    public void Cancel(View view) {
        Intent intent = new Intent(getApplicationContext(), ListController.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    //Delete the track and then its asociated points
    public void Delete (View view) {
        deleteTrack(track);
        for (Point p : points) {
            deletePoint(p);
        }

        //Redirect to ListController
        Intent intent = new Intent(getApplicationContext(), ListController.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    //Method to delete the track
    private void deleteTrack(Track track) {
        sql.deleteTrack(track);
    }


    //Method to delete the point
    private void deletePoint(Point point) {
        sql.deletePoint(point);
    }
}
