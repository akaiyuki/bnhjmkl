package com.smartwave.taskr.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.daprlabs.cardstack.SwipeDeck;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.smartwave.taskr.core.AppController;
import com.smartwave.taskr.core.BaseActivity;
import com.smartwave.taskr.R;
import com.smartwave.taskr.core.DBHandler;
import com.smartwave.taskr.core.Engine;
import com.smartwave.taskr.core.SharedPreferencesCore;
import com.smartwave.taskr.core.TSingleton;
import com.smartwave.taskr.dialog.DialogActivity;
import com.smartwave.taskr.dialog.SampleSupportDialogFragment;
import com.smartwave.taskr.fragment.TaskDescriptionFragment;
import com.smartwave.taskr.object.TaskObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class TaskActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, GoogleApiClient.ConnectionCallbacks {

    private static final String TAG = "TaskActivity";
    private SwipeDeck cardStack;
    private Context context = this;

    private SwipeDeckAdapter adapter;
    private ArrayList<String> testData;

    public GoogleApiClient google_api_client;

    private ArrayList<TaskObject> mResultSet = new ArrayList<>();

    private SwipeDeck mTextProject;
    private SwipeDeck mTextDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

         /* Initialize toolbar */
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Task List");

        google_api_client =  new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API,Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();

        final DBHandler db = new DBHandler(this);

        ImageView mImageSettings = (ImageView) toolbar.findViewById(R.id.settings);
        mImageSettings.setVisibility(View.GONE);
        mImageSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                startActivity(new Intent(TaskActivity.this, MainActivity.class));

                Intent intent = new Intent(TaskActivity.this, MainActivity.class);
                intent.putExtra("goto", "task_details");
                startActivity(intent);


                overridePendingTransition(R.anim.animate_right_to_left, R.anim.animate_fade_out);

            }
        });

        TextView mTextLogout = (TextView) toolbar.findViewById(R.id.logout);
        mTextLogout.setVisibility(View.GONE);
        mTextLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                LoginActivity.INSTANCE.is_signInBtn_clicked = false;

                if (google_api_client.isConnected()) {
                    Plus.AccountApi.clearDefaultAccount(google_api_client);
                    google_api_client.disconnect();
                    google_api_client.connect();
//            changeUI(false);
                }

                    Log.d("sign out clicked", "clicked");

//                TSingleton.setLogoutGmail("1");

                db.removeAll();
                SharedPreferencesCore.clearAllPreferences();

                startActivity(new Intent(TaskActivity.this, LoginActivity.class));
                finish();

            }
        });

        TextView mTextTitle = (TextView) toolbar.findViewById(R.id.titlename);
        mTextTitle.setText("Tasks List");

//       if (!db.getAllTask().isEmpty()){
//           db.removeAll();
//           db.addTask(new TaskObject("Task 1", " Create database", "listed"));
//           db.addTask(new TaskObject("Task 2", "Login with Gmail", "listed"));
//
//       } else {
//           // Inserting Shop/Rows
//           Log.d("Insert: ", "Inserting ..");
//           db.addTask(new TaskObject("Task 1", " Create database", "listed"));
//           db.addTask(new TaskObject("Task 2", "Login with Gmail", "listed"));
//       }



        // Reading all tasks

        mResultSet.clear();

        Log.d("Reading: ", "Reading all tasks..");
        final List<TaskObject> tasks = db.getAllTask();

        for (TaskObject taskObject : tasks) {
            String log = "Id: " + taskObject.getId() + " ,TaskName: " + taskObject.getTaskName() + " ,TaskDescription: "
                    + taskObject.getTaskDescription() + " ,TaskStatus: "
                    + taskObject.getTaskStatus() + " ,TaskProject: "
                    + taskObject.getTaskProject() + " ,TaskDate: "
                    + taskObject.getTaskDate() + " ,TaskEstimate: "
                    + taskObject.getTaskEstimate();
            // Writing shops  to log
            Log.d("Task: : ", log);

            mResultSet.add(taskObject);

        }



        cardStack = (SwipeDeck) findViewById(R.id.swipe_deck);
        cardStack.setHardwareAccelerationEnabled(true);


        testData = new ArrayList<>();
        testData.add("0");
        testData.add("1");
        testData.add("2");
        testData.add("3");
        testData.add("4");

        adapter = new SwipeDeckAdapter(mResultSet, this);
        adapter.notifyDataSetChanged();
        cardStack.setAdapter(adapter);


        cardStack.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int position) {
                Log.i("MainActivity", "card was swiped left, position in adapter: " + position);

                TaskObject taskObject = mResultSet.get(position);

                db.updateTask(taskObject.getId(),taskObject.getTaskName(),taskObject.getTaskDescription(),"backlogs",taskObject.getTaskProject(),taskObject.getTaskDate(),taskObject.getTaskEstimate());

                Log.d("left", String.valueOf(taskObject.getTaskStatus()));

            }

            @Override
            public void cardSwipedRight(int position) {
                Log.i("MainActivity", "card was swiped right, position in adapter: " + position);

                TaskObject taskObject = mResultSet.get(position);

                db.updateTask(taskObject.getId(),taskObject.getTaskName(),taskObject.getTaskDescription(),"in progress",taskObject.getTaskProject(),taskObject.getTaskDate(),taskObject.getTaskEstimate());


                Log.d("right", String.valueOf(taskObject.getTaskStatus()));

            }

            @Override
            public void cardsDepleted() {
                Log.i("MainActivity", "no more cards");

//                int number = 4;
//                float floatNum = (float) ((4.0 / 10f) + 2);
//
//                SampleSupportDialogFragment fragment
//                        = SampleSupportDialogFragment.newInstance(
//                       8,
//                        floatNum,
//                        false,
//                        false,
//                        false,
//                        false
//                );
//                fragment.show(getSupportFragmentManager(), "blur_sample");

                DialogActivity.showDialogMessage(TaskActivity.this,"no cards");



            }

            @Override
            public void cardActionDown() {
                Log.i(TAG, "cardActionDown");
            }

            @Override
            public void cardActionUp() {
                Log.i(TAG, "cardActionUp");
            }

        });
        cardStack.setLeftImage(R.id.left_image);
        cardStack.setRightImage(R.id.right_image);

        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardStack.swipeTopCardLeft(180);

            }
        });
        Button btn2 = (Button) findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardStack.swipeTopCardRight(180);
            }
        });

        Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testData.add("a sample string.");
