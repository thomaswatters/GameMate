package com.gameshare.luisman.gameshareapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.mikepenz.actionitembadge.library.ActionItemBadgeAdder;
import com.mikepenz.iconics.typeface.FontAwesome;


public class ProfileActivity extends ActionBarActivity {

    private Button[] buttons = new  Button[3];
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        context = this;

        getSupportActionBar().setTitle(getString(R.string.my_profile));
        buttons[0] = (Button) findViewById(R.id.add_user_games_button);
        buttons[1] = (Button) findViewById(R.id.view_user_games_button);
        buttons[2] = (Button) findViewById(R.id.change_user_settings_button);


        buttons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AddUpdateGameActivity.class);
                startActivity(i);
            }
        });

        buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ViewGamesActivity.class);
                startActivity(i);
            }
        });

        buttons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean wrapInScrollView = true;

                new MaterialDialog.Builder(context)
                        .title(getString(R.string.change_settings))
                        .customView(R.layout.change_settings, wrapInScrollView)
                        .positiveText(getString(R.string.ok))
                        .negativeText(getString(R.string.cancel))
                        .callback(new MaterialDialog.ButtonCallback(){
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                View oldPasswordView = dialog.findViewById(R.id.old_password);
                                View oldPasswordConfirmation = dialog.findViewById(R.id.old_password2);
                                View newPassword = dialog.findViewById(R.id.new_password);
                                View newPasswordConfirmation = dialog.findViewById(R.id.new_password2);
                                View zipcode = dialog.findViewById(R.id.new_location);
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);

        //you can add some logic (hide it if the count == 0)
        int badgeCount = 1;
        if (badgeCount > 0) {
            ActionItemBadge.update(this, menu.findItem(R.id.item_samplebadge), FontAwesome.Icon.faw_inbox, ActionItemBadge.BadgeStyles.DARK_GREY, badgeCount);
        } else {
            ActionItemBadge.hide(menu.findItem(R.id.item_samplebadge));
        }

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
