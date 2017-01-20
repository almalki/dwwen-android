package com.dwwen.oauth2;

public class OAuth2Client {

    private final String username;
    private final String password;
    private final String clientId = "XXXXXXXXXXXXXXXXXXXXXXXX";
    private final String clientSecret = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
    private final String site = "https://dwwen.com";

    public OAuth2Client(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }


    public String getPassword() {
        return password;
    }


    public String getClientId() {
        return clientId;
    }


    public String getClientSecret() {
        return clientSecret;
    }


    public String getSite() {
        return site;
    }

    public Token getAccessToken() {
        OAuth2Config oauthConfig = new OAuth2Config.OAuth2ConfigBuilder(username, password, clientId, clientSecret, site)
                .grantType("password").build();
        return OAuthUtils.getAccessToken(oauthConfig);
    }
}
