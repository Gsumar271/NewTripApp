package com.gno.newtripapp;

/**
 * Created on 10/20/17.
 */
public class EventItem {

    String event;
    int fromTime;
    int toTime;
    String tripName;
    int day;
    long created;

    public EventItem(String _event, String _tripName, int _day){
        this(_event, 0, 0, _tripName, _day, System.currentTimeMillis());
    }



    public EventItem(String _event, int _from, int _to, String _tripName, int _day, long _created) {

        event = _event;
        fromTime = _from;
        toTime = _to;
        tripName = _tripName;

        day = _day;
        created = _created;

    }

    public long getCreated() {
        return created;
    }

    public int getFromTime() {

        return fromTime;
    }

    public int getToTime() {

        return toTime;
    }



    public String getEvent() {

        return event;
    }

    public String getTripName() {
        return tripName;
    }

    public int getDay() {
        return day;
    }

    @Override
    public String toString() {
        return "EventItem{" +
                "event='" + event + '\'' +
                ", dueDate=" + created +
                '}';
    }


}
