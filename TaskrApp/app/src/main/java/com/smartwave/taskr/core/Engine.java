package com.smartwave.taskr.core;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.inputmethod.InputMethodManager;

import com.smartwave.taskr.core.BaseActivity;

/**
 * Created by smartwavedev on 3/30/16.
 */
public class Engine {
    public static void switchFragment(BaseActivity baseActivity, Fragment fragment, int frame) {

        FragmentManager fm = baseActivity.getSupportFragmentManager();

        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(frame, fragment);
        transaction.addToBackStack(fragment.getClass().toString());
        transaction.commit();
    }

    public static void onHomePress(BaseActivity baseActivity) {
        FragmentManager fm = baseActivity.getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 1) {
            fm.popBackStack();
        } else {
            baseActivity.finish();
        }

    }

    public static Fragment getActiveFragment(BaseActivity baseActivity) {
        FragmentManager fm = baseActivity.getSupportFragmentManager();

        if (fm.getBackStackEntryCount() == 0) {
            return null;
        }
        String tag = fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName();
        return fm.findFragmentByTag(tag);
    }

    public static void popFragment(BaseActivity baseActivity) {
        baseActivity.getSupportFragmentManager().popBackStack();
    }

    public static void hideSoftKeyboard(BaseActivity baseActivity) {

        if (baseActivity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) baseActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(baseActivity.getCurrentFocus().getWindowToken(), 0);

        }
    }

}
