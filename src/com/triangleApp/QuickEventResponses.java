package com.triangleApp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class QuickEventResponses extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quick_event_responses);
		
		Bundle b = getIntent().getExtras();
		String title, creator, date, time;
		title = b.getString("title").substring(7);
		creator = b.getString("creator").substring(15);
		date = b.getString("date");
		time = b.getString("time");
		
		TextView topBar = (TextView) findViewById(R.id.quickEventTitle);
		topBar.setText("Title: " + title + '\n' + "Creator: " + creator
							 + '\n' + "Date: " + date + '\n' + "Time: " + time);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_quick_event_responses, menu);
		return true;
	}
	
	String formatTime(String time) {
		if(time.indexOf(':') == 1)
			time = '0' + time;
		int hours = Integer.parseInt(time.substring(0, 2));
		
		if(time.charAt(5) == 'p' && hours != 12) {
			hours += 12;
			time = hours + time.substring(2);
		}
		
		time = time.substring(0, 5) + ":00";
		return time;
	}
	
	public void back(View v){
		Intent intent = new Intent(this, QuickEventList.class);
		startActivity(intent);
	}

}
