package com.example.zuo81.zztt.model;

import android.graphics.Bitmap;

import static android.R.attr.type;

/**
 * Created by zuo81 on 2017/10/11.
 */
public class Card {
    private Bitmap bitmap;
    private String name;
    private String action;
    public int type;

    public Card(Bitmap bitmap, String name, String action) {
        this(bitmap, name, action, 0);
    }

    public Card(Bitmap bitmap, String name, String action, int type) {
        this.bitmap = bitmap;
        this.name = name;
        this.action = action;
        this.type = type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
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