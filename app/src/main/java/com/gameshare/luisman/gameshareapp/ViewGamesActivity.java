package com.gameshare.luisman.gameshareapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import java.util.ArrayList;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;


public class ViewGamesActivity extends ActionBarActivity {
    public static Context context;
    public static ArrayList<DummyUserGame> userGames = new ArrayList<>();
    public static ArrayList<Card> cards = null;
    public static CardArrayAdapter mCardArrayAdapter = null;
    private BroadcastReceiver receiver;
    private CardListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_games);

        context = this;
        cards = new ArrayList<>();

        for(DummyUserGame currentGame : userGames)
        {
            UserGameCard card = new UserGameCard(this, R.layout.user_game_card_inner_content, currentGame);
            cards.add(card);
        }

        mCardArrayAdapter = new CardArrayAdapter(this, cards);

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



    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver), new IntentFilter(AddUpdateGameActivity.RESULT));
    }

    private void updateData() {
        mCardArrayAdapter.notifyDataSetChanged();
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
        if (id == R.id.action_delete_games) {

            if(cards.size() == 0) return true;

            new MaterialDialog.Builder(context)
                    .content(R.string.delete_confirmation2)
                    .positiveText(R.string.confirm_delete2)
                    .negativeText(R.string.no)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            cards.clear();
                            userGames.clear();
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

    public static void showDeleteConfirmation(final Context context, final UserGameCard gameToBeDeleted){
        new MaterialDialog.Builder(context)
                .content(R.string.delete_confirmation)
                .positiveText(R.string.confirm_delete)
                .negativeText(R.string.no)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        cards.remove(gameToBeDeleted);
                        userGames.remove(gameToBeDeleted.userGame);
                        mCardArrayAdapter.notifyDataSetChanged();
                        Toast.makeText(context, gameToBeDeleted.userGame.getTitle() + " has been deleted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                    }
                })
                .show();
    }
}
