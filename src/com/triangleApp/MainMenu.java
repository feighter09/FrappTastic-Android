package com.triangleApp;

import com.triangleApp.R;
import com.triangleApp.util.PreferenceData;

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
	
	public void logOut(View v){
		PreferenceData.clearLoggedIn(this);
		Intent intent = new Intent(this, Login.class);
		startActivity(intent);
	}

}
