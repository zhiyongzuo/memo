package com.example.zuo81.zztt.model;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zuo81.zztt.R;

import de.hdodenhof.circleimageview.CircleImageView;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by zuo81 on 2017/10/18.
 */
public class Card2ViewBinder extends ItemViewBinder<Card, Card2ViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_card2_layout, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Card card) {
        holder.circleImageView.setImageBitmap(card.getBitmap());
        holder.textView.setText(card.getCardName());
        holder.setAction(card.getAction());
    }

    static class ViewHolder extends ClickableViewHolder {
        private CircleImageView circleImageView;
        private TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.item_card2_circle_image_view);
            textView = itemView.findViewById(R.id.item_card2_text_view);
        }
    }
}
