package com.dkit.oop.sd2.DTOs;

import java.util.Date;  // Import the Date class for due_date

public class Task {
    private int taskId;
    private String title;
    private String status;
    private String priority;
    private String description;
    private Date dueDate;

    public Task(int taskId, String title, String status, String priority, String description, Date dueDate) {
        this.taskId = taskId;
        this.title = title;
        this.status = status;
        this.priority = priority;
        this.description = description;
        this.dueDate = dueDate;
    }

    public Task(String title, String status, String priority, String description, Date dueDate) {
        this.taskId = 0;
        this.title = title;
        this.status = status;
        this.priority = priority;
        this.description = description;
        this.dueDate = dueDate;
    }

    public Task() {
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }



    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                ", priority='" + priority + '\'' +
                ", description='" + description + '\'' +
                ", dueDate=" + dueDate +
                '}';
    }
}
