package com.example.zuo81.zztt.model;

import android.graphics.Bitmap;

/**
 * Created by zuo81 on 2017/10/11.
 */
public class Card {
    private Bitmap bitmap;
    private String name;
    private String action;

    public Card(Bitmap bitmap, String name, String action) {
        this.bitmap = bitmap;
        this.name = name;
        this.action = action;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setCardName(String name) {
        this.name = name;
    }

    public String getCardName() {
        return name;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}