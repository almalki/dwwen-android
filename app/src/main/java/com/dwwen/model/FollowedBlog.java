package com.dwwen.model;

import java.util.Date;

/**
 * Created by abdulaziz on 12/23/2014.
 */
public class FollowedBlog {
    private Blog blog;
    private Date createdDate;

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
