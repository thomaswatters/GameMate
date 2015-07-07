package com.gameshare.luisman.gameshareapp;


import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Button;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AddUpdateGameActivity extends ActionBarActivity {

    private EditText gameTitleEditText;
    private Spinner gameSystemSpinner;
    private Button button;
    private CheckBox[] checkBoxes;
    private int[] checkboxIds = new int[]
    {
        R.id.sellRadioButton,
        R.id.shareRadioButton,
        R.id.tradeRadioButton
    };

    private DummyUserGame gameToBeEdited;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);

        gameTitleEditText = (EditText) findViewById(R.id.game_title);
        gameSystemSpinner = (Spinner) findViewById(R.id.spinner);
        checkBoxes = new CheckBox[3];

        for (int i = 0; i < checkBoxes.length; i++)
        {
            checkBoxes[i] = (CheckBox) findViewById(checkboxIds[i]);
        }

        button = (Button) findViewById(R.id.add_update_game_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position = -1;

                Boolean isUpdate = button.getText().toString().equals((String) getResources().getString(R.string.update_game));

                if(isUpdate)
                {
                    for(position = 0; position < ViewGamesActivity.userGames.size(); position++)
                    {
                        DummyUserGame current = ViewGamesActivity.userGames.get(position);

                        if(current.equals(gameToBeEdited))
                            break;
                    }

                    ViewGamesActivity.userGames.remove(position);
                }

                DummyUserGame newGame = new DummyUserGame();

                HashMap<String, Boolean> flags = new HashMap<>();

                for(int i = 0; i < checkBoxes.length; i++)
                {
                    if(checkBoxes[i].isChecked())
                    {
                        flags.put(checkBoxes[i].getText().toString(), true);
                    }
                    else
                    {
                        flags.put(checkBoxes[i].getText().toString(), false);
                    }
                }

                newGame.setTitle(gameTitleEditText.getText().toString());
                newGame.setSystem((String) gameSystemSpinner.getSelectedItem());
                newGame.setFlags(flags);
                newGame.setDate(new Date().toString());
                newGame.setImageUrl("https://flugelmeister.files.wordpress.com/2011/03/halo-2.jpg");


                //NOTE: SAVE newGame into DB. Following code is just for testing
                if(isUpdate)
                {
                    ViewGamesActivity.userGames.add(position, newGame);
                    ViewGamesActivity.mCardArrayAdapter.notifyDataSetChanged();
                }else
                {
                    ViewGamesActivity.userGames.add(newGame);
                }

                finish();
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.game_platforms_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gameSystemSpinner.setAdapter(adapter);

        //if not a new game, get intent to edit it

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if(extras == null) return;

        getSupportActionBar().setTitle(getString(R.string.update_game));
        button.setText(getString(R.string.update_game));

        gameToBeEdited = (DummyUserGame) extras.getSerializable("editGame");

        gameTitleEditText.setText(gameToBeEdited.getTitle());

        String[] platforms = getResources().getStringArray(R.array.game_platforms_array);
        int i;

        for(i = 0; i < platforms.length; i++)
        {
            if(platforms[i].equals((String) gameToBeEdited.getSystem())) break;
        }

        gameSystemSpinner.setSelection(i);

        Iterator it = gameToBeEdited.getFlags().entrySet().iterator();

        while (it.hasNext())
        {
            Map.Entry pair = (Map.Entry)it.next();

            if(pair.getKey().equals((String) "Share") && (Boolean) pair.getValue())
            {
                checkBoxes[1].setChecked(true);
            }

            if(pair.getKey().equals((String) "Sell") && (Boolean) pair.getValue())
            {
                checkBoxes[0].setChecked(true);
            }

            if(pair.getKey().equals((String) "Trade") && (Boolean) pair.getValue())
            {
                checkBoxes[2].setChecked(true);
            }
        }
        //end of edit
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_game, menu);
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
