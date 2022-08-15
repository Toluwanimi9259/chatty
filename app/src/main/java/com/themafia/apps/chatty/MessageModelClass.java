package com.themafia.apps.chatty;

public class MessageModelClass {

    String message;
    String from;

    public MessageModelClass() {
    }

    public MessageModelClass(String message, String from) {
        this.message = message;
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
