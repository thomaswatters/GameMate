package com.gameshare.luisman.gameshareapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class RegisterActivity extends ActionBarActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserRegisterTask mAuthTask = null;
    // UI references.
    private TextView mRegisterView;
    private AutoCompleteTextView mUsernameView;
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;
    private EditText mZipcodeView;
    private View mProgressView;
    private View mRegisterFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // Set up the register form.
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.username_register);
        mPasswordView = (EditText) findViewById(R.id.password_register);
        mConfirmPasswordView = (EditText) findViewById(R.id.password_register2);
        mEmailView = (EditText) findViewById(R.id.email_register);
        mZipcodeView = (EditText) findViewById(R.id.location_register);

        mPasswordView.setOnEditorActionListener(EditTextActionListener());
        mConfirmPasswordView.setOnEditorActionListener(EditTextActionListener());
        mEmailView.setOnEditorActionListener(EditTextActionListener());
        mZipcodeView.setOnEditorActionListener(EditTextActionListener());

        Button mUserRegisterButton = (Button) findViewById(R.id.user_register_button);
        mUserRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mRegisterFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.register_progress);
    }

    private TextView.OnEditorActionListener EditTextActionListener() {
        return new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        };
    }

    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);
        mConfirmPasswordView.setError(null);
        mEmailView.setError(null);
        mZipcodeView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String confirmPassword = mConfirmPasswordView.getText().toString();
        String zipcode = mZipcodeView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        } else
            //Check if password match
            if(!PasswordsMatches(password, confirmPassword)){
                mConfirmPasswordView.setError(getString(R.string.error_incorrect_password));
                focusView = mConfirmPasswordView;
                cancel = true;
            }

        // Check for a valid username.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            mUsernameView.setError(getString(R.string.error_invalid_username));
            focusView = mUsernameView;
            cancel = true;
        }

        //check email
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        //Check zipcode
        if (TextUtils.isEmpty(zipcode)) {
            mZipcodeView.setError(getString(R.string.error_field_required));
            focusView = mZipcodeView;
            cancel = true;
        } else if (!isZipcodeValid(zipcode)) {
            mZipcodeView.setError(getString(R.string.error_invalid_zipcode));
            focusView = mZipcodeView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            CreateUserModel um = new CreateUserModel();

            um.userName = username;
            um.passWord = password;
            um.confirmPassword = confirmPassword;
            um.zipCode = zipcode;
            um.email = email;

            String url = getResources().getString(R.string.webservice_baseurl);
            mAuthTask = new UserRegisterTask(um, url);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isZipcodeValid(String zipcode) {
        return Pattern.matches("^\\d{5}(?:[-\\s]\\d{4})?$", zipcode);
    }

    private boolean isEmailValid(String email) {
        return Pattern.matches("^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$", email);
    }

    private boolean PasswordsMatches(String password, String confirmPassword){
        return password.equalsIgnoreCase(confirmPassword);
    }
    private boolean isUsernameValid(String username) {
        return username.length() >= 6 && !Character.isDigit(username.charAt(0));
    }

    private boolean isPasswordValid(String password) {
        return Pattern.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[#$!%^&+=*])(?=\\S+$).{8,}$", password);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);

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
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private CreateUserModel model;
        private String baseUrl;
        private List<String> errors;

        UserRegisterTask(CreateUserModel model, String baseUrl) {
            this.model = model;
            this.baseUrl = baseUrl;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            HttpURLConnection httpcon;
            String url = baseUrl + "api/accounts/register";
            String data = null;
            String result = null;

            try {
                //Connect
                httpcon = (HttpURLConnection) ((new URL(url).openConnection()));
                httpcon.setDoOutput(true);
                httpcon.setRequestProperty("Content-Type", "application/json");
                httpcon.setRequestProperty("Accept", "application/json");
                httpcon.setRequestMethod("POST");
                httpcon.connect();

                data = model.getJson().toString();
                //Write
                OutputStream os = httpcon.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(data);
                writer.close();
                os.close();

                //Read
                BufferedReader br;
                try {
                    br = new BufferedReader(new InputStreamReader(httpcon.getInputStream(), "UTF-8"));

                    httpcon.disconnect();
                    return true;
                }
                catch (IOException e)
                {
                    br = new BufferedReader(new InputStreamReader(httpcon.getErrorStream(), "UTF-8"));

                    String line = null;
                    StringBuilder sb = new StringBuilder();

                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }

                    br.close();
                    result = sb.toString();

                    JSONObject json = new JSONObject(result).getJSONObject("modelState");
                    JSONArray names = json.names();
                    errors = new ArrayList<String>();
                    for(int i = 0; i < names.length(); i++)
                    {
                        JSONArray values = json.getJSONArray(names.get(i).toString());
                        for(int j = 0; j < values.length(); j++)
                        {
                            errors.add(values.getString(j));
                        }
                    }

                    httpcon.disconnect();
                    return false;

                }
            }
            catch (IOException e)
            {
                return false;
            }
            catch (Exception e){
                System.err.println(e.getMessage());
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                Toast.makeText(getApplicationContext(), getString(R.string.register_success), Toast.LENGTH_LONG).show();
                finish();
            } else {
                StringBuilder builder = new StringBuilder();
                for(String str : errors)
                    builder.append("*" + str + "\n\n");

                ((TextView)findViewById(R.id.txt_register_error)).setText(builder.toString());
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}
