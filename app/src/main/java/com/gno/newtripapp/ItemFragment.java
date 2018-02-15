package com.gno.newtripapp;

import android.app.Activity;
import android.app.ListFragment;
import android.view.View;
import android.widget.ListView;

/**
 * Created on 10/20/17.
 */
public class ItemFragment extends ListFragment {



    public interface OnNewItemSelectedListener {
        public void onNewItemSelected(int itemsId);
    }

    private OnNewItemSelectedListener onItemNoteSelected;


    @Override
    public void onListItemClick(ListView l, View v, int position, long id){

        onItemNoteSelected.onNewItemSelected(position);

    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        try{
            onItemNoteSelected = (OnNewItemSelectedListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement OnNewItemAddedListener");
        }
    }

}
