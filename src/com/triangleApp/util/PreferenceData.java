package com.triangleApp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class PreferenceData 
{
    static final String PREF_LOGGEDIN_USER_UNIQNAME = "logged_in_uniqname";
    static final String PREF_USER_LOGGEDIN_STATUS = "logged_in_status";

    public static SharedPreferences getSharedPreferences(Context ctx) 
    {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setLoggedInUserUniqname(Context ctx, String uniqname) 
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_LOGGEDIN_USER_UNIQNAME, uniqname);
        editor.commit();
    }

    public static String getLoggedInUniqname(Context ctx) 
    {
        return getSharedPreferences(ctx).getString(PREF_LOGGEDIN_USER_UNIQNAME, "");
    }

    public static void setUserLoggedInStatus(Context ctx, boolean status) 
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(PREF_USER_LOGGEDIN_STATUS, status);
        editor.commit();
    }

    public static boolean getUserLoggedInStatus(Context ctx) 
    {
        return getSharedPreferences(ctx).getBoolean(PREF_USER_LOGGEDIN_STATUS, false);
    }

    public static void clearLoggedInUniqname(Context ctx) 
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.remove(PREF_LOGGEDIN_USER_UNIQNAME);
        editor.remove(PREF_USER_LOGGEDIN_STATUS);
        editor.commit();
    }   
}