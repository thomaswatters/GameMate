package com.gameshare.luisman.gameshareapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.net.HttpURLConnection;
import java.util.ArrayList;


public class SearchGamesActivity extends ActionBarActivity {

    private MaterialDialog searchDialog;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private String[] mDrawerTitles;
    private DrawerAdapter drawerAdapter;
    private ArrayList<DrawerItem> dataList;
    private BroadcastReceiver receiver;
    public static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_games);
        context = this;

        SharedPreferences sp = getSharedPreferences("UserCred",MODE_PRIVATE);
        Boolean isAuth = sp.getBoolean("IsAuth", false);

        mDrawerTitles = getResources().getStringArray(R.array.drawer_array);

        dataList = new ArrayList<>();

        // dataList.add(new DrawerItem(getResources().getString(R.string.my_database), null));
        dataList.add(new DrawerItem(mDrawerTitles[0], R.drawable.ic_account_box_black_48dp));
        dataList.add(new DrawerItem(mDrawerTitles[1], R.drawable.ic_vpn_key_black_48dp));

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        drawerAdapter = new DrawerAdapter(this, R.layout.custom_drawer_item, dataList);

        mDrawerList.setAdapter(drawerAdapter);
        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener(this, dataList));

        // enable ActionBar app icon to behave as action to toggle nav drawer

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View view) {
                //     invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                //    invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);

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

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateData(intent);
            }
        };
    }
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver), new IntentFilter(LoginActivity.RESULT));
    }

    private void updateData(Intent intent) {
        Boolean isAuth = intent.getBooleanExtra("isAuth", false);

        if(!isAuth){
            mDrawerTitles = getResources().getStringArray(R.array.drawer_array);
        } else{
            mDrawerTitles = getResources().getStringArray(R.array.drawer_array2);
        }

        dataList.clear();

        dataList.add(new DrawerItem(mDrawerTitles[0], R.drawable.ic_account_box_black_48dp));

        dataList.add(new DrawerItem(mDrawerTitles[1], R.drawable.ic_vpn_key_black_48dp));

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener(this, dataList));

        drawerAdapter.notifyDataSetChanged();

        mDrawerLayout.closeDrawers();

        if(!isAuth)
            Toast.makeText(this, getString(R.string.logged_out_confirmation), Toast.LENGTH_SHORT).show();
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
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_games, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
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
