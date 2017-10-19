package com.example.zuo81.zztt.model;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zuo81.zztt.DetailActivity;
import com.example.zuo81.zztt.LoginActivity;
import com.example.zuo81.zztt.R;
import com.example.zuo81.zztt.utils.RestartAPPTool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import static com.example.zuo81.zztt.utils.ConstantHelper.APP_NAME;
import static com.example.zuo81.zztt.utils.ConstantHelper.LOGIN_NAME;
import static com.example.zuo81.zztt.utils.ConstantHelper.LOCAL_DOWNLOAD;
import static com.example.zuo81.zztt.utils.ConstantHelper.LOCAL_UPLOAD;
import static com.example.zuo81.zztt.utils.ConstantHelper.ME;
import static com.example.zuo81.zztt.utils.ConstantHelper.SHARED_PREFERENCE_NAME_LOGIN;

/**
 * Created by zuo81 on 2017/10/11.
 */

public class ClickableViewHolder extends RecyclerView.ViewHolder {
    public static String SD_DIRECTORY_PATH = Environment.getExternalStorageDirectory() + "/" + APP_NAME;
    private String ACTION;

    public void setAction(String ACTION) {
        this.ACTION = ACTION;
    }

    public ClickableViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context mContext = view.getContext();
                String DATABASE_NAME = mContext.getSharedPreferences(SHARED_PREFERENCE_NAME_LOGIN, Context.MODE_PRIVATE).getString(LOGIN_NAME, APP_NAME) + ".db";
                String SD_DATABASE_PATH = SD_DIRECTORY_PATH + "/" + DATABASE_NAME;
                String DATA_DATABASE_PATH = "/data/data/com.example.zuo81.zztt/databases/" + DATABASE_NAME;
                if(ACTION != null) {
                    switch(ACTION) {
                        //copy db
                        case LOCAL_UPLOAD:
                            deleteOutputFileAndCreateInputFile(SD_DATABASE_PATH, DATA_DATABASE_PATH);
                            Toast.makeText(view.getContext(), "已备份至本地", Toast.LENGTH_SHORT).show();
                            break;
                        case LOCAL_DOWNLOAD:
                            deleteOutputFileAndCreateInputFile(DATA_DATABASE_PATH, SD_DATABASE_PATH);
                            RestartAPPTool.restartAPP(view.getContext(), 0);
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    public void deleteOutputFileAndCreateInputFile(String OUTPUT_PATH, String INPUT_PATH) {
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

            /*File inputDBFile = new File(INPUT_PATH);
            InputStream ips = new FileInputStream(inputDBFile);
            OutputStream ops = new FileOutputStream(outputDBFile);
            byte[] bytes = new byte[1024];
            while(ips.read(bytes) > 0) {
                ops.write(bytes);
            }
            ips.close();
            ops.close();*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
