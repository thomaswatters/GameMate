package com.gameshare.luisman.gameshareapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;


public class InboxActivity extends ActionBarActivity {
    private Context context;
    private ArrayList<Card> cards;
    public static ArrayList<Message> messages = new ArrayList<>();
    public static CardArrayAdapter mCardArrayAdapter = null;
    private CardListView listView;
    private BroadcastReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        context = this;
        cards = new ArrayList<>();

        //TEST
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        messages.add(new Message("Luis", dateFormat.format(date), "Hi, I'm interested in Halo. Here is my number: 555-5555-5555", false));

        //End

        for(Message message : messages)
        {
            MessageCard card = new MessageCard(this, R.layout.message_layout, message);
            cards.add(card);
        }

        mCardArrayAdapter = new CardArrayAdapter(this, cards);
        mCardArrayAdapter.setEnableUndo(true);

        listView = (CardListView) findViewById(R.id.myList);
        if (listView != null)
        {
            listView.setAdapter(mCardArrayAdapter);
        }

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateData();
            }
        };
    }

    private void updateData() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inbox, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete_messages) {

            if(cards.size() == 0) return true;

            new MaterialDialog.Builder(context)
                    .content(R.string.delete_confirmation3)
                    .positiveText(R.string.confirm_delete2)
                    .negativeText(R.string.no)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            cards.clear();
                            messages.clear();
                            mCardArrayAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                        }
                    })
                    .show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
