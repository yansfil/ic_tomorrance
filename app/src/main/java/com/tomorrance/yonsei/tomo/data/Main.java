package com.tomorrance.yonsei.tomo.data;

/**
 * Created by hoyeonlee on 2018. 3. 24..
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Main{

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("isfirst")
    @Expose
    private Integer isfirst;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getIsfirst() {
        return isfirst;
    }

    public void setIsfirst(Integer isfirst) {
        this.isfirst = isfirst;
    }

}