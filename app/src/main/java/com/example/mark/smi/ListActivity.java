package com.example.mark.smi;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;


public class ListActivity extends com.example.mark.smi.Menu {

    private SqlDAO sql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        sql = new SqlDAO(this);
        sql.open();

        final List<Track> values = sql.getAllTracks();

        ArrayAdapter<Track> adapter = new ArrayAdapter<Track>(this, android.R.layout.simple_list_item_1, values);
        ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(getApplicationContext(), DetailedList.class);
                Track track = values.get(position);
                //If you wanna send any data to nextActicity.class you can use
                i.putExtra("track", track);

                startActivity(i);
            }
        });


        /*
        List<Point> values2 = sql.getAllPoints();
        ArrayAdapter<Point> adapter2 = new ArrayAdapter<Point>(this, android.R.layout.simple_list_item_1, values2);
        ListView list2 = (ListView) findViewById(R.id.list_point);
        list2.setAdapter(adapter2); */
    }
}
