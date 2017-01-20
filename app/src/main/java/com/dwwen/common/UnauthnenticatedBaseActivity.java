package com.dwwen.common;

import com.dwwen.oauth2.TokenManager;

/**
 * Created by abdulaziz on 12/14/2014.
 */
public class UnauthnenticatedBaseActivity extends BaseActivity {

    @Override
    protected void postInject() {
        setTokenManager(new DummyTokenManager());
    }

    public static class DummyTokenManager extends TokenManager{

        @Override
        public void checkToken(){
            return;
        }
    }
}