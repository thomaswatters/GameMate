package com.gameshare.luisman.gameshareapp;

import android.content.SharedPreferences;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Thomas on 6/30/2015.
 */
public class AuthServiceProvider {
    private static String clientId = "b387ea4bdf2349d6b37e59ec7e42f43a";
    private static String baseUrl = "https://gamemateservice.azurewebsites.net/";


    public boolean isBadRequest() {
        return BadRequest;
    }

    private boolean BadRequest;

    public String  Login(String username, String password)
    {

        String url = baseUrl + "oauth/token";

        try {
            HttpURLConnection httpcon;
            httpcon = (HttpURLConnection) ((new URL(url).openConnection()));
            httpcon.setDoOutput(true);
            httpcon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpcon.setRequestProperty("Accept", "application/json");
            httpcon.setRequestMethod("POST");
            httpcon.connect();

            String data = "grant_type=password&username=" + username + "&password=" + password + "&client_id=" + clientId;


            //Write
            OutputStream os = httpcon.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(data);
            writer.close();
            os.close();


            BufferedReader br;
            try {
                br = new BufferedReader(new InputStreamReader(httpcon.getInputStream(), "UTF-8"));


                String line = null;
                StringBuilder sb = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                br.close();
                String result = sb.toString();
                JSONObject json = new JSONObject(result);
                BadRequest = false;
                return json.getString("access_token");

            } catch (IOException e)
            {

                br = new BufferedReader(new InputStreamReader(httpcon.getErrorStream(), "UTF-8"));

                String line = null;
                StringBuilder sb = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                br.close();
                String result = sb.toString();

                JSONObject json = new JSONObject(result);
                BadRequest = true;
                return json.getString("error_description");
            }

        }catch (Exception e)
        {

        }

        return null;
    }
}
