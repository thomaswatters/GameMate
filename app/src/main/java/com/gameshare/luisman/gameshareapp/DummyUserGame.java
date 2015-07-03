package com.gameshare.luisman.gameshareapp;

import java.util.HashMap;

/**
 * Created by LuisMan on 7/2/2015.
 */
public class DummyUserGame
{
    /*
    Properties
     */
    private String title;
    private String system;
    private HashMap<String, Boolean> flags;
    private String date;
    private String imageUrl;
    /*
    Constructors
     */
    public DummyUserGame(){ }
    public DummyUserGame(String title, String system, HashMap<String, Boolean> flags){
        this.title = title;
        this.system = system;
        this.flags = flags;
    }

    /*
    * Getters and setters
    * */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public HashMap<String, Boolean> getFlags() {
        return flags;
    }

    public void setFlags(HashMap<String, Boolean> flags) {
        this.flags = flags;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
