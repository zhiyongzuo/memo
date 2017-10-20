package com.example.zuo81.zztt.model;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zuo81.zztt.R;

import de.hdodenhof.circleimageview.CircleImageView;
import me.drakeet.multitype.ItemViewBinder;

import static com.example.zuo81.zztt.utils.ConstantHelper.ACTION_LOCAL_DOWNLOAD;
import static com.example.zuo81.zztt.utils.ConstantHelper.ACTION_LOCAL_UPLOAD;

/**
 * Created by zuo81 on 2017/10/18.
 */
public class Card2ViewBinder extends ItemViewBinder<Card, Card2ViewBinder.ViewHolder> {
    private View.OnClickListener onClickListener;
    private View.OnClickListener onClickListener2;

    public Card2ViewBinder(View.OnClickListener onClickListener, View.OnClickListener onClickListener2) {
        this.onClickListener = onClickListener;
        this.onClickListener2 = onClickListener2;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_card2_layout, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Card card) {
        String cardName = card.getCardName();
        holder.circleImageView.setImageBitmap(card.getBitmap());
        holder.textView.setText(cardName);
        //holder.setAction(card.getAction());
        String actionStr = card.getAction();
        if(!TextUtils.isEmpty(actionStr) && actionStr.equals(ACTION_LOCAL_UPLOAD)) {
            holder.itemView.setOnClickListener(onClickListener);
        } else if(!TextUtils.isEmpty(actionStr) && actionStr.equals(ACTION_LOCAL_DOWNLOAD)) {
            holder.itemView.setOnClickListener(onClickListener2);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView circleImageView;
        private TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.item_card2_circle_image_view);
            textView = itemView.findViewById(R.id.item_card2_text_view);
        }
    }
}
