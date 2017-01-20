package com.dwwen.common;

import android.app.Activity;

import com.dwwen.auth.ChangePasswordActivity;
import com.dwwen.blogposts.BlogPostsActivity;
import com.dwwen.blogs.AddBlogActivity;
import com.dwwen.blogs.BlogDetailFragment;
import com.dwwen.blogs.BlogListFragment;
import com.dwwen.blogs.BlogsClient;
import com.dwwen.blogs.CategoriesActivity;
import com.dwwen.favroites.FavoritesActivity;
import com.dwwen.log.EventLogger;
import com.dwwen.oauth2.AuthRequestInterceptor;
import com.dwwen.oauth2.TokenManager;
import com.dwwen.post.PostActivity;
import com.dwwen.post.PostClient;
import com.dwwen.registration.RegistrationActivity;
import com.dwwen.registration.UserClient;
import com.dwwen.search.PostSearchActivity;
import com.dwwen.settings.FileBackedSettings;
import com.dwwen.settings.Settings;
import com.dwwen.timeline.TimelineClient;
import com.dwwen.timeline.TimelineFragment;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.otto.Bus;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

import static dagger.Provides.Type.SET;

@Module(injects = {
        PostActivity.class,
        TimelineFragment.class,
        CategoriesActivity.class,
        BlogListFragment.class,
        RegistrationActivity.class,
        FavoritesActivity.class,
        ChangePasswordActivity.class,
        AddBlogActivity.class,
        BlogPostsActivity.class,
        PostSearchActivity.class,
        BlogDetailFragment.class}, library = true)
public class ProdAppModule {

    private final ShipFasterApplication application;

    public ProdAppModule(ShipFasterApplication application) {
        this.application = application;
    }

    @Provides
    Settings provideSettings(FileBackedSettings settings) {
        return settings;
    }

    @Provides
    @Singleton
    Bus provideBus() {
        return new Bus();
    }

    @Provides
    @Singleton
    TokenManager provideTokenManager() {
        return new TokenManager(application);
    }

    @Provides(type = SET)
    @RegisterOnResume
    Object registerEventLogger(EventLogger logger) {
        return logger;
    }

    @Provides
    Activity provideResumedActivity() {
        return application.getResumedActivity();
    }

    @Provides
    RestAdapter provideRestAdapter() {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint("https://api.dwwen.com/v1")
                .setConverter(new GsonConverter(gson))
                .setRequestInterceptor(new AuthRequestInterceptor(application))
                .build();

        return restAdapter;
    }

    @Provides @Named("UnauthRestAdapter")
    RestAdapter provideUnauthRestAdapter() {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint("https://api.dwwen.com/v1")
                .setConverter(new GsonConverter(gson))
                .build();

        return restAdapter;
    }

    @Provides
    TimelineClient provideTimelineClient(RestAdapter restAdapter) {
        return restAdapter.create(TimelineClient.class);
    }

    @Provides
    BlogsClient provideBlogsClient(RestAdapter restAdapter) {
        return restAdapter.create(BlogsClient.class);
    }

    @Provides
    PostClient providePostClient(RestAdapter restAdapter) {
        return restAdapter.create(PostClient.class);
    }

    @Provides @Named("UnAuthUserClient")
    UserClient provideUnAuthUserClient(@Named("UnauthRestAdapter") RestAdapter restAdapter) {
        return restAdapter.create(UserClient.class);
    }

    @Provides
    UserClient provideUserClient(RestAdapter restAdapter) {
        return restAdapter.create(UserClient.class);
    }
}
