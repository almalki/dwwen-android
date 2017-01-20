package com.dwwen.post;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dwwen.R;
import com.dwwen.common.BaseActivity;
import com.dwwen.model.Post;
import com.dwwen.settings.SettingsActivity;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.util.Date;

import javax.inject.Inject;

public class PostActivity extends BaseActivity {

    public final static String ARG_POST_ID = "post_id";
    public final static String ARG_POST_TITLE = "post_title";
    public final static String ARG_POST_DATE = "post_date";
    public final static String ARG_POST_IMAGE= "post_image";
    public final static String ARG_BLOG_NAME = "blog_name";
    public final static String ARG_BLOG_IMAGE = "blog_image";


    @Inject
    PostHandler postHandler;

    Post post;
    boolean liked = false;
    boolean favorited = false;

    MenuItem likeItem;
    MenuItem favItem;
    MenuItem shareItem;
    ShareActionProvider mShareActionProvider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final long postId = getIntent().getLongExtra(ARG_POST_ID, 0L);
        final String postImage = getIntent().getStringExtra(ARG_POST_IMAGE);
        final String postTitle = getIntent().getStringExtra(ARG_POST_TITLE);
        final String blogImage = getIntent().getStringExtra(ARG_BLOG_IMAGE);
        final String blogName = getIntent().getStringExtra(ARG_BLOG_NAME);
        final long publishDate = getIntent().getLongExtra(ARG_POST_DATE, 0L);

        ImageView blogImageV = (ImageView) findViewById(R.id.blogImage);
        TextView blogNameV = (TextView) findViewById(R.id.blogName);
        TextView publishDateV = (TextView) findViewById(R.id.publishDate);
        TextView postTitleV = (TextView) findViewById(R.id.postTitle);
        ImageView postImageV = (ImageView) findViewById(R.id.postImage);

        if(postImage != null) {
            Picasso.with(this)
                    .load(postImage)
                    .placeholder(R.drawable.pplaceholder)
                    .into(postImageV);
        }
        else{
            postImageV.setVisibility(View.GONE);
        }
        if(blogImage != null) {
            Picasso.with(this)
                    .load(blogImage)
                    .into(blogImageV);
        }
        publishDateV.setText(DateUtils.getRelativeTimeSpanString(publishDate));
        postTitleV.setText(postTitle);
        blogNameV.setText(blogName);

        setMainView(findViewById(R.id.postBody));
        setProgressView(findViewById(R.id.loading_progress));

        Toolbar toolbar = (Toolbar) findViewById(R.id.actins_toolbar);
        toolbar.inflateMenu(R.menu.post_actions);

        shareItem = toolbar.getMenu().findItem(R.id.action_share);
        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);

        // Set an OnMenuItemClickListener to handle menu item clicks

        toolbar.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // Handle the menu item
                        int id = item.getItemId();

                        if (id == R.id.action_like) {
                            if (liked) {
                                item.setIcon(R.drawable.ic_action_like);
                                postHandler.unlike(postId);
                                liked = false;
                            } else {
                                item.setIcon(R.drawable.ic_action_like_sel);
                                postHandler.like(postId);
                                liked = true;
                            }
                        }

                        if (id == R.id.action_fav) {
                            if (favorited) {
                                item.setIcon(R.drawable.ic_action_fav);
                                postHandler.removeFavroite(postId);
                                favorited = false;
                            } else {
                                item.setIcon(R.drawable.ic_action_fav_sel);
                                postHandler.favorite(postId);
                                favorited = true;
                            }
                        }
                        if (id == R.id.action_open_web) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(post.getLink()));
                            startActivity(intent);
                            // this was disabled for security reasons, to prevent external pages from
                            // emulating the app login page and tricking user to enter their credentials
//                            Intent intent = new Intent(PostActivity.this, ReadPostActivity.class);
  //                          intent.putExtra(ReadPostActivity.POST_URL_PARAM, post.getLink());
    //                        startActivity(intent);
                        }
                        return true;
                    }
                });

        likeItem = toolbar.getMenu().findItem(R.id.action_like);
        favItem = toolbar.getMenu().findItem(R.id.action_fav);

        showProgress(true);
        postHandler.fetchPost(postId);
    }

    @Subscribe
    public void onPostSuccess(PostSuccess event) {
        post = event.post;
        TextView publishDateV = (TextView) findViewById(R.id.publishDate);
        TextView postContent = (TextView) findViewById(R.id.postContent);

        publishDateV.setText(DateUtils.getRelativeTimeSpanString(post.getPublishedDate().getTime()));
        postContent.setText(post.getContent());

        if(post.getLiked()){
            liked = true;
            likeItem.setIcon(getResources().getDrawable(R.drawable.ic_action_like_sel));
        }
        if(post.getFavorited()){
            favorited = true;
            favItem.setIcon(getResources().getDrawable(R.drawable.ic_action_fav_sel));
        }
        setShareIntent();
        showProgress(false);
    }

    @Subscribe
    public void onPostFailure(PostFailure event) {
        Toast.makeText(this, R.string.unknown_error, Toast.LENGTH_LONG).show();
        showProgress(false);
    }

    @Subscribe
    public void onLikeSuccess(LikeSuccessEvent event) {
        //nothing??
    }

    @Subscribe
    public void onLikeFailure(LikeFailureEvent event) {
        Toast.makeText(this, R.string.unknown_error, Toast.LENGTH_LONG).show();
        if(liked){
            if(event.action == LikeSuccessEvent.Action.Like){
                likeItem.setIcon(getResources().getDrawable(R.drawable.ic_action_like));
                liked = false;
            }
        }
        else {
            if(event.action == LikeSuccessEvent.Action.UnLike){
                likeItem.setIcon(getResources().getDrawable(R.drawable.ic_action_like_sel));
                liked = true;
            }
        }
    }

    @Subscribe
    public void onFavSuccessEvent(FavSuccessEvent event) {
        //nothing??
    }

    @Subscribe
    public void onFavFailureEvent(FavFailureEvent event) {
        Toast.makeText(this, R.string.unknown_error, Toast.LENGTH_LONG).show();
        if(liked){
            if(event.action == FavSuccessEvent.Action.Favorite){
                favItem.setIcon(getResources().getDrawable(R.drawable.ic_action_fav));
                favorited = false;
            }
        }
        else {
            if(event.action == FavSuccessEvent.Action.Remove){
                favItem.setIcon(getResources().getDrawable(R.drawable.ic_action_fav_sel));
                favorited = true;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
            //noinspection SimplifiableIfStatement
        else if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setShareIntent() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, post.getTitle()+ " \n "+post.getLink());
        shareIntent.setType("text/plain");
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }
}
