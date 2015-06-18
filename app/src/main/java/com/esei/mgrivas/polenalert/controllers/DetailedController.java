package com.esei.mgrivas.polenalert.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.esei.mgrivas.polenalert.Point;
import com.esei.mgrivas.polenalert.R;
import com.esei.mgrivas.polenalert.SqlDAO;
import com.esei.mgrivas.polenalert.Track;
import com.esei.mgrivas.polenalert.controllers.ListController;

import java.util.List;

//This activity is the controller for the detailed activity
public class DetailedController extends com.esei.mgrivas.polenalert.Menu {
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
