package com.dwwen.post;

import com.dwwen.model.ApiResponse;
import com.dwwen.model.Post;

/**
 * Created by abdulaziz on 12/18/2014.
 */
public class FavroitesListSuccessEvent {

    public  final ApiResponse<Post> response;

    FavroitesListSuccessEvent(ApiResponse<Post> response){
        this.response = response;
    }
}
