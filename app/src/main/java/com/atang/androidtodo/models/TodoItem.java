package com.atang.androidtodo.models;

/**
 * Created by atang on 3/29/16.
 */
public class TodoItem {

    private int id;
    private String text;
    private String priority;
    private String completionDate;


    public TodoItem() {
    }

    public TodoItem(int id, String text, String priority, String completionDate) {
        this.id = id;
        this.text = text;
        this.priority = priority;
        this.completionDate = completionDate;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getPriority() {
        return priority;
    }

    public String getCompletionDate() {
        return completionDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setCompletionDate(String completionDate) {
        this.completionDate = completionDate;
    }

}
