package com.triangleApp;

import com.triangleApp.R;

import com.parse.PushService;
import com.triangleApp.util.PreferenceData;
import com.triangleApp.util.QuickEventType;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

import android.content.Context;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Login extends Activity {

	private EditText uniqnameInput, passwordInput;
	private Button submit;
	
	private Context ctx;
	
	private View mLoginFormView;
	private View mLoginStatusView;
	
	private String uniqname, password, firstname, lastname;
	
	private static boolean firstLogin = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		ctx = getBaseContext();
		
		uniqnameInput = (EditText) findViewById(R.id.uniqnameInput);
		passwordInput = (EditText) findViewById(R.id.passwordInput);
		submit = (Button) findViewById(R.id.sign_in_button);
		
		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		
		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				uniqname = uniqnameInput.getText().toString();
				password = passwordInput.getText().toString();
				
				if(uniqname.length() == 0 || password.length() == 0){
					Toast.makeText(ctx, "Please enter first and last name", Toast.LENGTH_SHORT)
						 .show();
				} else {
					showProgress(true);
					new LoginHttpPost().execute(uniqname, password);
				}
			}
		});
		
		uniqname = PreferenceData.getLoggedInUniqname(this);
		password = PreferenceData.getLoggedInPassword(this);
		
		if(!uniqname.isEmpty() && !password.isEmpty()){
			showProgress(true);
			new LoginHttpPost().execute(uniqname, password);
		}
	}
	
	private class LoginHttpPost extends AsyncTask<String, Void, JSONObject>{
		
		private HttpClient client;
		private HttpPost post;
		private HttpResponse response;
		private final String url = "http://www.triangleumich.com/mobile_login_check.php";
		
		public LoginHttpPost(){
			client = new DefaultHttpClient();
			post = new HttpPost(url);
		}
		
		public JSONObject doPost(List<NameValuePair> params){
			try {
				post.setEntity(new UrlEncodedFormEntity(params));
				response = client.execute(post);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			JSONObject JSON = null;
			try {
				String res = EntityUtils.toString(response.getEntity());
				 JSON = new JSONObject(new JSONTokener(res));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return JSON;
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			client = new DefaultHttpClient();
			post = new HttpPost(url);
			
			if(params.length != 2)
				return null;
			
			List<NameValuePair> phpParams = new ArrayList<NameValuePair>();
			phpParams.add(new BasicNameValuePair("name", params[0]));
			phpParams.add(new BasicNameValuePair("password", params[1]));
			
			return doPost(phpParams);
		}
		
		@Override
		protected void onPostExecute(JSONObject result){
			try {
				if(result.getBoolean("success")){
					firstname = result.getString("firstName"); lastname = result.getString("lastName");
					
					if(firstLogin){
						setLogin(uniqname, password, firstname, lastname);
						setUpParse();
						firstLogin = false;
					}
					Toast.makeText(getBaseContext(), "Welcome, " + firstname + " " + lastname, Toast.LENGTH_LONG).show();
					goToMenu();
				} else {
					showProgress(false);
					Toast.makeText(getBaseContext(), "Failure: " + result.getString("error"), Toast.LENGTH_LONG).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	private void setLogin(String uniq, String pass, String first, String last){
		PreferenceData.setLoggedInUserInfo(getBaseContext(), uniq, pass, first, last);
	}
	
	private void setUpParse(){
		for(QuickEventType rb : QuickEventType.values())
			PushService.subscribe(getBaseContext(), rb.toString(), QuickEventList.class);
	}
	
	private void goToMenu(){
		Intent intent = new Intent(this, MainMenu.class);
		startActivity(intent);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

}
