package com.example.zuo81.zztt.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zuo81.zztt.R;
import com.example.zuo81.zztt.ob.Function;
import com.example.zuo81.zztt.ob.ObservableManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class BasicContactAndCompanyFragment extends Fragment {


    public BasicContactAndCompanyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //ObservableManager.newInstance().removeObserver(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_basic_contact_and_company, container, false);
    }

}
