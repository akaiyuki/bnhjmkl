package com.smartwave.taskr.fragment;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.OvershootInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.smartwave.taskr.R;
import com.smartwave.taskr.core.AppController;
import com.smartwave.taskr.core.BaseActivity;
import com.smartwave.taskr.core.DBComment;
import com.smartwave.taskr.core.DBHandler;
import com.smartwave.taskr.core.SharedPreferencesCore;
import com.smartwave.taskr.core.TSingleton;
import com.smartwave.taskr.dialog.ChooseDateDialog;
import com.smartwave.taskr.dialog.DialogActivity;
import com.smartwave.taskr.object.CommentObject;
import com.smartwave.taskr.object.TaskObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskDescriptionFragment extends Fragment {

    private ArrayList<TaskObject> mResultSet = new ArrayList<>();

    private FloatingActionMenu mFloatingButton;

    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private FloatingActionButton fab3;
    private FloatingActionButton fab4;


    private List<FloatingActionMenu> menus = new ArrayList<>();
    private Handler mUiHandler = new Handler();

    private FloatingActionButton fabEdit;

    private TextView mTextDate;
    private DBHandler db;
    private TextView mTextEstimate;

    private ArrayList<CommentObject> mResultComment = new ArrayList<>();



    private ExpandableHeightListView mListView;
    private CommentAdapter commentAdapter;
    private EditText mEditComment;
    private Button mButtonComment;


    public TaskDescriptionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task_description, container, false);

        db = new DBHandler(getActivity());

         /* Initialize toolbar */
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.app_bar);
        ((BaseActivity) getActivity()).setSupportActionBar(toolbar);
        ((BaseActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((BaseActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((BaseActivity) getActivity()).getSupportActionBar().setTitle("Task Details");

        ImageView mImageSettings = (ImageView) toolbar.findViewById(R.id.settings);
        mImageSettings.setVisibility(View.GONE);

        TextView mTextLogout = (TextView) toolbar.findViewById(R.id.logout);
        mTextLogout.setVisibility(View.GONE);

        TextView mTextTitle = (TextView) toolbar.findViewById(R.id.titlename);
        mTextTitle.setText("Task Details");


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



        TextView mTextName = (TextView) view.findViewById(R.id.textname);
        TextView mTextDesc = (TextView) view.findViewById(R.id.textdesc);
        TextView mTextStatus = (TextView) view.findViewById(R.id.textstatus);

        mTextDate = (TextView) view.findViewById(R.id.textdate);
        TextView mTextProject = (TextView) view.findViewById(R.id.textproject);



        mTextName.setText(SharedPreferencesCore.getSomeStringValue(AppController.getInstance(),"taskname"));
        mTextDesc.setText(SharedPreferencesCore.getSomeStringValue(AppController.getInstance(), "taskdesc"));
        mTextStatus.setText("Status: "+SharedPreferencesCore.getSomeStringValue(AppController.getInstance(), "taskstatus"));


        mTextDate.setText("Due Date: "+SharedPreferencesCore.getSomeStringValue(AppController.getInstance(),"taskdate"));
        mTextProject.setText(SharedPreferencesCore.getSomeStringValue(AppController.getInstance(),"taskproject"));


        mFloatingButton = (FloatingActionMenu) view.findViewById(R.id.menu_blue);
        mFloatingButton.setClosedOnTouchOutside(true);
        mFloatingButton.hideMenuButton(false);

        fab1 = (FloatingActionButton) view.findViewById(R.id.fab14);
        fab2 = (FloatingActionButton) view.findViewById(R.id.fab24);
        fab3 = (FloatingActionButton) view.findViewById(R.id.fab34);
        fab4 = (FloatingActionButton) view.findViewById(R.id.fab35);

        mTextEstimate = (TextView) view.findViewById(R.id.textestimate);
        mTextEstimate.setText("Estimate: "+TSingleton.getTaskEstimate()+" mins.");


        // Reading comments database
        if (mResultComment.isEmpty()){
            final DBComment db = new DBComment(getActivity());
            final List<CommentObject> comments = db.getAllTask();

            for (CommentObject taskObject : comments) {
                String log = "Id: " + taskObject.getTaskId() + " ,CommentName: " + taskObject.getTaskComment();
                Log.d("Comment: : ", log);

                if (TSingleton.getTaskId().equalsIgnoreCase(taskObject.getTaskId())){
                    mResultComment.add(taskObject);
                }

//                mResultComment.add(taskObject);
            }
        }

        mListView = (ExpandableHeightListView) view.findViewById(R.id.listview);
        commentAdapter = new CommentAdapter(getActivity(), R.layout.custom_row_comment, mResultComment);
        mListView.setAdapter(commentAdapter);
        mListView.setExpanded(true);

        mEditComment = (EditText) view.findViewById(R.id.editcomment);
        mButtonComment = (Button) view.findViewById(R.id.buttoncomment);

        mButtonComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mEditComment.getText().length() != 0){

                    Calendar c = Calendar.getInstance();

                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                    String formattedDate = df.format(c.getTime());


                    final DBComment db = new DBComment(getActivity());
                    db.addTask(new CommentObject(TSingleton.getTaskId(), mEditComment.getText().toString(),
                            SharedPreferencesCore.getSomeStringValue(AppController.getInstance(),"username"), formattedDate));

                    final List<CommentObject> tasks = db.getAllTask();

                    mResultComment.clear();

                    for (CommentObject taskObject : tasks) {
                        String log = "Id: " + taskObject.getTaskId() + " ,CommentName: " + taskObject.getTaskComment();
                        // Writing shops  to log
                        Log.d("Comment from add edit: ", log);

                        if (TSingleton.getTaskId().equalsIgnoreCase(taskObject.getTaskId())){
                            mResultComment.add(taskObject);
                        }

                    }

                    commentAdapter = new CommentAdapter(getActivity(), R.layout.custom_row_comment, mResultComment);
                    commentAdapter.notifyDataSetChanged();
                    mListView.setAdapter(commentAdapter);

                    mEditComment.setText("");


                }

            }
        });


        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        menus.add(mFloatingButton);

        fab1.setOnClickListener(clickListener);
        fab2.setOnClickListener(clickListener);
        fab3.setOnClickListener(clickListener);
        fab4.setOnClickListener(clickListener);

        int delay = 400;
        for (final FloatingActionMenu menu : menus) {
            mUiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    menu.showMenuButton(true);
                }
            }, delay);
            delay += 150;
        }

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                fabEdit.show(true);
//            }
//        }, delay + 150);



