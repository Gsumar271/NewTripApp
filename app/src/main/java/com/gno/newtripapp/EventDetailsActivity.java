package com.gno.newtripapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;


/**
 * Created on 10/20/17.
 */
public class EventDetailsActivity extends Activity
        implements
        EventDialogFragment.OnUpdateClickedListener,
        TimePickerFragment.TimeChangedListener
{

    private String fromOrTo;
    public final static int RESULT_DELETE = 2;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_fragment_layout);



            // During initial setup, plug in the details fragment.
            String newEvent = getIntent().getExtras().getString("event");
            String tripName = getIntent().getExtras().getString("tripName");
            int day = getIntent().getExtras().getInt("day");


            EventItem newEventItem = new EventItem(newEvent, tripName, day);

            newEventItem.fromTime = getIntent().getExtras().getInt("timeFrom");
            newEventItem.toTime = getIntent().getExtras().getInt("timeTo");
            newEventItem.created = getIntent().getExtras().getLong("created");
            position = getIntent().getExtras().getInt("position");



            EventDialogFragment details = EventDialogFragment.newInstance(this, newEventItem);

            getFragmentManager().beginTransaction().add(R.id.fragment_container, details, "details_fragment").commit();
    }


    public void onTimePickerClicked(View v, String fromTo){

        fromOrTo = fromTo;


       // getFragmentManager().beginTransaction().replace(R.id.fragment_container, tpf).addToBackStack(null).commit();

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        //Create and show dialog
        TimePickerFragment tpf = new TimePickerFragment();
        tpf.show(ft, "timepick_dialog");



    }


    public void updateActivity(EventItem event) {
        // TODO Auto-generated method stub


        Intent result = new Intent();
        result.putExtra("event", event.getEvent());
        result.putExtra("timeFrom", event.getFromTime());
        result.putExtra("timeTo", event.getToTime());
        result.putExtra("tripName", event.getTripName());
        result.putExtra("day", event.getDay());
        result.putExtra("position", position);
        result.putExtra("created", event.getCreated());



        setResult(RESULT_OK, result);
        finish();
    }


    public void cancelActivity() {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void deleteClicked(EventItem event){

        Intent result = new Intent();
        result.putExtra("created", event.getCreated());
        result.putExtra("position", position);

        setResult(RESULT_DELETE, result);
        finish();
    }

    public void mapClicked(){

        Intent intent = new Intent();
        intent.setClass(this, MapsActivity.class);

        startActivityForResult(intent, 1);


    }

    public void onTimeHasChanged(int hour, int min) {

        EventDialogFragment edf = (EventDialogFragment) getFragmentManager().
                findFragmentByTag("details_fragment");

        edf.updateTime(hour, fromOrTo);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        //remove current time dialog
        Fragment prevDialog = getFragmentManager().findFragmentByTag("timepick_dialog");
        ft.remove(prevDialog);


       // getFragmentManager().popBackStack();

       // FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(edf).attach(edf).commit();


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.main, menu);


        return true;
    }


}

