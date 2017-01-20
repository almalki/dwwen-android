package com.dwwen.timeline;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dwwen.R;
import com.dwwen.activity.MainActivity;
import com.dwwen.blogs.BlogListActivity;
import com.dwwen.blogs.BlogListFragment;
import com.dwwen.blogs.BlogsHandler;
import com.dwwen.blogs.event.FollowedBlogsListFailureEvent;
import com.dwwen.blogs.event.FollowedBlogsListSuccessEvent;
import com.dwwen.common.BaseFragment;
import com.dwwen.model.FollowedBlog;
import com.dwwen.model.Post;
import com.dwwen.ui.RecycledEndlessScrollListener;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class TimelineFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRecyclerView;
    private PostListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecycledEndlessScrollListener scrollListener;

    @Inject
    TimelineHandler timelineHandler;

    @Inject
    BlogsHandler blogsHandler;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.activity_timeline, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.timeline_recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_timeline);
        swipeRefreshLayout.setOnRefreshListener(this);

        setMainView(mRecyclerView);
        setProgressView(view.findViewById(R.id.loading_progress));

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new PostListAdapter(this.getActivity(), new ArrayList<Post>());
        mRecyclerView.setAdapter(mAdapter);

        // endless scroll
        scrollListener = new RecycledEndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                timelineHandler.fetchTimeline(page);
            }
        };
        mRecyclerView.setOnScrollListener(scrollListener);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        mAdapter.setBigImages(prefs.getString("timeline_post_view_pref", "large").equalsIgnoreCase("large"));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showProgress(true);
        timelineHandler.fetchTimeline(1);
        blogsHandler.fetchFollowedBlogs(1);
    }

    @Subscribe
    public void onTimelineSuccess(TimelineSuccess event) {

        if(swipeRefreshLayout.isRefreshing()){
            scrollListener.reset();
            swipeRefreshLayout.setRefreshing(false);
            mAdapter.clear();
        }
        for(Post post : event.posts.getResults()){
            mAdapter.add(post);
        }
        if(event.posts.getNext() == null){
            scrollListener.setExhausted(true);
            mAdapter.done();
        }
        if(isAdded()) {
            showProgress(false);
        }
    }

    @Subscribe
    public void onTimelineFailure(TimelineFailure event) {
        Toast.makeText(this.getActivity(), R.string.unknown_error, Toast.LENGTH_LONG).show();
        scrollListener.setExhausted(true);
        if(isAdded()) {
            showProgress(false);
        }
    }

    @Subscribe
    public void onFollowedBlogsSuccess(FollowedBlogsListSuccessEvent event) {
        List<FollowedBlog> blogs = event.blogs.getResults();
        if(blogs == null || blogs.size() == 0){
            final Intent detailIntent = new Intent(this.getActivity(), BlogListActivity.class);
            detailIntent.putExtra(BlogListFragment.STATE_CATEGORY_ID, 0);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setPositiveButton(R.string.show_blogs, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    startActivity(detailIntent);
                    dialog.dismiss();
                }
            });
            builder.setMessage(R.string.not_following_msg)
                    .setTitle(R.string.following);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @Subscribe
    public void onFollowedBlogsFailure(FollowedBlogsListFailureEvent event) {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setColorSchemeColors(R.color.pink);
        timelineHandler.fetchTimeline(1);
    }
}