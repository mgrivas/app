package com.esei.mgrivas.polenalert.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.esei.mgrivas.polenalert.R;
import com.esei.mgrivas.polenalert.Support.SqlDAO;
import com.esei.mgrivas.polenalert.Support.Menu;
import com.esei.mgrivas.polenalert.entities.Track;

import java.util.List;

//This activity is the controller for the activity_new_track layout
public class ObserverListController extends Menu {

    private SqlDAO sql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        sql = new SqlDAO(this);
        sql.open();

        final List<Track> values = sql.getAllTracks();

        ArrayAdapter<Track> adapter = new ArrayAdapter<Track>(this, R.layout.list_layout_tracks, values);
        ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(getApplicationContext(), ObserverMode.class);
                Track track = values.get(position);
                //If you wanna send any data to nextActicity.class you can use
                i.putExtra("track", track);

                startActivity(i);
            }
        });
    }

    //Hide the language option in the menu
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action_language).setVisible(false);
        return true;
    }
}
