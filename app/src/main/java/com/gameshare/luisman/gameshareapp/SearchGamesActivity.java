package com.gameshare.luisman.gameshareapp;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class SearchGamesActivity extends ActionBarActivity {

    private MaterialDialog searchDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_games);

        ListView listView = (ListView) findViewById(R.id.list);
        // NOTE: usergames is hardcoded. that should be downloaded from asynctask
        PublicGameAdapter adapter = new PublicGameAdapter(this, R.layout.user_game_card_inner_content, ViewUserGamesActivity.userGames);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showOptions();
            }
        });

        listView.setAdapter(adapter);
    }

    private void showOptions() {
        new MaterialDialog.Builder(this)
                .items(R.array.options)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                    }
                })
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_games, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search) {
            new MaterialDialog.Builder(this)
                    .title(R.string.search)
                    .inputType(InputType.TYPE_CLASS_TEXT)
                    .input(getString(R.string.game_example), getString(R.string.game_example), new MaterialDialog.InputCallback() {
                        @Override
                        public void onInput(MaterialDialog dialog, CharSequence input) {

                            search(input);
                        }
                    }).show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void search(CharSequence query) {

        searchDialog = new MaterialDialog.Builder(this)
                .title(getString(R.string.searching) + query)
                .content(R.string.please_wait)
                .progress(true, 0)
                .show();
         new AsyncSearchGames(query.toString()).execute();
    }

    public class AsyncSearchGames extends AsyncTask<Object, Object, Object> {

        private HttpURLConnection connection;

        private String query;

        public AsyncSearchGames(String query) {
            this.query = query;
        }

        @Override
        protected Object doInBackground(Object... params) {
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
            return null;
        }
        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            Toast.makeText(getApplicationContext(), "not found", Toast.LENGTH_SHORT).show();
            searchDialog.cancel();
        }
    }
}
