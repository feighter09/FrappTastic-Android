package com.triangleApp.popUps;

import java.util.Calendar;

import com.triangleApp.R;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment
								implements TimePickerDialog.OnTimeSetListener {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current time as the default values for the picker
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		
		// Create a new instance of TimePickerDialog and return it
		return new TimePickerDialog(getActivity(), this, hour, minute,
									DateFormat.is24HourFormat(getActivity()));
	}
	
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		//format the time string
		
		boolean AM = false;
		
		if(hourOfDay >= 12)
			hourOfDay -= 12;
		else
			AM = true;
		
		if(hourOfDay == 0)
			hourOfDay = 12;
		
		String time = hourOfDay + ":";
		time += minute < 10 ? "0" + minute : minute;
		time += AM ? " AM" : " PM";
		
		//find the event time box and set the time to selected time
		TextView eventTime = (TextView) getActivity().findViewById(R.id.quickEventTime);
		eventTime.setText(time);
		
	}
}