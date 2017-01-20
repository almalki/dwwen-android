package com.dwwen.blogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dwwen.R;
import com.dwwen.blogs.event.BlogFailure;
import com.dwwen.blogs.event.BlogSuccess;
import com.dwwen.blogs.event.FollowFailureEvent;
import com.dwwen.blogs.event.FollowSuccessEvent;
import com.dwwen.common.BaseFragment;
import com.dwwen.model.Blog;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

/**
 * A fragment representing a single Blog detail screen.
 * This fragment is either contained in a {@link BlogListActivity}
 * in two-pane mode (on tablets) or a {@link BlogDetailActivity}
 * on handsets.
 */
public class BlogDetailFragment extends BaseFragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    View view;

    /**
     * The dummy content this fragment is presenting.
     */
    private Integer blogId;
    private boolean followed;
    private FloatingActionButton followButton;

    @Inject
    BlogsHandler blogsHandler;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BlogDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            blogId = getArguments().getInt(ARG_ITEM_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_blog_detail, container, false);

        setMainView(rootView.findViewById(R.id.mainView));
        setProgressView(rootView.findViewById(R.id.loading_progress));

        followButton = (FloatingActionButton) rootView.findViewById(R.id.follow);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
    }

    @Override
    public void onResume() {
        super.onResume();
        showProgress(true);
        blogsHandler.fetchBlog(blogId);
    }

    @Subscribe
    public void onBlogSuccess(BlogSuccess event) {
        Blog blog = event.blog;
        followed = !blog.isFollowed();
        toggle();

        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(followed) {
                    blogsHandler.unFollowBlog(blogId);
                    toggle();
                }
                else{
                    blogsHandler.followBlog(blogId);
                    toggle();
                }
            }
        });

        TextView namev = (TextView) view.findViewById(R.id.blogName);
        ImageView imageV = (ImageView) view.findViewById(R.id.blogImage);
        TextView fcV = (TextView) view.findViewById(R.id.followersCount);
        TextView descV = (TextView) view.findViewById(R.id.blogDescription);
        namev.setText(blog.getName());
        fcV.setText(blog.getFollowersCount()+"");
        descV.setText(blog.getDescription());
        if(blog.getImage() != null) {
            Picasso.with(this.getActivity())
                    .load(blog.getImage())
                    .into(imageV);
        }
        if(isAdded()) {
            showProgress(false);
        }
    }

    @Subscribe
    public void onBlogFailure(BlogFailure event) {
        //TODO show Error
        System.out.println("got failure");
        if(isAdded()) {
            showProgress(false);
        }
    }

    @Subscribe
    public void onFollowSuccess(FollowSuccessEvent event) {
    }

    @Subscribe
    public void onFollowFailure(FollowFailureEvent event) {

        toggle();
        Toast.makeText(this.getActivity(), R.string.unknown_error, Toast.LENGTH_LONG).show();
    }

    private void toggle(){
        if(followed){
            followButton.setColorNormalResId(R.color.white);
            followButton.setIcon(R.drawable.ic_fab_star_off);
            followed = false;
        }
        else{
            followButton.setColorNormalResId(R.color.pink_pressed);
            followButton.setIcon(R.drawable.ic_fab_star);
            followed = true;
        }
    }

}
