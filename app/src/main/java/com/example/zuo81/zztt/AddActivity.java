package com.example.zuo81.zztt;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.zuo81.zztt.model.PhoneInfoModel;
import com.example.zuo81.zztt.ob.Function;
import com.example.zuo81.zztt.ob.ObservableManager;
import com.example.zuo81.zztt.utils.DBUtils;
import com.example.zuo81.zztt.utils.PhotoHelper;
import com.orhanobut.logger.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static com.example.zuo81.zztt.utils.ConstantHelper.COMPANY_ITEM_CHANGE;
import static com.example.zuo81.zztt.utils.ConstantHelper.ITEM_ADD_CONTACT;
import static com.example.zuo81.zztt.utils.ConstantHelper.CONTACT_ITEM_CHANGE;
import static com.example.zuo81.zztt.utils.ConstantHelper.ITEM_CHANGE_COMPANY;


public class AddActivity extends AppCompatActivity{
    private EditText et_name;
    private EditText et_phone;
    private EditText et_phone2;
    private EditText et_email;
    private EditText et_sex;
    private EditText et_company;
    private ImageView imageView;
    private String name = "";
    private String phone = "";
    private String phone2 = "";
    private String email = "";
    private String photo = "";
    private String sex = "";
    private String company = "";
    private String imgPath = "";
    private List<String> acquaintance = null;
    private List<Function> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        et_name = (EditText) findViewById(R.id.et_name);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_phone2 = (EditText) findViewById(R.id.et_phone2);
        et_email = (EditText) findViewById(R.id.et_email);
        et_sex = (EditText) findViewById(R.id.et_sex);
        et_company = (EditText) findViewById(R.id.et_company);
        imageView=(ImageView)findViewById(R.id.edit_imageView);
        list = new ArrayList<>();
    }

    public void sure(View v) {
        name = et_name.getText().toString().trim();
        phone = et_phone.getText().toString().trim();
        phone2 = et_phone2.getText().toString().trim();
        email = et_email.getText().toString().trim();
        sex = et_sex.getText().toString().trim();
        company = et_company.getText().toString().trim();
        photo=imgPath;
        if (name.equals("") || phone.equals(""))
            Toast.makeText(AddActivity.this, "姓名和手机号1必须填", Toast.LENGTH_SHORT).show();
        else {
            List<PhoneInfoModel> phoneInfoList = DBUtils.getAllPhoneInfo();
            List<String> nameList = new ArrayList<>();
            for(int i=0; i<phoneInfoList.size(); i++) {
                nameList.add(phoneInfoList.get(i).getName());
            }
            if(!nameList.contains(name)) {
                PhoneInfoModel phoneInfoModel = new PhoneInfoModel();
                phoneInfoModel.setName(name);
                phoneInfoModel.setPhoneNumber(phone);
                phoneInfoModel.setPhoneNumber2(phone2);
                phoneInfoModel.setEmail(email);
                phoneInfoModel.setSex(sex);
                phoneInfoModel.setCompany(company);
                phoneInfoModel.setPhoto(photo);
                DBUtils.savePhoneInfo(phoneInfoModel);
                Logger.d(DBUtils.getAllPhoneInfo().size());
                //因为viewpager加tablayout生命周期问题可能不会刷新，所以如下处理， 而不是放入fragment的onStrart()中刷新
                //一定要有两个，否则contactFragment companyFragment会让companyFragment通知不到
                Object notify = ObservableManager.newInstance()
                        .notify(CONTACT_ITEM_CHANGE, ITEM_ADD_CONTACT, name, phoneInfoModel.getId());
                Object notify2 = ObservableManager.newInstance()
                        .notify(COMPANY_ITEM_CHANGE, ITEM_CHANGE_COMPANY);
                Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, name + "  已存在,不能重复添加", Toast.LENGTH_SHORT).show();
            }

        }
    }
    /**
     * 点击头像选择系统图库或调用相机来选择图片
     *
     * @param view
     */

    public void selectPhoto(View view) {
        //如果版本>=Android6.0并且检查自身权限没有被赋予时，请求权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
        } else {
            //点击打开相册
            PhotoHelper.selectMyPhotoFormGallery(this, PhotoHelper.REQUEST_LOAD_PHOTO_PICKED);
        }
    }
    //处理权限申请回调
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == 2) {
            // 用户取消授权这个数组为空，如果你同时申请两个权限，那么grantResults的length就为2，分别记录你两个权限的申请结果
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //点击打开相册
                PhotoHelper.selectMyPhotoFormGallery(this, PhotoHelper.REQUEST_LOAD_PHOTO_PICKED);
            } else {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!this.shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        //用户已经完全拒绝，或手动关闭了权限开启此对话框缓解一下尴尬...
                        AlertDialog dialog = new AlertDialog.Builder(this)
                                .setMessage("不开启该权限将无法正常工作，请在设置中手动开启！")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        getAppDetailSettingIntent(AddActivity.this);
                                    }
                                })
                                .setNegativeButton("取消", null).create();
                        dialog.show();

                    } else {
                        //用户一直拒绝并一直不勾选“不再提醒”
                        //不执行该权限对应功能模块，也不用提示，因为下次需要权限还会弹出对话框
                    }
                }
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //以下代码可以跳转到应用详情，可以通过应用详情跳转到权限界面(6.0系统测试可用)
    private void getAppDetailSettingIntent(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        startActivity(localIntent);
    }
    /**
     * 在onActivityResult中实现裁剪功能,并把图片显示出来
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PhotoHelper.REQUEST_LOAD_PHOTO_PICKED:
                    //imgPath 为裁剪后保存的图片的路径
                    imgPath = PhotoHelper.doCropPhoto(this, data.getData(), PhotoHelper.REQUEST_PHOTO_CROP, true);
                    break;
                case PhotoHelper.REQUEST_PHOTO_CROP:
                    //在这里显示或处理裁剪后的照片
                    Bitmap bitmap = getLoacalBitmap(imgPath); //从本地取图片
                    imageView.setImageBitmap(bitmap);    //设置Bitmap
                    break;
            }
        }
    }
    /**
     * 加载本地图片
     *
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            if (!url.equals("")) {
                FileInputStream fis = new FileInputStream(url);
                return BitmapFactory.decodeStream(fis);
            }
            else return null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
