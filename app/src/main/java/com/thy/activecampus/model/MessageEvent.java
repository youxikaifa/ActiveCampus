package com.thy.activecampus.model;

/**
 * Created by Jin on 7/29.
 */

public class MessageEvent {

    public String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public MessageEvent(String message) {
        this.message = message;
    }
    public MessageEvent() {

    }
}