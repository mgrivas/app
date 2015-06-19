package com.esei.mgrivas.polenalert.Support;

import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.esei.mgrivas.polenalert.R;


//This class define de Menu and ActionBar to all the default activities
public class Menu extends ActionBarActivity {
    //Method to create the options in the Menu
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

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
            case R.id.action_email:
                showEmail();
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

    public void showEmail() {
        EmailDialogFragment email = new EmailDialogFragment();
        email.show(getSupportFragmentManager(),"email");
    }
}
