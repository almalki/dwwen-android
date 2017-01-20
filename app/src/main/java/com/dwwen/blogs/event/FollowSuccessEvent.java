package com.dwwen.blogs.event;

/**
 * Created by abdulaziz on 12/12/2014.
 */
public class FollowSuccessEvent {
    public static enum Action{
        Follow, Unfollow;
    }

    public final Action action;

    public FollowSuccessEvent(Action action){
        this.action = action;
    }
}
