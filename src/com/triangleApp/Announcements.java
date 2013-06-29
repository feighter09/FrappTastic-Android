package com.triangleApp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;

public class Announcements extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_announcements);
		
		WebView webview = (WebView) findViewById(R.id.announcementsWebView);
		webview.loadUrl("http://triangleumich.com/announcements.php");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_announcements, menu);
		return true;
	}
	
	public void backToMenu(View v){
		Intent intent = new Intent(this, MainMenu.class);
		startActivity(intent);
	}
}
