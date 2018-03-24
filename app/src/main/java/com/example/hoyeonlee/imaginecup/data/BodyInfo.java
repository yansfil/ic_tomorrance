package com.example.hoyeonlee.imaginecup.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BodyInfo {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("item")
    @Expose
    private Item item;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

}