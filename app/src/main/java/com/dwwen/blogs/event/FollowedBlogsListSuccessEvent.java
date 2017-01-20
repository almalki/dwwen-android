package com.dwwen.blogs.event;

import com.dwwen.model.ApiResponse;
import com.dwwen.model.FollowedBlog;

/**
 * Created by abdulaziz on 12/23/2014.
 */
public class FollowedBlogsListSuccessEvent {
    public  final ApiResponse<FollowedBlog> blogs;

    public FollowedBlogsListSuccessEvent(ApiResponse<FollowedBlog> blogs){
        this.blogs = blogs;
    }
}
