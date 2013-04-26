package com.triangleApp.popUps;

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

import com.triangleApp.QuickEventResponses;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

public class QuickEventRespondDialog extends DialogFragment {
	
	private String title, name, date, time, going;
	public Context activityContext;
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	activityContext = getActivity();
		
		String dialogMessage = "Are you going to this event?";
    	
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(dialogMessage)
               .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   //send response to DB
                       String dayStr, monthStr, yearStr, dateStr;
                       dayStr = date.substring(3, 5);
                       monthStr = date.substring(0, 2);
                       yearStr = date.substring(6);
                       dateStr = yearStr + "-" + monthStr + "-" + dayStr;
                       
                       time = formatTime(time);
                       going = "1";
                       new QuickEventResponseHttpPost().execute(title, name, dateStr, time, going);
                   }
               })
               .setNegativeButton("No", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   //send response to DB
                       String dayStr, monthStr, yearStr, dateStr;
                       dayStr = date.substring(3, 5);
                       monthStr = date.substring(0, 2);
                       yearStr = date.substring(6);
                       dateStr = yearStr + "-" + monthStr + "-" + dayStr;
                       
                       time = formatTime(time);
                       going = "0";
                	   new QuickEventResponseHttpPost().execute(title, name, dateStr, time, going);
                   }
               });
        // Create the AlertDialog object and return it
        return builder.create();
    }
	
	private class QuickEventResponseHttpPost extends AsyncTask<String, Void, String>{
		
		private HttpClient client;
		private HttpPost post;
		private HttpResponse response;
		private final String url = "http://www.triangleumich.com/quick_event_response.php";
		
		public QuickEventResponseHttpPost(){
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
			
			String responseStr = "";
			try {
				responseStr = EntityUtils.toString(response.getEntity());
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return responseStr;
		}

		@Override
		protected String doInBackground(String... params) {
			client = new DefaultHttpClient();
			post = new HttpPost(url);
			
			if(params.length != 5)
				return null;
			
			List<NameValuePair> phpParams = new ArrayList<NameValuePair>();
			phpParams.add(new BasicNameValuePair("title", params[0]));
			phpParams.add(new BasicNameValuePair("name", params[1]));
			phpParams.add(new BasicNameValuePair("date", params[2]));
			phpParams.add(new BasicNameValuePair("time", params[3]));
			phpParams.add(new BasicNameValuePair("going", params[4]));
			
			return doPost(phpParams);
		}
		
		@Override
		protected void onPostExecute(String result){
			if(!result.equals("success"))
				result = "Something went wrong, if this persists, let Austin know he fucked up.";
			
			if(activityContext instanceof QuickEventResponses)
				((QuickEventResponses)activityContext).loadResponses();
			else
				Toast.makeText(activityContext, result, Toast.LENGTH_LONG).show();
		}
		
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
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getGoing() {
		return going;
	}

	public void setGoing(String going) {
		this.going = going;
	}

	public Context getActivityContext() {
		return activityContext;
	}

	public void setActivityContext(Context activityContext) {
		this.activityContext = activityContext;
	}
	
}
