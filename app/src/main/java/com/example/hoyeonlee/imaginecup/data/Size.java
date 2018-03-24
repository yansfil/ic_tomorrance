package com.example.hoyeonlee.imaginecup.data;

/**
 * Created by hoyeonlee on 2018. 3. 23..
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Size {

    @SerializedName("arm")
    @Expose
    private Integer arm;
    @SerializedName("leg")
    @Expose
    private Integer leg;

    public Integer getArm() {
        return arm;
    }

    public void setArm(Integer arm) {
        this.arm = arm;
    }

    public Integer getLeg() {
        return leg;
    }

    public void setLeg(Integer leg) {
        this.leg = leg;
    }

}