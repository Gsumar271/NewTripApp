package com.gno.newtripapp;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by eugenesumaryev on 2/7/18.
 */

public class TripPickerDialog extends DialogFragment {

    long _date;
    String dateText;
    Boolean from;
    String buttonClicked;
    String fromText, toText;

    public interface OnNewTripSelectedListener {
         void onNewTripSelected  (String tripName, String _from, String _to);
         void onDatePickerClicked(View v, String whichButton);
    }

    private OnNewTripSelectedListener onNewTripSelectedListener;
    /*
    static TripPickerDialog newInstance(long _dateSelected, String _buttonClicked) {

        TripPickerDialog tpd = new TripPickerDialog();

        //Supply new input as arguments
        Bundle args = new Bundle();
        args.putLong("_date", _dateSelected);
        args.putString("_button", _buttonClicked);
        tpd.setArguments(args);

        return tpd;
    }
    */

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


        //Initial set up
        fromText = "";
        toText = "";
        /*
        Date date;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");


        _date = getArguments().getLong("_date");
        buttonClicked = getArguments().getString("_button");

        date = new Date(_date);
        dateText = sdf.format(date);


        if(buttonClicked.equals("from")) {
            fromText = sdf.format(date);
        }
        else if (buttonClicked.equals("to")) {
            toText = sdf.format(date);
        }
        else
            ;
            */


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_trip_dialog, container, false);
        final EditText tripText = view.findViewById(R.id.myEditText2);
        tripText.setText("New Trip Name");

        Button fromButton = (Button)view.findViewById(R.id.from_button);
        fromButton.setText(fromText);
        fromButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //inform other classes which date picker button was clicked
                buttonClicked = "from";
                onNewTripSelectedListener.onDatePickerClicked(v, buttonClicked);
            }
        });

        Button toButton = (Button)view.findViewById(R.id.to_button);
        toButton.setText(toText);
        toButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //inform other classes which date picker button was clicked
                buttonClicked = "to";
                onNewTripSelectedListener.onDatePickerClicked(v, buttonClicked);
            }
        });


        Button okButton = (Button)view.findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // When okButton is clicked, call up to owning activity and save.
                //Activity will populate selected note with info
                String trip = tripText.getText().toString();
                onNewTripSelectedListener.onNewTripSelected(trip, fromText, toText);
                dismiss();

            }
        });

        Button cancelButton = (Button)view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // When cancelButton is clicked, just return.
                 dismiss();
                // onNewTripSelectedListener.cancelActivity();
            }
        });


        return view;
    }

    public void updateButtons(Date _date, String whichButton){

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");

        if(whichButton.equals("from")) {
            fromText = sdf.format(_date);
        }
        else if (whichButton.equals("to")) {
            toText = sdf.format(_date);
        }
        else
            ;


    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        try{
            onNewTripSelectedListener = (OnNewTripSelectedListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement OnNewItemAddedListener");
        }
    }

}
