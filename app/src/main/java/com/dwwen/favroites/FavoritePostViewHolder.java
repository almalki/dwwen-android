package com.dwwen.favroites;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dwwen.R;
import com.dwwen.timeline.PostViewHolder;

/**
 * Created by abdulaziz on 12/26/2014.
 */
public class FavoritePostViewHolder extends PostViewHolder {

    public FavoritePostViewHolder(View itemView, PostClickHandler clickHandler) {
        super(itemView);
        this.clickHandler = clickHandler;
        publishDate = (TextView) itemView.findViewById(R.id.publishDate);
        blogName = (TextView) itemView.findViewById(R.id.blogName);
        postTitle = (TextView) itemView.findViewById(R.id.postTitle);
        postImage = (ImageView)itemView.findViewById(R.id.postImage);
        blogImage = (ImageView)itemView.findViewById(R.id.blogImage);
        View content = (View)itemView.findViewById(R.id.postInfo);
        View blogInfo = (View)itemView.findViewById(R.id.blogInfo);
        content.setOnClickListener(this);
    }

}
