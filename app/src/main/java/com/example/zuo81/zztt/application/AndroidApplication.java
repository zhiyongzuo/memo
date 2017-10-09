package com.example.zuo81.zztt.application;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;

import org.litepal.LitePalApplication;

/**
 * Created by zuo81 on 2017/9/25.
 */

public class AndroidApplication extends LitePalApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Bugly.init(getApplicationContext(), "f2fe356ce0", false);
        //CrashReport.initCrashReport(getApplicationContext(), "f2fe356ce0", false);
    }
}