//                ArrayList<String> newData = new ArrayList<>();
//                newData.add("some new data");
//                newData.add("some new data");
//                newData.add("some new data");
//                newData.add("some new data");
//
//                SwipeDeckAdapter adapter = new SwipeDeckAdapter(newData, context);
//                cardStack.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });


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

    public class SwipeDeckAdapter extends BaseAdapter {

//        private List<String> data;
        private Context context;

        private ArrayList<TaskObject> data = new ArrayList<>();

        public SwipeDeckAdapter(ArrayList<TaskObject> data, Context context) {
            this.data = data;
            this.context = context;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View v = convertView;
            if (v == null) {
                LayoutInflater inflater = getLayoutInflater();
                // normally use a viewholder
                v = inflater.inflate(R.layout.test_card2, parent, false);
            }
            //((TextView) v.findViewById(R.id.textView2)).setText(data.get(position));
//            ImageView imageView = (ImageView) v.findViewById(R.id.offer_image);
//            Picasso.with(context).load(R.drawable.ticket_image).fit().centerCrop().into(imageView);
            TextView textView = (TextView) v.findViewById(R.id.sample_text);

            TextView textProject = (TextView) v.findViewById(R.id.textproject);
            TextView textDate = (TextView) v.findViewById(R.id.textdate);

            TextView textDesc = (TextView) v.findViewById(R.id.textdescription);
//            final String item = (String)getItem(position);
//            textView.setText(item);

//            v.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.i("Layer type: ", Integer.toString(v.getLayerType()));
//                    Log.i("Hwardware Accel type:", Integer.toString(View.LAYER_TYPE_HARDWARE));
//                    Intent i = new Intent(v.getContext(), MainActivity.class);
//                    v.getContext().startActivity(i);
//                }
//            });

            final TaskObject row = data.get(position);
            textView.setText(row.getTaskName());

            textProject.setText(row.getTaskProject());
            textDate.setText("Due Date: "+row.getTaskDate());

            textDesc.setText(row.getTaskDescription());



            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("position_item", row.getTaskName());

                    SharedPreferencesCore.setSomeStringValue(AppController.getInstance(),"taskname",row.getTaskName());
                    SharedPreferencesCore.setSomeStringValue(AppController.getInstance(),"taskdesc", row.getTaskDescription());
                    SharedPreferencesCore.setSomeStringValue(AppController.getInstance(),"taskstatus", "");

                    SharedPreferencesCore.setSomeStringValue(AppController.getInstance(),"taskdate", row.getTaskDate());
                    SharedPreferencesCore.setSomeStringValue(AppController.getInstance(),"taskproject", row.getTaskProject());


                    TSingleton.setTaskName(row.getTaskName());
                    TSingleton.setTaskDesc(row.getTaskDescription());
                    TSingleton.setTaskStatus("");
                    TSingleton.setTaskDate(row.getTaskDate());
                    TSingleton.setTaskProject(row.getTaskProject());
                    TSingleton.setTaskId(String.valueOf(row.getId()));
                    TSingleton.setTaskEstimate("");

                    Intent intent = new Intent(TaskActivity.this, MainActivity.class);
                    intent.putExtra("goto","task_description");
                    startActivity(intent);

                }
            });

            

            return v;
        }
    }

}
