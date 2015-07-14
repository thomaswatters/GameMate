package com.gameshare.luisman.gameshareapp;

import android.content.SharedPreferences;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by LuisMan on 7/9/2015.
 */
public class AuthenticatedHttpConnection extends HttpURLConnection
{

    /**
     * Constructs a new {@code HttpURLConnection} instance pointing to the
     * resource specified by the {@code url}.
     *
     * @param url the URL of this connection.
     * @see URL
     * @see URLConnection
     */
    protected AuthenticatedHttpConnection(URL url, SharedPreferences sp) {
        super(url);
        this.setRequestProperty("Authorization", "Bearer" + sp.getString("Token", ""));
        this.setRequestProperty("Content-type", "application/json");
        this.setRequestProperty("Accept", "application/json");

    }

    @Override
    public void disconnect() {

    }

    @Override
    public boolean usingProxy() {
        return false;
    }

    @Override
    public void connect() throws IOException {

    }
}
