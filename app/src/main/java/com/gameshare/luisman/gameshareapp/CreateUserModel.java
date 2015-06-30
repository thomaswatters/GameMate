package com.gameshare.luisman.gameshareapp;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by LuisMan on 6/30/2015.
 */
public class CreateUserModel {
    public String userName;
    public String passWord;
    public String zipCode;
    public String confirmPassword;
    public String email;


    public JSONObject getJson()
    {

        JSONObject json = new JSONObject();
        try {
            json.put("UserName", userName);
            json.put("Password", passWord);
            json.put("Email", email);
            json.put("ConfirmPassword", confirmPassword);
            json.put("ZipCode", zipCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;

    }
}
