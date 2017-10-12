package com.example.zuo81.zztt.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.example.zuo81.zztt.R;
import com.example.zuo81.zztt.adapter.ChipsAdapter;
import com.example.zuo81.zztt.model.ChipsModel;
import com.example.zuo81.zztt.model.PhoneInfoModel;
import com.example.zuo81.zztt.ui.OnRemoveListener;
import com.example.zuo81.zztt.utils.DBUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.example.zuo81.zztt.DetailActivity.DETAIL_ACTIVITY_NAME;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BasicRelationFragment extends Fragment implements View.OnClickListener  {
    private static final String EXTRA = "data";
    private RecyclerView rv;
    private EditText editText;
    private Button button;
    private RecyclerView.Adapter adapter;
    private String name;
    List<ChipsModel> chipsList;
    List<String> nameList;
    String relation;

    public abstract String getRelation();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        relation = getRelation();
        View view = inflater.inflate(R.layout.fragment_armyfriend, container, false);
        rv = (RecyclerView)view.findViewById(R.id.rvTest);
        button = (Button)view.findViewById(R.id.btnInsert);
        editText = (EditText)view.findViewById(R.id.et);
        button.setOnClickListener(this);

        Bundle bundle = getArguments();
        if(bundle != null) {
            name = (String)bundle.get(DETAIL_ACTIVITY_NAME);
        }
        return view;
    }
//除了放入onStart（）， 好像没有其它方法能及时更新了。。。
    @Override
    public void onStart() {
        super.onStart();
        chipsList  = DBUtils.findChipsModel(relation, name);
        Logger.d("oncreate mList" + chipsList + "," + chipsList.size());
        if(chipsList == null) {
            chipsList = new ArrayList<>();
        }

        adapter = new ChipsAdapter(chipsList, onRemoveListener);

        ChipsLayoutManager spanLayoutManager = ChipsLayoutManager.newBuilder(getContext())
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .build();

        if (rv != null) {
            rv.addItemDecoration(new SpacingItemDecoration(getResources().getDimensionPixelOffset(R.dimen.item_space),
                    getResources().getDimensionPixelOffset(R.dimen.item_space)));
        }

        rv.setLayoutManager(spanLayoutManager);
       /* rv.getRecycledViewPool().setMaxRecycledViews(0, 10);
        rv.getRecycledViewPool().setMaxRecycledViews(1, 10);*/
        rv.setAdapter(adapter);
    }

    private OnRemoveListener onRemoveListener = new OnRemoveListener() {
        @Override
        public void onItemRemoved(int position) {
            String father = chipsList.get(position).getFather();
            String relation = chipsList.get(position).getResource();
            String name = chipsList.get(position).getName();
            DBUtils.deleteChipsModelFromId(chipsList.get(position).getId());
            chipsList.remove(position);
            Logger.d("listener" + chipsList.size() + chipsList);
            adapter.notifyItemRemoved(position);
            adapter.notifyDataSetChanged();
            //
            DBUtils.deleteChipsModelFromFRN(name, relation, father);
            Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onClick(View v) {
        String s = editText.getText().toString();
        if (!s.equals("")) {
            nameList = new ArrayList<>();
            for(int i=0; i<chipsList.size(); i++) {
                nameList.add(chipsList.get(i).getName());
            }
            editText.setText("");
            if(!nameList.contains(s)) {
                ChipsModel chipsModel = new ChipsModel();
                chipsModel.setName(s);
                chipsModel.setResource(relation);
                chipsModel.setFather(name);
                DBUtils.saveChipsModel(chipsModel);
                chipsList.add(chipsModel);
                adapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "添加成功", Toast.LENGTH_SHORT).show();
                //查找PhoneInfo中是否存在s， 若存在，往PhoneInfo的s中添加的ChipsModel name。相似鱼后台反向添加
                List<PhoneInfoModel> phoneInfoModelList = DBUtils.getPhoneInfoFromName(s);
                int size = phoneInfoModelList.size();
                if(size > 0) {
                    ChipsModel model = new ChipsModel();
                    model.setName(name);
                    model.setResource(relation);
                    model.setFather(s);
                    DBUtils.saveChipsModel(model);
                }
            } else {
                Toast.makeText(getContext(), "已存在  " + s + "  不能重复添加", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "输入框不能为空", Toast.LENGTH_SHORT).show();
        }
    }
}
