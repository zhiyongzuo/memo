package com.example.zuo81.zztt.model;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zuo81.zztt.DetailActivity;
import com.example.zuo81.zztt.LoginActivity;
import com.example.zuo81.zztt.R;

import de.hdodenhof.circleimageview.CircleImageView;
import me.drakeet.multitype.ItemViewBinder;

import static com.example.zuo81.zztt.utils.ConstantHelper.APP_NAME;
import static com.example.zuo81.zztt.utils.ConstantHelper.LOGIN_NAME;
import static com.example.zuo81.zztt.utils.ConstantHelper.SHARED_PREFERENCE_NAME_LOGIN;
import static com.example.zuo81.zztt.utils.ConstantHelper.TYPE_CARD;

/**
 * Created by zuo81 on 2017/10/11.
 */
public class CardViewBinder extends ItemViewBinder<Card, CardViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_card_layout, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Card card) {
        holder.circleImageView.setImageBitmap(card.getBitmap());
        holder.textView.setText(card.getCardName());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView circleImageView;
        private TextView textView;
        private TextView textViewSwitch;
        Context mContext;

        ViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            circleImageView = itemView.findViewById(R.id.item_card_circle_image_view);
            textView = itemView.findViewById(R.id.item_card_text_view);
            textViewSwitch = itemView.findViewById(R.id.item_card_text_view_switch);
            itemView.setOnClickListener(onClickListener);
            textViewSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mContext.startActivity(new Intent(mContext, LoginActivity.class));
                }
            });
        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String loginName = view.getContext()
                        .getSharedPreferences(SHARED_PREFERENCE_NAME_LOGIN, Context.MODE_PRIVATE)
                        .getString(LOGIN_NAME, APP_NAME);
                if (loginName.equals(APP_NAME)) {
                    mContext.startActivity(new Intent(mContext, LoginActivity.class));
                }else {
                    mContext.startActivity(new Intent(mContext, DetailActivity.class));
                }
            }
        };
    }
}
