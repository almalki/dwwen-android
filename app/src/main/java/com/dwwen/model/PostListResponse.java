package com.dwwen.model;

import java.util.List;

/**
 * Created by abdulaziz on 11/30/2014.
 */

public class PostListResponse {
    private String next;
    private String previous;
    private List<Post> results;

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<Post> getResults() {
        return results;
    }

    public void setResults(List<Post> results) {
        this.results = results;
    }
}