package com.smartwave.taskr.core;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by smartwavedev on 6/20/16.
 */
public class AppController extends Application {

    private SQLiteDatabase mDatabase;
    private static AppController mInstance;


    @Override
    public void onCreate() {
        super.onCreate();



        mInstance = this;
//        JodaTimeAndroid.init(this);
        SharedPreferencesCore.init(mInstance);
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }


}
