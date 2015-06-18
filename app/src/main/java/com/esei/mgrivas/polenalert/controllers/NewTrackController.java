package com.esei.mgrivas.polenalert.controllers;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.esei.mgrivas.polenalert.R;

//This activity is the controller for the activity_new_track layout
public class NewTrackController extends com.esei.mgrivas.polenalert.Menu {

    //Variables to handle de views and intent extras
    EditText name;
    EditText comment;
    public final static String MESSAGE_NAME = "com.example.mark.smi.MESSAGE_NAME";
    public final static String MESSAGE_COMMENT = "com.example.mark.smi.MESSAGE_COMMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_track);
    }

    //Hide the language option in the menu
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action_language).setVisible(false);
        return true;
    }

    public void nextManual(View view) {
        //Get the EditText with the name and the comment
        name = (EditText) findViewById(R.id.edit_name);
        comment = (EditText) findViewById(R.id.edit_comments);

        //If the name value is empty we can't continue
        if (TextUtils.isEmpty(name.getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), R.string.name_empty, Toast.LENGTH_LONG).show();
        } else {
            //Create the message to the next activity and load it
            String message_name = name.getText().toString().trim();
            String message_comment = comment.getText().toString().trim();

            Intent intent = new Intent(this, ManualController.class);
            intent.putExtra(MESSAGE_NAME, message_name);
            intent.putExtra(MESSAGE_COMMENT, message_comment);
            startActivity(intent);
        }
    }

    public void nextGps(View view) {
        //Get the EditText with the name and the comment
        name = (EditText) findViewById(R.id.edit_name);
        comment = (EditText) findViewById(R.id.edit_comments);

        //If the name value is empty we can't continue
        if (TextUtils.isEmpty(name.getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), R.string.name_empty, Toast.LENGTH_LONG).show();
        } else {
            //Create the message to the next activity and load it
            String message_name = name.getText().toString().trim();
            String message_comment = comment.getText().toString().trim();

            Intent intent = new Intent(this, GpsController.class);
            intent.putExtra(MESSAGE_NAME, message_name);
            intent.putExtra(MESSAGE_COMMENT, message_comment);
            startActivity(intent);
        }

    }
}
