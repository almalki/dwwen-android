package com.dwwen.model;

import java.util.List;

/**
 * Created by abdulaziz on 12/19/2014.
 */
public class AddBlogError {

    private List<String> name;
    private  List<String> blogUrl;
    private List<String> rssUrl;
    private  List<String> description;
    private List<String> image;

    private Blog existingBlog;
    private String detail;

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }

    public List<String> getBlogUrl() {
        return blogUrl;
    }

    public void setBlogUrl(List<String> blogUrl) {
        this.blogUrl = blogUrl;
    }

    public List<String> getRssUrl() {
        return rssUrl;
    }

    public void setRssUrl(List<String> rssUrl) {
        this.rssUrl = rssUrl;
    }

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Blog getExistingBlog() {
        return existingBlog;
    }

    public void setExistingBlog(Blog existingBlog) {
        this.existingBlog = existingBlog;
    }
}
