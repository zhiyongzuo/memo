package com.example.zuo81.zztt.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.example.zuo81.zztt.R;
import com.example.zuo81.zztt.adapter.ChipsAdapter;
import com.example.zuo81.zztt.model.ChipsEntity;
import com.example.zuo81.zztt.ui.OnRemoveListener;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import static com.example.zuo81.zztt.DetailActivity.CLASSMATES;
import static com.example.zuo81.zztt.DetailActivity.DETAIL_ACTIVITY_NAME;


/**
 */
public class ClassMatesFragment extends Fragment implements View.OnClickListener {
    private static final String EXTRA = "data";
    private RecyclerView rvTest;
    private EditText editText;
    private Button button;
    private RecyclerView.Adapter adapter;
    private String name;
    List<ChipsEntity> chipsList = new ArrayList<>();
    private static int i = 1;

    //@RestrictTo(RestrictTo.Scope.SUBCLASSES)
    public ClassMatesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_armyfriend, container, false);
        rvTest = (RecyclerView)view.findViewById(R.id.rvTest);
        button = (Button)view.findViewById(R.id.btnInsert);
        editText = (EditText)view.findViewById(R.id.et);
        button.setOnClickListener(this);

        Bundle bundle = getArguments();
        if(bundle != null) {
            name = (String)bundle.get(DETAIL_ACTIVITY_NAME);
        }
      /*  album = DataSupport.where("name=?", name).find(Album.class).get(0);
        if(album==null) {
            album = new Album();
            album.setName(name);
            //album.save();
        }*/

        chipsList  = DataSupport.where("resource=?and father=?", CLASSMATES, name).find(ChipsEntity.class);
        Logger.d("oncreate mList" + chipsList + "," + chipsList.size());
        if(chipsList == null) {
            chipsList = new ArrayList<>();
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new ChipsAdapter(chipsList, onRemoveListener);

        ChipsLayoutManager spanLayoutManager = ChipsLayoutManager.newBuilder(getContext())
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .build();

        if (rvTest != null) {
            rvTest.addItemDecoration(new SpacingItemDecoration(getResources().getDimensionPixelOffset(R.dimen.item_space),
                    getResources().getDimensionPixelOffset(R.dimen.item_space)));
        }

        rvTest.setLayoutManager(spanLayoutManager);
        rvTest.getRecycledViewPool().setMaxRecycledViews(0, 10);
        rvTest.getRecycledViewPool().setMaxRecycledViews(1, 10);
        rvTest.setAdapter(adapter);
    }

    private OnRemoveListener onRemoveListener = new OnRemoveListener() {
        @Override
        public void onItemRemoved(int position) {
            DataSupport.deleteAll(ChipsEntity.class, "name=? and resource=? and father=? and repeatStr=?", chipsList.get(position).getName(), chipsList.get(position).getResource(), chipsList.get(position).getFather(), String.valueOf(chipsList.get(position).getRepeatStr()));
            chipsList.remove(position);
            Log.i("activity", "delete at " + position);
            Logger.d("listener" + chipsList.size() + chipsList);
            adapter.notifyItemRemoved(position);
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onClick(View v) {
        String s = editText.getText().toString();
        if (!s.equals("")) {
            ChipsEntity chipsEntity = new ChipsEntity();
            chipsEntity.setName(s);
            chipsEntity.setResource(CLASSMATES);
            chipsEntity.setFather(name);
            chipsEntity.setRepeatStr(i++);

            if(chipsList!=null) {
                chipsList.add(chipsEntity);
            } else {
                chipsList = new ArrayList<>();
                chipsList.add(chipsEntity);
            }
            adapter.notifyDataSetChanged();

            DataSupport.saveAll(chipsList);
        }
    }
}
