package com.smartwave.taskr.core;

/**
 * Created by smartwavedev on 6/16/16.
 */
public class TSingleton {

    public static String logoutGmail;
    public static String dueDate;
    public static String taskName;
    public static String taskDesc;
    public static String taskStatus;
    public static String taskProject;
    public static String taskId;
    public static String taskDate;
    public static String taskEstimate;
    public static String userName;

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        TSingleton.userName = userName;
    }

    public static String getTaskEstimate() {
        return taskEstimate;
    }

    public static void setTaskEstimate(String taskEstimate) {
        TSingleton.taskEstimate = taskEstimate;
    }

    public static String getTaskDate() {
        return taskDate;
    }

    public static void setTaskDate(String taskDate) {
        TSingleton.taskDate = taskDate;
    }

    public static String getTaskId() {
        return taskId;
    }

    public static void setTaskId(String taskId) {
        TSingleton.taskId = taskId;
    }

    public static String getTaskProject() {
        return taskProject;
    }

    public static void setTaskProject(String taskProject) {
        TSingleton.taskProject = taskProject;
    }

    public static String getTaskStatus() {

        return taskStatus;
    }

    public static void setTaskStatus(String taskStatus) {
        TSingleton.taskStatus = taskStatus;
    }

    public static String getTaskDesc() {

        return taskDesc;
    }

    public static void setTaskDesc(String taskDesc) {
        TSingleton.taskDesc = taskDesc;
    }

    public static String getTaskName() {

        return taskName;
    }

    public static void setTaskName(String taskName) {
        TSingleton.taskName = taskName;
    }

    public static String getDueDate() {
        return dueDate;
    }

    public static void setDueDate(String dueDate) {
        TSingleton.dueDate = dueDate;
    }

    public static String getLogoutGmail() {
        return logoutGmail;
    }

    public static void setLogoutGmail(String logoutGmail) {
        TSingleton.logoutGmail = logoutGmail;
    }
}
