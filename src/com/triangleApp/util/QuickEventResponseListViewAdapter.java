package com.triangleApp.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.triangleApp.R;

public class QuickEventResponseListViewAdapter extends BaseAdapter {

    private Context context;
    private List<QuickEventResponse> responses;

	public QuickEventResponseListViewAdapter(Context context, JSONArray arr) {
        this.context = context;
        this.responses = new ArrayList<QuickEventResponse>();
        
        for (int i = 0; i < arr.length(); i++) {
        	QuickEventResponse response = new QuickEventResponse();
        	try {
            	response.setName( arr.getJSONObject(i).get("name").toString() );
            	response.setGoing( arr.getJSONObject(i).get("going").toString() );
            	
                responses.add(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
	}
	
	public QuickEventResponse getContact(int position){
		return responses.get(position);
	}

    @Override
    public int getCount() {
        return responses.size();
    }

    @Override
    public Object getItem(int position) {
        return responses.get(position);
    }


	@Override
    public long getItemId(int position) {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
    	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.quick_event_response, null);
        convertView.setClickable(false);
        
        TextView name, going;
        name = (TextView) convertView.findViewById(R.id.responseName);
        going = (TextView) convertView.findViewById(R.id.responseGoing);

        name.setText( responses.get(position).getName() );
        going.setText( responses.get(position).getGoing() );
        return convertView;
    }

}
