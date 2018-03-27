package com.example.hoyeonlee.imaginecup.data;

/**
 * Created by hoyeonlee on 2018. 3. 23..
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Size {

    @SerializedName("biacromion")
    @Expose
    private float biacromion;
    @SerializedName("chest")
    @Expose
    private float chest;
    @SerializedName("upperArm")
    @Expose
    private float upperArm;
    @SerializedName("waist")
    @Expose
    private float waist;
    @SerializedName("hip")
    @Expose
    private float hip;
    @SerializedName("midThigh")
    @Expose
    private float midThigh;
    @SerializedName("calf")
    @Expose
    private float calf;

    public float getBiacromion() {
        return biacromion;
    }

    public void setBiacromion(float biacromion) {
        this.biacromion = biacromion;
    }

    public float getChest() {
        return chest;
    }

    public void setChest(float chest) {
        this.chest = chest;
    }

    public float getUpperArm() {
        return upperArm;
    }

    public void setUpperArm(float upperArm) {
        this.upperArm = upperArm;
    }

    public float getWaist() {
        return waist;
    }

    public void setWaist(float waist) {
        this.waist = waist;
    }

    public float getHip() {
        return hip;
    }

    public void setHip(float hip) {
        this.hip = hip;
    }

    public float getMidThigh() {
        return midThigh;
    }

    public void setMidThigh(float midThigh) {
        this.midThigh = midThigh;
    }

    public float getCalf() {
        return calf;
    }

    public void setCalf(float calf) {
        this.calf = calf;
    }
}