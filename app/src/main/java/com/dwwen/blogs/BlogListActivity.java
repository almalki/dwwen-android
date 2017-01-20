package com.dwwen.blogs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.dwwen.R;
import com.dwwen.settings.SettingsActivity;

/**
 * An activity representing a list of Blogs. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link BlogDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link BlogListFragment} and the item details
 * (if present) is a {@link BlogDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link BlogListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class BlogListActivity extends ActionBarActivity
        implements BlogListFragment.Callbacks {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (findViewById(R.id.blog_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((BlogListFragment) getFragmentManager()
                    .findFragmentById(R.id.blog_list))
                    .setActivateOnItemClick(true);
        }

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            int categoryId = getIntent().getExtras().getInt(BlogListFragment.STATE_CATEGORY_ID, 0);
            arguments.putInt(BlogListFragment.STATE_CATEGORY_ID, categoryId);
            BlogListFragment fragment = new BlogListFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .add(R.id.blog_list_container, fragment)
                    .commit();
        }

        // TODO: If exposing deep links into your app, handle intents here.
    }

    /**
     * Callback method from {@link BlogListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(Integer id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putInt(BlogDetailFragment.ARG_ITEM_ID, id);
            BlogDetailFragment fragment = new BlogDetailFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .replace(R.id.blog_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, BlogDetailActivity.class);
            detailIntent.putExtra(BlogDetailFragment.ARG_ITEM_ID, id);
            int categoryId = getIntent().getExtras().getInt(BlogListFragment.STATE_CATEGORY_ID);
            detailIntent.putExtra(BlogListFragment.STATE_CATEGORY_ID, categoryId);
            startActivity(detailIntent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            finish();
        }

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
