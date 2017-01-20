package com.dwwen.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dwwen.R;
import com.dwwen.common.BaseActivity;
import com.dwwen.model.ChangePasswordError;
import com.dwwen.registration.ChangePasswordFailureEvent;
import com.dwwen.registration.ChangePasswordSuccessEvent;
import com.dwwen.registration.UserHandler;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

public class ChangePasswordActivity extends BaseActivity {

    private EditText oldPasswordView;
    private EditText newPasswordView;
    private EditText rePasswordView;

    @Inject
    UserHandler userHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        setMainView(findViewById(R.id.change_password_form));
        setProgressView(findViewById(R.id.loading_progress));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        oldPasswordView = (EditText) findViewById(R.id.old_password);
        newPasswordView = (EditText) findViewById(R.id.new_password);
        rePasswordView = (EditText) findViewById(R.id.re_password);

        rePasswordView.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.changePassword || id == EditorInfo.IME_NULL) {
                    changePassword(rePasswordView);
                    return true;
                }
                return false;
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_change_password, menu);
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

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void changePassword(View view){
        // Reset errors.
        oldPasswordView.setError(null);
        newPasswordView.setError(null);
        rePasswordView.setError(null);

        // Store values at the time of the login attempt.
        String oldPassword = oldPasswordView.getText().toString();
        String newPassword = newPasswordView.getText().toString();
        String rePassword = rePasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(oldPassword) || !isPasswordValid(oldPassword)) {
            oldPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = oldPasswordView;
            cancel = true;
        }

        else if (TextUtils.isEmpty(newPassword) || !isPasswordValid(newPassword)) {
            newPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = newPasswordView;
            cancel = true;
        }

        else if (TextUtils.isEmpty(rePassword) || !isPasswordValid(rePassword)) {
            rePasswordView.setError(getString(R.string.error_invalid_password));
            focusView = rePasswordView;
            cancel = true;
        }

        else if(! newPassword.equals(rePassword)){
            rePasswordView.setError(getString(R.string.not_matching));
            focusView = rePasswordView;
            cancel = true;
        }

        else if(oldPassword.equals(newPassword)){
            newPasswordView.setError(getString(R.string.password_not_changed));
            focusView = newPasswordView;
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
            userHandler.changePassword(oldPassword, newPassword);
        }

    }

    private boolean isPasswordValid(String password) {
        return password.length() > 7;
    }

    @Subscribe
    public void onChangePasswordSuccess(ChangePasswordSuccessEvent event){
        finish();
        Toast.makeText(this, R.string.password_changed_successfully, Toast.LENGTH_LONG).show();
    }

    @Subscribe
    public void onChangePasswordFailure(ChangePasswordFailureEvent event){
        ChangePasswordError error = event.error;
        if(error != null){
            if(error.getOldPassword() != null && error.getOldPassword().size() > 0){
                oldPasswordView.setError(error.getOldPassword().get(0));
            }

            if(error.getNewPassword() != null && error.getNewPassword().size() > 0){
                newPasswordView.setError(error.getNewPassword().get(0));
            }

            if(error.getDetail() != null) {
                Toast.makeText(this, error.getDetail(), Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(this, R.string.unknown_error, Toast.LENGTH_LONG).show();
        }
        showProgress(false);

    }

}