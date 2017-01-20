package com.dwwen.timeline;

import com.dwwen.model.PostListResponse;

/**
 * Created by abdulaziz on 11/29/2014.
 */
public class TimelineSuccess {

    public final PostListResponse posts;
    public TimelineSuccess(PostListResponse posts) {
        this.posts = posts;
    }
}
