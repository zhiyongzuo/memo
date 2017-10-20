package com.example.zuo81.zztt.fragment;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.zuo81.zztt.DetailActivity;
import com.example.zuo81.zztt.R;
import com.example.zuo81.zztt.model.Card;
import com.example.zuo81.zztt.model.CardViewBinder;
import com.example.zuo81.zztt.model.Card2ViewBinder;
import com.example.zuo81.zztt.model.PhoneInfoModel;
import com.example.zuo81.zztt.utils.DBUtils;
import com.example.zuo81.zztt.utils.LetterTileProvider;
import com.example.zuo81.zztt.utils.RestartAPPTool;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import me.drakeet.multitype.ClassLinker;
import me.drakeet.multitype.ItemViewBinder;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import static android.content.Context.MODE_PRIVATE;
import static com.example.zuo81.zztt.utils.ConstantHelper.APP_NAME;
import static com.example.zuo81.zztt.utils.ConstantHelper.ACTION_LOCAL_DOWNLOAD;
import static com.example.zuo81.zztt.utils.ConstantHelper.ACTION_LOCAL_UPLOAD;
import static com.example.zuo81.zztt.utils.ConstantHelper.LOGIN_NAME;
import static com.example.zuo81.zztt.utils.ConstantHelper.ME;
import static com.example.zuo81.zztt.utils.ConstantHelper.SD_DIRECTORY_PATH;
import static com.example.zuo81.zztt.utils.ConstantHelper.SHARED_PREFERENCE_NAME_LOGIN;
import static com.example.zuo81.zztt.utils.ConstantHelper.TYPE_CARD;

/**
 * A simple {@link Fragment} subclass.
 */
@RuntimePermissions
public class MeFragment extends Fragment{
    private MultiTypeAdapter multiTypeAdapter;
    private Items items;
    private RecyclerView rvMe;
    private Bitmap bitmap;
    private String ACTION;

    public MeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        multiTypeAdapter = new MultiTypeAdapter();
        multiTypeAdapter.register(Card.class)
                .to(new CardViewBinder(), new Card2ViewBinder(onClickListener, onClickListener2))
                .withClassLinker(new ClassLinker<Card>() {
                    @NonNull
                    @Override
                    public Class<? extends ItemViewBinder<Card, ?>> index(@NonNull Card card) {
                        if(card.type == TYPE_CARD) {
                            return CardViewBinder.class;
                        } else {
                            return Card2ViewBinder.class;
                        }
                    }
                });

        items = new Items();
        items.add(new Card(BitmapFactory.decodeResource(getResources(), R.drawable.me), ME, ME, TYPE_CARD));
        items.add(new Card(BitmapFactory.decodeResource(getResources(), R.drawable.local_backup), "备份", ACTION_LOCAL_UPLOAD));
        items.add(new Card(BitmapFactory.decodeResource(getResources(), R.drawable.local_download), "回滚至上次备份(app自动重启)", ACTION_LOCAL_DOWNLOAD));
        multiTypeAdapter.setItems(items);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_me, container, false);
        rvMe = view.findViewById(R.id.rv_me);

        rvMe.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMe.setAdapter(multiTypeAdapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Logger.d("onStart");
        String name = getContext().getSharedPreferences(SHARED_PREFERENCE_NAME_LOGIN, MODE_PRIVATE).getString(LOGIN_NAME, "");
        if (!TextUtils.isEmpty(name)) {
            PhoneInfoModel phoneInfoModel = DBUtils.getPhoneInfoFromName(name).get(0);
            String photoStr = phoneInfoModel.getPhoto();
            if (!TextUtils.isEmpty(photoStr)) {
                bitmap = DetailActivity.getLocalBitmap(photoStr);
            } else {
                bitmap = new LetterTileProvider(getContext()).getLetterTile(name);
            }
            items.set(0, new Card(bitmap, name, ME, TYPE_CARD));
            multiTypeAdapter.setItems(items);
            multiTypeAdapter.notifyDataSetChanged();
        }
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void operateExternalStorage() {
        Context mContext = getContext();
        String DATABASE_NAME = mContext.getSharedPreferences(SHARED_PREFERENCE_NAME_LOGIN, Context.MODE_PRIVATE).getString(LOGIN_NAME, APP_NAME) + ".db";
        String SD_DATABASE_PATH = SD_DIRECTORY_PATH + "/" + DATABASE_NAME;
        String DATA_DATABASE_PATH = "/data/data/com.example.zuo81.zztt/databases/" + DATABASE_NAME;
        if(ACTION != null) {
            switch(ACTION) {
                //copy db
                case ACTION_LOCAL_UPLOAD:
                    deleteOutputFileAndCreateInputFile(SD_DATABASE_PATH, DATA_DATABASE_PATH);
                    Toast.makeText(mContext, "已备份至本地", Toast.LENGTH_SHORT).show();
                    break;
                case ACTION_LOCAL_DOWNLOAD:
                    deleteOutputFileAndCreateInputFile(DATA_DATABASE_PATH, SD_DATABASE_PATH);
                    RestartAPPTool.restartAPP(mContext, 0);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MeFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnShowRationale({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onShowRationale(final PermissionRequest request) {
        new AlertDialog.Builder(getContext())
                .setPositiveButton("allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("denied", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage("确定打开Storage权限？")
                .show();
    }

    @OnPermissionDenied({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onPermissionDenied() {
        Toast.makeText(getContext(), "Storage权限拒绝", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onNeverAskAgain() {
        Toast.makeText(getContext(), "在设置-应用-当前应用打开Storage权限", Toast.LENGTH_SHORT).show();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ACTION = ACTION_LOCAL_UPLOAD;
            MeFragmentPermissionsDispatcher.operateExternalStorageWithPermissionCheck(MeFragment.this);
        }
    };
    View.OnClickListener onClickListener2 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ACTION = ACTION_LOCAL_DOWNLOAD;
            MeFragmentPermissionsDispatcher.operateExternalStorageWithPermissionCheck(MeFragment.this);
        }
    };


    public void deleteOutputFileAndCreateInputFile(String OUTPUT_PATH, String INPUT_PATH) {
        File directoryFile = new File(SD_DIRECTORY_PATH);
        if(!directoryFile.exists()) {
            directoryFile.mkdirs();
        }

        File outputDBFile = new File(OUTPUT_PATH);
        try {
            if(outputDBFile.exists()) {
                outputDBFile.delete();
            }
            outputDBFile.createNewFile();

            FileChannel inChannel = new FileInputStream(new File(INPUT_PATH)).getChannel();
            FileChannel outChannel = new FileOutputStream(new File(OUTPUT_PATH)).getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
            inChannel.close();
            outChannel.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
