package com.example.zuo81.zztt.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.zuo81.zztt.R;
import com.example.zuo81.zztt.ob.Function;

import static com.example.zuo81.zztt.utils.ConstantHelper.COMPANY_AEROVANE;
import static com.example.zuo81.zztt.utils.ConstantHelper.CONTACT_AEROVANE;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BasicContactAndCompanyFragment extends Fragment implements Function {
    private SearchView mSearchView;
    public RecyclerView rv;

    public BasicContactAndCompanyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_contact_company, container, false);
        mSearchView = view.findViewById(R.id.search_view);
        rv = view.findViewById(R.id.rv_fragment_contact);

        ImageView mSearchIcon= mSearchView.findViewById(R.id.search_button);
        mSearchIcon.setImageResource(R.drawable.search_long);

        mSearchView.setIconifiedByDefault(true);
        mSearchView.setOnQueryTextListener(onQueryTextListener);
        String aerovane = getAerovane();
        if (aerovane!=null && aerovane.equals(CONTACT_AEROVANE)) {
            mSearchView.setQueryHint("请输入查询姓名");
        } else if(aerovane!=null && aerovane.equals(COMPANY_AEROVANE)) {
            mSearchView.setQueryHint("请输入查询单位");
        }
        handleMultiTypeAdapter();
        return view;
    }

    public abstract String getAerovane();

    public abstract void handleMultiTypeAdapter();

    public abstract void handleOnQueryTextChange(String newText);

    SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            handleOnQueryTextChange(newText);
            return true;
        }
    };

}
