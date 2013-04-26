package com.triangleApp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import com.triangleApp.popUps.QuickEventRespondDialog;
import com.triangleApp.util.PreferenceData;
import com.triangleApp.util.QuickEventResponse;
import com.triangleApp.util.QuickEventResponseListViewAdapter;

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
import android.widget.TextView;

public class QuickEventResponses extends Activity {

	private String title, creator, date, time;
	private View quickEventResponsesLayout, statusView;
	
	private ListView responseList;
	List<QuickEventResponse> contacts;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quick_event_responses);
		
		Bundle b = getIntent().getExtras();
		
		title = b.getString("title").substring(7);
		creator = b.getString("creator").substring(15);
		date = b.getString("date");
		time = b.getString("time");
		
		TextView topBar = (TextView) findViewById(R.id.quickEventTitle);
		topBar.setText("Title: " + title + '\n' + "Creator: " + creator
							 + '\n' + "Date: " + date + '\n' + "Time: " + time);
		
		quickEventResponsesLayout = findViewById(R.id.responseList);
		statusView = findViewById(R.id.quickEventsResponsesStatus);
		
		responseList = (ListView) findViewById(R.id.quickEventResponsesListView);
		contacts = new ArrayList<QuickEventResponse>();
		
		loadResponses();
	}
	
	public void loadResponses(){
		showProgress(true);
		new QuickEventsResponsesHttpPost().execute(title, formatDate(date), formatTime(time));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_quick_event_responses, menu);
		return true;
	}
	
	public void back(View v){
		Intent intent = new Intent(this, QuickEventList.class);
		startActivity(intent);
	}
	
	public void respond(View v){
		QuickEventRespondDialog respondDialog = new QuickEventRespondDialog();
		
		String name = PreferenceData.getLoggedInFirstname(this) + ' ' + PreferenceData.getLoggedInLastname(this); 
		respondDialog.setTitle(title);
		respondDialog.setName(name);
		respondDialog.setDate(date);
		respondDialog.setTime(time);
		
		respondDialog.show(getFragmentManager(), "respondDialog");
	}
	
	public class QuickEventsResponsesHttpPost extends AsyncTask<String, Void, String>{
		
		private HttpClient client;
		private HttpPost post;
		private HttpResponse response;
		private final String url = "http://www.triangleumich.com/get_quick_event_responses.php";

		public QuickEventsResponsesHttpPost(){
			client = new DefaultHttpClient();
			post = new HttpPost(url);
		}
		
		public String doPost(List<NameValuePair> params){
			try {
				HttpEntity entity;
				entity = new UrlEncodedFormEntity(params);
				
				post.addHeader(entity.getContentType());
				post.setEntity(entity);
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
			
			if(params.length != 3)
				return null;
			
			List<NameValuePair> phpParams = new ArrayList<NameValuePair>();
			phpParams.add(new BasicNameValuePair("title", params[0]));
			phpParams.add(new BasicNameValuePair("date", params[1]));
			phpParams.add(new BasicNameValuePair("time", params[2]));
			
			return doPost(phpParams);
		}
		
		@Override
		protected void onPostExecute(String result){
			try {
				JSONArray responsesJSON = new JSONArray(result);
				QuickEventResponseListViewAdapter adapter = new QuickEventResponseListViewAdapter(getBaseContext(), responsesJSON);
				responseList.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			showProgress(false);
		}
	}
	
	public String formatDate(String date) {
		String dayStr, monthStr, yearStr, dateStr;
        dayStr = date.substring(3, 5);
        monthStr = date.substring(0, 2);
        yearStr = date.substring(6);
        dateStr = yearStr + "-" + monthStr + "-" + dayStr;
        
        return dateStr;
	}
	
	public String formatTime(String time) {
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

			quickEventResponsesLayout.setVisibility(View.VISIBLE);
			quickEventResponsesLayout.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							quickEventResponsesLayout.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			statusView.setVisibility(show ? View.VISIBLE : View.GONE);
			quickEventResponsesLayout.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

}
