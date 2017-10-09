package com.example.zuo81.zztt.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zuo81.zztt.DetailActivity;
import com.example.zuo81.zztt.R;
import com.example.zuo81.zztt.model.ChipsEntity;
import com.example.zuo81.zztt.model.PhoneInfo;
import com.example.zuo81.zztt.ui.OnRemoveListener;
import com.example.zuo81.zztt.utils.DBUtils;
import com.example.zuo81.zztt.utils.LetterTileProvider;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.zuo81.zztt.MainActivity.COMPANYWORKERACTIVITY_COMPANY_NAME;


public class ChipsAdapter extends  RecyclerView.Adapter<ChipsAdapter.ViewHolder> {

    private List<ChipsEntity> chipsEntities;
    private OnRemoveListener onRemoveListener;
    private boolean isShowingPosition;
    private Context context;
    private List<PhoneInfo> list;
    private LetterTileProvider mLetterTileProvider;

    public ChipsAdapter(List<ChipsEntity> chipsEntities, OnRemoveListener onRemoveListener) {
        this.chipsEntities = chipsEntities;
        this.onRemoveListener = onRemoveListener;
    }

    public ChipsAdapter(List<ChipsEntity> chipsEntities, OnRemoveListener onRemoveListener, boolean isShowingPosition) {
        this.chipsEntities = chipsEntities;
        this.onRemoveListener = onRemoveListener;
        this.isShowingPosition = isShowingPosition;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Logger.addLogAdapter(new AndroidLogAdapter());
        context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chip, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindItem(chipsEntities.get(position));
    }

    @Override
    public int getItemCount() {
        if(chipsEntities!=null) {
            return chipsEntities.size();
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDescription;
        private CircleImageView ivPhoto;
        private TextView tvName;
        private ImageButton ibClose;
        private TextView tvPosition;

        ViewHolder(View itemView) {
            super(itemView);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            ivPhoto = (CircleImageView) itemView.findViewById(R.id.ivPhoto);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            ibClose = (ImageButton) itemView.findViewById(R.id.ibClose);
            tvPosition = (TextView) itemView.findViewById(R.id.tvPosition);
        }

        void bindItem(ChipsEntity entity) {
            itemView.setTag(entity.getName());
            if (TextUtils.isEmpty(entity.getDescription())) {
                tvDescription.setVisibility(View.GONE);
            } else {
                tvDescription.setVisibility(View.VISIBLE);
                tvDescription.setText(entity.getDescription());
            }

       /*     if (entity.getDrawableResId() != 0) {
                ivPhoto.setVisibility(View.VISIBLE);
                Glide.with(ivPhoto.getContext()).load(entity.getDrawableResId())
                        .transform(new CircleTransform(ivPhoto.getContext())).into(ivPhoto);
            } else {
                ivPhoto.setVisibility(View.GONE);
            }
*/
            mLetterTileProvider = new LetterTileProvider(context);
            ivPhoto.setImageBitmap(mLetterTileProvider.getLetterTile(entity.getName()));

            tvName.setText(entity.getName());

            if (isShowingPosition) {
                tvPosition.setText(String.valueOf(getAdapterPosition()));
            }

            tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = tvName.getText().toString();
                    Logger.d(s);
                    list = DBUtils.getPhoneInfoFromName(s);
                    if (list!=null && list.size()>0 && list.get(0).getName().equals(s)) {
                        Intent intent = new Intent(context, DetailActivity.class);
                        intent.putExtra(COMPANYWORKERACTIVITY_COMPANY_NAME, tvName.getText().toString());
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "没有此联系人信息", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            ibClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onRemoveListener != null && getAdapterPosition() != -1) {
                        onRemoveListener.onItemRemoved(getAdapterPosition());
                    }
                }
            });
        }
    }
}
