package com.gno.newtripapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * Created on 10/21/17.
 */
public class EventDialogFragment extends Fragment {


    private static String DIALOG_EVENT = "DIALOG_EVENT";
    private static String DIALOG_TIME_FROM = "DIALOG_TIME_FROM";
    private static String DIALOG_TIME_TO = "DIALOG_TIME_TO";
    private static String DIALOG_TRIP_NAME = "DIALOG_TRIP_NAME";
    private static String DIALOG_DAY = "DIALOG_DAY";
    private static String DIALOG_CREATED = "DIALOG_CREATED";

    private String fromOrTo;
    private static EventItem eventItem;
    private static Button fromButton, toButton;
    private EditText eventText;

  //  private int returned = 0;

    public interface OnUpdateClickedListener {
        public void updateActivity(EventItem event);
        public void cancelActivity();
        public void deleteClicked(EventItem event);
        public void onTimePickerClicked(View v, String fromOrTo);
        public void mapClicked();
    }

    private OnUpdateClickedListener onUpdateClickedListener;

    public static EventDialogFragment newInstance(Context context, EventItem _event) {

        // Create a new Fragment instance with the specified
        // parameters.
        EventDialogFragment fragment = new EventDialogFragment();
        Bundle args = new Bundle();

        eventItem = _event;
       // SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
       // SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
       // String dateString = sdf.format(todoItem.getCreated());

        String eventText = eventItem.getEvent();

        int fromTime = eventItem.getFromTime();
        int toTime = eventItem.getToTime();
        String trioName = eventItem.getTripName();
        int day = eventItem.getDay();



        args.putString(DIALOG_EVENT, eventText);
        args.putInt(DIALOG_TIME_FROM, fromTime);
        args.putInt(DIALOG_TIME_TO, toTime);
        args.putString(DIALOG_TRIP_NAME, trioName);
        args.putInt(DIALOG_DAY, day);

        fragment.setArguments(args);


        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.event_details_layout, container, false);

        String newEvent = getArguments().getString(DIALOG_EVENT);
        //int stringLimit = (newTodo.length()<10) ? newTodo.length() : 10;
        //newTodo = newTodo.trim().substring(0, stringLimit);

        int newFromTime = getArguments().getInt(DIALOG_TIME_FROM);
        int newToTime = getArguments().getInt(DIALOG_TIME_TO);
        String newTripName = getArguments().getString(DIALOG_TRIP_NAME);
        int newDay = getArguments().getInt(DIALOG_DAY);



        eventText = (EditText)view.findViewById(R.id.content_text);
        eventText.setText(newEvent);


        //Set FromTime button to set up the start time of the event
        fromButton = (Button)view.findViewById(R.id.from_button);
        //check if user updated TimePicker and update from Button
       // if (returned == 1) {
        fromButton.setText(String.valueOf(eventItem.getFromTime()));
        //    returned = 0;
       // }
        fromButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //inform other classes which time picker button was clicked
                fromOrTo = "from";
                onUpdateClickedListener.onTimePickerClicked(v, fromOrTo);
            }
        });

        //Set ToTime button to set up the finish time of the event
        toButton = (Button)view.findViewById(R.id.to_button);
        //check if user updated TimePicker and update to Button
       // if (returned == 2) {
        toButton.setText(String.valueOf(eventItem.getToTime()));
        //    returned = 0;
       // }
        toButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //inform other classes which time picker button was clicked
                fromOrTo = "to";
                onUpdateClickedListener.onTimePickerClicked(v, fromOrTo);
            }
        });



        Button okButton = (Button)view.findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // When okButton is clicked, call up to owning activity and save.
                //okClicked;
                //Activity will populate selected note with info

                eventItem.event = eventText.getText().toString();

                //  onUpdateClickedListener = (OnUpdateClickedListenetodotActivity();

                onUpdateClickedListener.updateActivity(eventItem);
                // dismiss();

            }
        });

        Button cancelButton = (Button)view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // When cancelButton is clicked, just return.
                // dismiss();
                //  onUpdateClickedListener = (OnUpdateClickedListener)getActivity();
                onUpdateClickedListener.cancelActivity();
            }
        });

        Button mapButton = (Button)view.findViewById(R.id.map_button);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUpdateClickedListener.mapClicked();
            }
        });

        Button deleteButton = (Button)view.findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUpdateClickedListener.deleteClicked(eventItem);
            }
        });



        return view;
    }


    public void updateTime(int hour, String fromTo){

        if (fromTo == "from") {
            eventItem.fromTime = hour;
           // fromButton.setText(String.valueOf(hour));
        }
        else {
            eventItem.toTime = hour;
           // toButton.setText(String.valueOf(hour));
        }


    }



    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        try{
            onUpdateClickedListener = (OnUpdateClickedListener)getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement OnDateClickedListener");
        }
    }


}

