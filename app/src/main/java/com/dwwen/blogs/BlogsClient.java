package com.dwwen.blogs;

import com.dwwen.model.ApiResponse;
import com.dwwen.model.Blog;
import com.dwwen.model.Category;
import com.dwwen.model.FollowedBlog;

import java.util.List;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.MultipartTypedOutput;
import retrofit.mime.TypedInput;

/**
 * Created by abdulaziz on 11/29/2014.
 */

public interface BlogsClient {

    @GET("/categories/")
    void categories(Callback<List<Category>> callback);

    @GET("/blogs/")
    void blogs(@Query("category") Integer category,
               @Query("page") int page,
               Callback<ApiResponse<Blog>> callback);

    @GET("/blogs/")
    void blogs(@Query("category") Integer category,
               @Query("page") int page,
               @Query("orderBy") String orderBy,
               Callback<ApiResponse<Blog>> callback);


    @Multipart
    @POST("/blogs/")
    void addBlog(@Part("name") String name,
                 @Part("blog_url") String blogUrl,
                 @Part("rss_url") String rssUrl,
                 @Part("description") String description,
                 @Part("image") TypedInput image,
                 Callback<Blog> callback);

    @POST("/blogs/")
    void addBlog(@Body MultipartTypedOutput multipartOutput, Callback<Blog> callback);

    @GET("/blogs/{id}/")
    void blog(@Path("id") Integer id, Callback<Blog> callback);

    @POST("/blogs/{id}/follow/")
    void follow(@Path("id") Integer id, Callback<Response> callback);

    @DELETE("/blogs/{id}/follow/")
    void unfollow(@Path("id") Integer id, Callback<Response> callback);

    @GET("/following/")
    void fetchFollowedBlogs(@Query("page") int page, Callback<ApiResponse<FollowedBlog>> callback);

}
