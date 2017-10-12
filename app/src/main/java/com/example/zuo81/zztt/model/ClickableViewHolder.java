package com.example.zuo81.zztt.model;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.zuo81.zztt.ADActivity;
import com.example.zuo81.zztt.MainActivity;
import com.example.zuo81.zztt.ob.ObservableManager;
import com.example.zuo81.zztt.utils.RestartAPPTool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

import static com.example.zuo81.zztt.utils.ConstantHelper.DATA_DATABASE_PATH;
import static com.example.zuo81.zztt.utils.ConstantHelper.ITEM_CHANGE_COMPANY;
import static com.example.zuo81.zztt.utils.ConstantHelper.ITEM_CHANGE_CONTACT;
import static com.example.zuo81.zztt.utils.ConstantHelper.ITEM_DELETE_CONTACT;
import static com.example.zuo81.zztt.utils.ConstantHelper.SD_DATABASE_PATH;
import static com.example.zuo81.zztt.utils.ConstantHelper.SD_DIRECTORY_PATH;
import static com.example.zuo81.zztt.utils.ConstantHelper.LOCAL_DOWNLOAD;
import static com.example.zuo81.zztt.utils.ConstantHelper.LOCAL_UPLOAD;
import static com.example.zuo81.zztt.utils.ConstantHelper.ME;

/**
 * Created by zuo81 on 2017/10/11.
 */

public class ClickableViewHolder extends RecyclerView.ViewHolder {
    private String ACTION;

    public void setAction(String ACTION) {
        this.ACTION = ACTION;
    }

    public ClickableViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                            /*Object notify = ObservableManager.newInstance()
                                    .notify(ITEM_CHANGE_CONTACT, ITEM_CHANGE_CONTACT);
                            Object notify2 = ObservableManager.newInstance()
                                    .notify(ITEM_CHANGE_COMPANY, ITEM_CHANGE_COMPANY);*/
                            //Toast.makeText(view.getContext(), "已还原至上次备份", Toast.LENGTH_SHORT).show();
                            break;
                        case ME:
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
