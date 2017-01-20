package com.dwwen.blogs;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dwwen.R;
import com.dwwen.model.Category;

import java.util.List;


public class CategoriesListAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    private final Context context;
    private List<Category> categories;

    public CategoriesListAdapter(Context context, List<Category> categories){
        this.categories = categories;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder catViewHolder, int i) {
        Category cat = categories.get(i);
        catViewHolder.categoryName.setText(cat.getName());

    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final Category cat = categories.get(i);
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.category, viewGroup, false);
        return new CategoryViewHolder(itemView, new CategoryViewHolder.CategoryClickListener(){
            @Override
            public void onCategoryClicked(View view, int position) {
                Intent intent = new Intent(context, BlogListActivity.class);
                Category category = categories.get(position);
                intent.putExtra(BlogListFragment.STATE_CATEGORY_ID, category.getId());
                context.startActivity(intent);
            }
        });
    }
}