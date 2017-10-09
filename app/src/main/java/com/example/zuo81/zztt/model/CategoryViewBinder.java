package com.example.zuo81.zztt.model;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zuo81.zztt.R;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by tomsdeath on 2017/8/14.
 */

public class CategoryViewBinder extends ItemViewBinder<Category, CategoryViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_super, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Category category) {
        holder.category.setText(category.text);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        private final TextView category;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.category = (TextView) itemView.findViewById(R.id.tv_group);
        }
    }
}
