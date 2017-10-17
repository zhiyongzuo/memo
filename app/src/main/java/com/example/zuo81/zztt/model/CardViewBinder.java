package com.example.zuo81.zztt.model;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zuo81.zztt.R;

import de.hdodenhof.circleimageview.CircleImageView;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by zuo81 on 2017/10/11.
 */
public class CardViewBinder extends ItemViewBinder<Card, CardViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_card, parent, false);
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
            circleImageView = itemView.findViewById(R.id.item_card_circle_image_view);
            textView = itemView.findViewById(R.id.item_card_text_view);
        }
    }
}
