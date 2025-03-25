package com.example.todoapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    // UI components
    private EditText editText;
    private Spinner typeSpinner;
    private Button addButton;
    private ListView listView;

    private DatabaseHelper databaseHelper;

    // List of tasks
    private ArrayList<Task> taskList;
    private TaskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        databaseHelper = new DatabaseHelper(this);

        // Initialize UI components
        editText = findViewById(R.id.editText);
        typeSpinner = findViewById(R.id.typeSpinner);
        addButton = findViewById(R.id.addButton);
        listView = findViewById(R.id.listView);


        // Load tasks from the database when the app starts
        loadTasks();

        // Populate the Spinner with task types
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this, R.array.task_types, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(spinnerAdapter);

        // Set click listener for the "Add" button
        addButton.setOnClickListener(v -> {
            String taskName = editText.getText().toString().trim();
            String taskType = typeSpinner.getSelectedItem().toString();
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

            if (!taskName.isEmpty()) {
                Task newTask = new Task(taskName, taskType, timestamp, false);
                databaseHelper.addTask(newTask);
                taskList.clear();
                taskList.addAll(databaseHelper.getAllTasks());
                adapter.notifyDataSetChanged();
                editText.setText("");
            }
        });
    }

    // Method to reload tasks from SQLite and update ListView
    private void loadTasks() {
        taskList = databaseHelper.getAllTasks(); // Fetch all tasks
        adapter = new TaskAdapter(this, taskList);
        listView.setAdapter(adapter);
    }
}