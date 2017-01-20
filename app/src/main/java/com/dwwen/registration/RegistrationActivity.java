package com.dwwen.registration;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dwwen.R;
import com.dwwen.activity.MainActivity;
import com.dwwen.common.UnauthnenticatedBaseActivity;
import com.dwwen.model.RegistrationError;
import com.dwwen.model.User;
import com.dwwen.oauth2.OAuth2Client;
import com.dwwen.oauth2.Token;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

/**
 * Created by abdulaziz on 12/14/2014.
 */
public class RegistrationActivity extends UnauthnenticatedBaseActivity {

    @Inject
    UserHandler userHandler;
    private UserLoginTask mAuthTask = null;

    // UI references.
    private TextView mUsernameView;
    private TextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mRegFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Set up the login form.
        mEmailView = (TextView) findViewById(R.id.email);
        mUsernameView = (TextView) findViewById(R.id.username);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    register();
                    return true;
                }
                return false;
            }
        });

        Button registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        mRegFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void register() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mUsernameView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String username = mUsernameView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.

        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            mUsernameView.setError(getString(R.string.error_invalid_username));
            focusView = mUsernameView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
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
            userHandler.register(username, email, password);
        }
    }

    public void login(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        showProgress(false);
        finish();
    }

    @Subscribe
    public void onRegisterSuccess(RegisterSuccessEvent event) {
        User user = event.user;
        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("USER_INFO_USERNAME", user.getUsername());
        editor.putString("USER_INFO_EMAIL", user.getEmail());
        editor.putString("USER_INFO_URL", user.getUrl());
        editor.putLong("USER_INFO_JOINED", user.getDateJoined().getTime());
        editor.putLong("USER_INFO_LAST_LOGIN", user.getLastLogin().getTime());
        editor.commit();
        mAuthTask = new UserLoginTask(mUsernameView.getText().toString(), mPasswordView.getText().toString());
        mAuthTask.execute((Void) null);
    }

    @Subscribe
    public void onRegsiterFailure(RegsiterFailureEvent event) {
        RegistrationError error = event.error;
        if(error != null){
            if(error.getEmail() != null && error.getEmail().size() > 0){
                mEmailView.setError(error.getEmail().get(0));
            }
            if(error.getUsername() != null && error.getUsername().size() > 0){
                mUsernameView.setError(error.getUsername().get(0));
            }
            if(error.getPassword() != null && error.getPassword().size() > 0){
                mPasswordView.setError(error.getPassword().get(0));
            }
            if(error.getDetail() != null){
                Toast.makeText(this, error.getDetail(), Toast.LENGTH_LONG).show();
            }
        }
        showProgress(false);
    }


    public void saveToken(Token token) {
        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("auth_access_token", token.getAccessToken());
        editor.putString("auth_refresh_token", token.getRefreshToken());
        editor.putLong("auth_token_expires_at", token.getExpiresAt());
        editor.putLong("auth_token_expires_in", token.getExpiresIn());
        editor.commit();
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.length() > 5;
    }

    private boolean isUsernameValid(String username) {
        //TODO: Replace this with your own logic
        return username.length() > 2;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 7;
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

            mRegFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mRegFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Token> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Token doInBackground(Void... params) {
            try {
                OAuth2Client client = new OAuth2Client(mEmail, mPassword);
                Token token = client.getAccessToken();
                return token;
            }
            catch (Exception e){
                return null;
            }
        }

        @Override
        protected void onPostExecute(final Token token) {
            mAuthTask = null;
            if (token != null) {
                saveToken(token);
                login();
            } else {
                showProgress(false);
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}
