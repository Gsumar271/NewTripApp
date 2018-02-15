package com.gno.newtripapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class TripNameAdapter extends ArrayAdapter<Trip> {

int resource;

	public TripNameAdapter(Context context,
                           int resource,
                           List<Trip> items)
	{
			super(context, resource, items);
			this.resource = resource;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    LinearLayout todoView;
	    
	   Trip item = getItem(position);
	   String tripName = item.getTripName();


	    if (convertView == null) {
	      todoView = new LinearLayout(getContext());
	      String inflater = Context.LAYOUT_INFLATER_SERVICE;
	      LayoutInflater li;
	      li = (LayoutInflater)getContext().getSystemService(inflater);
	      li.inflate(resource, todoView, true);
	    } else {
	      todoView = (LinearLayout) convertView;
	    }

	    TextView categoryView = (TextView)todoView.findViewById(R.id.category_text);

	    categoryView.setText(tripName);
	    
	   // Log.v("Category: ", item);
	    return todoView;
	}

}
