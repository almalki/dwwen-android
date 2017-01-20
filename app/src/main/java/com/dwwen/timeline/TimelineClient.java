package com.dwwen.timeline;

import com.dwwen.model.PostListResponse;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by abdulaziz on 11/29/2014.
 */

public interface TimelineClient {
    @GET("/timeline/")
    void timeline(@Query("page") int page, Callback<PostListResponse> callback);
}