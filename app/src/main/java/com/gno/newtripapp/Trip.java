package com.gno.newtripapp;

import java.util.ArrayList;

/**
 * Created by eugenesumaryev on 1/30/18.
 */

public class Trip {
    private String tripName;
    private int days;
    private ArrayList<String> numDays;

    public Trip (String _tripName, int _days){
        this.tripName = _tripName;
        this.days = _days;

    }

    public String getTripName() {

        return tripName;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int i){
        days = i;
    }

    public ArrayList<String> getNumDays() {
        int i;
        numDays = new ArrayList<String>();

        for (i = 0; i++ < days; )
            numDays.add("" + i);
        return numDays;
    }
}
