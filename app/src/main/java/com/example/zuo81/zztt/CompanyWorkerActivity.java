package com.example.zuo81.zztt;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.zuo81.zztt.model.Category;
import com.example.zuo81.zztt.model.CategoryViewBinder;
import com.example.zuo81.zztt.model.Name;
import com.example.zuo81.zztt.model.NameViewBinder;
import com.example.zuo81.zztt.model.PhoneInfo;
import com.example.zuo81.zztt.utils.DBUtils;
import com.example.zuo81.zztt.utils.Eyes;

import java.util.List;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

import static com.example.zuo81.zztt.MainActivity.COMPANYWORKERACTIVITY_COMPANY_NAME;

public class CompanyWorkerActivity extends AppCompatActivity {
    private RecyclerView rv;
    private MultiTypeAdapter adapter;
    private Items mItems;
    private int id;
    private String company;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_worker);
        Intent intent = getIntent();
        company = intent.getStringExtra(COMPANYWORKERACTIVITY_COMPANY_NAME);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        rv = (RecyclerView)findViewById(R.id.cw_rv);
        setSupportActionBar(toolbar);

        AppBarLayout appbarLayout = (AppBarLayout)findViewById(R.id.app_bar);
        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        toolbarLayout.setTitle(company);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(CompanyWorkerActivity.this, AddActivity.class);
                mIntent.putExtra(COMPANYWORKERACTIVITY_COMPANY_NAME, company);
                startActivity(mIntent);
            }
        });

        Eyes.setStatusBarColorForCollapsingToolbar(this, appbarLayout, toolbarLayout, toolbar, ContextCompat.getColor(this, R.color.colorPrimary));
    }

    @Override
    protected void onStart() {
        super.onStart();
        List<PhoneInfo> list = DBUtils.getPhoneInfoFromCompany(company);
        adapter = new MultiTypeAdapter();
        adapter.register(Category.class, new CategoryViewBinder());
        adapter.register(Name.class, new NameViewBinder(CompanyWorkerActivity.this));
        rv.setLayoutManager(new LinearLayoutManager(CompanyWorkerActivity.this));
        //rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mItems = new Items();
        mItems.add(new Category("单位成员"));
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getCompany() !=null && !list.get(i).getCompany().equals("")) {
                    int index = list.get(i).getCompany().indexOf(company);
                    if (index != -1) {
                        mItems.add(new Name(list.get(i).getName(), list.get(i).getId()));
                    }
                }
            }
        }
        adapter.setItems(mItems);
        rv.setAdapter(adapter);
    }
}
