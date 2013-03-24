package com.triangleApp;

import com.triangleApp.popUps.NotificationSettingsDialog;

import android.os.Bundle;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class QuickEventMenu extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quick_event_menu);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_quick_event_menu, menu);
		return true;
	}
	
	public void goToMakeQuickEvent(View v){
		Intent intent = new Intent(this, MakeQuickEvent.class);
		startActivity(intent);
	}
	
	public void goToQuickEventList(View v){
		Intent intent = new Intent(this, QuickEventList.class);
		startActivity(intent);
	}
	
	public void notificationSettings(View v){
		DialogFragment notificationSettings = new NotificationSettingsDialog();
		notificationSettings.show(getFragmentManager(), "notificationSettings");
	}
	
	public void goToQuickEventsList(View v){
		Intent intent = new Intent(this, QuickEventList.class);
		startActivity(intent);
	}

	public void backToMenu(View v){
		Intent intent = new Intent(this, MainMenu.class);
		startActivity(intent);
	}



}
