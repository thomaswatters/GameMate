package com.gameshare.luisman.gameshareapp;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by LuisMan on 7/2/2015.
 */
public class DummyUserGame implements Serializable
{
    /*
    Properties
     */
    private String title;
    private String system;
    private String date;
    private String imageUrl;
    private HashMap<String, Boolean> flags;
    /*
    Constructors
     */
    public DummyUserGame(){ }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DummyUserGame that = (DummyUserGame) o;

        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (system != null ? !system.equals(that.system) : that.system != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (imageUrl != null ? !imageUrl.equals(that.imageUrl) : that.imageUrl != null)
            return false;
        return !(flags != null ? !flags.equals(that.flags) : that.flags != null);
    }
}
