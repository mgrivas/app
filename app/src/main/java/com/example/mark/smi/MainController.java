package com.example.mark.smi;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;

//This activity is the controller for the activity_main layout
public class MainController extends com.example.mark.smi.Menu{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //When new track button pressed, Load the NewTrack activity
    public void NewTrack(View view) {
        Intent intent = new Intent(this, NewTrackController.class);
        startActivity(intent);
    }

    //When list tracks button pressed, Load the ListTrack activity
    public void ListTrack(View view) {
        Intent intent = new Intent(this, ListController.class);
        startActivity(intent);
    }

    //When show button pressed, Load the ShowMap activity
    public void ShowMap(View view) {
        Intent intent = new Intent(this, ShowMapActivity.class);
        startActivity(intent);
    }

    //When the observer button pressed, Load the Observer activity
    public void Observer(View view) {
        Intent intent = new Intent(this, Observer.class);
        startActivity(intent);
    }
}
