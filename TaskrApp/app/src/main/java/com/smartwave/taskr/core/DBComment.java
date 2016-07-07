package com.smartwave.taskr.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.smartwave.taskr.object.CommentObject;
import com.smartwave.taskr.object.TaskObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smartwavedev on 6/29/16.
 */
public class DBComment extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "commentsDatabase";

    // Contacts table name
    private static final String TABLE_TASKS = "comments";

    // Shops Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TASK_ID = "task_id";
    private static final String KEY_TASK_COMMENT = "task_comment";
    private static final String KEY_TASK_NAME = "task_name";
    private static final String KEY_TASK_DATE = "task_date";

    public DBComment(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TASK_ID + " TEXT,"
                + KEY_TASK_COMMENT + " TEXT,"
                + KEY_TASK_NAME + " TEXT,"
                + KEY_TASK_DATE + " TEXT" + ")";
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
    public void addTask(CommentObject commentObject) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TASK_ID, commentObject.getTaskId());
        values.put(KEY_TASK_COMMENT, commentObject.getTaskComment());
        values.put(KEY_TASK_NAME, commentObject.getTaskName());
        values.put(KEY_TASK_DATE, commentObject.getCommentDate());

        // Inserting Row
        db.insert(TABLE_TASKS, null, values);
        db.close(); // Closing database connection
    }

    // Getting one shop
    public CommentObject getTask(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TASKS, new String[]{KEY_ID,
                        KEY_TASK_ID, KEY_TASK_COMMENT, KEY_TASK_NAME, KEY_TASK_DATE}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        CommentObject contact = new CommentObject(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
        // return shop
        return contact;
    }

    // Getting All Shops
    public List<CommentObject> getAllTask() {
        List<CommentObject> shopList = new ArrayList<CommentObject>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TASKS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                CommentObject taskObject = new CommentObject();
                taskObject.setId(Integer.parseInt(cursor.getString(0)));
                taskObject.setTaskId(cursor.getString(1));
                taskObject.setTaskComment(cursor.getString(2));
                taskObject.setTaskName(cursor.getString(3));
                taskObject.setCommentDate(cursor.getString(4));
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
    public int updateTask(int id, String taskId, String taskComment, String taskName, String taskDate) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TASK_ID, taskId);
        values.put(KEY_TASK_COMMENT, taskComment);
        values.put(KEY_TASK_NAME, taskName);
        values.put(KEY_TASK_DATE, taskDate);

        // updating row
        return db.update(TABLE_TASKS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    // Deleting a shop
    public void deleteTask(CommentObject taskObject) {
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

