package com.example.zuo81.zztt.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zuo81.zztt.R;
import com.example.zuo81.zztt.model.Name;
import com.example.zuo81.zztt.model.Name2ViewBinder;
import com.example.zuo81.zztt.model.PhoneInfoModel;
import com.example.zuo81.zztt.ob.Function;
import com.example.zuo81.zztt.ob.ObservableManager;
import com.example.zuo81.zztt.utils.DBUtils;
import com.orhanobut.logger.Logger;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

import static com.example.zuo81.zztt.utils.ConstantHelper.COMPANY_AEROVANE;
import static com.example.zuo81.zztt.utils.ConstantHelper.COMPANY_ITEM_CHANGE;
import static com.example.zuo81.zztt.utils.ConstantHelper.ITEM_CHANGE_COMPANY;


/**
 * A simple {@link Fragment} subclass.
 */
public class CompanyFragment extends BasicContactAndCompanyFragment {
    private MultiTypeAdapter multiTypeAdapter;

    @Override
    public void onDestroy() {
        super.onDestroy();
        ObservableManager.newInstance().removeObserver(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ObservableManager.newInstance().registerObserver(COMPANY_ITEM_CHANGE, this);
    }

    @Override
    public String getAerovane() {
        return COMPANY_AEROVANE;
    }

    @Override
    public void handleMultiTypeAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        linearLayoutManager.setStackFromEnd(true);
        rv.setLayoutManager(linearLayoutManager);
        multiTypeAdapter = new MultiTypeAdapter();
        multiTypeAdapter.register(Name.class, new Name2ViewBinder(getContext()));
        rv.setAdapter(multiTypeAdapter);
        init();
    }



    @Override
    public Object function(Object[] data) {
        String s = (String)Arrays.asList(data).get(0);
        switch(s) {
            case ITEM_CHANGE_COMPANY:
                init();
            break;
        }
        return null;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    private void init() {
        Items items = new Items();
        List companyList = new ArrayList<>();
        List<PhoneInfoModel> list = DBUtils.getAllPhoneInfo();
        Logger.d(list.size());
        for(int i=0; i<list.size(); i++) {
            String s = list.get(i).getCompany();
            if (s!=null && !s.equals("") && !companyList.contains(s)) {
                companyList.add(s);
                items.add(new Name(s));
            }
        }
        multiTypeAdapter.setItems(items);
        multiTypeAdapter.notifyDataSetChanged();
    }

    @Override
    public void handleOnQueryTextChange(String newText) {
        Map<String, Long> map = new LinkedHashMap<>();
        List<PhoneInfoModel> list = DBUtils.getAllPhoneInfo();
        for (int i=0; i<list.size(); i++) {
            String s = list.get(i).getName();
            if (s!=null && !s.equals("") && s.contains(newText)) {
                map.put(list.get(i).getName(), list.get(i).getId());
            }
        }
        Items items2 = new Items();
        for(Object oj : map.keySet()) {
            String s = (String)oj;
            items2.add(new Name(s, map.get(s)));
        }
        multiTypeAdapter.setItems(items2);
        multiTypeAdapter.notifyDataSetChanged();
    }
}
