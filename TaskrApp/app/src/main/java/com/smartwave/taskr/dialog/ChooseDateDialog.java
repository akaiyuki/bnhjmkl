package com.smartwave.taskr.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.smartwave.taskr.core.BaseActivity;
import com.smartwave.taskr.core.DBHandler;
import com.smartwave.taskr.core.TSingleton;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by smartwavedev on 6/27/16.
 */
public class ChooseDateDialog implements DatePickerDialog.OnDateSetListener{

    private Calendar calendar;
    private EditText mEditText;
    private Context mContext;
    private TextView mTextDate;
    private DBHandler dbHandler;

    public ChooseDateDialog(BaseActivity baseActivity, TextView editText, DBHandler dbHandler) {
        this.mTextDate = editText;
        this.dbHandler = dbHandler;
        calendar = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        dpd.show(baseActivity.getFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(year, monthOfYear, dayOfMonth);

//        mEditText.setText(monthOfYear + 1 + "/" + dayOfMonth + "/" + year);

        String date = monthOfYear + 1 + "/" + dayOfMonth + "/" + year;
        TSingleton.setDueDate(date);

        dbHandler.updateTask(Integer.parseInt(TSingleton.getTaskId()),TSingleton.getTaskName(),TSingleton.getTaskDesc(),TSingleton.getTaskStatus(),
                TSingleton.getTaskProject(),date,TSingleton.getTaskEstimate());



        mTextDate.setText("Due Date: "+date);

    }



}
