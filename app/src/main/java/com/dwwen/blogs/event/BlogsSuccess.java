package com.dwwen.blogs.event;

import com.dwwen.model.ApiResponse;
import com.dwwen.model.Blog;

/**
 * Created by abdulaziz on 12/1/2014.
 */
public class BlogsSuccess {
    public final ApiResponse<Blog> blogs;
    public BlogsSuccess(ApiResponse<Blog> blogs){
        this.blogs = blogs;
    }
}
