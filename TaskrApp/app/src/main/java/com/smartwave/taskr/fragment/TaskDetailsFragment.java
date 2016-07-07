package com.smartwave.taskr.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.smartwave.taskr.R;
import com.smartwave.taskr.activity.LoginActivity;
import com.smartwave.taskr.activity.MainActivity;
import com.smartwave.taskr.activity.UnfoldableDetailsActivity;
import com.smartwave.taskr.core.AppController;
import com.smartwave.taskr.core.BaseActivity;
import com.smartwave.taskr.core.DBHandler;
import com.smartwave.taskr.core.Engine;
import com.smartwave.taskr.core.SharedPreferencesCore;
import com.smartwave.taskr.core.TSingleton;
import com.smartwave.taskr.design.SlidingTabLayout;
import com.smartwave.taskr.dialog.DialogActivity;
import com.smartwave.taskr.object.TaskObject;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.tvbarthel.lib.blurdialogfragment.BlurDialogEngine;


/**
 * A simple {@link Fragment} subclass.
 */
public class TaskDetailsFragment extends Fragment {


    protected String[] tabTitleList = {"BACKLOGS", "IN PROGRESS", "FINISHED"};
    private SamplePagerAdapter mPageAdapter;
    private ListView mListViewTask;
    private ListAdapter listAdapter;
    private ArrayList<TaskObject> mResultSet = new ArrayList<>();
    private ArrayList<TaskObject> mResultSetProgress = new ArrayList<>();
    private ArrayList<TaskObject> mResultSetFinish = new ArrayList<>();
    private ArrayList<TaskObject> mResultSetBacklog = new ArrayList<>();

    public GoogleApiClient google_api_client;


    public TaskDetailsFragment() {
        // Required empty public constructor
    }



    /**
     * A custom {@link android.support.v4.view.ViewPager} title strip which looks much like Tabs present in Android v4.0 and
     * above, but is designed to give continuous feedback to the user when scrolling.
     */
    private SlidingTabLayout mSlidingTabLayout;

    /**
     * A {@link android.support.v4.view.ViewPager} which will be used in conjunction with the {@link SlidingTabLayout} above.
     */
    private ViewPager mViewPager;

