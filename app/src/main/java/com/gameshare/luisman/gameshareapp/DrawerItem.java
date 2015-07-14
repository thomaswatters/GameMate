package com.gameshare.luisman.gameshareapp;

/**
 * Created by LuisMan on 7/11/2015.
 */
public class DrawerItem {

    String ItemName;
    Integer imgResID;

    public DrawerItem(String itemName, Integer imgResID) {
        super();
        ItemName = itemName;
        this.imgResID = imgResID;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public Integer getImgResID() {
        return imgResID;
    }

    public void setImgResID(int imgResID) {
        this.imgResID = imgResID;
    }

}