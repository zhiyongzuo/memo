package com.example.zuo81.zztt.model;

/**
 * Created by zuo81 on 2017/10/11.
 */
public class Card {
    private int avatarResId;
    private String name;
    private String action;

    public Card(int avatarResId, String name, String action) {
        this.avatarResId = avatarResId;
        this.name = name;
        this.action = action;
    }

    public void setAvatarResId(int avatarResId) {
        this.avatarResId = avatarResId;
    }

    public int getAvatarResId() {
        return avatarResId;
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