package com.dwwen.favroites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dwwen.R;
import com.dwwen.common.BaseActivity;
import com.dwwen.model.Post;
import com.dwwen.post.FavoritesListFailureEvent;
import com.dwwen.post.FavroitesListSuccessEvent;
import com.dwwen.post.PostHandler;
import com.dwwen.settings.SettingsActivity;
import com.dwwen.timeline.PostListAdapter;
import com.dwwen.ui.RecycledEndlessScrollListener;
import com.dwwen.ui.SwipeDismissRecyclerViewTouchListener;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

public class FavoritesActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private PostListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecycledEndlessScrollListener scrollListener;
    private List<Post> posts;
    private Set<Post> uniquePosts = new LinkedHashSet<>();

    @Inject
    protected PostHandler postHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        mRecyclerView = (RecyclerView) findViewById(R.id.favroites_recycler_view);

        setMainView(mRecyclerView);
        setProgressView(findViewById(R.id.loading_progress));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        posts = new ArrayList<Post>();
        mAdapter = new FavoritePostListAdapter(this, posts);
        mRecyclerView.setAdapter(mAdapter);

        scrollListener = new RecycledEndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                postHandler.fetchFavroites(page);
            }
        };
        mRecyclerView.setOnScrollListener(scrollListener);

        SwipeDismissRecyclerViewTouchListener touchListener =
                new SwipeDismissRecyclerViewTouchListener(
                        mRecyclerView,
                        new SwipeDismissRecyclerViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }
                            @Override
                            public void onDismiss(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    // mLayoutManager.removeView(mLayoutManager.getChildAt(position));
                                    postHandler.removeFavroite(posts.get(position).getId());
                                    posts.remove(position);
                                    int i = (position / 20) + 1;
                                    postHandler.fetchFavroites(i);
                                    mAdapter.notifyItemRemoved(position);
                                    scrollListener.stepBack();
                                }
                            }
                        });
        mRecyclerView.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        //mRecyclerView.setOnScrollListener(touchListener.makeScrollListener());

        showProgress(true);
        postHandler.fetchFavroites(1);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favorites, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void onFavroitesListSuccess(FavroitesListSuccessEvent event) {
        for(Post post : event.response.getResults()){
            if(!uniquePosts.contains(post))
            mAdapter.add(post);
            uniquePosts.add(post);
        }
        if(event.response.getNext() == null){
            mAdapter.done();
            scrollListener.setExhausted(true);
        }
        if(posts.size() == 0){
            findViewById(R.id.empty_msg).setVisibility(View.VISIBLE);
        }
        else{
            findViewById(R.id.empty_msg).setVisibility(View.GONE);
        }
        showProgress(false);
    }

    @Subscribe
    public void onFavoritesListFailure(FavoritesListFailureEvent event) {
        Toast.makeText(this, R.string.unknown_error, Toast.LENGTH_LONG).show();
        mAdapter.done();
        showProgress(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        mAdapter.setBigImages(prefs.getString("timeline_post_view_pref", "large").equalsIgnoreCase("large"));
    }
}
