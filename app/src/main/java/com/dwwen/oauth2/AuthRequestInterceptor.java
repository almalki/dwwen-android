package com.dwwen.oauth2;

import android.content.Context;
import android.content.SharedPreferences;

import com.dwwen.R;

import retrofit.RequestInterceptor;

/**
 * Created by abdulaziz on 11/29/2014.
 */
public class AuthRequestInterceptor implements RequestInterceptor {

    Context context;

    public AuthRequestInterceptor(Context context){
        this.context = context;
    }

    @Override
    public void intercept(RequestInterceptor.RequestFacade request) {

        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String accessToken = sharedPref.getString("auth_access_token", null);

        Long expiresAt = sharedPref.getLong("auth_token_expires_at", 0);
        if(expiresAt <= System.currentTimeMillis()) {

        }
            request.addHeader("Authorization", "Bearer "+accessToken);
    }
}
