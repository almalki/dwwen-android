package com.dwwen.post;

/**
 * Created by abdulaziz on 12/12/2014.
 */
public class LikeSuccessEvent {
    public static enum Action{
        Like, UnLike;
    }

    public final Action action;

    public LikeSuccessEvent(Action action){
        this.action = action;
    }
}
