package com.dwwen.registration;

import com.dwwen.model.User;

/**
 * Created by abdulaziz on 11/29/2014.
 */
public class RegisterSuccessEvent {

    public final User user;
    public RegisterSuccessEvent(User user) {
        this.user = user;
    }
}
