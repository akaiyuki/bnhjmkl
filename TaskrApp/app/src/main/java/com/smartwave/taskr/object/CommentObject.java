package com.smartwave.taskr.object;

/**
 * Created by smartwavedev on 6/29/16.
 */
public class CommentObject {

    public String taskId;
    public String taskComment;
    private int id;
    private String taskName;
    private String CommentDate;

    public CommentObject() {
    }

    public CommentObject(int id, String taskId, String taskComment, String taskName, String commentDate) {
        this.id = id;
        this.taskId = taskId;
        this.taskComment = taskComment;
        this.taskName = taskName;
        this.CommentDate = commentDate;
    }

    public CommentObject(String taskId, String taskComment, String taskName, String commentDate) {
        this.taskId = taskId;
        this.taskComment = taskComment;
        this.taskName = taskName;
        this.CommentDate = commentDate;
    }

    public String getCommentDate() {
        return CommentDate;
    }

    public void setCommentDate(String commentDate) {
        CommentDate = commentDate;
    }

    public String getTaskName() {

        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskComment() {
        return taskComment;
    }

    public void setTaskComment(String taskComment) {
        this.taskComment = taskComment;
    }

    public String getTaskId() {

        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
