package com.dwwen.blogposts;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.dwwen.R;
import com.dwwen.common.BaseActivity;
import com.dwwen.model.Post;
import com.dwwen.post.PostHandler;
import com.dwwen.post.event.BlogPostsEvent;
import com.dwwen.timeline.PostListAdapter;
import com.dwwen.ui.RecycledEndlessScrollListener;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import javax.inject.Inject;

public class BlogPostsActivity extends BaseActivity {

    public final static String BLOG_ID = "BLOG_ID";

    private RecyclerView mRecyclerView;
    private PostListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecycledEndlessScrollListener scrollListener;

    @Inject
    PostHandler postHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_posts);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.posts);

        setMainView(mRecyclerView);
        setProgressView(findViewById(R.id.loading_progress));

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new PostListAdapter(this, new ArrayList<Post>());
        mRecyclerView.setAdapter(mAdapter);

        final int blodId = getIntent().getIntExtra(BLOG_ID, 0);

        scrollListener = new RecycledEndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                postHandler.fetchBlogPosts(blodId, page);
            }
        };
        mRecyclerView.setOnScrollListener(scrollListener);

        postHandler.fetchBlogPosts(blodId, 1);
        showProgress(true);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_blog_posts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void onBlogPostsEvent(BlogPostsEvent event) {

        if(event.status) {
            for (Post post : event.posts.getResults()) {
                mAdapter.add(post);
            }
            if (event.posts.getNext() == null) {
                scrollListener.setExhausted(true);
                mAdapter.done();
            }
        }
        else {
            Toast.makeText(this, R.string.unknown_error, Toast.LENGTH_LONG).show();
        }
        showProgress(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        mAdapter.setBigImages(prefs.getString("timeline_post_view_pref", "large").equalsIgnoreCase("large"));
    }

}
