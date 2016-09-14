package com.daimler.peninsula.goeuro.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.daimler.peninsula.goeuro.R;

/**
 * Created by Mehul on 13/09/16.
 */
public class AlertDialogFragment extends DialogFragment {
    private static final String ARG_TITLE = "ARG_TITLE";
    String alertTitle;
    public AlertDialogFragment(){

    }

    /**
     * This method is used to create the instance of the AlertDialog Fragment
     * This alert dialog has only one button as Ok
     * @param title - Shows the title of the popup
     * @return
     */
    public static AlertDialogFragment getInstance(String title){
        AlertDialogFragment alertDialogFragment = new AlertDialogFragment();
        if(!TextUtils.isEmpty(title)) {
            Bundle bundle = new Bundle();
            bundle.putString(ARG_TITLE, title);
            alertDialogFragment.setArguments(bundle);
        }
        return alertDialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if(getArguments() != null){
            alertTitle = getArguments().getString(ARG_TITLE);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(alertTitle)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
