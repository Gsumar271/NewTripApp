package com.gno.newtripapp;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by eugenesumaryev on 2/1/18.
 */

public class MainTripActivity extends AppCompatActivity
        implements ItemFragment.OnNewItemSelectedListener,
        DayItemFragment.OnNewDaySelectedListener,
        EventItemFragment.OnNewEventSelectedListener,
        TripPickerDialog.OnNewTripSelectedListener,
        DatePickerFragment.OnNewDateSelectedListener,
        LoaderManager.LoaderCallbacks<Cursor>
{

    private TextView mTextMessage;


    private TripNameAdapter tna;
    private DaysAdapter da;
    private EventItemAdapter eia;


  //  private List<String> trips;
   // private ArrayList<Integer> daysInt;
    private ArrayList<String> days;
   // private String tripName="";
   // private ArrayList<EventItem> allEvents;



    private ArrayList<EventItem> events;
    private String whichButtonClicked;

    private ArrayList <Trip> allTrips;
    private Trip trip;
    private int day;


   // private int dayCounter = 0;
    private int resID;

    FragmentManager fm = getFragmentManager();
    ItemFragment itemFragment;


    public enum ItemType{
        TRIP, DAY, EVENT
    }
    ItemType itemType = ItemType.TRIP;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentManager fm = getFragmentManager();

       // itemFragment = (ItemFragment) fm.findFragmentById(R.id.ItemFragment);

        // Create the arraylist of Trips
        allTrips = new ArrayList<Trip>();
        //Create arraylist of Days
        days = new ArrayList<String>();
        //Create arraylist of Events to use for each day
        events = new ArrayList<EventItem>();
        //Create arrayList of all the Events
      //  allEvents = new ArrayList<EventItem>();
        //Create a default trip
        trip = new Trip("",0);
        day = 0;
        whichButtonClicked = "";

        itemFragment = new ItemFragment();

        resID = R.layout.trip_name;
        tna = new TripNameAdapter(this, resID, allTrips);
        //Bind the array adapter to the listview/listfragment .
        itemFragment.setListAdapter(tna);

        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fragment_container, itemFragment);
        ft.commit();

        getLoaderManager().initLoader(0, null, this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (itemType){
                    case TRIP:
                        /*
                        String name =  "New Trip " + dayCounter;
                        Trip thisTrip = new Trip(name, dayCounter);
                        //insert trip into alltrips array
                        allTrips.add(thisTrip);
                        dayCounter++;
                        tna.notifyDataSetChanged();
                        updateTrip(thisTrip);
                        */
                        openTripPickerDialog(false);
                        break;
                    case DAY:
                        //set number of days as one additional day
                        trip.setDays(trip.getDays()+1);
                        //update the fragment view
                        da.newItems(trip.getNumDays());
                        updateTrip(trip);
                        //  Log.v("secndnumofDays", ""+days.size());
                      //  dayCounter++;
                        break;
                    case EVENT:
                        //get the name of the trip and the number of days
                        String tripName2 = trip.getTripName();
                        int numOfDays2 = day;

                        EventItem event = new EventItem("New Event", tripName2, numOfDays2);
                        events.add(event);
                     //   allEvents.add(event);
                        updateAllEvents(event);

                        eia.notifyDataSetChanged();
                        break;
                }
            }
        });

        //tell the program it should check whether to display the new trip dialog
        boolean shouldCheck = true;
        openTripPickerDialog(shouldCheck);
    }

    public void openTripPickerDialog(boolean shouldCheck){
       // long initialDays = 0;
       // String initialButton = "";
        boolean mustShow = true;

        //this checks whether dialog should be displayed
        //it's displayed only if no trips are present
        //or user pressed addbutton. shouldCheck var is used
        //to tell the program if it should check for trips size
        if (shouldCheck){
            if (allTrips.size() == 0)
                mustShow = false;
        }


        // Create the fragment and show it as a dialog.
       // DialogFragment newTripFragment = TripPickerDialog.newInstance(initialDays, initialButton);
        //mustshow indicates dialog must be shown
        if(mustShow) {
            DialogFragment newTripFragment = new TripPickerDialog();
            newTripFragment.show(getFragmentManager(), "trip_dialog");
        }
    }

    public void onDatePickerClicked(View v, String whichButton){

       // whichButtonClicked = whichButton;
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        //remove current trip dialog
        Fragment prevDialog = getFragmentManager().findFragmentByTag("trip_dialog");
        ft.remove(prevDialog);
        ft.addToBackStack(null);
       // ft.commit();

        //Create and show dialog
        DatePickerFragment dpf = new DatePickerFragment();
        dpf.changeClickedButton(whichButton);
        dpf.show(ft, "datepick_dialog");

    }


    public void onNewDateSelected(Date dateSelected, String whichButton)
    {
      // long newDate = dateSelected.getTime();

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        //remove current trip dialog
        Fragment prevDialog = getFragmentManager().findFragmentByTag("datepick_dialog");
        ft.remove(prevDialog);
        //ft.addToBackStack(null);
        //getFragmentManager().popBackStack();

        TripPickerDialog tpd =(TripPickerDialog) getFragmentManager().findFragmentByTag("trip_dialog");
        tpd.updateButtons(dateSelected, whichButton);

        getFragmentManager().popBackStack();

        //Create and show dialog
       // DialogFragment newTripFragment = TripPickerDialog.newInstance(newDate, whichButton);
       // newTripFragment.show(getFragmentManager(), "trip_dialog");


    }

    public void onNewTripSelected(String _trip, String fromDate, String toDate) {
        int _numDays;
        Trip newTrip;

      /*
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prevDialog = getFragmentManager().findFragmentByTag("trip_dialog");
        ft.remove(prevDialog);
        ft.commit();
        */

        //Convert returned String values to Dates
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");

        try {
            Date firstDate = sdf.parse(fromDate);
            Date secondDate = sdf.parse(toDate);


            //Calculate the difference between dates
            long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            _numDays = (int) diff + 1;

            newTrip = new Trip(_trip, _numDays);

            updateTrip(newTrip);

         //   Log.v("newtrip", newTrip.getTripName());
        }
        catch (ParseException pEx){
            Log.e("error", pEx.getMessage());
        }

      //  newTrip = new Trip(_trip, 0);

      //  updateTrip(newTrip);
    }


    @Override
    public void onNewItemSelected(int itemsId) {

        //get the trip
        trip = allTrips.get(itemsId);
        //get the list of integers in that trip
        days = trip.getNumDays();
      //  Log.v("firstnumofDays", ""+days.size());

        DayItemFragment dayItemFragment = new DayItemFragment();

        resID = R.layout.day_list_row;
        da = new DaysAdapter(this, resID, trip.getNumDays());
        dayItemFragment.setListAdapter(da);
        itemType = ItemType.DAY;

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        ft.replace(R.id.fragment_container, dayItemFragment);
        ft.addToBackStack(null);

        // Commit the transaction
        ft.commit();

        da.notifyDataSetChanged();

    }

    @Override
    public void onNewDaySelected(int itemsId) {


        events.clear();

        day = itemsId;


        EventItemFragment eif = new EventItemFragment();

        resID = R.layout.event_list_row;
        eia = new EventItemAdapter(this, resID, events);
        eif.setListAdapter(eia);

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        ft.replace(R.id.fragment_container, eif);
        ft.addToBackStack(null);

        // Commit the transaction
        ft.commit();

        itemType = ItemType.EVENT;

        getLoaderManager().restartLoader(1, null, this);

      //  eia.notifyDataSetChanged();
    }

    @Override
    public void onNewEventSelected(int itemsId) {
        EventItem event = events.get(itemsId);
        //extract eventItem and send it to this method
        startDetailsActivity(event, itemsId);
        itemType = ItemType.TRIP;
    }

    @Override
    public void onBackPressed(){
        fm.popBackStack();
        switch(itemType) {
            case EVENT:
                itemType = ItemType.DAY;
                break;
            case DAY:
                itemType = ItemType.TRIP;
                break;
            default:
                break;

        }

    }

    private void updateTrip(Trip trip){

        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();

        switch(itemType) {
            case TRIP:
                values.put(TripContentProvider.KEY_TRIP_NAME, trip.getTripName());
                values.put(TripContentProvider.KEY_NUM_OF_DAYS, trip.getDays());
                cr.insert(TripContentProvider.CONTENT_URI_TRIPS, values);
                break;
            case DAY:
               // String where = TripContentProvider.KEY_TRIP_NAME + " = " + trip.getTripName();
                String where = TripContentProvider.KEY_TRIP_NAME + " LIKE ? ";
                String[] args = {trip.getTripName()};
                values.put(TripContentProvider.KEY_NUM_OF_DAYS, trip.getDays());
                cr.update(TripContentProvider.CONTENT_URI_TRIPS, values, where, args);
                break;
            default:
                break;
        }

        getLoaderManager().restartLoader(0, null, this);


    }

    private void updateAllEvents(EventItem event){
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();

        values.put(TripContentProvider.KEY_EVENT, event.getEvent());
        values.put(TripContentProvider.KEY_TRIP_NAME, event.getTripName());
        values.put(TripContentProvider.KEY_DAY, event.getDay());
        values.put(TripContentProvider.KEY_TIME_FROM, event.getFromTime());
        values.put(TripContentProvider.KEY_TIME_TO, event.getToTime());
        values.put(TripContentProvider.KEY_CREATED, event.getCreated());

        String where = TripContentProvider.KEY_CREATED + " = " + event.getCreated();

        //If event is new, insert it into provider, if not then update it
        Cursor query = cr.query(TripContentProvider.CONTENT_URI_EVENTITEMS, null, where, null, null);


        if (query.getCount() == 0)
            cr.insert(TripContentProvider.CONTENT_URI_EVENTITEMS, values);
        else
            cr.update(TripContentProvider.CONTENT_URI_EVENTITEMS, values, where, null);


        query.close();
        //  Log.v("inserting name: ", event.getTripName());
         //Log.v("inserting day: ", ""+event.getDay());

       // events.clear();
        getLoaderManager().restartLoader(0, null, this);

        eia.notifyDataSetChanged();
       // getFragmentManager().popBackStack();

    }


    private void updateDays(String _trip){

        /*

        String tripName = _trip;
        Integer days = Integer.valueOf(allTrips.get(tripName));
        resID = R.layout.day_list_row;
        da = new DaysAdapter(this, resID, days);
        itemFragment.setListAdapter(da);
        itemType = ItemType.DAY;

        */


    }


    //Starts the DetailsActivity using the Intent
    private void startDetailsActivity(EventItem eventItem, int position){


        Intent intent = new Intent();
        intent.setClass(this, EventDetailsActivity.class);
        intent.putExtra("event", eventItem.getEvent());
        intent.putExtra("timeFrom", eventItem.getFromTime());
        intent.putExtra("timeTo", eventItem.getToTime());
        intent.putExtra("created", eventItem.getCreated());
        intent.putExtra("tripName", eventItem.getTripName());
        intent.putExtra("day", eventItem.getDay());
        intent.putExtra("position", position);

        startActivityForResult(intent, 1);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //	Log.v("ResultCode: ", String.valueOf(requestCode));

        if (resultCode == Activity.RESULT_OK) {

            // During initial setup, plug in the details fragment.
            String newEvent = data.getExtras().getString("event");
            String tripName = data.getExtras().getString("tripName");

            int day = data.getExtras().getInt("day");

            EventItem newEventItem = new EventItem(newEvent, tripName, day);

            newEventItem.fromTime = data.getExtras().getInt("timeFrom");
            newEventItem.toTime = data.getExtras().getInt("timeTo");
            newEventItem.created = data.getExtras().getLong("created");

            itemType= ItemType.EVENT;

          //  Log.v("itemtype: ", "not event");

           // Log.v("itemtype: ", "is event");

            updateAllEvents(newEventItem);

        }
        if (resultCode == EventDetailsActivity.RESULT_DELETE) {

            long eventCreated = data.getExtras().getLong("created");
            int position = data.getExtras().getInt("position");

            deleteEvent(eventCreated, position);

        }

    }

    public void deleteEvent(long _eventCreated, int position) {


        //EventItem _event = eventItems.get(position);


        if (position != -1) {
            events.remove(position);
            eia.notifyDataSetChanged();

            ContentResolver cr = getContentResolver();


            String where = TripContentProvider.KEY_CREATED + " = " + _eventCreated;
            // String[] whereArgs = new String[] { note.getTask() };

            Cursor query = cr.query(TripContentProvider.CONTENT_URI_EVENTITEMS, null, where, null, null);

            if (query.getCount() != 0) {

                cr.delete(TripContentProvider.CONTENT_URI_EVENTITEMS, where, null);
            }


            query.close();
        }

    }


    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        CursorLoader loader;
        String where;

        if (itemType == ItemType.TRIP || itemType == ItemType.DAY) {
            loader = new CursorLoader(this,
                    TripContentProvider.CONTENT_URI_TRIPS, null, null, null, null);
        }
        else {

            where = TripContentProvider.KEY_TRIP_NAME + "=?" +
                    " AND " + TripContentProvider.KEY_DAY + "=?";
            String[] selArgs= new String[]{trip.getTripName(), ""+day};

            loader = new CursorLoader(this,
                    TripContentProvider.CONTENT_URI_EVENTITEMS, null, where, selArgs, null);

        }
        return loader;
    }

    public void onLoadFinished(Loader<Cursor>loader, Cursor cursor){

        Trip newTrip;

        if (itemType == ItemType.TRIP || itemType == ItemType.DAY ) {

            int keyTrip = cursor.getColumnIndexOrThrow(TripContentProvider.KEY_TRIP_NAME);
            int keyDays = cursor.getColumnIndexOrThrow(TripContentProvider.KEY_NUM_OF_DAYS);

            allTrips.clear();
            while (cursor.moveToNext()) {
                newTrip = new Trip (cursor.getString(keyTrip),
                        cursor.getInt(keyDays));
               allTrips.add(newTrip);

            }

            //creating a temp List in order to add all the keys
            //to arraylist and update the view
            //List<String> tempList = new ArrayList<String>(allTrips.keySet());
            // trips.addAll(tempList);
            tna.notifyDataSetChanged();



        }

        if (itemType == ItemType.EVENT ) {

            int keyEventIndex = cursor.getColumnIndexOrThrow(TripContentProvider.KEY_EVENT);
            int keyTimeFromIndex = cursor.getColumnIndexOrThrow(TripContentProvider.KEY_TIME_FROM);
            int keyTimeToIndex = cursor.getColumnIndexOrThrow(TripContentProvider.KEY_TIME_TO);
            int keyCreatedIndex = cursor.getColumnIndexOrThrow(TripContentProvider.KEY_CREATED);
            int keyTripNameIndex = cursor.getColumnIndexOrThrow(TripContentProvider.KEY_TRIP_NAME);
            int keyDayIndex = cursor.getColumnIndexOrThrow(TripContentProvider.KEY_DAY);

            events.clear();
             Log.v("events clear: ", "clear");
            while (cursor.moveToNext()) {
                EventItem newItem = new EventItem(cursor.getString(keyEventIndex), cursor.getInt(keyTimeFromIndex),
                        cursor.getInt(keyTimeToIndex), cursor.getString(keyTripNameIndex),
                        cursor.getInt(keyDayIndex), cursor.getLong(keyCreatedIndex));
                events.add(newItem);
                Log.v("events clear: ", "build");
            }
            eia.notifyDataSetChanged();
        }

        cursor.close();
    }



    public void onLoaderReset(Loader<Cursor> loader) {

         getLoaderManager().restartLoader(1, null, this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(1, null, this);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //  getMenuInflater().inflate(R.menu.event, menu);
        return true;
    }
    

}


