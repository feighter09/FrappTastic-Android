package com.triangleApp.http;

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

import com.triangleApp.util.functionWrapper;

import android.os.AsyncTask;
import android.widget.Toast;

public class HttpPostRequest extends AsyncTask<String, Void, String>{
	
	private HttpClient client;
	private HttpPost post;
	private HttpResponse response;
	private final String url;
	private functionWrapper afterExecute;
	
	public HttpPostRequest(String requestURL){
		url = requestURL;
		client = new DefaultHttpClient();
		post = new HttpPost(requestURL);
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
		afterExecute.fcn(result);
	}
}