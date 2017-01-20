package com.dwwen.model;

import java.util.List;

/**
 * Created by abdulaziz on 12/1/2014.
 */
public class ApiResponse<T> {
    private String next;
    private String previous;
    private List<T> results;

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

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

}
