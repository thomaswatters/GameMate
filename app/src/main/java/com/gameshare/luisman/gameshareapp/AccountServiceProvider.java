package com.gameshare.luisman.gameshareapp;

import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Objects;

/**
 * Created by Thomas on 7/14/2015.
 */
public class AccountServiceProvider {

    private boolean BadRequest;

    Context context;
    AccountServiceProvider(Context context)
    {
        this.context = context;
    }


    private static String clientId = "b387ea4bdf2349d6b37e59ec7e42f43a";
    private static String baseUrl = "https://gamemateservice.azurewebsites.net/";



    private JSONObject postAPI(String apiMethod, Object model)
    {

        String token = PreferenceManager.getDefaultSharedPreferences(context).getString("Token", "");
        try
        {
            HttpURLConnection httpcon;
            httpcon = (HttpURLConnection) ((new URL(baseUrl + apiMethod).openConnection()));
            httpcon.setDoOutput(true);
            httpcon.setRequestProperty("Authorization", "Bearer " + token);
            httpcon.setRequestProperty("Content-type", "application/json");
            httpcon.setRequestProperty("Accept", "application/json");
            httpcon.setRequestMethod("POST");
            httpcon.connect();

            //Write
            OutputStream os = httpcon.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write((new Gson()).toJson(model));
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
                JSONObject jsonReturn = new JSONObject(result);
                BadRequest = false;
                return jsonReturn;

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

                JSONObject jsonReturn = new JSONObject(result);
                BadRequest = true;
                return jsonReturn;
            }



        }
        catch (Exception e)
        {

        }

        return null;

    }

    private JSONObject getAPI(String url)
    {
        String token = PreferenceManager.getDefaultSharedPreferences(context).getString("Token", "");
        try
        {
            HttpURLConnection httpcon;
            httpcon = (HttpURLConnection) ((new URL(baseUrl + url).openConnection()));
            httpcon.setRequestMethod("GET");
            httpcon.setRequestProperty("Authorization", "Bearer " + token);

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
                JSONObject jsonReturn = new JSONObject(result);
                BadRequest = false;
                return jsonReturn;

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

                JSONObject jsonReturn = new JSONObject(result);
                BadRequest = true;
                return jsonReturn;
            }
        }
        catch (Exception e)
        {

        }

        return null;


    }

    public JSONObject ChangePassword(String oldP, String newP, String confP)
    {
        ChangePasswordModel model = new ChangePasswordModel();
        model.OldPassword = oldP;
        model.NewPassword = newP;
        model.ConfirmPassword = confP;

        return postAPI("api/accounts/ChangePassword", model);
    }

    public JSONObject UpdateSettings(String zip, String email, String password)
    {
        UpdateSettingsModel model = new UpdateSettingsModel();
        model.ZipCode = zip;
        model.Email = email;
        model.Password = password;

        return postAPI("api/accounts/UpdateSettings", model);
    }

    public JSONObject GetUserInfo(String username)
    {
        return getAPI("api/accounts/user/" + username + "/");
    }

    public JSONObject AddGame(String title, String gameSystem, List<String> flags, String username)
    {
        AddGameModel model = new AddGameModel();
        model.Title = title;
        model.GameSystem = gameSystem;
        model.Flags = flags;
        model.UserName = username;

        return postAPI("api/user/addgame", model);
    }

    private class ChangePasswordModel
    {
        String OldPassword;
        String NewPassword;
        String ConfirmPassword;
    }

    private class UpdateSettingsModel
    {
        String ZipCode;
        String Email;
        String Password;
    }

    private class AddGameModel
    {
        String Title;
        String GameSystem;
        List<String> Flags;
        String UserName;
    }

    public boolean isBadRequest()
    {
        return BadRequest;
    }
}
