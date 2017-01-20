package com.dwwen.blogs.event;

import com.dwwen.model.Blog;

/**
 * Created by abdulaziz on 12/19/2014.
 */
public class AddBlogSuccessEvent {

    public final Blog blog;

    public AddBlogSuccessEvent(Blog blog){
        this.blog = blog;
    }
}