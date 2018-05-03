package com.tomorrance.yonsei.tomo.data;

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
        String value = String.format("%.1f",biacromion);
        return Float.valueOf(value);
    }

    public void setBiacromion(float biacromion) {
        this.biacromion = biacromion;
    }

    public float getChest() {
        String value = String.format("%.1f",chest);
        return Float.valueOf(value);
    }

    public void setChest(float chest) {
        this.chest = chest;
    }

    public float getUpperArm() {
        String value = String.format("%.1f",upperArm);
        return Float.valueOf(value);
    }

    public void setUpperArm(float upperArm) {
        this.upperArm = upperArm;
    }

    public float getWaist() {
        String value = String.format("%.1f",waist);
        return Float.valueOf(value);
    }

    public void setWaist(float waist) {
        this.waist = waist;
    }

    public float getHip() {
        String value = String.format("%.1f",hip);
        return Float.valueOf(value);
    }

    public void setHip(float hip) {
        this.hip = hip;
    }

    public float getMidThigh() {
        String value = String.format("%.1f",midThigh);
        return Float.valueOf(value);
    }

    public void setMidThigh(float midThigh) {
        this.midThigh = midThigh;
    }

    public float getCalf() {
        String value = String.format("%.1f",calf);
        return Float.valueOf(value);
    }

    public void setCalf(float calf) {
        this.calf = calf;
    }
}