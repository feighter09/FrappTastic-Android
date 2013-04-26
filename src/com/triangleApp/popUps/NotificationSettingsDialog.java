package com.triangleApp.popUps;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.parse.PushService;
import com.triangleApp.QuickEventList;
import com.triangleApp.R;
import com.triangleApp.util.QuickEventType;

public class NotificationSettingsDialog extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		String message = "Receive notifications for the following event types: ";
		final ArrayList<Integer> mSelectedItems = new ArrayList<Integer>();  // Where we track the selected items
	    
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Set the dialog title
	    builder.setTitle(message)
//	    	   .setMessage(message)
	    // Specify the list array, the items to be selected by default (null for none),
	    // and the listener through which to receive callbacks when items are selected
	           .setMultiChoiceItems(R.array.notificationSettings, null,
	                      new DialogInterface.OnMultiChoiceClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int which,
	                       boolean isChecked) {
					if (isChecked) {
	                       // If the user checked the item, add it to the selected items
	                       mSelectedItems.add(which);
	                   } else if (mSelectedItems.contains(which)) {
	                       // Else, if the item is already in the array, remove it 
	                       mSelectedItems.remove(Integer.valueOf(which));
	                   }
	               }
	           })
	    // Set the action buttons
	           .setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   // User clicked OK, so save the mSelectedItems results somewhere
	                   // or return them to the component that opened the dialog
	            	   
	            	   //unsubscribe from everything
	            	   for(QuickEventType rb : QuickEventType.values())
	            		   PushService.unsubscribe(getActivity(), rb.toString());
	            	   //subscribe to selected event types
	            	   for(Integer i : mSelectedItems)
	            		   PushService.subscribe(getActivity(),
	            				   				 QuickEventType.values()[i].toString(), 
	            				   				 QuickEventList.class);
	               }
	           })
	           .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   //do nothing
	               }
	           });

	    return builder.create();
	}
}
