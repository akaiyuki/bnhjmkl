package com.smartwave.taskr.core;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by smartwavedev on 6/20/16.
 */
public class SharedPreferencesCore {

    private static SharedPreferences mSharedPreferences;
    private static Context mContext;

    private static final String APP_PREFS= "APP_SETTINGS";

    private SharedPreferencesCore() {}

    public static void init(Context context) {
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
    }

    private static SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }

    public static String getSomeStringValue(Context context, String key) {
        return mSharedPreferences.getString(key , "");
    }

    public static void setSomeStringValue(Context context, String key, String newValue) {
        final SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key , newValue);
//        editor.commit();
        editor.apply();
    }

//    public static void setSomeArrayValue(String key, ArrayList<String> newValue) {
//
//        for (int i = 0; i<newValue.size(); i++){
//            String newKey = newValue.get(i);
//
//            SharedPreferencesCore.setSomeStringValue(AppController.getInstance(), key+newKey, newValue.get(i));
//        }
//
//
//    }


    public static void saveJsonToSharedPref(JSONObject jsonObj, String keyOffset) {

        Iterator<String> iter = jsonObj.keys();
        while (iter.hasNext()) {
            String key = iter.next();
            try {
                SharedPreferencesCore.setSomeStringValue(AppController.getInstance(), keyOffset+key, jsonObj.getString(key));
//                PDebug.logDebug("saveJsonToSharedPref", key + " = " + jsonObj.getString(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    public static void clearAllPreferences() {
//        mSharedPreferences.edit().clear().commit();
        mSharedPreferences.edit().clear().apply();
    }

}


