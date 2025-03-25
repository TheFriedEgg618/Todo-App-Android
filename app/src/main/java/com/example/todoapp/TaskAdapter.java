package com.example.todoapp;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import java.util.List;

public class TaskAdapter extends ArrayAdapter<Task> {
    private DatabaseHelper databaseHelper;  // Database helper for managing tasks
    private Context context;

    // Constructor to initialize context and database helper
    public TaskAdapter(Context context, List<Task> tasks) {
        super(context, 0, tasks);  // Calls ArrayAdapter constructor
        this.context = context;
        databaseHelper = new DatabaseHelper(context);  // Initialize DatabaseHelper
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate the task item layout if not already created
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_item, parent, false);
        }

        Task task = getItem(position);  // Get the task at the current position
        // Find UI elements in the  layout
        TextView taskName = convertView.findViewById(R.id.taskName);
        TextView taskType = convertView.findViewById(R.id.taskType);
        TextView taskTimestamp = convertView.findViewById(R.id.taskTimestamp);
        CheckBox taskCheckBox = convertView.findViewById(R.id.taskCheckBox);
        Button deleteButton = convertView.findViewById(R.id.deleteButton);

        // Set task details in the UI
        taskName.setText(task.getName());
        taskType.setText("Type: " + task.getType());
        taskTimestamp.setText("Created: " + task.getTimestamp());
        taskCheckBox.setChecked(task.isCompleted());  // Set task completion status

        // Update task completion status when checkbox is checked/unchecked
        taskCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setCompleted(isChecked);
            databaseHelper.updateTaskCompletion(task.getId(), isChecked);  // Update database
        });

        // Set delete button with a confirmation dialog
        deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(context)  // Show a confirmation dialog
                    .setTitle("Delete Task")
                    .setMessage("Are you sure you want to delete this task?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        databaseHelper.deleteTask(task.getId());  // Delete task from database
                        remove(task);  // Remove task from list
                        notifyDataSetChanged();  // Refresh the ListView
                    })
                    .setNegativeButton("Cancel", null)  // Cancel deletion
                    .show();
        });

        return convertView;  // Return the updated view for the task
    }
}
