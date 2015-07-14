package com.gameshare.luisman.gameshareapp;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

/**
 * Created by LuisMan on 7/9/2015.
 */
public class PublicGameClickListener implements AdapterView.OnItemClickListener {

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Context context = view.getContext();

//        TextView textViewItem = ((TextView) view.findViewById(R.id.textViewItem));
//
//        // get the clicked item name
//        String listItemText = textViewItem.getText().toString();
//
//        // get the clicked item ID
//        String listItemId = textViewItem.getTag().toString();
//
//        // just toast it
//        Toast.makeText(context, "Item: " + listItemText + ", Item ID: " + listItemId, Toast.LENGTH_SHORT).show();
//
//        ((MainActivity) context).alertDialogStores.cancel();

    }

}