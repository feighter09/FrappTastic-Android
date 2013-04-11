package com.triangleApp.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.triangleApp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class DirectoryListViewAdapter extends BaseAdapter {

    private Context context;
    private List<DirectoryContact> contacts;

	public DirectoryListViewAdapter(Context context, JSONArray arr) {
        this.context = context;
        this.contacts = new ArrayList<DirectoryContact>();
        
        for (int i = 0; i < arr.length(); i++) {
        	DirectoryContact contact = new DirectoryContact();
        	try {
            	String name = arr.getJSONObject(i).get("first").toString() + " "
            					+ arr.getJSONObject(i).get("last").toString();
            	contact.setName(name);
            	contact.setPhone( arr.getJSONObject(i).get("phone").toString() );
            	contact.setEmail( arr.getJSONObject(i).get("email").toString() + "@umich.edu" );
            	
                contacts.add(contact);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
	}
	
	public DirectoryContact getContact(int position){
		return contacts.get(position);
	}

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int position) {
        return contacts.get(position);
    }


	@Override
    public long getItemId(int position) {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
    	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.directory_contact, null);
        
        TextView name, phone, email;
        name = (TextView) convertView.findViewById(R.id.contactName);
        phone = (TextView) convertView.findViewById(R.id.contactPhoneNumber);
        email = (TextView) convertView.findViewById(R.id.contactEmail);

        name.setText( contacts.get(position).getName() );
        phone.setText( contacts.get(position).getPhone() );
        email.setText( contacts.get(position).getEmail() );
        return convertView;
    }

}
