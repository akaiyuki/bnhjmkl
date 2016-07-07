package com.smartwave.taskr.object;

/**
 * Created by smartwavedev on 6/16/16.
 */
public class TaskObject {

    private int id;
    private String taskName;
    private String taskDescription;
    private String taskStatus;
    private String taskProject;
    private String taskDate;
    private String taskEstimate;

    public TaskObject() {
    }

    public TaskObject(int id, String taskName, String taskDescription, String taskStatus, String taskProject, String taskDate, String taskEstimate) {
        this.id = id;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = taskStatus;
        this.taskProject = taskProject;
        this.taskDate = taskDate;
        this.taskEstimate = taskEstimate;
    }

    public TaskObject(String taskName, String taskDescription, String taskStatus, String taskProject, String taskDate, String taskEstimate) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = taskStatus;
        this.taskProject = taskProject;
        this.taskDate = taskDate;
        this.taskEstimate = taskEstimate;
    }

    public String getTaskEstimate() {
        return taskEstimate;
    }

    public void setTaskEstimate(String taskEstimate) {
        this.taskEstimate = taskEstimate;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    public String getTaskProject() {

        return taskProject;
    }

    public void setTaskProject(String taskProject) {
        this.taskProject = taskProject;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTaskDescription() {

        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskName() {

        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

}
