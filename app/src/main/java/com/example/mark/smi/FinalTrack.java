package com.example.mark.smi;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;


public class FinalTrack extends com.example.mark.smi.Menu {
    private String name_selected;
    private String comment_selected;
    List<Point> points;

    Track track;

    private SqlDAO sql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_track);

        Intent intent = getIntent();
        name_selected = intent.getStringExtra(GoogleController.MESSAGE_NAME);
        TextView textView = (TextView) findViewById(R.id.final_name_selected);
        textView.setText(name_selected);

        comment_selected = intent.getStringExtra(GoogleController.MESSAGE_COMMENT);
        TextView textView2 = (TextView) findViewById(R.id.final_comment_selected);
        textView2.setText(comment_selected);

        points = getIntent().getExtras().getParcelableArrayList("points");

        ArrayAdapter<Point> adapter = new ArrayAdapter<Point>(this, android.R.layout.simple_list_item_1, points);
        ListView list = (ListView) findViewById(R.id.list_point);
        list.setAdapter(adapter);

        sql = new SqlDAO(this);
        sql.open();
    }

    public void Cancel(View view) {
        super.onBackPressed();
    }

    public void Accept (View view) {
        insertTrack();
        insertPoints();

        Intent intent = new Intent(getApplicationContext(), MainController.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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
