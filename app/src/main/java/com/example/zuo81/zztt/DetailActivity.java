package com.example.zuo81.zztt;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.zuo81.zztt.fragment.ArmyFriendRelationFragment;
import com.example.zuo81.zztt.fragment.DetailInfoFragment;
import com.example.zuo81.zztt.fragment.ClassMatesRelationFragment;
import com.example.zuo81.zztt.fragment.FamilyRelationFragment;
import com.example.zuo81.zztt.fragment.FellowTownsManRelationFragment;
import com.example.zuo81.zztt.fragment.FriendRelationFragment;
import com.example.zuo81.zztt.ob.ObservableManager;
import com.example.zuo81.zztt.utils.Eyes;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.zuo81.zztt.model.PhoneInfoModel;
import com.example.zuo81.zztt.utils.DBUtils;
import com.example.zuo81.zztt.utils.PhotoHelper;
import com.orhanobut.logger.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import static com.ashokvarma.bottomnavigation.BottomNavigationBar.MODE_FIXED;
import static com.example.zuo81.zztt.MainActivity.COMPANYWORKERACTIVITY_COMPANY_NAME;
import static com.example.zuo81.zztt.utils.ConstantHelper.APP_NAME;
import static com.example.zuo81.zztt.utils.ConstantHelper.CONTACT_ITEM_CHANGE;
import static com.example.zuo81.zztt.utils.ConstantHelper.ITEM_CHANGE_CONTACT;
import static com.example.zuo81.zztt.utils.ConstantHelper.LOGIN_NAME;
import static com.example.zuo81.zztt.utils.ConstantHelper.PUT_EXTRA_NAME_EDIT_ACTIVITY;
import static com.example.zuo81.zztt.utils.ConstantHelper.REQUEST_CODE_GET_EDIT_NAME;
import static com.example.zuo81.zztt.utils.ConstantHelper.REQUEST_CODE_READ_EXTERNAL_STORAGE;
import static com.example.zuo81.zztt.utils.ConstantHelper.SHARED_PREFERENCE_NAME_LOGIN;


