package com.triangleApp.popUps;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;

import com.parse.*;
import com.triangleApp.R;

public class QuickEventDialog extends DialogFragment {
    
	public String eventTitle, eventTime, eventType;
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	String dialogMessage = "Send " + eventType + " event: \"" + eventTitle + "\" at " + eventTime + "?";
    	final String pushMessage = "New " + eventType + " event: \"" + eventTitle + "\" at " + eventTime; 
    	
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.areYouSure)
        	   .setMessage(dialogMessage)
               .setPositiveButton(R.string.quickEventNotify, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       ParsePush push = new ParsePush();
                       push.setChannel(eventType);
                       push.setMessage(pushMessage);
                       push.sendInBackground();
                   }
               })
               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // User cancelled the dialog
                   }
               });
        // Create the AlertDialog object and return it
        return builder.create();
    }

	
	
	public String getEventTitle() {
		return eventTitle;
	}

	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}

	public String getEventTime() {
		return eventTime;
	}

	public void setEventTime(String eventTime) {
		this.eventTime = eventTime;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	
}
