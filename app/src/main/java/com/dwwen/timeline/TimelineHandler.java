package com.dwwen.timeline;

import com.dwwen.model.PostListResponse;
import com.squareup.otto.Bus;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@Singleton
public class TimelineHandler {

    @Inject
    TimelineClient timelineClient;

    @Inject
    Bus bus;

    public void fetchTimeline(int page) {
        timelineClient.timeline(page, new Callback<PostListResponse>() {
            @Override
            public void success(PostListResponse posts, Response response) {
                TimelineSuccess result = new TimelineSuccess(posts);
                bus.post(result);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                TimelineFailure result = new TimelineFailure();
                bus.post(result);
            }
        });
    }
}
