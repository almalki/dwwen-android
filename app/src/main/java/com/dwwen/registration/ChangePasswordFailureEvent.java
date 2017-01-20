package com.dwwen.registration;

import com.dwwen.model.ChangePasswordError;

/**
 * Created by abdulaziz on 12/19/2014.
 */
public class ChangePasswordFailureEvent {
    public  final ChangePasswordError error;

    public ChangePasswordFailureEvent(ChangePasswordError error){
        this.error = error;
    }
}
