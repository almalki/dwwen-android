package com.dwwen.post;

/**
 * Created by abdulaziz on 12/12/2014.
 */
public class LikeFailureEvent {
    public final LikeSuccessEvent.Action action;

    public LikeFailureEvent(LikeSuccessEvent.Action action){
        this.action = action;
    }

}
