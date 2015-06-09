package com.example.mark.smi;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;


public class DetailedList extends com.example.mark.smi.Menu {
    private String name_selected;
    private String comment_selected;
    List<Point> points;

    Track track;

    private SqlDAO sql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_list);

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

        ArrayAdapter<Point> adapter = new ArrayAdapter<Point>(this, android.R.layout.simple_list_item_1, points);
        ListView list = (ListView) findViewById(R.id.list_point);
        list.setAdapter(adapter);

    }

    public void Cancel(View view) {
        super.onBackPressed();
    }

    public void Eliminar (View view) {
        deleteTrack(track);
        for (Point p : points) {
            deletePoint(p);
        }

        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }

    private void deleteTrack(Track track) {
        sql.deleteTrack(track);
    }

    private void deletePoint(Point point) {
        sql.deletePoint(point);
    }

    private void insertTrack() {
        track = new Track(name_selected);
        track.setComment(comment_selected);
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        String date = today.monthDay + " / " + today.month;
        track.setDate(date);

        Log.d("This is the output", track.getDate());

        track = sql.createTrack(track);
    }

    private void insertPoints() {
        for (Point point : points) {
            point.setTrackId(track.getId());
            sql.createPoint(point);
        }
    }
}
