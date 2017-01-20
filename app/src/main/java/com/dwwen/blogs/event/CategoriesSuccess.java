package com.dwwen.blogs.event;

import com.dwwen.model.Category;

import java.util.List;

/**
 * Created by abdulaziz on 11/29/2014.
 */
public class CategoriesSuccess {

    public final List<Category> categories;
    public CategoriesSuccess(List<Category> categories) {
        this.categories = categories;
    }
}
