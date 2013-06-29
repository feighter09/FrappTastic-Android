package com.triangleApp.util;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseAnalytics;

public class DefaultApplication extends Application{
	
	@Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, "JTeN6P0AlreKF6pmi8G4TBnHpH5N25WMRLcmNlg4", 
 			   "sAGC53pRcBjfGysAJU8tlM9m5QBxc6UhT4uMow71"); 
    }
}
