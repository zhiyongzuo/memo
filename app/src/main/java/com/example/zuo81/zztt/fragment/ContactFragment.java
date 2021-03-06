package com.example.zuo81.zztt.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;

import com.example.zuo81.zztt.model.Name;
import com.example.zuo81.zztt.model.NameViewBinder;
import com.example.zuo81.zztt.model.PhoneInfoModel;
import com.example.zuo81.zztt.ob.ObservableManager;
import com.example.zuo81.zztt.utils.DBUtils;
import com.orhanobut.logger.Logger;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

import static com.example.zuo81.zztt.utils.ConstantHelper.CONTACT_AEROVANE;
import static com.example.zuo81.zztt.utils.ConstantHelper.ITEM_ADD_CONTACT;
import static com.example.zuo81.zztt.utils.ConstantHelper.CONTACT_ITEM_CHANGE;
import static com.example.zuo81.zztt.utils.ConstantHelper.ITEM_CHANGE_CONTACT;
import static com.example.zuo81.zztt.utils.ConstantHelper.ITEM_DELETE_CONTACT;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends BasicContactAndCompanyFragment {
    private MultiTypeAdapter multiTypeAdapter;
    private Items items;

    @Override
    public void onDestroy() {
        super.onDestroy();
        ObservableManager.newInstance().removeObserver(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ObservableManager.newInstance().registerObserver(CONTACT_ITEM_CHANGE, this);
    }

    @Override
    public String getAerovane() {
        return CONTACT_AEROVANE;
    }

    @Override
    public void handleMultiTypeAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        linearLayoutManager.setStackFromEnd(true);
        rv.setLayoutManager(linearLayoutManager);
        multiTypeAdapter = new MultiTypeAdapter();
        multiTypeAdapter.register(Name.class, new NameViewBinder(getContext()));
        rv.setAdapter(multiTypeAdapter);
        initialRV();
    }

    private void initialRV() {
        items = new Items();
        List<PhoneInfoModel> list = DBUtils.getAllPhoneInfo();
        Logger.d(list.size());
        for(int i=0; i<list.size(); i++) {
            items.add(new Name(list.get(i).getName(), list.get(i).getId()));
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
                initialRV();
                break;
            default:
                break;
        }
        return null;
    }
}
