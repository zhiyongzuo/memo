package com.example.zuo81.zztt.fragment;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zuo81.zztt.DetailActivity;
import com.example.zuo81.zztt.R;
import com.example.zuo81.zztt.model.Card;
import com.example.zuo81.zztt.model.CardViewBinder;
import com.example.zuo81.zztt.model.PhoneInfoModel;
import com.example.zuo81.zztt.ob.Function;
import com.example.zuo81.zztt.utils.DBUtils;
import com.example.zuo81.zztt.utils.LetterTileProvider;
import com.orhanobut.logger.Logger;

import java.util.Arrays;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

import static android.content.Context.MODE_PRIVATE;
import static com.example.zuo81.zztt.utils.ConstantHelper.LOCAL_DOWNLOAD;
import static com.example.zuo81.zztt.utils.ConstantHelper.LOCAL_UPLOAD;
import static com.example.zuo81.zztt.utils.ConstantHelper.LOGIN_NAME;
import static com.example.zuo81.zztt.utils.ConstantHelper.LOGIN_PASSWORD;
import static com.example.zuo81.zztt.utils.ConstantHelper.ME;
import static com.example.zuo81.zztt.utils.ConstantHelper.SHARED_PREFERENCE_NAME_LOGIN;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment{
    private MultiTypeAdapter multiTypeAdapter;
    private Items items;
    private RecyclerView rvMe;
    private Bitmap bitmap;

    public MeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        multiTypeAdapter = new MultiTypeAdapter();
        multiTypeAdapter.register(Card.class, new CardViewBinder());
        items = new Items();
        items.add(new Card(BitmapFactory.decodeResource(getResources(), R.drawable.me), ME, ME));
        items.add(new Card(BitmapFactory.decodeResource(getResources(), R.drawable.local_backup), "备份", LOCAL_UPLOAD));
        items.add(new Card(BitmapFactory.decodeResource(getResources(), R.drawable.local_download), "回滚至上次备份", LOCAL_DOWNLOAD));
        multiTypeAdapter.setItems(items);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_me, container, false);
        rvMe = view.findViewById(R.id.rv_me);

        rvMe.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMe.setAdapter(multiTypeAdapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Logger.d("onStart");
        String name = getContext().getSharedPreferences(SHARED_PREFERENCE_NAME_LOGIN, MODE_PRIVATE).getString(LOGIN_NAME, "");
        if (!TextUtils.isEmpty(name)) {
            PhoneInfoModel phoneInfoModel = DBUtils.getPhoneInfoFromName(name).get(0);
            String photoStr = phoneInfoModel.getPhoto();
            if (!TextUtils.isEmpty(photoStr)) {
                bitmap = DetailActivity.getLocalBitmap(photoStr);
            } else {
                bitmap = new LetterTileProvider(getContext()).getLetterTile(name);
            }
            items.set(0, new Card(bitmap, name, ME));
            multiTypeAdapter.setItems(items);
            multiTypeAdapter.notifyDataSetChanged();
        }
    }
}
