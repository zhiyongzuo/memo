package com.example.zuo81.zztt.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zuo81.zztt.R;
import com.example.zuo81.zztt.model.Card;
import com.example.zuo81.zztt.model.CardViewBinder;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

import static com.example.zuo81.zztt.utils.ConstantHelper.LOCAL_DOWNLOAD;
import static com.example.zuo81.zztt.utils.ConstantHelper.LOCAL_UPLOAD;
import static com.example.zuo81.zztt.utils.ConstantHelper.ME;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment {
    private MultiTypeAdapter multiTypeAdapter;
    private Items items;
    private RecyclerView rvMe;

    public MeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        multiTypeAdapter = new MultiTypeAdapter();
        multiTypeAdapter.register(Card.class, new CardViewBinder());
        items = new Items();
        items.add(new Card(R.drawable.me, "我", ME));
        items.add(new Card(R.drawable.local_backup, "备份", LOCAL_UPLOAD));
        items.add(new Card(R.drawable.local_download, "回滚至上次备份（应用将会重启）", LOCAL_DOWNLOAD));
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

}
