package com.smartwave.taskr.dialog;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartwave.taskr.R;
import com.smartwave.taskr.core.BaseActivity;
import com.smartwave.taskr.core.TSingleton;

/**
 * Created by smartwavedev on 6/20/16.
 */
public class DialogActivity {

    public static void showDialogMessage(BaseActivity baseActivity, String message){

        final Dialog dialog = new Dialog(baseActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_fragment);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        TextView mTextTitle = (TextView) dialog.findViewById(R.id.textView);
        mTextTitle.setText("No more cards to display");


        LinearLayout mButtonDone = (LinearLayout) dialog.findViewById(R.id.button_ok);
        mButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.dismiss();


            }
        });

        dialog.show();


    }

    public static void showDialogEstimate(BaseActivity baseActivity){
        final Dialog dialog = new Dialog(baseActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_estimate);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final EditText mEditText = (EditText) dialog.findViewById(R.id.edit_estimate);



        LinearLayout mButtonDone = (LinearLayout) dialog.findViewById(R.id.button_ok);
        mButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mEditText.getText().length() != 0){
                    TSingleton.setTaskEstimate(mEditText.getText().toString());
                    Log.d("estimate",mEditText.getText().toString());
                }

                dialog.dismiss();


            }
        });

        dialog.show();

    }


}
