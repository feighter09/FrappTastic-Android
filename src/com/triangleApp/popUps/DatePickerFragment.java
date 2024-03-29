package com.triangleApp.popUps;

import java.util.Calendar;

import com.triangleApp.R;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

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
	
	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {
		String date = "";
		
		month++; //they're 0 indexed
		date = (month >= 10 ? month : "0" + month) + "/";
		date += (day >= 10 ? day : "0" + day) + "/" + year;
		
		TextView eventDate = (TextView) getActivity().findViewById(R.id.quickEventDate);
		eventDate.setText(date);
	}
}