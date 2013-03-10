package com.triangleApp;

import com.parse.Parse;
import com.triangleApp.util.PreferenceData;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class Login extends Activity {
	/**
	 * A dummy authentication store containing known user names and passwords.
	 * TODO: remove after connecting to a real authentication system.
	 */
	private static final String[] DUMMY_CREDENTIALS = new String[] {
			"afeight:asdf", "pcgood:noturnonred" };

	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mUniqname;
	private String mPassword;

	// UI references.
	private EditText mUniqnameView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
	
	private void setLogin(){
		PreferenceData.setUserLoggedInStatus(this, true);
	}
	
	private void setupParse(){
		Intent intent = new Intent(this, ParseLogin.class);
		startActivity(intent);
	}
	
	private void goToMenu(){
		Intent intent = new Intent(this, MainMenu.class);
		startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Parse.initialize(this, "JTeN6P0AlreKF6pmi8G4TBnHpH5N25WMRLcmNlg4", 
				   "sAGC53pRcBjfGysAJU8tlM9m5QBxc6UhT4uMow71"); 
		
		if(PreferenceData.getUserLoggedInStatus(this))
			goToMenu();

		setContentView(R.layout.activity_login);

		// Set up the login form.
		mUniqname = getIntent().getStringExtra(EXTRA_EMAIL);
		mUniqnameView = (EditText) findViewById(R.id.uniqnameInput);
		mUniqnameView.setText(mUniqname);

		mPasswordView = (EditText) findViewById(R.id.password);
//		mPasswordView
//				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//					@Override
//					public boolean onEditorAction(TextView textView, int id,
//							KeyEvent keyEvent) {
//						if (id == R.id.login || id == EditorInfo.IME_NULL) {
//							attemptLogin();
//							return true;
//						}
//						return false;
//					}
//				});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin(View v) {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mUniqnameView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mUniqname = mUniqnameView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mUniqname)) {
			mUniqnameView.setError(getString(R.string.error_field_required));
			focusView = mUniqnameView;
			cancel = true;
		} else if (mUniqname.contains("@")) {
			mUniqnameView.setError(getString(R.string.error_invalid_uniqname));
			focusView = mUniqnameView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
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

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				// Simulate network access.
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				return false;
			}

			for (String credential : DUMMY_CREDENTIALS) {
				String[] pieces = credential.split(":");
				if (pieces[0].equals(mUniqname)) {
					// Account exists, return true if the password matches.
					return pieces[1].equals(mPassword);
				}
			}

			return false;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);

			if (success) {
				setLogin();
				setupParse();
//				finish();
			} else {
				mUniqnameView.setError(getString(R.string.error_incorrect_uniqname));
				mUniqnameView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
}
