package com.gameshare.luisman.gameshareapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class SettingsActivity extends ActionBarActivity {

    private Button[] buttons = new  Button[2];
    private Context context;
    UserDataModel userData;

    private class GetUserTask extends AsyncTask
    {
        private UserDataModel userData;
        GetUserTask(UserDataModel userData)
        {
            this.userData = userData;
        }


        @Override
        protected Object doInBackground(Object[] params) {

            AccountServiceProvider ser = new AccountServiceProvider(getApplicationContext());

            JSONObject json = ser.GetUserInfo(userData.UserName);

            try {
                userData.Email = json.getString("email");
                userData.ZipCode = json.getString("zipCode");
            }
            catch (Exception e)
            {}
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);


        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        userData = new UserDataModel();
        userData.UserName = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("UserName", " ");

        GetUserTask userTask = new GetUserTask(userData);
        userTask.execute();

        context = this;

        getSupportActionBar().setTitle(getString(R.string.my_settings));

        buttons[0] = (Button) findViewById(R.id.change_password_button);
        buttons[1] = (Button) findViewById(R.id.change_email_location_button);

        buttons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean wrapInScrollView = true;

                new MaterialDialog.Builder(context)
                        .autoDismiss(false)
                        .title(getString(R.string.change_password))
                        .customView(R.layout.change_password_settings, wrapInScrollView)
                        .inputType(InputType.TYPE_CLASS_TEXT |
                                InputType.TYPE_TEXT_VARIATION_PASSWORD |
                                InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                        .positiveText(getString(R.string.ok))
                        .negativeText(getString(R.string.cancel))
                        .alwaysCallInputCallback()
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                EditText oldPasswordView = (EditText) dialog.findViewById(R.id.old_password);
                                EditText newPasswordView = (EditText) dialog.findViewById(R.id.new_password);
                                EditText newPasswordConfirmationView = (EditText) dialog.findViewById(R.id.new_password2);

                                oldPasswordView.setError(null);
                                newPasswordView.setError(null);
                                newPasswordConfirmationView.setError(null);

                                String oldPassword = oldPasswordView.getText().toString();
                                String newPassword = newPasswordView.getText().toString();
                                String confirmedPassword = newPasswordConfirmationView.getText().toString();

                                if (TextUtils.isEmpty(oldPassword)) {
                                    oldPasswordView.setError(getString(R.string.error_field_required));
                                    return;
                                }

                                if (TextUtils.isEmpty(newPassword)) {
                                    newPasswordView.setError(getString(R.string.error_field_required));
                                    return;
                                }

                                if (TextUtils.isEmpty(confirmedPassword)) {
                                    newPasswordConfirmationView.setError(getString(R.string.error_field_required));
                                    return;
                                }

                                // Check for a valid password, if the user entered one.
                                if (!isPasswordValid(newPassword)) {
                                    newPasswordView.setError(getString(R.string.error_invalid_password));
                                    return;
                                } else
                                    //Check if password match
                                    if(!PasswordsMatches(newPassword, confirmedPassword)){
                                        newPasswordConfirmationView.setError(getString(R.string.error_incorrect_password));
                                        return;
                                    }

                                ChangePasswordTask task = new ChangePasswordTask(oldPassword, newPassword, confirmedPassword);
                                task.execute();

                                dialog.dismiss();
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean wrapInScrollView = true;

                MaterialDialog dialog = new MaterialDialog.Builder(context)
                        .autoDismiss(false)
                        .title(getString(R.string.change_email_location))
                        .customView(R.layout.change_other_settings, wrapInScrollView)
                        .positiveText(getString(R.string.ok))
                        .negativeText(getString(R.string.cancel))
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                EditText locationView = (EditText) dialog.findViewById(R.id.new_location);
                                EditText emailView = (EditText) dialog.findViewById(R.id.new_email);
                                EditText passwordView = (EditText) dialog.findViewById(R.id.current_password);

                                String password = passwordView.getText().toString();
                                String email = emailView.getText().toString();
                                String location = locationView.getText().toString();

                                locationView.setError(null);
                                emailView.setError(null);
                                passwordView.setError(null);

                                //check email
                                if (TextUtils.isEmpty(email)) {
                                    emailView.setError(getString(R.string.error_field_required));
                                    return;
                                } else if (!isEmailValid(email)) {
                                    emailView.setError(getString(R.string.error_invalid_email));
                                    return;
                                }

                                //Check zipcode
                                if (TextUtils.isEmpty(location)) {
                                    locationView.setError(getString(R.string.error_field_required));
                                    return;
                                } else if (!isZipcodeValid(location)) {
                                    locationView.setError(getString(R.string.error_invalid_zipcode));
                                    return;
                                }

                                if (TextUtils.isEmpty(password)) {
                                    passwordView.setError(getString(R.string.error_field_required));
                                    return;
                                }

                                ChangeOtherSettings task = new ChangeOtherSettings(password, location, email);

                                task.execute();

                                dialog.dismiss();
                            }


                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .show();

                ((EditText)dialog.getCustomView().findViewById(R.id.new_location)).setText(userData.ZipCode);

                ((EditText)dialog.getCustomView().findViewById(R.id.new_email)).setText(userData.Email);
            }


        });



    }

    private boolean isZipcodeValid(String zipcode) {
        return Pattern.matches("^\\d{5}(?:[-\\s]\\d{4})?$", zipcode);
    }

    private boolean isEmailValid(String email) {
        return Pattern.matches("^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$", email);
    }

    private boolean PasswordsMatches(String password, String confirmPassword) {
        return password.equalsIgnoreCase(confirmPassword);
    }

    private boolean isPasswordValid(String password) {
        return Pattern.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[#$!%^&+=*])(?=\\S+$).{8,}$", password);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
   //     getMenuInflater().inflate(R.menu.menu_settings, menu);
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

    /**
     * Represents an asynchronous ChangePassword task
     */
    public class ChangePasswordTask extends AsyncTask<Void, Void, Boolean> {

        private final String oldPassword;
        private final String newPassword;
        private final String confirmedPassword;
        private String error_msg;

        ChangePasswordTask(String oldPassword, String newPassword, String confirmedPassword) {
            this.oldPassword = oldPassword;
            this.newPassword = newPassword;
            this.confirmedPassword = confirmedPassword;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.


            AccountServiceProvider ser = new AccountServiceProvider(getApplicationContext());
            List<String> errors = new ArrayList<>();

            try
            {
                JSONObject json = ser.ChangePassword(oldPassword, newPassword, confirmedPassword);

                if(ser.isBadRequest())
                {
                        JSONArray modelState = json.getJSONArray("modelState");
                        for(int i = 0; i < modelState.length(); i++)
                        {
                        }

//                    SharedPreferences sp = getSharedPreferences("UserCred", MODE_PRIVATE);
//                    sp.edit().clear();
//                    sp.edit().commit();
//
//                    error_msg = message;


                    return false;
                }
                else
                {
//                    SharedPreferences sp = getSharedPreferences("UserCred", MODE_PRIVATE);
//                    sp.edit().putString("Token", message);
//                    sp.edit().putBoolean("IsAuth", true);
//                    sp.edit().commit();

                    return true;
                }
            }
            catch (Exception e)
            {

            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {

            } else {


            }
        }
    }


    /**
     * Represents an asynchronous Change email/location task
     */
    public class ChangeOtherSettings extends AsyncTask<Void, Void, Boolean> {

        private final String password;
        private final String location;
        private final String email;

        private String error_msg;

        ChangeOtherSettings(String password, String location, String email) {
            this.password = password;
            this.location = location;
            this.email = email;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.


            AccountServiceProvider ser = new AccountServiceProvider(getApplicationContext());
            List<String> errors = new ArrayList<>();

            try
            {
                JSONObject json = ser.UpdateSettings(location, email, password);

                if(ser.isBadRequest())
                {
                    JSONArray modelState = json.getJSONArray("modelState");
                    for(int i = 0; i < modelState.length(); i++)
                    {
                    }

//                    SharedPreferences sp = getSharedPreferences("UserCred", MODE_PRIVATE);
//                    sp.edit().clear();
//                    sp.edit().commit();
//
//                    error_msg = message;



                    return false;
                }
                else
                {
//                    SharedPreferences sp = getSharedPreferences("UserCred", MODE_PRIVATE);
//                    sp.edit().putString("Token", message);
//                    sp.edit().putBoolean("IsAuth", true);
//                    sp.edit().commit();

                    return true;
                }
            }
            catch (Exception e)
            {

            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {

            } else {


            }
        }
    }





}
