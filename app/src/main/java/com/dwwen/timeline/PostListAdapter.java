package com.dwwen.timeline;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dwwen.R;
import com.dwwen.blogs.BlogDetailActivity;
import com.dwwen.blogs.BlogDetailFragment;
import com.dwwen.model.Post;
import com.dwwen.post.PostActivity;
import com.squareup.picasso.Picasso;

import java.util.List;


public class PostListAdapter extends RecyclerView.Adapter<PostViewHolder> {

    protected final Context context;
    protected List<Post> posts;
    private boolean bigImages;

    public PostListAdapter(Context context, List<Post> posts){
        this.posts = posts;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    @Override
    public void onBindViewHolder(PostViewHolder postViewHolder, int i) {
        if(postViewHolder.getItemViewType() == 2){
            return;
        }
        Post post = posts.get(i);
        if(post.getImage() != null) {
            Picasso.with(context)
                    .load(post.getImage())
                    .placeholder(R.drawable.pplaceholder)
                    .error(R.drawable.pplaceholder)
                    .into(postViewHolder.postImage);
            postViewHolder.postImage.setVisibility(View.VISIBLE);
        }
        else {
            Picasso.with(context)
                    .load(R.drawable.sep)
                    .into(postViewHolder.postImage);
            postViewHolder.postImage.setVisibility(View.GONE);
        }

        Picasso.with(context)
                .load(post.getBlogObj().getImage())
                .placeholder(R.drawable.default_blog)
                .error(R.drawable.default_blog)
                .into(postViewHolder.blogImage);
        postViewHolder.publishDate.setText(DateUtils.getRelativeTimeSpanString(post.getPublishedDate().getTime()));
        postViewHolder.postTitle.setText(post.getTitle());
        postViewHolder.blogName.setText(post.getBlogObj().getName());
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
        else { //if(i == 1) small image
            itemView = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.post_small_image, viewGroup, false);
        }

        return new PostViewHolder(itemView, new PostViewHolder.PostClickHandler() {
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

                ActivityCompat.startActivity((Activity)context, intent, options.toBundle());

            }
        }, new PostViewHolder.PostClickHandler() {
            @Override
            public void onClick(View caller, int position) {
                Post post = posts.get(position);
                Intent detailIntent = new Intent(context, BlogDetailActivity.class);
                detailIntent.putExtra(BlogDetailFragment.ARG_ITEM_ID, post.getBlogObj().getId());
                context.startActivity(detailIntent);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if(posts.get(position).getId() == 0){
            return 2;
        }
        if(bigImages)
            return 0;
        else return 1;
    }

    public void add(Post post) {

        if(posts.size() == 5){
            posts.add(post);
            posts.add(new Post());
        }
        else if(posts.size() > 5){
            posts.add(posts.size()-1, post);
        }
        else{
            posts.add(post);
        }
        notifyItemInserted(posts.indexOf(post));
    }

    public class ProgressViewHolder extends PostViewHolder {
        public ProgressViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void done(){
        if(posts.size() > 5) {
            int pos = posts.size() - 1;
            if(posts.get(pos).getId() == 0) {
                posts.remove(pos);
                notifyItemRemoved(pos);
            }
        }
    }

    public void clear(){
        posts.clear();
        notifyDataSetChanged();
    }

    public boolean isBigImages() {
        return bigImages;
    }

    public void setBigImages(boolean bigImages) {
        this.bigImages = bigImages;
    }
}