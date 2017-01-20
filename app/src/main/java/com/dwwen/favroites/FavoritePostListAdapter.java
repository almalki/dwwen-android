package com.dwwen.favroites;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dwwen.R;
import com.dwwen.model.Post;
import com.dwwen.post.PostActivity;
import com.dwwen.timeline.PostListAdapter;
import com.dwwen.timeline.PostViewHolder;

import java.util.List;

/**
 * Created by abdulaziz on 12/26/2014.
 */
public class FavoritePostListAdapter extends PostListAdapter {
    public FavoritePostListAdapter(Context context, List<Post> posts) {
        super(context, posts);
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if(i == 2){
            View itemView = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.progress, viewGroup, false);
            return new ProgressViewHolder(itemView);
        }
        final View itemView;
        if(i == 0){
            itemView = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.post, viewGroup, false);
        }
        else {//if(i == 1) { if small image
            itemView = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.post_small_image, viewGroup, false);
        }

        return new FavoritePostViewHolder(itemView, new PostViewHolder.PostClickHandler() {
            @Override
            public void onClick(View caller, int position) {
                Post post = posts.get(position);
                Intent intent = new Intent(context, PostActivity.class);
                intent.putExtra(PostActivity.ARG_POST_ID, post.getId());
                intent.putExtra(PostActivity.ARG_POST_TITLE, post.getTitle());
                intent.putExtra(PostActivity.ARG_POST_IMAGE, post.getImage());
                intent.putExtra(PostActivity.ARG_BLOG_IMAGE, post.getBlogObj().getImage());
                intent.putExtra(PostActivity.ARG_BLOG_NAME, post.getBlogObj().getName());
                intent.putExtra(PostActivity.ARG_POST_DATE, post.getPublishedDate().getTime());

                Pair<View, String> titlePair = new Pair<View, String>(itemView.findViewById(R.id.postTitle), "transition_post_title");
                Pair<View, String> imagePair = new Pair<View, String>(itemView.findViewById(R.id.postImage), "transition_post_image");
                Pair<View, String> publishDatePair = new Pair<View, String>(itemView.findViewById(R.id.publishDate), "transition_post_date");
                Pair<View, String> blogNamePair = new Pair<View, String>(itemView.findViewById(R.id.blogName), "transition_post_blog_name");
                Pair<View, String> blogImagePair = new Pair<View, String>(itemView.findViewById(R.id.blogImage), "transition_post_blog_image");

                ActivityOptionsCompat options;
                if(post.getImage() != null) {
                    options =
                            ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,
                                    new Pair[]{titlePair, imagePair, publishDatePair, blogImagePair, blogNamePair}  // The view which starts the transition
                                    // The transitionName of the view we’re transitioning to
                            );
                }
                else {
                    options =
                            ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,
                                    new Pair[]{titlePair, publishDatePair, blogImagePair, blogNamePair}  // The view which starts the transition
                                    // The transitionName of the view we’re transitioning to
                            );
                }
                ActivityCompat.startActivity((Activity) context, intent, options.toBundle());
            }});
    }
}
