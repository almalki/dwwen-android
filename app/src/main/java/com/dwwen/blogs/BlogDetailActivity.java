package com.dwwen.blogs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.dwwen.R;
import com.dwwen.blogposts.BlogPostsActivity;
import com.dwwen.settings.SettingsActivity;

/**
 * An activity representing a single Blog detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link BlogListActivity}.
 * <p/>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link BlogDetailFragment}.
 */
public class BlogDetailActivity extends ActionBarActivity {

    private int blogId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);

        // Show the Up button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            blogId = getIntent().getIntExtra(BlogDetailFragment.ARG_ITEM_ID, 0);
            arguments.putInt(BlogDetailFragment.ARG_ITEM_ID, blogId);
            BlogDetailFragment fragment = new BlogDetailFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .add(R.id.blog_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_blog_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        if (id == R.id.action_list_posts) {
            Intent intent = new Intent(this, BlogPostsActivity.class);
            intent.putExtra(BlogPostsActivity.BLOG_ID, blogId);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
