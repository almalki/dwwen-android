package com.dwwen.blogs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.dwwen.R;
import com.dwwen.blogs.event.CategoriesFailure;
import com.dwwen.blogs.event.CategoriesSuccess;
import com.dwwen.common.BaseActivity;
import com.dwwen.settings.SettingsActivity;
import com.dwwen.ui.DividerItemDecoration;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

/**
 * Created by abdulaziz on 12/1/2014.
 */
public class CategoriesActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Inject
    BlogsHandler blogsHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                getResources().getDrawable(R.drawable.abc_list_divider_mtrl_alpha)));
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        setMainView(mRecyclerView);
        setProgressView(findViewById(R.id.loading_progress));

        showProgress(true);
        blogsHandler.fetchCategories();

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

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Subscribe
    public void onCategoriesSuccess(CategoriesSuccess event) {
        mAdapter = new CategoriesListAdapter(this, event.categories);
        mRecyclerView.setAdapter(mAdapter);
        showProgress(false);
    }

    @Subscribe
    public void onCategoriesFailure(CategoriesFailure event) {
        showProgress(false);
        Toast.makeText(this, R.string.unknown_error, Toast.LENGTH_LONG).show();
    }
}
