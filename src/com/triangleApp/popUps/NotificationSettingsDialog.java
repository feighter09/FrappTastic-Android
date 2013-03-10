package com.triangleApp.popUps;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.triangleApp.R;

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
	            	   // set parse subscriptions
	               }
	           })
	           .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	               }
	           });

	    return builder.create();
	}
}
