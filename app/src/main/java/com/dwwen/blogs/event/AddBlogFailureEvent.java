package com.dwwen.blogs.event;

import com.dwwen.model.AddBlogError;

/**
 * Created by abdulaziz on 12/19/2014.
 */
public class AddBlogFailureEvent {
    public  final  AddBlogError error;

    public AddBlogFailureEvent(AddBlogError error){
        this.error = error;
    }
}
