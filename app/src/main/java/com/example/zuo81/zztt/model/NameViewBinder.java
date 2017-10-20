package com.example.zuo81.zztt.model;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zuo81.zztt.DetailActivity;
import com.example.zuo81.zztt.R;
import com.example.zuo81.zztt.ob.ObservableManager;
import com.example.zuo81.zztt.utils.DBUtils;
import com.example.zuo81.zztt.utils.LetterTileProvider;
import com.orhanobut.logger.Logger;

import de.hdodenhof.circleimageview.CircleImageView;
import me.drakeet.multitype.ItemViewBinder;

import static com.example.zuo81.zztt.MainActivity.COMPANYWORKERACTIVITY_COMPANY_NAME;
import static com.example.zuo81.zztt.utils.ConstantHelper.COMPANY_ITEM_CHANGE;
import static com.example.zuo81.zztt.utils.ConstantHelper.CONTACT_ITEM_CHANGE;
import static com.example.zuo81.zztt.utils.ConstantHelper.ITEM_CHANGE_COMPANY;
import static com.example.zuo81.zztt.utils.ConstantHelper.ITEM_DELETE_CONTACT;
import static org.litepal.LitePalApplication.getContext;


/**
 * Created by zuo81 on 2017/9/11.
 */
public class NameViewBinder extends ItemViewBinder<Name, NameViewBinder.ViewHolder> {
    private Context mContext;
    private Bitmap bitmap;
    LetterTileProvider mLetterTileProvider;

    public NameViewBinder(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_contact_company, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Name name) {
        String nameStr = name.getName();
        holder.mTextView.setText(nameStr);
        holder.textViewId.setText(String.valueOf(name.getId()));
        mLetterTileProvider = new LetterTileProvider(mContext);
        PhoneInfoModel phoneInfoModel = DBUtils.getPhoneInfoFromName(nameStr).get(0);
        String photoStr = phoneInfoModel.getPhoto();
        if (!TextUtils.isEmpty(photoStr)) {
            bitmap = DetailActivity.getLocalBitmap(photoStr);
        } else {
            bitmap = new LetterTileProvider(getContext()).getLetterTile(nameStr);
        }
        holder.mImageView.setImageBitmap(bitmap);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
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
                    Intent intent = new Intent(v.getContext(), DetailActivity.class);
                    intent.putExtra(COMPANYWORKERACTIVITY_COMPANY_NAME, mTextView.getText());
                    v.getContext().startActivity(intent);
                }
            });
            //能实现滑动删除吗
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle(R.string.contact).setMessage("   将要删除: " + mTextView.getText());
                    builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Logger.d(Long.parseLong(textViewId.getText().toString()) +"  " + getAdapterPosition());
                            DBUtils.deleteFromId(Long.parseLong(textViewId.getText().toString()));
                            Object notify = ObservableManager.newInstance()
                                    .notify(CONTACT_ITEM_CHANGE, ITEM_DELETE_CONTACT, getAdapterPosition());
                            Object notify2 = ObservableManager.newInstance()
                                    .notify(COMPANY_ITEM_CHANGE, ITEM_CHANGE_COMPANY);
                            Toast.makeText(view.getContext(), "删除成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setPositiveButton("取消", null);
                    builder.show();
                    return false;
                }
            });

        }
    }
}
