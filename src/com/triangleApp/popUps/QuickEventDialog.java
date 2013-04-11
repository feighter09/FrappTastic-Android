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

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.parse.*;
import com.triangleApp.R;
import com.triangleApp.util.PreferenceData;

public class QuickEventDialog extends DialogFragment {
    
	public String eventTitle, eventDate, eventTime, eventType;
	public Context activityContext;
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	activityContext = getActivity();
		
		String dialogMessage = "Send " + eventType + " event: \"" + eventTitle + "\" at " + eventTime + ", " + eventDate + "?";
    	final String pushMessage = "New " + eventType + " event: \"" + eventTitle + "\" at " + eventTime + ", " + eventDate; 
    	
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.areYouSure)
        	   .setMessage(dialogMessage)
               .setPositiveButton(R.string.quickEventNotify, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       //send event to DB
                       String name;
                       name = PreferenceData.getLoggedInFirstname(getActivity()) + " " 
                    		   + PreferenceData.getLoggedInLastname(getActivity());
                       
                       String dayStr, monthStr, yearStr, dateStr;
                       dayStr = eventDate.substring(3, 5);
                       monthStr = eventDate.substring(0, 2);
                       yearStr = eventDate.substring(6);
                       dateStr = yearStr + "-" + monthStr + "-" + dayStr;
                       
                       int hours;
                       if(eventTime.charAt(1) == ':')
                    	   hours = Integer.parseInt( eventTime.substring(0, 1) );
                       else
                    	   hours = Integer.parseInt( eventTime.substring(0, 2) );
                       
                       if( eventTime.subSequence( eventTime.length() - 2, eventTime.length() ).equals("PM") )
                    	   hours += 12;
                       if(hours % 12 == 0)
                    	   hours -= 12;
                       
                       String time = hours < 10 ? "0" + hours : "" + hours;
                       time += eventTime.charAt(1) == ':' ? eventTime.substring(1, 4) : eventTime.substring(2, 5);  
                       new QuickEventHttpPost().execute(eventTitle, name, dateStr, time);
                       
                       //send push via parse
                	   ParsePush push = new ParsePush();
                       push.setChannel(eventType);
                       push.setMessage(pushMessage);
                       push.sendInBackground();
                   }
               })
               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // User cancelled the dialog
                   }
               });
        // Create the AlertDialog object and return it
        return builder.create();
    }
	
	public void sendToast(String str){
		Toast.makeText(getActivity(), str, Toast.LENGTH_LONG).show();
	}
	
	private class QuickEventHttpPost extends AsyncTask<String, Void, String>{
		
		private HttpClient client;
		private HttpPost post;
		private HttpResponse response;
		private final String url = "http://www.triangleumich.com/add_quick_event.php";
		
		public QuickEventHttpPost(){
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
			
			if(params.length != 4)
				return null;
			
			List<NameValuePair> phpParams = new ArrayList<NameValuePair>();
			phpParams.add(new BasicNameValuePair("title", params[0]));
			phpParams.add(new BasicNameValuePair("name", params[1]));
			phpParams.add(new BasicNameValuePair("date", params[2]));
			phpParams.add(new BasicNameValuePair("time", params[3]));
			
			return doPost(phpParams);
		}
		
		@Override
		protected void onPostExecute(String result){
//			Toast.makeText(activityContext, result, Toast.LENGTH_LONG).show();
			parseResult(result);
		}
		
		private boolean parseResult(String response){
			if(response.equals("success"))
				return true;
			
			String result;
			result = response.substring(8);
			
			if( response.substring(0, 7).equals("failure") ){
				result = response.substring(9);
				return false;
			}
			
			return true;
		}
		
	}
	
	public String getEventTitle() {
		return eventTitle;
	}

	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}
	
	public String getEventDate() {
		return eventDate;
	}

	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}

	public String getEventTime() {
		return eventTime;
	}

	public void setEventTime(String eventTime) {
		this.eventTime = eventTime;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	
}