//        mFloatingButton.setOnMenuButtonClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mFloatingButton.isOpened()) {
//                    Toast.makeText(getActivity(), mFloatingButton.getMenuButtonLabelText(), Toast.LENGTH_SHORT).show();
//                }
//
//                mFloatingButton.toggle(true);
//            }
//        });

    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fab14:
                    Log.d("clicked", "set estimate");

                    showDialogEstimate();
                    break;
                case R.id.fab24:
                    Log.d("clicked", "due date");
                    ChooseDateDialog datePicker = new ChooseDateDialog((BaseActivity) getActivity(), mTextDate, db);
                    break;
                case R.id.fab34:
                    Log.d("clicked", "move to finished");

                    db.updateTask(Integer.parseInt(TSingleton.getTaskId()),TSingleton.getTaskName(),TSingleton.getTaskDesc(),"finished",
                            TSingleton.getTaskProject(),TSingleton.getTaskDate(),TSingleton.getTaskEstimate());

                    break;

                case R.id.fab35:
                    Log.d("clicked", "add comment");
                    showDialogComment();
                    break;
            }
        }
    };



    private void showDialogEstimate(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_estimate);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final EditText mEditText = (EditText) dialog.findViewById(R.id.edit_estimate);

        LinearLayout mButtonDone = (LinearLayout) dialog.findViewById(R.id.button_ok);
        mButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mEditText.getText().length() != 0){
                    TSingleton.setTaskEstimate(String.valueOf(mEditText.getText()));
                }

                mTextEstimate.setText("Estimate: "+mEditText.getText().toString()+" mins.");

                db.updateTask(Integer.parseInt(TSingleton.getTaskId()),TSingleton.getTaskName(),TSingleton.getTaskDesc(),TSingleton.getTaskStatus(),
                        TSingleton.getTaskProject(),TSingleton.getTaskDate(),TSingleton.getTaskEstimate());


                dialog.dismiss();


            }
        });

        dialog.show();
    }

    private void showDialogComment(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_comment);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final EditText mEditText = (EditText) dialog.findViewById(R.id.edit_comment);

        LinearLayout mButtonDone = (LinearLayout) dialog.findViewById(R.id.button_ok);
        mButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mEditText.getText().length() != 0){

                    Calendar c = Calendar.getInstance();

                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                    String formattedDate = df.format(c.getTime());


                    final DBComment db = new DBComment(getActivity());
                    db.addTask(new CommentObject(TSingleton.getTaskId(), mEditText.getText().toString(), TSingleton.getUserName(), formattedDate));

                    final List<CommentObject> tasks = db.getAllTask();

                    for (CommentObject taskObject : tasks) {
                        String log = "Id: " + taskObject.getTaskId() + " ,CommentName: " + taskObject.getTaskComment();
                        // Writing shops  to log
                        Log.d("Comment from add edit: ", log);

                        mResultComment.add(taskObject);
                    }

                }

                dialog.dismiss();


            }
        });

        dialog.show();
    }


    public class CommentAdapter extends ArrayAdapter<CommentObject> {

        Context mContext;
        int mResId;
        ArrayList<CommentObject> mResultSet = new ArrayList<>();

        class ViewHolder{
            TextView name;
            TextView txtCommentName;
            TextView txtDate;
            ImageView image;
        }

        public CommentAdapter(Context context, int resource, ArrayList<CommentObject> data) {
            super(context, resource, data);
            this.mContext = context;
            this.mResId = resource;
            this.mResultSet = data;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;

            if (convertView == null)
            {
                //Inflate layout
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                convertView = inflater.inflate(mResId, null);
                holder = new ViewHolder();

                holder.name = (TextView) convertView.findViewById(R.id.textview);
                holder.image = (ImageView) convertView.findViewById(R.id.tasklogo);
                holder.txtCommentName = (TextView) convertView.findViewById(R.id.txtname);
                holder.txtDate = (TextView) convertView.findViewById(R.id.txtdate);


                convertView.setTag(holder);
            }
            else { holder = (ViewHolder) convertView.getTag(); }

            final CommentObject comment = mResultSet.get(position);

            if (TSingleton.getTaskId().equalsIgnoreCase(comment.getTaskId())){
                holder.name.setText(comment.getTaskComment());
                holder.txtCommentName.setText(comment.getTaskName());
                holder.txtDate.setText(comment.getCommentDate());
            }
            else {
                holder.name.setText("");
                holder.image.setVisibility(View.GONE);
            }




            return convertView;
        }
    }







}
