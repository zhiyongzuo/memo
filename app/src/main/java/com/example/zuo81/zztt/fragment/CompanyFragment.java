package com.example.zuo81.zztt.fragment;


import android.database.sqlite.SQLiteDatabase;
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

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

import static com.example.zuo81.zztt.utils.ConstantHelper.ITEM_CHANGE_COMPANY;


/**
 * A simple {@link Fragment} subclass.
 */
public class CompanyFragment extends Fragment implements Function {
    private SearchView searchView;
    private RecyclerView rv;
    private MultiTypeAdapter multiTypeAdapter;
    private Items items2;
    private LinearLayoutManager linearLayoutManager;

    public CompanyFragment() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ObservableManager.newInstance().removeObserver(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ObservableManager.newInstance().registerObserver(ITEM_CHANGE_COMPANY, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_contact, container, false);
        searchView = view.findViewById(R.id.search_view);
        rv = view.findViewById(R.id.rv_fragment_contact);

        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(onQueryTextListener);
        searchView.setQueryHint("请输入查询姓名");

        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        linearLayoutManager.setStackFromEnd(true);
        rv.setLayoutManager(linearLayoutManager);
        multiTypeAdapter = new MultiTypeAdapter();
        multiTypeAdapter.register(Name.class, new Name2ViewBinder(getContext()));
        rv.setAdapter(multiTypeAdapter);
        init();
        return view;
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
    public Object function(Object[] data) {
        String s = (String)data[0];
        if(s!=null && s.equals(ITEM_CHANGE_COMPANY)) {
            SQLiteDatabase db = LitePal.getDatabase();
            Logger.d("db");
        }
        init();
        return null;
    }

    SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            Map<String, Long> map = new LinkedHashMap<>();
            List<PhoneInfoModel> list = DBUtils.getAllPhoneInfo();
            for (int i=0; i<list.size(); i++) {
                String s = list.get(i).getName();
                if (s!=null && !s.equals("") && s.contains(newText)) {
                    map.put(list.get(i).getName(), list.get(i).getId());
                }
            }
            items2 = new Items();
            for(Object oj : map.keySet()) {
                String s = (String)oj;
                items2.add(new Name(s, map.get(s)));
            }
            multiTypeAdapter.setItems(items2);
            multiTypeAdapter.notifyDataSetChanged();
            return false;
        }
    };
}
