package com.dwwen.blogs.event;

import com.dwwen.model.Blog;

/**
 * Created by abdulaziz on 12/1/2014.
 */
public class BlogSuccess {
    public final Blog blog;
    public BlogSuccess(Blog blog){
        this.blog = blog;
    }
}
