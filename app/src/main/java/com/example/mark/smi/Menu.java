package com.example.mark.smi;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;

import java.util.List;


//This class define de Menu and ActionBar to all the default activities
public class Menu extends ActionBarActivity {
    //Method to create the options in the Menu
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        //If the loader activity is NewTrack, hide the language action icon
        Log.i("Mensaje de nombre de clase", getApplicationContext().getClass().getCanonicalName());
       if (getApplicationContext().getClass().getName().contains("NewTrackActivity")) {
            menu.findItem(R.id.action_language).setVisible(false);
        }
        return true;
    }

    //Method to handle the option selected in the menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_language:
                language();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //When pressed, the action button will open the LanguageDialog
    public void language() {
        LanguageDialogFragment lang = new LanguageDialogFragment();
        lang.show(getSupportFragmentManager(),"lang");
    }
}
