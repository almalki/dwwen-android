package com.dwwen.search;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dwwen.R;
import com.dwwen.common.BaseActivity;
import com.dwwen.model.Post;
import com.dwwen.post.PostHandler;
import com.dwwen.search.event.PostSearchEvent;
import com.dwwen.settings.SettingsActivity;
import com.dwwen.timeline.PostListAdapter;
import com.dwwen.ui.RecycledEndlessScrollListener;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import javax.inject.Inject;

public class PostSearchActivity extends BaseActivity {

    public final static String QUERY = "SEARCH_QUERY";

    private RecyclerView mRecyclerView;
    private TextView mMsgView;
    private PostListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecycledEndlessScrollListener scrollListener;

    @Inject
    PostHandler postHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_search);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.posts);
        mMsgView = (TextView) findViewById(R.id.text_message);

        setMainView(mRecyclerView);
        setProgressView(findViewById(R.id.loading_progress));

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new PostListAdapter(this, new ArrayList<Post>());
        mRecyclerView.setAdapter(mAdapter);

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (!Intent.ACTION_SEARCH.equals(intent.getAction())) {
            Toast.makeText(this, R.string.unknown_error, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        showProgress(true);
        mMsgView.setVisibility(View.GONE);
        mAdapter.clear();

        final String query = intent.getStringExtra(SearchManager.QUERY);

        scrollListener = new RecycledEndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                    postHandler.search(query, page);
            }
        };
        mRecyclerView.setOnScrollListener(scrollListener);
        postHandler.search(query, 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post_search, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void onPostSearchEvent(PostSearchEvent event) {

        if(event.status) {

            if(event.posts != null && event.posts.getResults() != null && event.posts.getResults().size() > 0) {
                for (Post post : event.posts.getResults()) {
                    mAdapter.add(post);
                }
            }
            else{
                mMsgView.setVisibility(View.VISIBLE);
            }

            if (event.posts != null && event.posts.getNext() == null) {
                scrollListener.setExhausted(true);
                mAdapter.done();
            }
        }
        else {
            Toast.makeText(this, R.string.unknown_error, Toast.LENGTH_LONG).show();
            mAdapter.done();
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
