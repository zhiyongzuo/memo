package com.example.zuo81.zztt.utils;

import android.os.Environment;

/**
 * Created by zuo81 on 2017/10/11.
 */

public class ConstantHelper {
    public static final String LOCAL_DOWNLOAD = "LOCAL_DOWNLOAD";
    public static final String LOCAL_UPLOAD = "LOCAL_UPLOAD";
    public static final String ME = "ME";
    public static final String START_DETAIL_ACTIVITY = "START_DETAIL_ACTIVITY";
    public static final String CONTACT_ITEM_CHANGE = "CONTACT_ITEM_CHANGE";
    public static final String COMPANY_ITEM_CHANGE = "COMPANY_ITEM_CHANGE";
    public static final String ITEM_ADD_CONTACT = "ITEM_ADD_CONTACT";
    public static final String ITEM_DELETE_CONTACT = "ITEM_DELETE_CONTACT";
    public static final String ITEM_CHANGE_COMPANY = "ITEM_CHANGE_COMPANY";
    public static final String APP_NAME = "OMC";
    public static final String SD_DIRECTORY_PATH = Environment.getExternalStorageDirectory() + "/" + APP_NAME;
    public static final String DATABASE_NAME = "connection.db";
    public static final String SD_DATABASE_PATH = SD_DIRECTORY_PATH + "/" + DATABASE_NAME;
    public static final String DATA_DATABASE_PATH = "/data/data/com.example.zuo81.zztt/databases/" + DATABASE_NAME;

    public static final String AEROVANE = "aerovane";
    public static final String CONTACT_AEROVANE = "CONTACT_AEROVANE";
    public static final String COMPANY_AEROVANE = "CONPANY_AEROVANE";
}
