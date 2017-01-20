package com.dwwen.oauth2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.dwwen.R;
import com.dwwen.activity.MainActivity;

/**
 * Created by abdulaziz on 12/12/2014.
 */
public class TokenManager {

    private Context context;

    public TokenManager(Context context){
        this.context = context;
    }

    public TokenManager(){
    }

    public void checkToken(){

        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        String accessToken = sharedPref.getString("auth_access_token", null);
        if(accessToken == null){
            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("FINISH_LOGOUT", true);
            context.startActivity(intent);
            return;
        }

        Long expiresAt = sharedPref.getLong("auth_token_expires_at", 0);
        String refreshtoken = sharedPref.getString("auth_refresh_token", null);
        if(expiresAt <= System.currentTimeMillis()){
            OAuth2Client client = new OAuth2Client(null, null);
            OAuth2Config oauthConfig = new OAuth2Config.OAuth2ConfigBuilder(client.getUsername(),
                    client.getPassword(), client.getClientId(), client.getClientSecret(), client.getSite())
                    .grantType("refresh_token").build();
            Token token = new Token(0L, null, refreshtoken, null);
            try {
                token = OAuthUtils.refreshAccessToken(token, oauthConfig);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("auth_access_token", token.getAccessToken());
                editor.putString("auth_refresh_token", token.getRefreshToken());
                editor.putLong("auth_token_expires_at", token.getExpiresAt());
                editor.putLong("auth_token_expires_in", token.getExpiresIn());
                editor.commit();
            }
            catch (Exception e){
                Intent intent = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("FINISH_LOGOUT", true);
                context.startActivity(intent);
                return;
            }
        }
    }
}