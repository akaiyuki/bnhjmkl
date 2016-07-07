package com.smartwave.taskr.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.smartwave.taskr.object.TaskObject;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "shopsInfo";

    // Contacts table name
    private static final String TABLE_TASKS = "tasks";

    // Shops Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TASK_NAME = "task_name";
    private static final String KEY_TASK_DESCRIPTION = "task_description";
    private static final String KEY_TASK_STATUS = "task_status";
    private static final String KEY_TASK_PROJECT = "task_project";
    private static final String KEY_TASK_DATE = "task_date";
    private static final String KEY_TASK_ESTIMATE = "task_estimate";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TASK_NAME + " TEXT,"
                + KEY_TASK_DESCRIPTION + " TEXT,"
                + KEY_TASK_STATUS + " TEXT,"
                + KEY_TASK_PROJECT + " TEXT,"
                + KEY_TASK_DATE + " TEXT,"
                + KEY_TASK_ESTIMATE + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        // Creating tables again
        onCreate(db);
    }

    // Adding new shop
    public void addTask(TaskObject taskObject) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TASK_NAME, taskObject.getTaskName()); // Shop Name
        values.put(KEY_TASK_DESCRIPTION, taskObject.getTaskDescription()); // Shop Phone Number
        values.put(KEY_TASK_STATUS, taskObject.getTaskStatus());
        values.put(KEY_TASK_PROJECT, taskObject.getTaskProject());
        values.put(KEY_TASK_DATE, taskObject.getTaskDate());
        values.put(KEY_TASK_ESTIMATE, taskObject.getTaskEstimate());

        // Inserting Row
        db.insert(TABLE_TASKS, null, values);
        db.close(); // Closing database connection
    }

    // Getting one shop
    public TaskObject getTask(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TASKS, new String[]{KEY_ID,
                        KEY_TASK_NAME, KEY_TASK_DESCRIPTION, KEY_TASK_STATUS, KEY_TASK_PROJECT, KEY_TASK_DATE, KEY_TASK_ESTIMATE}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        TaskObject contact = new TaskObject(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
        // return shop
        return contact;
    }

    // Getting All Shops
    public List<TaskObject> getAllTask() {
        List<TaskObject> shopList = new ArrayList<TaskObject>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TASKS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TaskObject taskObject = new TaskObject();
                taskObject.setId(Integer.parseInt(cursor.getString(0)));
                taskObject.setTaskName(cursor.getString(1));
                taskObject.setTaskDescription(cursor.getString(2));
                taskObject.setTaskStatus(cursor.getString(3));
                taskObject.setTaskProject(cursor.getString(4));
                taskObject.setTaskDate(cursor.getString(5));
                taskObject.setTaskEstimate(cursor.getString(6));
                // Adding contact to list
                shopList.add(taskObject);
            } while (cursor.moveToNext());
        }

        // return contact list
        return shopList;
    }

    // Getting shops Count
    public int getTaskCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TASKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    // Updating a shop
    public int updateTask(int id, String taskName, String taskDescription, String taskStatus, String taskProject, String taskDate, String taskEstimate) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TASK_NAME, taskName);
        values.put(KEY_TASK_DESCRIPTION, taskDescription);
        values.put(KEY_TASK_STATUS, taskStatus);
        values.put(KEY_TASK_PROJECT, taskProject);
        values.put(KEY_TASK_DATE, taskDate);
        values.put(KEY_TASK_ESTIMATE, taskEstimate);

        // updating row
        return db.update(TABLE_TASKS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    // Deleting a shop
    public void deleteTask(TaskObject taskObject) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, KEY_ID + " = ?",
                new String[] { String.valueOf(taskObject.getId()) });
        db.close();
    }


    public void removeAll()
    {
        // db.delete(String tableName, String whereClause, String[] whereArgs);
        // If whereClause is null, it will delete all rows.
        SQLiteDatabase db = this.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
        db.delete(TABLE_TASKS, null, null);
    }

}

