package com.dwwen.registration;

import com.dwwen.model.ChangePasswordError;
import com.dwwen.model.RegistrationError;
import com.dwwen.model.User;
import com.squareup.otto.Bus;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by abdulaziz on 12/14/2014.
 */

@Singleton
public class UserHandler {

    @Inject
    UserClient userClient;

    @Inject @Named("UnAuthUserClient")
    UserClient registrationClient;

    @Inject
    Bus bus;

    public void register(String username, String email, String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        registrationClient.register(user, new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                RegisterSuccessEvent result = new RegisterSuccessEvent(user);
                bus.post(result);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Response r = retrofitError.getResponse();
                RegistrationError error = null;
                if (r != null && r.getStatus() == 400) {
                    error = (RegistrationError) retrofitError.getBodyAs(RegistrationError.class);
                }
                RegsiterFailureEvent result = new RegsiterFailureEvent(error);
                bus.post(result);
            }
        });
    }

    public void changePassword(String oldPassword, String newPassword) {
        userClient.changePassword(oldPassword, newPassword, new Callback<Response>() {
            @Override
            public void success(Response rsp, Response response) {
                ChangePasswordSuccessEvent result = new ChangePasswordSuccessEvent();
                bus.post(result);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Response r = retrofitError.getResponse();
                ChangePasswordError error = null;
                if (r != null && r.getStatus() == 400) {
                    error = (ChangePasswordError) retrofitError.getBodyAs(ChangePasswordError.class);
                }
                ChangePasswordFailureEvent result = new ChangePasswordFailureEvent(error);
                bus.post(result);
            }
        });
    }
}
