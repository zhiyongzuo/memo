package com.example.zuo81.zztt;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.zuo81.zztt.model.PhoneInfoModel;
import com.example.zuo81.zztt.utils.DBUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ADActivity extends AppCompatActivity {
    public int x;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_ad);

        if(getActionBar()!=null && getActionBar().isShowing()){
            getActionBar().hide();
        }

        x = getSharedPreferences("a", MODE_PRIVATE).getInt("x", 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 2);
        } else {
            init();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode) {
            case 2:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    init();
                }
                break;
        }
    }

    private void init() {
        if (x == 0) {
            Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            String phoneNumber;     //context.get获取通讯录。query查询Cursor为数据类型
            String phoneName;
            List<PhoneInfoModel> list = new ArrayList<>();
            List<String> nameList = new ArrayList<>();
            while (cursor.moveToNext()) {
                PhoneInfoModel phoneInfoModel = new PhoneInfoModel();
                phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                phoneName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                phoneInfoModel.setName(phoneName);
                phoneInfoModel.setPhoneNumber(phoneNumber);
                if (!nameList.contains(phoneName)) {
                    nameList.add(phoneName);
                    list.add(phoneInfoModel);
                }
            }
            Logger.d("onRequestPermissionResult" + list.size());
            DBUtils.saveAll(list);
            x = 2;
            getSharedPreferences("a", MODE_PRIVATE).edit().putInt("x", x).apply();
        }
        startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }
}