    /**
     * Inflates the {@link View} which will be displayed by this {@link Fragment}, from the app's
     * resources.
     */


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task_details, container, false);

        final DBHandler db = new DBHandler(getActivity());

         /* Initialize toolbar */
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.app_bar);
        ((BaseActivity) getActivity()).setSupportActionBar(toolbar);
        ((BaseActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((BaseActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((BaseActivity) getActivity()).getSupportActionBar().setTitle("");

        ImageView mImageSettings = (ImageView) toolbar.findViewById(R.id.settings);
        mImageSettings.setVisibility(View.GONE);

        TextView mTextLogout = (TextView) toolbar.findViewById(R.id.logout);
        mTextLogout.setVisibility(View.GONE);

        TextView mTextTitle = (TextView) toolbar.findViewById(R.id.titlename);
        mTextTitle.setText("Tasks List");


        final List<TaskObject> tasks = db.getAllTask();

        mResultSet.clear();
        mResultSetBacklog.clear();
        mResultSetProgress.clear();
        mResultSetFinish.clear();

        for (TaskObject taskObject : tasks) {
            String log = "Id: " + taskObject.getId() + " ,TaskName: " + taskObject.getTaskName() + " ,TaskDescription: "
                    + taskObject.getTaskDescription() + " ,TaskStatus: "
                    + taskObject.getTaskStatus();
            // Writing shops  to log
            Log.d("Task: : ", log);


            mResultSet.add(taskObject);

            Log.d("resultset", String.valueOf(mResultSet.size()));

        }


        for (int i = 0; i<mResultSet.size(); i++){
            TaskObject object = mResultSet.get(i);

            if (object.getTaskStatus().equalsIgnoreCase("backlogs")){
                mResultSetBacklog.add(object);
            } else if (object.getTaskStatus().equalsIgnoreCase("in progress")){
                mResultSetProgress.add(object);
            } else {
                mResultSetFinish.add(object);
            }

        }



        return view;
    }


    // BEGIN_INCLUDE (fragment_onviewcreated)
    /**
     * This is called after the {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)} has finished.
     * Here we can pick out the {@link View}s we need to configure from the content view.
     *
     * We set the {@link ViewPager}'s adapter to be an instance of {@link SamplePagerAdapter}. The
     * {@link SlidingTabLayout} is then given the {@link ViewPager} so that it can populate itself.
     *
     * @param view View created in {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // BEGIN_INCLUDE (setup_viewpager)
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mPageAdapter = new SamplePagerAdapter();
        mViewPager.setAdapter(mPageAdapter);

        mViewPager.setCurrentItem(1);

        // END_INCLUDE (setup_viewpager)

        // BEGIN_INCLUDE (setup_slidingtablayout)
        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);
        // END_INCLUDE (setup_slidingtablayout)



    }
    // END_INCLUDE (fragment_onviewcreated)


    /**
     * The {@link android.support.v4.view.PagerAdapter} used to display pages in this sample.
     * The individual pages are simple and just display two lines of text. The important section of
     * this class is the {@link #getPageTitle(int)} method which controls what is displayed in the
     * {@link SlidingTabLayout}.
     */
    class SamplePagerAdapter extends PagerAdapter {

        /**
         * @return the number of pages to display
         */
        @Override
        public int getCount() {
            return 3;
        }

        /**
         * @return true if the value returned from {@link #instantiateItem(ViewGroup, int)} is the
         * same object as the {@link View} added to the {@link ViewPager}.
         */
        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        // BEGIN_INCLUDE (pageradapter_getpagetitle)
        /**
         * Return the title of the item at {@code position}. This is important as what this method
         * returns is what is displayed in the {@link SlidingTabLayout}.
         * <p>
         * Here we construct one using the position value, but for real application the title should
         * refer to the item's contents.
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitleList[position];
        }
        // END_INCLUDE (pageradapter_getpagetitle)

        /**
         * Instantiate the {@link View} which should be displayed at {@code position}. Here we
         * inflate a layout from the apps resources and then change the text view to signify the position.
         */
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            // Inflate a new layout from our resources
            View view = getActivity().getLayoutInflater().inflate(R.layout.pager_item,
                    container, false);
            // Add the newly created View to the ViewPager
            container.addView(view);

            // Retrieve a ListView and populate
            mListViewTask = (ListView) view.findViewById(R.id.listview_task);


            if (position == 0) {
                listAdapter = new ListAdapter(getActivity(),R.layout.row_pager_item,mResultSetBacklog);
                listAdapter.notifyDataSetChanged();

                mListViewTask.setAdapter(listAdapter);

            } else if (position == 1) {
                listAdapter = new ListAdapter(getActivity(),R.layout.row_pager_item,mResultSetProgress);
                listAdapter.notifyDataSetChanged();

                mListViewTask.setAdapter(listAdapter);
            } else if (position == 2) {
                listAdapter = new ListAdapter(getActivity(),R.layout.row_pager_item,mResultSetFinish);
                listAdapter.notifyDataSetChanged();

                mListViewTask.setAdapter(listAdapter);
            }

            mListViewTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    TaskObject taskObject = (TaskObject) parent.getItemAtPosition(position);

                    SharedPreferencesCore.setSomeStringValue(AppController.getInstance(),"taskname",taskObject.getTaskName());
                    SharedPreferencesCore.setSomeStringValue(AppController.getInstance(),"taskdesc", taskObject.getTaskDescription());
                    SharedPreferencesCore.setSomeStringValue(AppController.getInstance(),"taskstatus", taskObject.getTaskStatus());

                    SharedPreferencesCore.setSomeStringValue(AppController.getInstance(),"taskdate", taskObject.getTaskDate());
                    SharedPreferencesCore.setSomeStringValue(AppController.getInstance(),"taskproject", taskObject.getTaskProject());

                    TSingleton.setTaskName(taskObject.getTaskName());
                    TSingleton.setTaskDesc(taskObject.getTaskDescription());
                    TSingleton.setTaskStatus(taskObject.getTaskStatus());
                    TSingleton.setTaskDate(taskObject.getTaskDate());
                    TSingleton.setTaskProject(taskObject.getTaskProject());
                    TSingleton.setTaskId(String.valueOf(taskObject.getId()));
                    TSingleton.setTaskEstimate(taskObject.getTaskEstimate());

                    Engine.switchFragment((BaseActivity) getActivity(), new TaskDescriptionFragment(), ((BaseActivity) getActivity()).getFrameLayout());


                }
            });



            // Return the View
            return view;
        }

        /**
         * Destroy the item from the {@link ViewPager}. In our case this is simply removing the
         * {@link View}.
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
//            Log.i(LOG_TAG, "destroyItem() [position: " + position + "]");
        }

    }





    public class ListAdapter extends ArrayAdapter<TaskObject> {

        Context mContext;
        ArrayList<TaskObject> mData = new ArrayList<>();
        int mResId;

        public ListAdapter(Context context, int resource, ArrayList<TaskObject> data) {
            super(context, resource, data);
            this.mContext = context;
            this.mResId = resource;
            this.mData = data;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public TaskObject getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;

            if (convertView == null) {
                //Inflate layout
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                convertView = inflater.inflate(mResId, null);
                holder = new ViewHolder();

                holder.text1 = (TextView) convertView.findViewById(R.id.textview);
//                holder.text2 = (TextView) convertView.findViewById(R.id.progress);
//                holder.text3 = (TextView) convertView.findViewById(R.id.finished);


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final TaskObject taskObject = mData.get(position);

            if (taskObject!=null){
                holder.text1.setText(taskObject.getTaskName());
            }




//            final TaskObject row = mData.get(position);
//
//
//            if (row.getTaskStatus().equalsIgnoreCase("backlogs")){
//                holder.text1.setText(row.getTaskName());
//                holder.text2.setText("");
//                holder.text3.setText("");
//            } else if (row.getTaskStatus().equalsIgnoreCase("in progress")){
//                holder.text2.setText(row.getTaskName());
//                holder.text1.setText("");
//                holder.text3.setText("");
//            } else {
//                holder.text3.setText(row.getTaskName());
//                holder.text2.setText("");
//                holder.text1.setText("");
//            }



            return convertView;
        }

        class ViewHolder {
            TextView text1;
            TextView text2;
            TextView text3;
            TextView text4;
        }
    }












}
