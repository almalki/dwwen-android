package com.dwwen.post.event;

import com.dwwen.model.ApiResponse;
import com.dwwen.model.Post;

/**
 * Created by abdulaziz on 12/24/2014.
 */
public class BlogPostsEvent {
    public final ApiResponse<Post> posts;
    public final boolean status;

    public BlogPostsEvent(ApiResponse<Post> posts, boolean status){
        this.posts = posts;
        this.status = status;
    }
}
