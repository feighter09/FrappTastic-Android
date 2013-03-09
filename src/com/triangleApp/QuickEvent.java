package com.triangleApp;

import com.triangleApp.popUps.QuickEventDialog;
import com.triangleApp.popUps.TimePickerFragment;
import com.triangleApp.util.QuickEventRadioButtons;
import com.triangleApp.R;

import android.os.Bundle;
import android.app.DialogFragment;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class QuickEvent extends FragmentActivity {

	public String eventType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quick_event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_quick_event, menu);
		return true;
	}
	
	public void setQuickEventTime(View v){
		DialogFragment timePicker = new TimePickerFragment();
		timePicker.show(getFragmentManager(), "timePicker");
	}
	
	public void notifyOthers(View v){
		String eventTitle, eventTime;
		
		EditText quickEventTitle = (EditText) findViewById(R.id.quickEventTitleInput);
		eventTitle = quickEventTitle.getText().toString();
		TextView quickEventTime = (TextView) findViewById(R.id.quickEventTime);
		eventTime = quickEventTime.getText().toString();
		
		QuickEventDialog notifyDialog = new QuickEventDialog();
		notifyDialog.setEventTitle(eventTitle);
		notifyDialog.setEventTime(eventTime);
		notifyDialog.setEventType(eventType);
		notifyDialog.show(getFragmentManager(), "notifyDialog");
	}
	
	public void backToMenu(View v){
		Intent intent = new Intent(this, MainMenu.class);
		startActivity(intent);
	}
	
	public void onRadioButtonClicked(View view) {
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();
	    
	    // Check which radio button was clicked
	    switch(view.getId()) {
	        case R.id.quickEventFoodRadioButton:
	            if (checked)
	                eventType = QuickEventRadioButtons.FOOD.toString();
	            break;
	        case R.id.quickEventBroHoodRadioButton:
	            if (checked)
	            	eventType = QuickEventRadioButtons.BROHOOD.toString();
	            break;
	        case R.id.quickEventSportsRadioButton:
	            if (checked)
	            	eventType = QuickEventRadioButtons.SPORTS.toString();
	            break;
	        case R.id.quickEventOtherRadioButton:
	            if (checked)
	            	eventType = QuickEventRadioButtons.OTHER.toString();
	            break;
	    }
	}
	
	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
}
