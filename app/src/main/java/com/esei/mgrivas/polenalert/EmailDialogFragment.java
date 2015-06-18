package com.esei.mgrivas.polenalert;

//Class to handle the language of the app
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;

public class EmailDialogFragment extends DialogFragment {

    public EmailDialogFragment() {
        // Empty constructor required for DialogFragment
    }


    //This will create the Dialog with the availables languages
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Create the alertDialog
        final EditText txtUrl = new EditText(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        new AlertDialog.Builder(getActivity())
                .setTitle("Email")
                .setView(txtUrl)
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String url = txtUrl.getText().toString();
                        setEmail(url);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();

        // Create the AlertDialog object and return it
        return builder.create();
    }

    private void setEmail(String url) {
        final GlobalClass globalVariable = (GlobalClass) GlobalClass.getContext();
        globalVariable.setEmail(url);
    }

}
