package com.smartwave.taskr.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.smartwave.taskr.R;
import com.smartwave.taskr.core.BaseActivity;
import com.smartwave.taskr.core.DBHandler;
import com.smartwave.taskr.object.TaskObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;
import de.codecrafters.tableview.TableView;

public class SettingsActivity extends BaseActivity {

    private ArrayList<TaskObject> mResultSet = new ArrayList<>();
    private ExpandableHeightListView expandableListView;
    private ListAdapter listAdapter;

    private ListTableDataAdapter listTableDataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

         /* Initialize toolbar */
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("");

        ImageView mImageSettings = (ImageView) toolbar.findViewById(R.id.settings);
        mImageSettings.setVisibility(View.GONE);

        TextView mTextTitle = (TextView) toolbar.findViewById(R.id.titlename);
        mTextTitle.setText("Tasks List");


        final DBHandler db = new DBHandler(this);

        final List<TaskObject> tasks = db.getAllTask();

        mResultSet.clear();

        for (TaskObject taskObject : tasks) {
            String log = "Id: " + taskObject.getId() + " ,TaskName: " + taskObject.getTaskName() + " ,TaskDescription: "
                    + taskObject.getTaskDescription() + " ,TaskStatus: "
                    + taskObject.getTaskStatus();
            // Writing shops  to log
            Log.d("Task: : ", log);


            mResultSet.add(taskObject);

            Log.d("resultset", String.valueOf(mResultSet.size()));

        }

        listAdapter = new ListAdapter(mResultSet, this);

        expandableListView = (ExpandableHeightListView) findViewById(R.id.expandable_listview);
        listAdapter.notifyDataSetChanged();
        expandableListView.setAdapter(listAdapter);

        expandableListView.setExpanded(true);


//        listTableDataAdapter = new ListTableDataAdapter(this, mResultSet);
//
//
//        TableView tableView = (TableView) findViewById(R.id.tableView);
//        tableView.setColumnCount(3);
//        tableView.setColumnWeight(0, 2);
//
//        tableView.setDataAdapter(listTableDataAdapter);

        expandableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TaskObject taskObject = (TaskObject) listAdapter.getItem(position);

                Log.d("selectedTask", taskObject.getTaskName());
            }
        });

    }



    public class ListAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<TaskObject> data = new ArrayList<>();

        public ListAdapter(ArrayList<TaskObject> data, Context context) {
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
                v = inflater.inflate(R.layout.row_item, parent, false);
            }
            TextView textView = (TextView) v.findViewById(R.id.backlog);
            TextView txtProgress = (TextView) v.findViewById(R.id.progress);
            TextView txtFinished = (TextView) v.findViewById(R.id.finished);

            final TaskObject row = data.get(position);


            if (row.getTaskStatus().equalsIgnoreCase("backlogs")){
                textView.setText(row.getTaskName());
                txtProgress.setText("");
                txtFinished.setText("");
            } else if (row.getTaskStatus().equalsIgnoreCase("in progress")){
                txtProgress.setText(row.getTaskName());
                textView.setText("");
                txtFinished.setText("");
            } else {
                txtFinished.setText(row.getTaskName());
                txtProgress.setText("");
                textView.setText("");
            }







            return v;
        }
    }



    public class ListTableDataAdapter extends TableDataAdapter<TaskObject> {

        private Context context;
        private ArrayList<TaskObject> data = new ArrayList<>();


        public ListTableDataAdapter(Context context, ArrayList<TaskObject> data) {
            super(context, data);

            this.data = data;
            this.context = context;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public TaskObject getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
            TaskObject row = getRowData(rowIndex);
            View renderedView = null;



            if (renderedView == null) {
                LayoutInflater inflater = getLayoutInflater();
                // normally use a viewholder
                renderedView = inflater.inflate(R.layout.row_item, parentView, false);
            }
            TextView textView = (TextView) renderedView.findViewById(R.id.backlog);
            TextView txtProgress = (TextView) renderedView.findViewById(R.id.progress);
            TextView txtFinished = (TextView) renderedView.findViewById(R.id.finished);

//            final TaskObject row = data.get(position);


            if (row.getTaskStatus().equalsIgnoreCase("backlogs")){
                textView.setText(row.getTaskName());
            } else if (row.getTaskStatus().equalsIgnoreCase("in progress")){
                txtProgress.setText(row.getTaskName());
            } else {
                txtFinished.setText(row.getTaskName());
            }





            switch (columnIndex) {
                case 0:
//                    renderedView = renderProducerLogo(car);


                    if (row.getTaskStatus().equalsIgnoreCase("backlogs")){
                        textView.setText(row.getTaskName());
                        Log.d("backlog", "backlog");
                    } else {
                        Log.d("backlog", "else");
                        textView.setText(row.getTaskName());
                    }

                    break;
                case 1:
//                    renderedView = renderCatName(car);
                    if (row.getTaskStatus().equalsIgnoreCase("in progress")){
                    txtProgress.setText(row.getTaskName());
                        Log.d("in progress", "in progress");
                } else {
                        Log.d("in progress", "else");
                        txtProgress.setText(row.getTaskName());
                    }
                    break;
                case 2:
//                    renderedView = renderPower(car);
                    if (row.getTaskStatus().equalsIgnoreCase("finished")){
                        txtFinished.setText(row.getTaskName());
                        Log.d("finished", "finished");
                    } else {
                        Log.d("finished", "else");
                        txtFinished.setText(row.getTaskName());
                    }
                    break;
            }

            return renderedView;
        }

    }




}
