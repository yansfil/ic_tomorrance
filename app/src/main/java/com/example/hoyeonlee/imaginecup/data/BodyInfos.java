package com.example.hoyeonlee.imaginecup.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BodyInfos {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("items")
    @Expose
    private ArrayList<Item> items;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

}