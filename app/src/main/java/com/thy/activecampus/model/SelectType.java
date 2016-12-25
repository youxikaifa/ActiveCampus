package com.thy.activecampus.model;

/**
 * Created by Jin on 7/29.
 */

public class SelectType {
    private String title;
    private int id;

    public SelectType(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