// The version of activity that uses BaseAdapter and regular int instead of a list
/*

 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentManager fm = getFragmentManager();

        itemFragment = (ItemFragment) fm.findFragmentById(R.id.ItemFragment);

        // Create the array list of items

        //initiate a HashMap to associate trip names with number of days
        allTrips = new ArrayList<Trip>();

        //list all the trips in the hashmap
       // trips = new ArrayList<String>(allTrips.keySet());


       //trips = new ArrayList<String>();

       // days = allTrips.get(tripName);
        days = new ArrayList<Integer>();;
        events = new ArrayList<EventItem>();
        allEvents = new ArrayList<EventItem>();


        switch (itemType) {
            case TRIP:
                //Create the array adapter to bind the array to the listview
                resID = R.layout.trip_name;
                tna = new TripNameAdapter(this, resID, allTrips);
                //Bind the array adapter to the listview/listfragment .
                itemFragment.setListAdapter(tna);
                break;
            case DAY:
                //Create the array adapter to bind the array to the listview
                resID = R.layout.day_list_row;
                da = new DaysAdapter(this, resID, days);
                //Bind the array adapter to the listview/listfragment .
                itemFragment.setListAdapter(da);
                break;
            case EVENT:
                //Create the array adapter to bind the array to the listview
                resID = R.layout.event_list_row;
                eia = new EventItemAdapter(this, resID, events);
                //Bind the array adapter to the listview/listfragment .
                itemFragment.setListAdapter(eia);
                break;
        }

     //   getLoaderManager().initLoader(0, null, this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (itemType){
                    case TRIP:
                        String trip = "New Trip" + dayCounter;
                        //insert trip into array
                        trips.add(trip);
                        //insert trip into hashmap to associate it with the number of days
                        allTrips.put(trip, dayCounter);
                        daysInTrip = dayCounter;
                        dayCounter++;
                        tna.notifyDataSetChanged();
                        break;
                    case DAY:
                        //retrieve the trip name
                        String tripName = trips.get(daysInTrip);

                        //retrieve the number of days in that trip
                        int numOfDays = allTrips.get(tripName);

                        numOfDays++;
                        //put new number of days in the trip hashmap
                        allTrips.put(tripName, numOfDays);


                        //update Integer object to which adapter has reference to
                        days = numOfDays;

                        //notify the adapter of changed array
                        da.notifyDataSetChanged();

                        dayCounter++;
                        break;
                    case EVENT:
                        //get the name of the trip and the number of days
                        String tripName2 = trips.get(daysInTrip);
                        int numOfDays2 = allTrips.get(tripName2);

                        EventItem event = new EventItem("New Event", tripName2, numOfDays2);
                        events.add(event);
                        allEvents.add(event);

                        eia.notifyDataSetChanged();
                        break;
                }

            }
        });
    }

    @Override
    public void onNewItemSelected(int itemsId) {

        switch (itemType) {
            case TRIP:
                tripName = trips.get(itemsId);
                days = allTrips.get(tripName);

                resID = R.layout.day_list_row;
                da = new DaysAdapter(this, resID, days);
                itemFragment.setListAdapter(da);
                itemType = ItemType.DAY;

                da.notifyDataSetChanged();
               // updateDays(trip);
                break;
            case DAY:
               // int day = itemsId;
               // tripName = trips.get(itemsId);

                //build events arraylist from allevents to match the tripName and particular day
                for(EventItem event : allEvents)
                {
                    if( event.tripName == tripName  && event.day == days)
                        events.add(event);
                }
                resID = R.layout.event_list_row;
                eia = new EventItemAdapter(this, resID, events);
                itemFragment.setListAdapter(eia);

                itemType = ItemType.EVENT;
                eia.notifyDataSetChanged();

                //changeFragment(itemsId);

                break;

            case EVENT:
                EventItem event = events.get(itemsId);
                //extract todoItem and send it to this method
                startDetailsActivity(event, itemsId);
                itemType = ItemType.TRIP;
                break;
        }
    }

 */
/*
        switch (itemType) {
            case TRIP:
                //Create the array adapter to bind the array to the listview
             //   resID = R.layout.trip_name;
             //   tna = new TripNameAdapter(this, resID, allTrips);
                //Bind the array adapter to the listview/listfragment .
             //   itemFragment.setListAdapter(tna);

                FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.fragment_container, itemFragment);
                ft.commit();
                break;
            case DAY:
                //Create the array adapter to bind the array to the listview
                resID = R.layout.day_list_row;
                da = new DaysAdapter(this, resID, days);
                //Bind the array adapter to the listview/listfragment .
                itemFragment.setListAdapter(da);
                break;
            case EVENT:
                //Create the array adapter to bind the array to the listview
                resID = R.layout.event_list_row;
                eia = new EventItemAdapter(this, resID, events);
                //Bind the array adapter to the listview/listfragment .
                itemFragment.setListAdapter(eia);
                break;
        }
        */
