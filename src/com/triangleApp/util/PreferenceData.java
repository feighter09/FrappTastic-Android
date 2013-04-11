package com.triangleApp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class PreferenceData{
    static final String PREF_LOGGEDIN_USER_UNIQNAME = "logged_in_uniqname";
    static final String PREF_LOGGEDIN_USER_PASSWORD = "logged_in_password";
    static final String PREF_LOGGEDIN_USER_FIRSTNAME = "logged_in_firstname";
    static final String PREF_LOGGEDIN_USER_LASTNAME = "logged_in_lastname";
    static final String PREF_USER_LOGGEDIN_STATUS = "logged_in_status";

    public static SharedPreferences getSharedPreferences(Context ctx){
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setLoggedInUserInfo(Context ctx, String uniqname, String password, String fname, String lname){
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_LOGGEDIN_USER_UNIQNAME, uniqname);
        editor.putString(PREF_LOGGEDIN_USER_PASSWORD, password);
        editor.putString(PREF_LOGGEDIN_USER_FIRSTNAME, fname);
        editor.putString(PREF_LOGGEDIN_USER_LASTNAME, lname);
        editor.commit();
    }

    public static String getLoggedInUniqname(Context ctx){
        return getSharedPreferences(ctx).getString(PREF_LOGGEDIN_USER_UNIQNAME, "");
    }
    
    public static String getLoggedInPassword(Context ctx){
        return getSharedPreferences(ctx).getString(PREF_LOGGEDIN_USER_PASSWORD, "");
    }
    
    public static String getLoggedInFirstname(Context ctx){
        return getSharedPreferences(ctx).getString(PREF_LOGGEDIN_USER_FIRSTNAME, "");
    }
    
    public static String getLoggedInLastname(Context ctx){
        return getSharedPreferences(ctx).getString(PREF_LOGGEDIN_USER_LASTNAME, "");
    }

    public static void setUserLoggedInStatus(Context ctx, boolean status){
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(PREF_USER_LOGGEDIN_STATUS, status);
        editor.commit();
    }

    public static boolean getUserLoggedInStatus(Context ctx){
        return getSharedPreferences(ctx).getBoolean(PREF_USER_LOGGEDIN_STATUS, false);
    }

    public static void clearLoggedIn(Context ctx){
        Editor editor = getSharedPreferences(ctx).edit();
        editor.remove(PREF_LOGGEDIN_USER_UNIQNAME);
        editor.remove(PREF_LOGGEDIN_USER_FIRSTNAME);
        editor.remove(PREF_LOGGEDIN_USER_LASTNAME);
        editor.remove(PREF_USER_LOGGEDIN_STATUS);
        editor.commit();
    }   
}