package com.gameshare.luisman.gameshareapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardListView;


public class ViewGamesActivity extends ActionBarActivity {

    public static ArrayList<Card> cards = null;
    public static CardArrayAdapter mCardArrayAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_games);

        cards = new ArrayList<>();

        HashMap<String, Boolean> flags = new HashMap<>();
        flags.put("Share", true);
        flags.put("Trade", false);
        flags.put("Sell", true);

        //Add cards to ArrayList
        for(int i = 1; i < 6; i++){
            //Create cards
            DummyUserGame game = new DummyUserGame("Halo " + i, "Nintendo " + i, flags);
            game.setDate("1/1/2001");
            game.setImageUrl("https://flugelmeister.files.wordpress.com/2011/03/halo-2.jpg");
            UserGameCard card = new UserGameCard(getApplicationContext(), R.layout.user_game_card_inner_content, game);
            cards.add(card);
        }

        mCardArrayAdapter = new CardArrayAdapter(this,cards);

        CardListView listView = (CardListView) findViewById(R.id.myList);
        if (listView != null)
        {
            listView.setAdapter(mCardArrayAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_games, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
