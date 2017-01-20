package com.dwwen.post;

import com.dwwen.model.ApiResponse;
import com.dwwen.model.Post;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by abdulaziz on 11/29/2014.
 */


public interface PostClient {
    @GET("/posts/{id}/")
    void fetchPost(@Path("id") long postId, Callback<Post> callback);

    @POST("/posts/{id}/like/")
    void like(@Path("id") long id, Callback<Response> callback);

    @DELETE("/posts/{id}/like/")
    void unLike(@Path("id") long id, Callback<Response> callback);

    @POST("/posts/{id}/favorite/")
    void favorite(@Path("id") long id, Callback<Response> callback);

    @DELETE("/posts/{id}/favorite/")
    void unFavorite(@Path("id") long id, Callback<Response> callback);

    @GET("/favorites/")
    void fetchFavroites(@Query("page") int page, Callback<ApiResponse<Post>> callback);

    @GET("/posts/")
    void fetchBlogPosts(@Query("blog") int blogId, @Query("id") int page, Callback<ApiResponse<Post>> callback);

    @GET("/posts/")
    void search(@Query("q") String q, @Query("page") int page, Callback<ApiResponse<Post>> callback);

}