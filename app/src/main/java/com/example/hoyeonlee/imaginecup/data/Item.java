package com.example.hoyeonlee.imaginecup.data;

/**
 * Created by hoyeonlee on 2018. 3. 23..
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("userid")
    @Expose
    private Integer userid;
    @SerializedName("weight")
    @Expose
    private Integer weight;
    @SerializedName("size")
    @Expose
    private Size size;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("index")
    @Expose
    private Integer index;
    @SerializedName("deviceid")
    @Expose
    private Integer deviceid;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;

    @SerializedName("bmi")
    @Expose
    private String bmi;

    @SerializedName("whr")
    @Expose
    private String whr;

    @SerializedName("shape")
    @Expose
    private String shape;

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(Integer deviceid) {
        this.deviceid = deviceid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getBmi() {
        return bmi;
    }

    public void setBmi(String bmi) {
        this.bmi = bmi;
    }

    public String getWhr() {
        return whr;
    }

    public void setWhr(String whr) {
        this.whr = whr;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }
}