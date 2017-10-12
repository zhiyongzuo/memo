package com.example.zuo81.zztt.utils;

import com.example.zuo81.zztt.model.ChipsModel;
import com.example.zuo81.zztt.model.PhoneInfoModel;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by zuo81 on 2017/9/25.
 */

public class DBUtils {

    public static List<ChipsModel> getAllChipsEntity() {
        return DataSupport.findAll(ChipsModel.class);
    }

    public static List<PhoneInfoModel> getAllPhoneInfo() {
        return DataSupport.findAll(PhoneInfoModel.class);
    }

    public static List<PhoneInfoModel> getPhoneInfoFromName(String name) {
        return DataSupport.where("name=?", name).find(PhoneInfoModel.class);
    }
    public static PhoneInfoModel getPhoneInfoFromId(long id) {
        return DataSupport.find(PhoneInfoModel.class, id);
    }

    public static List<PhoneInfoModel> getPhoneInfoFromCompany(String company) {
        return DataSupport.where("company=?", company).find(PhoneInfoModel.class);
    }

    public static void deleteFromName(String name) {
        DataSupport.deleteAll(PhoneInfoModel.class, "name=?", name);
    }

    public static void deleteFromId(long id) {
        DataSupport.delete(PhoneInfoModel.class, id);
    }

    public static void updateFromName(PhoneInfoModel phoneInfoModel, String name) {
        phoneInfoModel.updateAll("name=?", name);
    }

    public static void updateFromId(PhoneInfoModel phoneInfoModel, long id) {
        phoneInfoModel.update(id);
    }

    public static void saveAll(List<PhoneInfoModel> list) {
        DataSupport.saveAll(list);
    }

    public static void deleteRepeat() {}

    public static void savePhoneInfo(PhoneInfoModel phoneInfoModel) {
        phoneInfoModel.save();
    }





    public static void saveChipsModel(ChipsModel chipsModel) {
        chipsModel.save();
    }
    public static void deleteChipsModelFromId(long id) {
        DataSupport.delete(ChipsModel.class, id);
    }

    public static void deleteChipsModelFromFRN(String father, String resource, String name) {
        DataSupport.deleteAll(ChipsModel.class, "father=?and resource=?and name=?", father, resource, name);
    }

    public static List<ChipsModel> findChipsModel(String resource, String name) {
        return DataSupport.where("resource=?and father=?", resource, name).find(ChipsModel.class);
    }
}
