package com.triangleApp;

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

public class Login extends Activity {

	private EditText uniqnameInput, passwordInput;
	private Button submit;
	
	private Context ctx;
	
	private View mLoginFormView;
	private View mLoginStatusView;
	
	private String result, uniqname, password, firstname, lastname;
	
	private static boolean firstLogin = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		uniqnameInput = (EditText) findViewById(R.id.uniqnameInput);
		passwordInput = (EditText) findViewById(R.id.passwordInput);
		submit = (Button) findViewById(R.id.sign_in_button);
		
		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		
		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				uniqname = uniqnameInput.getText().toString();
				password = passwordInput.getText().toString().trim();
				
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
	
	private class LoginHttpPost extends AsyncTask<String, Void, String>{
		
		private HttpClient client;
		private HttpPost post;
		private HttpResponse response;
		private final String url = "http://www.triangleumich.com/mobile_login_check.php";
		
		public LoginHttpPost(){
			client = new DefaultHttpClient();
			post = new HttpPost(url);
		}
		
		public String doPost(List<NameValuePair> params){
			try {
				post.setEntity(new UrlEncodedFormEntity(params));
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
			
			if(params.length != 2)
				return null;
			
			List<NameValuePair> phpParams = new ArrayList<NameValuePair>();
			phpParams.add(new BasicNameValuePair("name", params[0]));
			phpParams.add(new BasicNameValuePair("password", params[1]));
			
			return doPost(phpParams);
		}
		
		@Override
		protected void onPostExecute(String result){
			if(parseResult(result)){
				if(firstLogin){
					setLogin(uniqname, password, firstname, lastname);
					setUpParse();
					firstLogin = false;
				}
				
				goToMenu();
			}// else if(result.equals(""))
			
		}
		
		private boolean parseResult(String response){
			boolean first = true;
			result = response.substring(8);
			
			if( response.substring(0, 7).equals("failure") ){
				result = response.substring(9);
				Toast.makeText(getBaseContext(), result, Toast.LENGTH_SHORT).show();
				return false;
			}
			
			firstname = lastname = "";
			for(char c : result.toCharArray()){
				if(c == '|'){
					first = false;
					continue;
				}
				
				if(first)
					firstname += c;
				else
					lastname += c;
			}
			
			Toast.makeText(getBaseContext(), "Welcome, " + firstname + " " + lastname, Toast.LENGTH_SHORT).show();
			return true;
		}
		
	}
	
	private void setLogin(String uniq, String pass, String first, String last){
		PreferenceData.setLoggedInUserInfo(getBaseContext(), uniq, pass, first, last);
	}
	
	private void setUpParse(){
		for(QuickEventType rb : QuickEventType.values())
			PushService.subscribe(getBaseContext(), rb.toString(), MakeQuickEvent.class);
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


//	/**
//	 * Attempts to sign in or register the account specified by the login form.
//	 * If there are form errors (invalid email, missing fields, etc.), the
//	 * errors are presented and no actual login attempt is made.
//	 */
//	public void attemptLogin(View v) {
//		if (mAuthTask != null) {
//			return;
//		}
//
//		// Reset errors.
//		mUniqnameView.setError(null);
//		mPasswordView.setError(null);
//
//		// Store values at the time of the login attempt.
//		mUniqname = mUniqnameView.getText().toString();
//		mPassword = mPasswordView.getText().toString();
//
//		boolean cancel = false;
//		View focusView = null;
//
//		// Check for a valid password.
//		if (TextUtils.isEmpty(mPassword)) {
//			mPasswordView.setError(getString(R.string.error_field_required));
//			focusView = mPasswordView;
//			cancel = true;
//		} else if (mPassword.length() < 4) {
//			mPasswordView.setError(getString(R.string.error_invalid_password));
//			cancel = true;
//		}
//
//		// Check for a valid email address.
//		if (TextUtils.isEmpty(mUniqname)) {
//			mUniqnameView.setError(getString(R.string.error_field_required));
//			focusView = mUniqnameView;
//			cancel = true;
//		} else if (mUniqname.contains("@")) {
//			mUniqnameView.setError(getString(R.string.error_invalid_uniqname));
//			focusView = mUniqnameView;
//			cancel = true;
//		}
//
//		if (cancel) {
//			// There was an error; don't attempt login and focus the first
//			// form field with an error.
//			focusView.requestFocus();
//		} else {
//			// Show a progress spinner, and kick off a background task to
//			// perform the user login attempt.
//			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
//			showProgress(true);
//			mAuthTask = new UserLoginTask();
//			mAuthTask.execute((Void) null);
//		}
//	}
//
//
//		@Override
//		protected void onPostExecute(final Boolean success) {
//			mAuthTask = null;
//			showProgress(false);
//
//			if (success) {
//				setLogin();
//				setUpParse();
//				goToMenu();
//			} else {
//				mUniqnameView.setError(getString(R.string.error_incorrect_uniqname));
//				mUniqnameView.requestFocus();
//			}
//		}
//
//		@Override
//		protected void onCancelled() {
//			mAuthTask = null;
//			showProgress(false);
//		}
//	}
//}
