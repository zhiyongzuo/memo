package com.example.zuo81.zztt.model;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zuo81.zztt.CompanyWorkerActivity;
import com.example.zuo81.zztt.DetailActivity;
import com.example.zuo81.zztt.R;
import com.example.zuo81.zztt.utils.LetterTileProvider;

import de.hdodenhof.circleimageview.CircleImageView;
import me.drakeet.multitype.ItemViewBinder;

import static com.example.zuo81.zztt.MainActivity.COMPANYWORKERACTIVITY_COMPANY_NAME;
import static com.example.zuo81.zztt.MainActivity.COMPANYWORKERACTIVITY_ID;
import static com.example.zuo81.zztt.MainActivity.COMPANYWORKERACTIVITY_POSITION;


/**
 * Created by zuo81 on 2017/9/11.
 */
public class NameViewBinder extends ItemViewBinder<Name, NameViewBinder.ViewHolder> {
    private Context mContext;
    private LetterTileProvider mLetterTileProvider;
    private long id;

    public NameViewBinder(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.rv_company, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Name name) {
        holder.mTextView.setText(name.getName());
        holder.textViewId.setText(String.valueOf(name.getId()));
        mLetterTileProvider = new LetterTileProvider(mContext);
        holder.mImageView.setImageBitmap(mLetterTileProvider.getLetterTile(name.getName()));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;
        private TextView textViewId;
        private CircleImageView mImageView;

        ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView)itemView.findViewById(R.id.name);
            mImageView = (CircleImageView)itemView.findViewById(R.id.imageView);
            textViewId = itemView.findViewById(R.id.id);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra(COMPANYWORKERACTIVITY_COMPANY_NAME, mTextView.getText());
                    intent.putExtra(COMPANYWORKERACTIVITY_ID, Long.parseLong(textViewId.getText().toString()));
                    intent.putExtra(COMPANYWORKERACTIVITY_POSITION, getAdapterPosition());
                    mContext.startActivity(intent);
                }
            });


        }
    }
}
