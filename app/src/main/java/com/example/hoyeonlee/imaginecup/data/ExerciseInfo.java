package com.example.hoyeonlee.imaginecup.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ExerciseInfo {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("items")
    @Expose
    private ArrayList<ExerciseData2> items;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public ArrayList<ExerciseData2> getItems() {
        return items;
    }

    public void setItems(ArrayList<ExerciseData2> items) {
        this.items = items;
    }

}