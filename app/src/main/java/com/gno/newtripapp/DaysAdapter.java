package com.gno.newtripapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created on 1/27/18.
 */

public class DaysAdapter extends ArrayAdapter<String> {

    private int resource;
    private List<String> items;

    public DaysAdapter(Context context,
                           int resource,
                           List<String> items)
    {
        super(context, resource, items);
        this.resource = resource;
        this.items = items;
    }


    public void newItems(List <String> items){
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public String getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout dayView;

       String item = getItem(position);


        if (convertView == null) {
            dayView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li;
            li = (LayoutInflater)getContext().getSystemService(inflater);
            li.inflate(resource, dayView, true);
        } else {
            dayView = (LinearLayout) convertView;
        }

        TextView dateView = (TextView)dayView.findViewById(R.id.day_text);
        String dayText = " DAY: " + item;

        dateView.setText(dayText);

        return dayView;
    }

}

/*

public class DaysAdapter extends BaseAdapter {

    Context context;
    int resource;
    int itemsCount;

    public DaysAdapter(Context context, int _resource, int _itemsCount) {

        this.context = context;
        this.resource = _resource;
        this.itemsCount = _itemsCount;
    }

    @Override
    public int getCount() {
        Log.v("getCount ", ""+itemsCount);
        return itemsCount;
    }

    @Override
    public Object getItem(int position) {
        Log.v("getItem ", ""+position);
        return Integer.valueOf(position);
    }

    @Override
    public long getItemId(int position) {
        Log.v("getItemId: ", ""+position);
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout dayView;

        //  Integer item = getItem(position);


        if (convertView == null) {
            dayView = new LinearLayout(context);
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li;
            li = (LayoutInflater)context.getSystemService(inflater);
            li.inflate(resource, dayView, true);
        } else {
            dayView = (LinearLayout) convertView;
        }

        TextView dateView = (TextView)dayView.findViewById(R.id.day_text);
        String dayText = "DAY" + position;

        dateView.setText(dayText);

        // Log.v("Category: ", item);
        return dayView;
    }

}

 */
