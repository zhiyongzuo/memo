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
import com.example.zuo81.zztt.model.NameViewBinder;
import com.example.zuo81.zztt.model.PhoneInfoModel;
import com.example.zuo81.zztt.ob.Function;
import com.example.zuo81.zztt.ob.ObservableManager;
import com.example.zuo81.zztt.utils.DBUtils;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

import static com.example.zuo81.zztt.utils.ConstantHelper.ITEM_ADD_CONTACT;
import static com.example.zuo81.zztt.utils.ConstantHelper.ITEM_CHANGE_CONTACT;
import static com.example.zuo81.zztt.utils.ConstantHelper.ITEM_DELETE_CONTACT;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment implements Function {
    public static int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private SearchView searchView;
    private RecyclerView rv;
    private MultiTypeAdapter multiTypeAdapter;
    private LinearLayoutManager linearLayoutManager;
    private Items items;
    private Items items2;

    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ObservableManager.newInstance().removeObserver(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.addLogAdapter(new AndroidLogAdapter());
        ObservableManager.newInstance().registerObserver(ITEM_CHANGE_CONTACT, this);
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
        multiTypeAdapter.register(Name.class, new NameViewBinder(getContext()));
        rv.setAdapter(multiTypeAdapter);
        items = new Items();
        List<PhoneInfoModel> list = DBUtils.getAllPhoneInfo();
        Logger.d(list.size());
        for(int i=0; i<list.size(); i++) {
            items.add(new Name(list.get(i).getName(), list.get(i).getId()));
        }
        multiTypeAdapter.setItems(items);
        multiTypeAdapter.notifyDataSetChanged();
        return view;
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

    @Override
    public Object function(Object[] data) {
        Logger.d(data);
        items = new Items(items);
        String s = (String)Arrays.asList(data).get(0);
        switch(s) {
            case ITEM_ADD_CONTACT:
                items.add(new Name((String)Arrays.asList(data).get(1), (long)Arrays.asList(data).get(2)));
                multiTypeAdapter.setItems(items);
                multiTypeAdapter.notifyDataSetChanged();
                int i = items.size() - 1;
                rv.scrollToPosition(i);
                break;
            case ITEM_DELETE_CONTACT:
                int position = (int)Arrays.asList(data).get(1);
                Logger.d(position);
                items.remove(position);
                multiTypeAdapter.setItems(items);
                multiTypeAdapter.notifyDataSetChanged();
                /*multiTypeAdapter.getItems().remove(Arrays.asList(data).get(1));//这两行会导致item立刻删掉，但把item滑动出屏幕后再滑回原位置，被删除的item又出现了
                multiTypeAdapter.notifyItemRemoved((int)Arrays.asList(data).get(1));*/
                break;
            case ITEM_CHANGE_CONTACT:
                onDestroy();
                break;
            default:
                break;
        }
        return null;
    }
}
