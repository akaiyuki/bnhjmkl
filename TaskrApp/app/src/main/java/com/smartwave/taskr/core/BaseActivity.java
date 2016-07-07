package com.smartwave.taskr.core;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

public class BaseActivity extends ActionBarActivity  {

    public static BaseActivity INSTANCE;

    private int mFrameLayout;

    public BaseActivity() {
        INSTANCE = this;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 1) {
            super.onBackPressed();
        } else {
            finish();
        }

    }

    public int getFrameLayout() {
        return this.mFrameLayout;
    }

    public void setFrameLayout(int resId) {
        this.mFrameLayout = resId;
    }

}
