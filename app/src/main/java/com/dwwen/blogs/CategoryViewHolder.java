package com.dwwen.blogs;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.dwwen.R;

/**
 * Created by abdulaziz on 11/29/2014.
 */
public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView categoryName;
    public CategoryClickListener listener;

    public CategoryViewHolder(View itemView, CategoryClickListener listener) {
        super(itemView);
        this.listener = listener;
        categoryName = (TextView) itemView.findViewById(R.id.categoryName);
        categoryName.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        listener.onCategoryClicked(view, getPosition());
    }

    public static interface CategoryClickListener{
        public void onCategoryClicked(View view, int position);
    }
}
