package com.feedhenry.maygurney;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class MarkerDialog extends DialogFragment {


    public interface MarkerDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, String val);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    MarkerDialogListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Add Marker");
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.input_dialog, null);
        final EditText input = (EditText)v.findViewById(R.id.marker_text);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(v)
                // Add action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogPositiveClick(MarkerDialog.this, input.getText().toString());
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(MarkerDialog.this);
                    }
                });
        return builder.create();
    }

    public void setMarkerDialogListener(MarkerDialogListener mListener) {
        this.mListener = mListener;
    }
}
