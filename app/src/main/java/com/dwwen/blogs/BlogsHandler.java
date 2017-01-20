package com.dwwen.blogs;

import com.dwwen.blogs.event.AddBlogFailureEvent;
import com.dwwen.blogs.event.AddBlogSuccessEvent;
import com.dwwen.blogs.event.BlogFailure;
import com.dwwen.blogs.event.BlogSuccess;
import com.dwwen.blogs.event.BlogsFailure;
import com.dwwen.blogs.event.BlogsSuccess;
import com.dwwen.blogs.event.CategoriesFailure;
import com.dwwen.blogs.event.CategoriesSuccess;
import com.dwwen.blogs.event.FollowFailureEvent;
import com.dwwen.blogs.event.FollowSuccessEvent;
import com.dwwen.blogs.event.FollowedBlogsListSuccessEvent;
import com.dwwen.model.AddBlogError;
import com.dwwen.model.ApiResponse;
import com.dwwen.model.Blog;
import com.dwwen.model.Category;
import com.dwwen.model.FollowedBlog;
import com.squareup.otto.Bus;

import java.io.File;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.MultipartTypedOutput;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

@Singleton
public class BlogsHandler {

    @Inject
    BlogsClient blogsClient;

    @Inject
    Bus bus;

    public void fetchCategories() {
        blogsClient.categories(new Callback<List<Category>>() {
            @Override
            public void success(List<Category> categories, Response response) {
                CategoriesSuccess result = new CategoriesSuccess(categories);
                bus.post(result);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                CategoriesFailure result = new CategoriesFailure();
                bus.post(result);
            }
        });
    }

    public void fetchBlogs(Integer catgoryId, int page){
        blogsClient.blogs(catgoryId, page, new Callback<ApiResponse<Blog>>() {
            @Override
            public void success(ApiResponse<Blog> blogs, Response response) {
                BlogsSuccess result = new BlogsSuccess(blogs);
                bus.post(result);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                BlogsFailure result = new BlogsFailure();
                bus.post(result);
            }
        });
    }

    public void fetchBlogs(Integer catgoryId, int page, String orderBy){
        blogsClient.blogs(catgoryId, page, orderBy, new Callback<ApiResponse<Blog>>() {
            @Override
            public void success(ApiResponse<Blog> blogs, Response response) {
                BlogsSuccess result = new BlogsSuccess(blogs);
                bus.post(result);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                BlogsFailure result = new BlogsFailure();
                bus.post(result);
            }
        });
    }

    public void fetchTopBlogs(Integer catgoryId, int page){
        blogsClient.blogs(catgoryId, page, "popular", new Callback<ApiResponse<Blog>>() {
            @Override
            public void success(ApiResponse<Blog> blogs, Response response) {
                BlogsSuccess result = new BlogsSuccess(blogs);
                bus.post(result);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                BlogsFailure result = new BlogsFailure();
                bus.post(result);
            }
        });
    }

    public void fetchBlog(Integer blogId){
        blogsClient.blog(blogId, new Callback<Blog>() {
            @Override
            public void success(Blog blog, Response response) {
                BlogSuccess result = new BlogSuccess(blog);
                bus.post(result);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                BlogFailure result = new BlogFailure();
                bus.post(result);
            }
        });
    }

    public void followBlog(Integer blogId){
        blogsClient.follow(blogId, new Callback<Response>() {
            @Override
            public void success(Response resp, Response response) {
                FollowSuccessEvent result = new FollowSuccessEvent(FollowSuccessEvent.Action.Follow);
                bus.post(result);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                FollowFailureEvent result = new FollowFailureEvent(FollowSuccessEvent.Action.Follow);
                bus.post(result);
            }
        });
    }

    public void unFollowBlog(Integer blogId){
        blogsClient.unfollow(blogId, new Callback<Response>() {
            @Override
            public void success(Response rsp, Response response) {
                FollowSuccessEvent result = new FollowSuccessEvent(FollowSuccessEvent.Action.Unfollow);
                bus.post(result);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                FollowFailureEvent result = new FollowFailureEvent(FollowSuccessEvent.Action.Unfollow);
                bus.post(result);
            }
        });
    }

    public void addBlog(Blog blog, File imageFile, String mimeType){
        TypedFile image = null;
        if(imageFile != null) {
            image = new TypedFile(mimeType, imageFile);
        }
        MultipartTypedOutput mto = new MultipartTypedOutput();
        for (String cat : blog.getCategories()){
            mto.addPart("categories", new TypedString(cat));
        }
        mto.addPart("name", new TypedString(blog.getName()));
        mto.addPart("blog_url", new TypedString(blog.getBlogUrl()));
        mto.addPart("rss_url", new TypedString(blog.getRssUrl()));
        mto.addPart("description", new TypedString(blog.getDescription()));
        if(image != null){
            mto.addPart("image", image);
        }
        blogsClient.addBlog(mto, new Callback<Blog>() {
            @Override
            public void success(Blog rspBlog, Response response) {
                AddBlogSuccessEvent result = new AddBlogSuccessEvent(rspBlog);
                bus.post(result);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Response r = retrofitError.getResponse();
                AddBlogError error = null;
                if (r != null && (r.getStatus() == 400 || r.getStatus() == 409)) {
                    error = (AddBlogError) retrofitError.getBodyAs(AddBlogError.class);
                }
                AddBlogFailureEvent result = new AddBlogFailureEvent(error);
                bus.post(result);
            }
        });
    }

    public void fetchFollowedBlogs(int page) {
        blogsClient.fetchFollowedBlogs(page, new Callback<ApiResponse<FollowedBlog>>() {
            @Override
            public void success(ApiResponse<FollowedBlog> blogs, Response response) {
                FollowedBlogsListSuccessEvent result = new FollowedBlogsListSuccessEvent(blogs);
                bus.post(result);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                CategoriesFailure result = new CategoriesFailure();
                bus.post(result);
            }
        });
    }
}
