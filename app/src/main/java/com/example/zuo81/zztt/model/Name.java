package com.example.zuo81.zztt.model;

import android.graphics.drawable.Drawable;

/**
 * Created by zuo81 on 2017/9/11.
 */
public class Name {
        private String name;
        private Drawable drawable;
        private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Name(String name) {
        this.name = name;
    }

    public Name(String name, long id) {
        this.name = name;
        this.id = id;
    }

    public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setImageView(Drawable drawable) {
            this.drawable = drawable;
        }
        public Drawable getImageView() {
            return drawable;
        }

}