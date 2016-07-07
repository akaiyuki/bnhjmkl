package com.smartwave.taskr.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.ptr.folding.FoldingDrawerLayout;
import com.smartwave.taskr.R;
import com.smartwave.taskr.core.BaseActivity;
import com.smartwave.taskr.core.DBHandler;
import com.smartwave.taskr.core.SharedPreferencesCore;
import com.smartwave.taskr.fragment.ProfileFragment;
import com.smartwave.taskr.fragment.TaskSwipeFragment;

public class InitialActivity extends ActionBarActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, GoogleApiClient.ConnectionCallbacks {

    private FoldingDrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mAnimalTitles;
    private ItemSelectedListener mItemSelectedListener;

    public GoogleApiClient google_api_client;


    static final boolean IS_HONEYCOMB = Build.VERSION.SDK_INT == Build.VERSION_CODES.HONEYCOMB;
    public static InitialActivity INSTANCE = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        INSTANCE = this;

        google_api_client =  new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API,Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();

        mTitle = mDrawerTitle = getTitle();
        mAnimalTitles = getResources().getStringArray(R.array.animal_array);
        mDrawerLayout = (FoldingDrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // mFoldLayout = (FoldingNavigationLayout)findViewById(R.id.fold_view);
        // mFoldLayout.setBackgroundColor(Color.BLACK);

        // set a custom shadow that overlays the main content when the drawer
        // opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mAnimalTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mItemSelectedListener = new ItemSelectedListener();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        // TODO: Remove the redundant calls to getSupportActionBar()
        //       and use variable actionBar instead
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
        getSupportActionBar().setTitle("Taskr");
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);


//        /* Initialize toolbar */
//        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
//        setTitle("");




        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
                mDrawerLayout, /* DrawerLayout object */
                R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open, /* "open drawer" description for accessibility */
                R.string.drawer_close /* "close drawer" description for accessibility */
        ) {

            @SuppressLint("NewApi")
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                if (IS_HONEYCOMB) {
                    invalidateOptionsMenu(); // creates call to
                    // onPrepareOptionsMenu()
                }

            }

            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @SuppressLint("NewApi")
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                if (IS_HONEYCOMB) {
                    invalidateOptionsMenu(); // creates call to
                    // onPrepareOptionsMenu()
                }
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);


        if (savedInstanceState == null) {
            selectItem(2);
        }


    }


    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    protected void onStart() {
        super.onStart();
        google_api_client.connect();
    }

    protected void onStop() {
        super.onStop();
        if (google_api_client.isConnected()) {
            google_api_client.disconnect();
        }
    }

    protected void onResume(){
        super.onResume();
        if (google_api_client.isConnected()) {
            google_api_client.connect();
        }
    }


    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
//        Fragment fragment = new MenuFragment();
////        Bundle args = new Bundle();
////        args.putInt(AnimalFragment.ARG_ANIMAL_NUMBER, position);
////        fragment.setArguments(args);
//
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.framelayout, fragment).commit();

//        startActivity(new Intent(InitialActivity.this, TaskActivity.class));
        if (position == 0){
            Fragment fragment = new ProfileFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.framelayout, fragment).commit();
        }
        if (position == 1){
            Intent intent = new Intent(InitialActivity.this, MainActivity.class);
            intent.putExtra("goto", "task_details");
            startActivity(intent);
        }
        else if (position == 2){
//            startActivity(new Intent(InitialActivity.this, TaskActivity.class));

            Fragment fragment = new TaskSwipeFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.framelayout, fragment).commit();

        } else if (position == 3){
            if (google_api_client.isConnected()) {
                Plus.AccountApi.clearDefaultAccount(google_api_client);
                google_api_client.disconnect();
                google_api_client.connect();
//            changeUI(false);
            }

            Log.d("sign out clicked", "clicked");

//                TSingleton.setLogoutGmail("1");
            final DBHandler db = new DBHandler(this);
            db.removeAll();
            SharedPreferencesCore.clearAllPreferences();

            startActivity(new Intent(InitialActivity.this, LoginActivity.class));
            finish();
        }


        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle("Sample App");
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
//        getSupportActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(config);
    }

    /**
     * Listens for selection events of the spinner located on the action bar.
     * Every time a new value is selected, the number of folds in the folding
     * view is updated and is also restored to a default unfolded state.
     */
    private class ItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            int mNumberOfFolds = Integer.parseInt(parent.getItemAtPosition(pos).toString());

            mDrawerLayout.getFoldingLayout(mDrawerList).setNumberOfFolds(mNumberOfFolds);

        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }


}
