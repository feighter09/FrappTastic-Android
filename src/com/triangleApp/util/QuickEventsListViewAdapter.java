package com.triangleApp.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.triangleApp.QuickEventResponses;
import com.triangleApp.R;

public class QuickEventsListViewAdapter extends BaseAdapter{
    
	private Context context;
    private List<QuickEventListing> quickEvents;

	public QuickEventsListViewAdapter(Context context, JSONArray arr) {
        this.context = context;
        this.quickEvents = new ArrayList<QuickEventListing>();
        
        for (int i = 0; i < arr.length(); i++) {
        	QuickEventListing quickEvent = new QuickEventListing();
        	try {
            	quickEvent.setTitle( arr.getJSONObject(i).get("title").toString() );
            	quickEvent.setCreator( arr.getJSONObject(i).get("creator").toString() );
            	
            	String date, tempDate, time, tempTime, when;
            	tempDate = arr.getJSONObject(i).get("date").toString();
            	date = tempDate.substring(5) + "-" + tempDate.substring(0, 4);
            	
            	tempTime = arr.getJSONObject(i).get("time").toString();
            	boolean pm = false;
            	int hours = Integer.parseInt(tempTime.substring(0, 2));
            	
            	if(hours >= 12)
            		pm = true;
            	if(pm && hours != 12)
            		hours -= 12;
            	if(hours == 0)
            		hours = 12;
            	
            	time = hours + tempTime.substring(2, 5);
            	when = time + (pm ? "pm" : "am") + ", " + date;
            	quickEvent.setDate(date);
            	quickEvent.setTime(when.substring(0, when.indexOf(',')));
            	quickEvent.setWhen(when);
        		
                quickEvents.add(quickEvent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        
	}
	
	public QuickEventListing getContact(int position){
		return quickEvents.get(position);
	}

    @Override
    public int getCount() {
        return quickEvents.size();
    }

    @Override
    public Object getItem(int position) {
        return quickEvents.get(position);
    }


	@Override
    public long getItemId(int position) {
        return position;
    }
	
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
    	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.quick_event_listing, null);
        
        TextView titleView, creatorView, whenView;
        titleView = (TextView) convertView.findViewById(R.id.quickEventListingTitle);
        creatorView = (TextView) convertView.findViewById(R.id.quickEventListingCreator);
        whenView = (TextView) convertView.findViewById(R.id.quickEventListingWhen);

        final String title, creator, when, date, time;
        title = quickEvents.get(position).getTitle();
        creator = quickEvents.get(position).getCreator();
        when = quickEvents.get(position).getWhen();
        date = quickEvents.get(position).getDate();
        time = quickEvents.get(position).getTime();
        
        titleView.setText(title);
        creatorView.setText(creator);
        whenView.setText(when);
        
        convertView.setClickable(true);
        convertView.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(context, QuickEventResponses.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				
				intent.putExtra("title", title);
				intent.putExtra("creator", creator);
				intent.putExtra("date", date);
				intent.putExtra("time", time);
				
				context.startActivity(intent);
			}
		});
        
        return convertView;
    }

}
