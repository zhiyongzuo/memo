package com.example.zuo81.zztt.utils;

import com.example.zuo81.zztt.model.ChipsEntity;
import com.example.zuo81.zztt.model.PhoneInfo;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by zuo81 on 2017/9/25.
 */

public class DBUtils {

    public static List<ChipsEntity> getAllChipsEntity() {
        return DataSupport.findAll(ChipsEntity.class);
    }

    public static List<PhoneInfo> getAllPhoneInfo() {
        return DataSupport.findAll(PhoneInfo.class);
    }

    public static List<PhoneInfo> getPhoneInfoFromName(String name) {
        return DataSupport.where("name=?", name).find(PhoneInfo.class);
    }
    public static PhoneInfo getPhoneInfoFromId(long id) {
        return DataSupport.find(PhoneInfo.class, id);
    }

    public static List<PhoneInfo> getPhoneInfoFromCompany(String company) {
        return DataSupport.where("company=?", company).find(PhoneInfo.class);
    }

    public static void deleteFromName(String name) {
        DataSupport.deleteAll(PhoneInfo.class, "name=?", name);
    }

    public static void deleteFromId(long id) {
        DataSupport.delete(PhoneInfo.class, id);
    }

    public static void updateFromName(PhoneInfo phoneInfo, String name) {
        phoneInfo.updateAll("name=?", name);
    }

    public static void updateFromId(PhoneInfo phoneInfo, long id) {
        phoneInfo.update(id);
    }

    public static void saveAll(List<PhoneInfo> list) {
        DataSupport.saveAll(list);
    }

    public static void deleteRepeat() {

    }

    public static void savePhoneInfo(PhoneInfo phoneInfo) {
        phoneInfo.save();
    }
}
