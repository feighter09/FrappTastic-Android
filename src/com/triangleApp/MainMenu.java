package com.triangleApp;

import com.parse.Parse;
import com.parse.PushService;
import com.triangleApp.R;
import com.triangleApp.util.PreferenceData;
import com.triangleApp.util.QuickEventType;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class MainMenu extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main_menu, menu);
		return true;
	}
	
	public void showAnnouncements(View v){
		Intent intent = new Intent(this, Announcements.class);
		startActivity(intent);
	}
	
	public void showQuickEvent(View v){
		Intent intent = new Intent(this, QuickEventMenu.class);
		startActivity(intent);
	}
	
	public void showDirectory(View v){
		Intent intent = new Intent(this, Directory.class);
		startActivity(intent);
	}
	
	public void logOut(View v){
		PreferenceData.clearLoggedIn(this);

		for(QuickEventType rb : QuickEventType.values())	//unsubscribe all parse notifications
			PushService.unsubscribe(getBaseContext(), rb.toString());
		
		Intent intent = new Intent(this, Login.class);
		startActivity(intent);
	}

}
