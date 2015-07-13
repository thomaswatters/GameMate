package com.gameshare.luisman.gameshareapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by LuisMan on 7/11/2015.
 */
public class DrawerItemClickListener implements android.widget.AdapterView.OnItemClickListener {
    private Context context;
    private ArrayList<DrawerItem> data;
    private LocalBroadcastManager broadcaster;
    static final public String RESULT = "REQUEST_PROCESSED";

    public DrawerItemClickListener(Context context, ArrayList<DrawerItem> dataList) {
        this.context = context;
        this.data = dataList;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch(position){
            case 0:
                if(!data.get(1).getItemName().equals("Sign out")){
                    Toast.makeText(context, "You should Login first", Toast.LENGTH_SHORT).show();
                    break;
                }

                Intent i = new Intent(context, ProfileActivity.class);
                context.startActivity(i);

                break;
            case 1:

                if(data.get(position).getItemName().equals("Sign out")){
                    SharedPreferences sp = context.getSharedPreferences("UserCred", context.MODE_PRIVATE);
                    sp.edit().clear();
                    sp.edit().commit();

                    broadcaster = LocalBroadcastManager.getInstance(SearchGamesActivity.context);
                    sendResult("update");
                }
                else{
                    Intent i2 = new Intent(context, LoginActivity.class);
                    context.startActivity(i2);
                }

                break;
        }
    }
    private void sendResult(String message) {
        Intent intent = new Intent(RESULT);
        if(message != null){
            intent.putExtra(message, message);
            intent.putExtra("isAuth", false);
        }

        broadcaster.sendBroadcast(intent);
    }
}
