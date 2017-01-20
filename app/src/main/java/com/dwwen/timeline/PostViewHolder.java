package com.dwwen.timeline;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dwwen.R;

/**
 * Created by abdulaziz on 11/29/2014.
 */
public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public ImageView blogImage;
    public TextView publishDate;
    public TextView blogName;
    public TextView postTitle;
    public ImageView postImage;
    protected PostClickHandler clickHandler;

    public PostViewHolder(View itemView){
        super(itemView);
    }

    public PostViewHolder(View itemView, PostClickHandler clickHandler, final PostClickHandler blogClickHandler) {
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
        blogInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blogClickHandler.onClick(view, getPosition());
            }
        });
    }

    @Override
    public void onClick(View v) {
        clickHandler.onClick(v, getPosition());
    }

    public static interface PostClickHandler {
        public void onClick(View caller, int position);
    }
}