public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    private BottomNavigationBar mBottomNavigationBar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private Toolbar toolbar;
    private View view;
    private PopupWindow mPopupWindow;
    private LinearLayout mLinearLayout;
    private TextView mChangeTextView;
    private EditText mEditTextChangeName;
    private Button mButtonSure;
    private ImageView imageView;
    private String imgPath = "";
    private Bundle bundle;
    private String name;
    private long id;
    private String photo = "";
    private FragmentTransaction ft;
    public static final String DETAIL_ACTIVITY_NAME = "BASIC";
    public static final String DETAIL_ACTIVITY_ID = "id";
    public static final String ARMYFRIEDS = "armyfriends";
    public static final String FRIEDS = "FRIEDS";
    public static final String CLASSMATES = "CLASSMATES";
    public static final String FAMILY = "FAMILY";
    public static final String FELLOWTOWNSMAN = "FELLOWTOWNSMAN";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mBottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        imageView = (ImageView) findViewById(R.id.edit_imageView);
        AppBarLayout mAppBarLayout = (AppBarLayout)findViewById(R.id.app_bar);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);

        Intent intent = getIntent();
        name = intent.getStringExtra(COMPANYWORKERACTIVITY_COMPANY_NAME);
        if(TextUtils.isEmpty(name)) {
            name = getSharedPreferences(SHARED_PREFERENCE_NAME_LOGIN, Context.MODE_PRIVATE).getString(LOGIN_NAME, APP_NAME);
        }
        List<PhoneInfoModel> list = DBUtils.getPhoneInfoFromName(name);
        if(list!=null && list.size()>0) {
            photo = list.get(0).getPhoto();
            id = list.get(0).getId();
        }
        Bitmap bitmap = getLocalBitmap(photo); //根据路径从本地取图片
        imageView.setImageBitmap(bitmap);    //设置Bitmap
        imageView.setOnClickListener(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        Eyes.setStatusBarColorForCollapsingToolbar(this, mAppBarLayout, mCollapsingToolbarLayout, toolbar, ContextCompat.getColor(this, R.color.colorPrimary));

        view = LayoutInflater.from(this).inflate(R.layout.pop, null);
        mChangeTextView = view.findViewById(R.id.pop_change_img_text_view);
        mLinearLayout = view.findViewById(R.id.linear_layout_for_edit_text_and_button_popup_window);
        mEditTextChangeName = view.findViewById(R.id.edit_text_for_change_name);
        mButtonSure = view.findViewById(R.id.button_for_change_name);
        mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        int color = Color.rgb(255, 255, 255);
        ColorDrawable dw = new ColorDrawable(color);
        mPopupWindow.setBackgroundDrawable(dw);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);

        ft = getSupportFragmentManager().beginTransaction();
        final DetailInfoFragment mDetailInfoFragment = new DetailInfoFragment();
        bundle = new Bundle();
        bundle.putString(DETAIL_ACTIVITY_NAME, name);
        mDetailInfoFragment.setArguments(bundle);
        ft.replace(R.id.frame_layout, mDetailInfoFragment).commit();
        mBottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.basic, "基本信息"))
                .addItem(new BottomNavigationItem(R.drawable.army_friend, "战友"))
                .addItem(new BottomNavigationItem(R.drawable.friend, "朋友"))
                .addItem(new BottomNavigationItem(R.drawable.classmates, "同学"))
                .addItem(new BottomNavigationItem(R.drawable.family, "家人"))
                .addItem(new BottomNavigationItem(R.drawable.fellowtownsman, "老乡"))
                .setMode(MODE_FIXED)
                .initialise();
        mBottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                switch(position) {
                    case 0:
                        ft.replace(R.id.frame_layout, mDetailInfoFragment).commit();
                        break;
                    case 1:
                        ArmyFriendRelationFragment afFragment = new ArmyFriendRelationFragment();
                        afFragment.setArguments(bundle);
                        ft.replace(R.id.frame_layout, afFragment).commit();
                        break;
                    case 2:
                        FriendRelationFragment fFragment = new FriendRelationFragment();
                        fFragment.setArguments(bundle);
                        ft.replace(R.id.frame_layout, fFragment).commit();
                        break;
                    case 3:
                        ClassMatesRelationFragment cmFragment = new ClassMatesRelationFragment();
                        cmFragment.setArguments(bundle);
                        ft.replace(R.id.frame_layout, cmFragment).commit();
                        break;
                    case 4:
                        FamilyRelationFragment familyFragment = new FamilyRelationFragment();
                        familyFragment.setArguments(bundle);
                        ft.replace(R.id.frame_layout, familyFragment).commit();
                        break;
                    case 5:
                        FellowTownsManRelationFragment ftmFragment = new FellowTownsManRelationFragment();
                        ftmFragment.setArguments(bundle);
                        ft.replace(R.id.frame_layout, ftmFragment).commit();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(int position) {}

            @Override
            public void onTabReselected(int position) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.d(name);
        mCollapsingToolbarLayout.setTitle(name);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()) {
            case R.id.action_change_name:
                startActivityForResult(new Intent(DetailActivity.this, EditActivity.class), REQUEST_CODE_GET_EDIT_NAME);
                /*mChangeTextView.setVisibility(View.GONE);
                mLinearLayout.setVisibility(View.VISIBLE);
                showPopWindow();*/
                break;
            case android.R.id.home:
                onBackPressed();
            default:
                break;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        mChangeTextView.setVisibility(View.VISIBLE);
        mLinearLayout.setVisibility(View.GONE);
        showPopWindow();
    }

    private void showPopWindow(){
        mPopupWindow.showAtLocation(findViewById(R.id.activity_detail_coordinatorLayout), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.7f;
        getWindow().setAttributes(params);
        mChangeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                        && ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DetailActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_READ_EXTERNAL_STORAGE);
                } else {
                    //点击打开相册
                    PhotoHelper.selectMyPhotoFormGallery(DetailActivity.this, PhotoHelper.REQUEST_LOAD_PHOTO_PICKED);
                }
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 1f;
                getWindow().setAttributes(params);
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 1f;
                getWindow().setAttributes(params);
                mPopupWindow.dismiss();
            }
        });
    }

    /**
     * 加载本地图片
     *
     * @param url
     * @return
     */
    public static Bitmap getLocalBitmap(String url) {
        try {
            if (url != null && !url.equals("")) {
                FileInputStream fis = new FileInputStream(url);
                return BitmapFactory.decodeStream(fis);
            } else return null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    //处理权限申请回调
    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            // 用户取消授权这个数组为空，如果你同时申请两个权限，那么grantResults的length就为2，分别记录你两个权限的申请结果
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //同意授权时。。。。。
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!this.shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                        //用户已经完全拒绝，或手动关闭了权限开启此对话框缓解一下尴尬...
                        android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(this)
                                .setMessage("不开启该权限将无法正常工作，请在设置中手动开启！")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        getAppDetailSettingIntent(DetailActivity.this);
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
        } else if(requestCode == REQUEST_CODE_READ_EXTERNAL_STORAGE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                PhotoHelper.selectMyPhotoFormGallery(this, PhotoHelper.REQUEST_LOAD_PHOTO_PICKED);
            } else {
                if(!this.shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog dialog = new AlertDialog.Builder(this)
                            .setMessage("不开启该权限将无法正常工作，请在设置中手动开启！")
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    getAppDetailSettingIntent(DetailActivity.this);
                                }
                            }).create();
                    dialog.show();
                }
            }
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
                    Bitmap bitmap = getLocalBitmap(imgPath); //从本地取图片
                    imageView.setImageBitmap(bitmap);    //设置Bitmap
                    PhoneInfoModel phoneInfoModel = new PhoneInfoModel();
                    phoneInfoModel.setPhoto(imgPath);
                    DBUtils.updateFromId(phoneInfoModel, id);
                    break;
                case REQUEST_CODE_GET_EDIT_NAME:
                    String s = data.getStringExtra(PUT_EXTRA_NAME_EDIT_ACTIVITY);
                    PhoneInfoModel infoModel = new PhoneInfoModel();
                    infoModel.setName(s);
                    DBUtils.updateFromId(infoModel, id);
                    name = s;
                    Object notify = ObservableManager.newInstance()
                            .notify(CONTACT_ITEM_CHANGE, ITEM_CHANGE_CONTACT);
                    break;
                default:
                    break;
            }
        }
    }

}
