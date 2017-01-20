package com.dwwen.blogs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dwwen.R;
import com.dwwen.model.Blog;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by abdulaziz on 12/11/2014.
 */
public class BlogAdapter extends ArrayAdapter<Blog> {

    public BlogAdapter(Context context,
                       List<Blog> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Blog blog = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.blog_brief, parent, false);
        }
        TextView namev = (TextView) convertView.findViewById(R.id.blogName);
        ImageView imageV = (ImageView) convertView.findViewById(R.id.blogImage);
        TextView fcV = (TextView) convertView.findViewById(R.id.followersCount);
        namev.setText(blog.getName());
        fcV.setText(blog.getFollowersCount()+"");
        Picasso.with(getContext())
                .load(blog.getImage())
                .placeholder(R.drawable.default_blog)
                .error(R.drawable.default_blog)
                .into(imageV);

        return convertView;
    }

}
