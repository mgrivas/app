package com.esei.mgrivas.polenalert.Support;

//Class to handle the language of the app
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Button;

import com.esei.mgrivas.polenalert.R;

import java.util.Locale;

public class LanguageDialogFragment extends DialogFragment {

    public LanguageDialogFragment() {
        // Empty constructor required for DialogFragment
    }


    //This will create the Dialog with the availables languages
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Create the alertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // With this resource we get the languages string-array form the string.xml file
        Resources res = getResources();
        String[] languages = res.getStringArray(R.array.androidlanguages);

        //Initialize the alertDialog
        builder.setTitle(R.string.lang_tittle)
                .setItems(languages, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                language("en");
                                break;

                            case 1:
                                language("es");
                                break;
                        }
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }

    //Method to change the element's language from the Main Activity
    public void language(String lang) {
        Locale myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
        Button myButton = (Button) getActivity().findViewById(R.id.button_new_track);
        Button myButton3 = (Button) getActivity().findViewById(R.id.button_observer);
        Button myButton2 = (Button) getActivity().findViewById(R.id.button_list_track);
        Button myButton4 = (Button) getActivity().findViewById(R.id.button_show_map);
        myButton.setText(R.string.new_track);
        myButton2.setText(R.string.list_track);
        myButton3.setText(R.string.observer);
        myButton4.setText(R.string.show_map);
    }
}
