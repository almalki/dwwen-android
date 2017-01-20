package com.dwwen.blogs;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dwwen.R;
import com.dwwen.blogs.event.AddBlogFailureEvent;
import com.dwwen.blogs.event.AddBlogSuccessEvent;
import com.dwwen.blogs.event.CategoriesFailure;
import com.dwwen.blogs.event.CategoriesSuccess;
import com.dwwen.blogs.event.FollowFailureEvent;
import com.dwwen.blogs.event.FollowSuccessEvent;
import com.dwwen.common.BaseActivity;
import com.dwwen.model.AddBlogError;
import com.dwwen.model.Blog;
import com.dwwen.model.Category;
import com.dwwen.settings.SettingsActivity;
import com.dwwen.ui.MultiSelectSpinner;
import com.dwwen.util.FileUtils;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class AddBlogActivity extends BaseActivity {

    private static final int PICK_IMAGE = 1;
    private String imageFilePath;
    private String mimeType;

    EditText nameView;
    EditText blogUrlView;
    EditText rssUrlView;
    EditText descriptionView;
    ImageView imageView;

    List<Category> categoryList;
    MultiSelectSpinner categoriesSelect;

    @Inject
    BlogsHandler blogsHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_blog);

        setMainView(findViewById(R.id.add_blog_form));
        setProgressView(findViewById(R.id.loading_progress));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.loaded_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        nameView = (EditText) findViewById(R.id.name);
        blogUrlView = (EditText) findViewById(R.id.blogUrl);
        rssUrlView = (EditText) findViewById(R.id.rssUrl);
        descriptionView = (EditText) findViewById(R.id.blogDescription);
        imageView = (ImageView) findViewById(R.id.loaded_image);
        categoriesSelect = (MultiSelectSpinner) findViewById(R.id.categories);

        blogsHandler.fetchCategories();
        showProgress(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri _uri = data.getData();

            imageFilePath = FileUtils.getPath(this, _uri);

            mimeType = getContentResolver().getType(_uri);

            ImageView img = (ImageView) findViewById(R.id.loaded_image);
            img.setImageURI(_uri);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_blog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

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

    public void addBlog(View view) {

        List<Integer> cats = categoriesSelect.getSelectedIndicies();
        if(cats.size() == 0){
            Toast.makeText(this, R.string.mandatory_category, Toast.LENGTH_LONG).show();
            return;
        }
        showProgress(true);

        List<String> selectedCategories = new ArrayList<String>();
        for(int catId : cats){
            selectedCategories.add(categoryList.get(catId).getUrl());
        }
        Blog blog = new Blog();
        File file = null;
        blog.setCategories(selectedCategories);
        blog.setName(nameView.getText().toString());
        blog.setBlogUrl(blogUrlView.getText().toString());
        blog.setRssUrl(rssUrlView.getText().toString());
        blog.setDescription(descriptionView.getText().toString());

        if (imageFilePath != null) {
            file = new File(imageFilePath);
        }
        blogsHandler.addBlog(blog, file, mimeType);
    }

    @Subscribe
    public void onAddBlogSuccess(AddBlogSuccessEvent event) {
        Blog blog = event.blog;
        blogsHandler.followBlog(blog.getId());
        Intent detailIntent = new Intent(this, BlogDetailActivity.class);
        detailIntent.putExtra(BlogDetailFragment.ARG_ITEM_ID, blog.getId());
        startActivity(detailIntent);
        finish();
        Toast.makeText(this, R.string.blog_added_successfully, Toast.LENGTH_LONG).show();
    }

    @Subscribe
    public void onAddBlogFailure(AddBlogFailureEvent event) {
        AddBlogError error = event.error;
        if (error != null) {

            if(error.getExistingBlog() != null){
                Intent detailIntent = new Intent(this, BlogDetailActivity.class);
                detailIntent.putExtra(BlogDetailFragment.ARG_ITEM_ID, error.getExistingBlog().getId());
                startActivity(detailIntent);
                finish();
                return;
            }

            if (error.getName() != null && error.getName().size() > 0) {
                nameView.setError(error.getName().get(0));
            }

            if (error.getBlogUrl() != null && error.getBlogUrl().size() > 0) {
                blogUrlView.setError(error.getBlogUrl().get(0));
            }


            if (error.getRssUrl() != null && error.getRssUrl().size() > 0) {
                rssUrlView.setError(error.getRssUrl().get(0));
            }

            if (error.getDescription() != null && error.getDescription().size() > 0) {
                descriptionView.setError(error.getDescription().get(0));
            }

            if (error.getDetail() != null) {
                Toast.makeText(this, error.getDetail(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, R.string.unknown_error, Toast.LENGTH_LONG).show();
        }
        showProgress(false);

    }

    @Subscribe
    public void onFollowSuccess(FollowSuccessEvent event) {
    }

    @Subscribe
    public void onFollowFailure(FollowFailureEvent event) {
    }

    @Subscribe
    public void onCategoriesSuccess(CategoriesSuccess event) {
        categoryList = event.categories;
        List<String> items = new ArrayList<String>();
        for(Category cat : categoryList){
            items.add(cat.getName());
        }
        categoriesSelect.setItems(items);
        showProgress(false);
    }

    @Subscribe
    public void onCategoriesFailure(CategoriesFailure event) {
        showProgress(false);
        findViewById(R.id.add_blog_form).setVisibility(View.GONE);
        Toast.makeText(this, R.string.unknown_error, Toast.LENGTH_LONG).show();
    }
}
