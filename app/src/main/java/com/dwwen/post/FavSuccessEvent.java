package com.dwwen.post;

/**
 * Created by abdulaziz on 12/17/2014.
 */
public class FavSuccessEvent {

    public static enum Action{
        Favorite, Remove;
    }

    public final Action action;

    public FavSuccessEvent(Action action){
        this.action = action;
    }

}
