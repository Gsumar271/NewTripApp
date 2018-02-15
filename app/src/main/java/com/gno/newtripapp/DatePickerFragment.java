package com.gno.newtripapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment
     implements DatePickerDialog.OnDateSetListener{

	private String buttonClicked;
	
	public interface OnNewDateSelectedListener {
        public void onNewDateSelected(Date selectedDate, String clicked);
    }

	private OnNewDateSelectedListener onNewDateSelectedListener;
	

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	public void onDateSet(DatePicker view, int year, int month, int day) {
		// Do something with the date chosen by the user
		Date dateSelected = new Date(year, month, day);
		//SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
	    //String date = sdf.format(dateSelected);
		
	    onNewDateSelectedListener.onNewDateSelected(dateSelected, buttonClicked);
	    
		return ;
	}

	public void changeClickedButton(String whichButton){

		buttonClicked = whichButton;
	}
	
	@Override
	public void onAttach(Activity activity){
		 super.onAttach(activity);
		
		 try{
			 onNewDateSelectedListener = (OnNewDateSelectedListener)activity;
		  } catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() +
		              " must implement OnNewItemAddedListener");
		  }
   }
}


