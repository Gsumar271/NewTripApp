package com.gno.newtripapp;

import android.app.Activity;
import android.app.ListFragment;
import android.view.View;
import android.widget.ListView;

/**
 * Created on 2/1/18.
 */

public class DayItemFragment extends ListFragment {

    public interface OnNewDaySelectedListener {
        public void onNewDaySelected(int itemsId);
    }

    private OnNewDaySelectedListener onDayItemSelected;


    @Override
    public void onListItemClick(ListView l, View v, int position, long id){

        onDayItemSelected.onNewDaySelected(position);

    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        try{
            onDayItemSelected = (OnNewDaySelectedListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement OnNewItemAddedListener");
        }
    }
}
