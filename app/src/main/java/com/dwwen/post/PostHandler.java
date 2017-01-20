package com.dwwen.post;

import com.dwwen.model.ApiResponse;
import com.dwwen.model.Post;
import com.dwwen.post.event.BlogPostsEvent;
import com.dwwen.search.event.PostSearchEvent;
import com.squareup.otto.Bus;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@Singleton
public class PostHandler {

    @Inject
    PostClient postClient;

    @Inject
    Bus bus;

    public void fetchPost(long postId) {
        postClient.fetchPost(postId, new Callback<Post>() {
            @Override
            public void success(Post post, Response response) {
                PostSuccess result = new PostSuccess(post);
                bus.post(result);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                PostFailure result = new PostFailure();
                bus.post(result);
            }
        });
    }

    public void like(long postId){
        postClient.like(postId, new Callback<Response>() {
            @Override
            public void success(Response resp, Response response) {
                LikeSuccessEvent result = new LikeSuccessEvent(LikeSuccessEvent.Action.Like);
                bus.post(result);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                LikeFailureEvent result = new LikeFailureEvent(LikeSuccessEvent.Action.Like);
                bus.post(result);
            }
        });
    }

    public void unlike(long postId){
        postClient.unLike(postId, new Callback<Response>() {
            @Override
            public void success(Response resp, Response response) {
                LikeSuccessEvent result = new LikeSuccessEvent(LikeSuccessEvent.Action.UnLike);
                bus.post(result);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                LikeFailureEvent result = new LikeFailureEvent(LikeSuccessEvent.Action.UnLike);
                bus.post(result);
            }
        });
    }

    public void favorite(long postId){
        postClient.favorite(postId, new Callback<Response>() {
            @Override
            public void success(Response resp, Response response) {
                FavSuccessEvent result = new FavSuccessEvent(FavSuccessEvent.Action.Favorite);
                bus.post(result);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                FavFailureEvent result = new FavFailureEvent(FavSuccessEvent.Action.Favorite);
                bus.post(result);
            }
        });
    }

    public void removeFavroite(long postId){
        postClient.unFavorite(postId, new Callback<Response>() {
            @Override
            public void success(Response resp, Response response) {
                FavSuccessEvent result = new FavSuccessEvent(FavSuccessEvent.Action.Remove);
                bus.post(result);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                FavFailureEvent result = new FavFailureEvent(FavSuccessEvent.Action.Remove);
                bus.post(result);
            }
        });
    }

    public void fetchFavroites(int page) {
        postClient.fetchFavroites(page, new Callback<ApiResponse<Post>>() {
            @Override
            public void success(ApiResponse<Post> posts, Response response) {
                FavroitesListSuccessEvent result = new FavroitesListSuccessEvent(posts);
                bus.post(result);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                FavoritesListFailureEvent result = new FavoritesListFailureEvent();
                bus.post(result);
            }
        });
    }

    public void fetchBlogPosts(int blogId, int page) {
        postClient.fetchBlogPosts(blogId, page, new Callback<ApiResponse<Post>>() {
            @Override
            public void success(ApiResponse<Post> posts, Response response) {
                BlogPostsEvent result = new BlogPostsEvent(posts, true);
                bus.post(result);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                BlogPostsEvent result = new BlogPostsEvent(null, false);
                bus.post(result);
            }
        });
    }

    public void search(String q, int page) {
        postClient.search(q, page, new Callback<ApiResponse<Post>>() {
            @Override
            public void success(ApiResponse<Post> posts, Response response) {
                PostSearchEvent result = new PostSearchEvent(posts, true);
                bus.post(result);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                PostSearchEvent result = new PostSearchEvent(null, false);
                bus.post(result);
            }
        });
    }

}
