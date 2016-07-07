package com.smartwave.taskr.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.smartwave.taskr.R;
import com.smartwave.taskr.core.BaseActivity;
import com.smartwave.taskr.core.Engine;
import com.smartwave.taskr.fragment.TaskDescriptionFragment;
import com.smartwave.taskr.fragment.TaskDetailsFragment;

public class MainActivity extends BaseActivity {

    public static MainActivity INSTANCE = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        INSTANCE = this;

                /* Initialize Frame Layout */
        setFrameLayout(R.id.framelayout);

//        Engine.switchFragment(INSTANCE, new TaskDetailsFragment(), getFrameLayout());

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {

            if (extras.containsKey("goto"))
            {
                Bundle bundle = new Bundle();
                Fragment newFragment = null;
                if (extras.getString("goto").equalsIgnoreCase("task_details")) {
                    newFragment = new TaskDetailsFragment();
                }
                else if (extras.getString("goto").equalsIgnoreCase("task_description")){
                    newFragment = new TaskDescriptionFragment();
                }

                try {
                    assert newFragment != null;
                    newFragment.setArguments(bundle);
                    Engine.switchFragment(INSTANCE, newFragment, getFrameLayout());
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
        }


    }
}
