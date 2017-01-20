package com.dwwen.registration;

import com.dwwen.model.RegistrationError;

/**
 * Created by abdulaziz on 11/29/2014.
 */
public class RegsiterFailureEvent {
    public final RegistrationError error;

    public RegsiterFailureEvent(RegistrationError error){
        this.error = error;
    }
}
