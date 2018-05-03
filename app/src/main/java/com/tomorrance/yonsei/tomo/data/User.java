package com.tomorrance.yonsei.tomo.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hoyeonlee on 2018. 3. 24..
 */
public class User {

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("height")
    @Expose
    private Object height;
    @SerializedName("name")
    @Expose
    private String name;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Object getHeight() {
        return height;
    }

    public void setHeight(Object height) {
        this.height = height;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
