package com.example.zuo81.zztt;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.zuo81.zztt.adapter.MyFragmentPagerAdapter;
import com.example.zuo81.zztt.fragment.CompanyFragment;
import com.example.zuo81.zztt.fragment.ContactFragment;
import com.example.zuo81.zztt.fragment.MeFragment;
import com.example.zuo81.zztt.utils.PhotoHelper;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.example.zuo81.zztt.model.ClickableViewHolder.SD_DIRECTORY_PATH;


public class MainActivity extends AppCompatActivity {
    public static String COMPANYWORKERACTIVITY_COMPANY_NAME = "COMPANY_WORKER_ACTIVITY_COMPANY_NAME";
    private List<Fragment> list;
    private MyFragmentPagerAdapter adapter;
    private long firstTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        Logger.addLogAdapter(new AndroidLogAdapter());
        File directoryFile = new File(SD_DIRECTORY_PATH);
        if(!directoryFile.exists()) {
            directoryFile.mkdir();
        }

        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        if(list == null) {
            list = new ArrayList<>();
        }
        list.add(new ContactFragment());
        list.add(new CompanyFragment());
        list.add(new MeFragment());
        adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), list);
        viewPager.setAdapter(adapter);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("姓名");//标题不会需要手动添加才显示。必须在setupWithViewPager之后添加
        tabLayout.getTabAt(1).setText("单位");
        tabLayout.getTabAt(2).setText("我");
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.canDrawOverlays(MainActivity.this)) {
                        Toast.makeText(MainActivity.this, "该软件需要悬浮窗权限，请授予！", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse("package:" + getPackageName()));
                        startActivityForResult(intent, 10);
                    } else {
                        Intent intent = new Intent(MainActivity.this, AddActivity.class);
                        startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(MainActivity.this, AddActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    Toast.makeText(this, "权限授予失败，无法开启悬浮窗", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "权限授予成功！", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(this, AddActivity.class);
                    startActivity(intent);
                }
            }

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            long secondTime = System.currentTimeMillis();
            if(secondTime - firstTime < 2000) {
                //System.exit(0);
                finish();
            } else {
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                firstTime = System.currentTimeMillis();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
