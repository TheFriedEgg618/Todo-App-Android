package com.example.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Name and Version
    private static final String DATABASE_NAME = "todo.db";
    private static final int DATABASE_VERSION = 1;

    // Table Name and Column Names
    private static final String TABLE_TASKS = "tasks";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_TIMESTAMP = "timestamp";
    private static final String COLUMN_COMPLETED = "completed";

    // SQL query to create the tasks table
    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_TASKS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_TYPE + " TEXT, " +
                    COLUMN_TIMESTAMP + " TEXT, " +
                    COLUMN_COMPLETED + " INTEGER)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Executes the table creation query when the database is created
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drops the existing table if it exists and recreates it
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }

    // Adds a new task to the database
    public void addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, task.getName());
        values.put(COLUMN_TYPE, task.getType());
        values.put(COLUMN_TIMESTAMP, task.getTimestamp());
        values.put(COLUMN_COMPLETED, task.isCompleted() ? 1 : 0);
        db.insert(TABLE_TASKS, null, values);
        db.close();
    }

    // Retrieves all tasks from the database
    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TASKS, null, null, null, null, null, COLUMN_TIMESTAMP + " DESC");

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                String type = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE));
                String timestamp = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIMESTAMP));
                boolean completed = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMPLETED)) == 1;

                // Adds each retrieved task to the list
                taskList.add(new Task(id, name, type, timestamp, completed));
            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();
        return taskList;
    }

    // Deletes a task from the database using its ID
    public void deleteTask(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Updates the completion status of a task
    public void updateTaskCompletion(int id, boolean completed) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COMPLETED, completed ? 1 : 0);
        db.update(TABLE_TASKS, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }
}

