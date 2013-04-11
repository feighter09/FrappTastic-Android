package com.triangleApp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import com.triangleApp.util.DirectoryContact;
import com.triangleApp.util.DirectoryListViewAdapter;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

public class Directory extends Activity {

	List<DirectoryContact> contacts;
	ListView contactList;
	
	private View directoryLayout, statusView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_brother_directory);
		
		directoryLayout = findViewById(R.id.directoryList);
		statusView = findViewById(R.id.directoryStatus);
		
		contactList = (ListView) findViewById(R.id.directoryListView);
		contacts = new ArrayList<DirectoryContact>();
		
		showProgress(true);
		new DirectoryHttpPost().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_brother_directory, menu);
		return true;
	}
	
	public void backToMenu(View v){
		Intent intent = new Intent(this, MainMenu.class);
		startActivity(intent);
	}

	private class DirectoryHttpPost extends AsyncTask<String, Void, String>{
		
		private HttpClient client;
		private HttpPost post;
		private HttpResponse response;
		private final String url = "http://www.triangleumich.com/mobile_directory.php";

		public DirectoryHttpPost(){
			client = new DefaultHttpClient();
			post = new HttpPost(url);
		}
		
		public String doPost(List<NameValuePair> params){
			try {
				//post.setEntity(new UrlEncodedFormEntity(params));
				response = client.execute(post);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			String res = "";
			try {
				res = EntityUtils.toString(response.getEntity());
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return res;
		}

		@Override
		protected String doInBackground(String... params) {
			client = new DefaultHttpClient();
			post = new HttpPost(url);
			
			List<NameValuePair> phpParams = new ArrayList<NameValuePair>();
			return doPost(phpParams);
		}
		
		@Override
		protected void onPostExecute(String result){
			try {
				JSONArray contactsJSON = new JSONArray(result);
				DirectoryListViewAdapter adapter = new DirectoryListViewAdapter(getBaseContext(), contactsJSON);
				contactList.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			showProgress(false);
		}
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			statusView.setVisibility(View.VISIBLE);
			statusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							statusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			directoryLayout.setVisibility(View.VISIBLE);
			directoryLayout.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							directoryLayout.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			statusView.setVisibility(show ? View.VISIBLE : View.GONE);
			directoryLayout.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
}
