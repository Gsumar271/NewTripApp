package com.gno.newtripapp;

import android.app.Activity;
import android.app.ListFragment;
import android.view.View;
import android.widget.ListView;

/**
 * Created by eugenesumaryev on 2/1/18.
 */

public class EventItemFragment extends ListFragment {

    public interface OnNewEventSelectedListener {
        public void onNewEventSelected(int itemsId);
    }

    private OnNewEventSelectedListener onEventSelected;


    @Override
    public void onListItemClick(ListView l, View v, int position, long id){

        onEventSelected.onNewEventSelected(position);

    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        try{
            onEventSelected = (OnNewEventSelectedListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement OnNewItemAddedListener");
        }
    }
}
