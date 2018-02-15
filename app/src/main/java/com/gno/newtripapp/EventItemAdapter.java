package com.gno.newtripapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created on 10/20/17.
 */
public class EventItemAdapter extends ArrayAdapter<EventItem> {

    int resource;
    Context mContext;
    final int maxTodoLength = 10;

    public interface OnDeleteClickedListener {
        public void deleteItem(int position);
    }

    private OnDeleteClickedListener onDeleteClickedListener;

    public EventItemAdapter(Context context,
                            int resource,
                            List<EventItem> items)
    {
        super(context, resource, items);
        this.resource = resource;
        mContext = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        RelativeLayout eventView;
        String eventString;
        int timeFrom, timeTo;
        String amPmString01 = "am", amPmString02 = "am";

        EventItem item = getItem(position);

        eventString = item.getEvent();


        //get "from" and "to" event times
        timeFrom = item.getFromTime();
        timeTo = item.getToTime();

        if (convertView == null) {
            eventView = new RelativeLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li;
            li = (LayoutInflater)getContext().getSystemService(inflater);
            li.inflate(resource, eventView, true);
        } else {
            eventView = (RelativeLayout) convertView;
        }


        TextView eventListView = (TextView)eventView.findViewById(R.id.eventList);
        ImageView imageView = (ImageView)eventView.findViewById(R.id.list_image);
        Button buttonView = (Button)eventView.findViewById(R.id.timeButton);


        int timePeriod = timeTo - timeFrom;


        eventListView.setText(eventString);

        String timeText = timeFrom + amPmString01 + " \n\n\n" + timeTo + amPmString02;
        buttonView.setText(timeText);

        imageView.setBackgroundColor(Color.GREEN);


        return eventView;
    }


}
