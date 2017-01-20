package com.dwwen.blogs.event;

/**
 * Created by abdulaziz on 12/12/2014.
 */
public class FollowFailureEvent {
    public final FollowSuccessEvent.Action action;

    public FollowFailureEvent(FollowSuccessEvent.Action action){
        this.action = action;
    }

}
