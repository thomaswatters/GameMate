package com.gameshare.luisman.gameshareapp;

/**
 * Created by LuisMan on 7/13/2015.
 */
public class Message {
    private String date;
    private String username;
    private String message;
    private boolean isRead;

    public Message(String user, String date, String message, boolean isRead){
        this.date = date;
        this.username = user;
        this.message = message;
        this.isRead = isRead;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
