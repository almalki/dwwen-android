package com.dwwen.post;

/**
 * Created by abdulaziz on 12/17/2014.
 */
public class FavFailureEvent {

    public final FavSuccessEvent.Action action;

    public FavFailureEvent(FavSuccessEvent.Action action){
        this.action = action;
    }

}
