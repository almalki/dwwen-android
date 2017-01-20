package com.dwwen.registration;

import com.dwwen.model.User;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by abdulaziz on 12/14/2014.
 */
public interface UserClient {

    @POST("/users/")
    void register(@Body User user, Callback<User> callback);

    @FormUrlEncoded
    @POST("/users/change_password/")
    void changePassword(@Field("old_password") String oldPassword,
                        @Field("new_password") String newPassword,
                        Callback<Response> callback);
}
