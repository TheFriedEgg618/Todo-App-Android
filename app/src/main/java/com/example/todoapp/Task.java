package com.example.todoapp;

public class Task {
    private int id;
    private String name;
    private String type;
    private String timestamp;
    private boolean completed;

    // Constructor with ID (used for retrieving tasks from the database)
    public Task(int id, String name, String type, String timestamp, boolean completed) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.timestamp = timestamp;
        this.completed = completed;
    }

    // Constructor without ID (used when creating a new task)
    public Task(String name, String type, String timestamp, boolean completed) {
        this.name = name;
        this.type = type;
        this.timestamp = timestamp;
        this.completed = completed;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public boolean isCompleted() {
        return completed;
    }

    // Setter to update completion status
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}